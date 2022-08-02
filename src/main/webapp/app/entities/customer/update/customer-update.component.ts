import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICustomer, Customer } from '../customer.model';
import { CustomerService } from '../service/customer.service';
import { IPassport } from 'app/entities/passport/passport.model';
import { PassportService } from 'app/entities/passport/service/passport.service';
import { GENDER } from 'app/entities/enumerations/gender.model';

@Component({
  selector: 'jhi-customer-update',
  templateUrl: './customer-update.component.html',
})
export class CustomerUpdateComponent implements OnInit {
  isSaving = false;
  gENDERValues = Object.keys(GENDER);

  passportsCollection: IPassport[] = [];

  editForm = this.fb.group({
    id: [],
    firstName: [null, [Validators.required]],
    middleName: [],
    lastName: [null, [Validators.required]],
    dob: [null, [Validators.required]],
    gender: [null, [Validators.required]],
    passport: [],
  });

  constructor(
    protected customerService: CustomerService,
    protected passportService: PassportService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customer }) => {
      this.updateForm(customer);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customer = this.createFromForm();
    if (customer.id !== undefined) {
      this.subscribeToSaveResponse(this.customerService.update(customer));
    } else {
      this.subscribeToSaveResponse(this.customerService.create(customer));
    }
  }

  trackPassportById(_index: number, item: IPassport): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomer>>): void {
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

  protected updateForm(customer: ICustomer): void {
    this.editForm.patchValue({
      id: customer.id,
      firstName: customer.firstName,
      middleName: customer.middleName,
      lastName: customer.lastName,
      dob: customer.dob,
      gender: customer.gender,
      passport: customer.passport,
    });

    this.passportsCollection = this.passportService.addPassportToCollectionIfMissing(this.passportsCollection, customer.passport);
  }

  protected loadRelationshipsOptions(): void {
    this.passportService
      .query({ filter: 'customer-is-null' })
      .pipe(map((res: HttpResponse<IPassport[]>) => res.body ?? []))
      .pipe(
        map((passports: IPassport[]) =>
          this.passportService.addPassportToCollectionIfMissing(passports, this.editForm.get('passport')!.value)
        )
      )
      .subscribe((passports: IPassport[]) => (this.passportsCollection = passports));
  }

  protected createFromForm(): ICustomer {
    return {
      ...new Customer(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      middleName: this.editForm.get(['middleName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      dob: this.editForm.get(['dob'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      passport: this.editForm.get(['passport'])!.value,
    };
  }
}
