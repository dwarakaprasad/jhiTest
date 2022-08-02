import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPaymentInfo, PaymentInfo } from '../payment-info.model';
import { PaymentInfoService } from '../service/payment-info.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { PAYMENTTYPE } from 'app/entities/enumerations/paymenttype.model';

@Component({
  selector: 'jhi-payment-info-update',
  templateUrl: './payment-info-update.component.html',
})
export class PaymentInfoUpdateComponent implements OnInit {
  isSaving = false;
  pAYMENTTYPEValues = Object.keys(PAYMENTTYPE);

  customersSharedCollection: ICustomer[] = [];

  editForm = this.fb.group({
    id: [],
    paymentType: [null, [Validators.required]],
    number: [null, [Validators.required]],
    expiry: [],
    security: [],
    customer: [],
  });

  constructor(
    protected paymentInfoService: PaymentInfoService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentInfo }) => {
      this.updateForm(paymentInfo);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paymentInfo = this.createFromForm();
    if (paymentInfo.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentInfoService.update(paymentInfo));
    } else {
      this.subscribeToSaveResponse(this.paymentInfoService.create(paymentInfo));
    }
  }

  trackCustomerById(_index: number, item: ICustomer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentInfo>>): void {
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

  protected updateForm(paymentInfo: IPaymentInfo): void {
    this.editForm.patchValue({
      id: paymentInfo.id,
      paymentType: paymentInfo.paymentType,
      number: paymentInfo.number,
      expiry: paymentInfo.expiry,
      security: paymentInfo.security,
      customer: paymentInfo.customer,
    });

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing(
      this.customersSharedCollection,
      paymentInfo.customer
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing(customers, this.editForm.get('customer')!.value)
        )
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));
  }

  protected createFromForm(): IPaymentInfo {
    return {
      ...new PaymentInfo(),
      id: this.editForm.get(['id'])!.value,
      paymentType: this.editForm.get(['paymentType'])!.value,
      number: this.editForm.get(['number'])!.value,
      expiry: this.editForm.get(['expiry'])!.value,
      security: this.editForm.get(['security'])!.value,
      customer: this.editForm.get(['customer'])!.value,
    };
  }
}
