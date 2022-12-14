import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEmployer, Employer } from '../employer.model';
import { EmployerService } from '../service/employer.service';
import { EMPLOYERTYPE } from 'app/entities/enumerations/employertype.model';

@Component({
  selector: 'jhi-employer-update',
  templateUrl: './employer-update.component.html',
})
export class EmployerUpdateComponent implements OnInit {
  isSaving = false;
  eMPLOYERTYPEValues = Object.keys(EMPLOYERTYPE);

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    ein: [null, [Validators.required]],
    started: [],
    employerType: [null, [Validators.required]],
  });

  constructor(protected employerService: EmployerService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employer }) => {
      this.updateForm(employer);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employer = this.createFromForm();
    if (employer.id !== undefined) {
      this.subscribeToSaveResponse(this.employerService.update(employer));
    } else {
      this.subscribeToSaveResponse(this.employerService.create(employer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployer>>): void {
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

  protected updateForm(employer: IEmployer): void {
    this.editForm.patchValue({
      id: employer.id,
      name: employer.name,
      ein: employer.ein,
      started: employer.started,
      employerType: employer.employerType,
    });
  }

  protected createFromForm(): IEmployer {
    return {
      ...new Employer(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      ein: this.editForm.get(['ein'])!.value,
      started: this.editForm.get(['started'])!.value,
      employerType: this.editForm.get(['employerType'])!.value,
    };
  }
}
