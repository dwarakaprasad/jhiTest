import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEventLogBook, EventLogBook } from '../event-log-book.model';
import { EventLogBookService } from '../service/event-log-book.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-event-log-book-update',
  templateUrl: './event-log-book-update.component.html',
})
export class EventLogBookUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    uuid: [],
    createdDate: [],
    updatedDate: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    archieved: [],
    user: [],
  });

  constructor(
    protected eventLogBookService: EventLogBookService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventLogBook }) => {
      if (eventLogBook.id === undefined) {
        const today = dayjs().startOf('day');
        eventLogBook.createdDate = today;
        eventLogBook.updatedDate = today;
      }

      this.updateForm(eventLogBook);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eventLogBook = this.createFromForm();
    if (eventLogBook.id !== undefined) {
      this.subscribeToSaveResponse(this.eventLogBookService.update(eventLogBook));
    } else {
      this.subscribeToSaveResponse(this.eventLogBookService.create(eventLogBook));
    }
  }

  trackUserById(_index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEventLogBook>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(eventLogBook: IEventLogBook): void {
    this.editForm.patchValue({
      id: eventLogBook.id,
      uuid: eventLogBook.uuid,
      createdDate: eventLogBook.createdDate ? eventLogBook.createdDate.format(DATE_TIME_FORMAT) : null,
      updatedDate: eventLogBook.updatedDate ? eventLogBook.updatedDate.format(DATE_TIME_FORMAT) : null,
      name: eventLogBook.name,
      description: eventLogBook.description,
      archieved: eventLogBook.archieved,
      user: eventLogBook.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, eventLogBook.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IEventLogBook {
    return {
      ...new EventLogBook(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? dayjs(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      updatedDate: this.editForm.get(['updatedDate'])!.value
        ? dayjs(this.editForm.get(['updatedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      archieved: this.editForm.get(['archieved'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
