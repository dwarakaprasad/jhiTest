import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPassport, Passport } from '../passport.model';
import { PassportService } from '../service/passport.service';
import { PASSPORTTYPE } from 'app/entities/enumerations/passporttype.model';

@Component({
  selector: 'jhi-passport-update',
  templateUrl: './passport-update.component.html',
})
export class PassportUpdateComponent implements OnInit {
  isSaving = false;
  pASSPORTTYPEValues = Object.keys(PASSPORTTYPE);

  editForm = this.fb.group({
    id: [],
    identity: [null, [Validators.required]],
    expiry: [null, [Validators.required]],
    issuingCountry: [null, [Validators.required]],
    documentNumber: [],
    passportType: [null, [Validators.required]],
  });

  constructor(protected passportService: PassportService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ passport }) => {
      this.updateForm(passport);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const passport = this.createFromForm();
    if (passport.id !== undefined) {
      this.subscribeToSaveResponse(this.passportService.update(passport));
    } else {
      this.subscribeToSaveResponse(this.passportService.create(passport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPassport>>): void {
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

  protected updateForm(passport: IPassport): void {
    this.editForm.patchValue({
      id: passport.id,
      identity: passport.identity,
      expiry: passport.expiry,
      issuingCountry: passport.issuingCountry,
      documentNumber: passport.documentNumber,
      passportType: passport.passportType,
    });
  }

  protected createFromForm(): IPassport {
    return {
      ...new Passport(),
      id: this.editForm.get(['id'])!.value,
      identity: this.editForm.get(['identity'])!.value,
      expiry: this.editForm.get(['expiry'])!.value,
      issuingCountry: this.editForm.get(['issuingCountry'])!.value,
      documentNumber: this.editForm.get(['documentNumber'])!.value,
      passportType: this.editForm.get(['passportType'])!.value,
    };
  }
}
