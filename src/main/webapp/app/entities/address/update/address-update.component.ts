import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAddress, Address } from '../address.model';
import { AddressService } from '../service/address.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';

@Component({
  selector: 'jhi-address-update',
  templateUrl: './address-update.component.html',
})
export class AddressUpdateComponent implements OnInit {
  isSaving = false;

  customersSharedCollection: ICustomer[] = [];

  editForm = this.fb.group({
    id: [],
    street1: [null, [Validators.required]],
    street2: [],
    city: [null, [Validators.required]],
    state: [],
    zipCode: [null, [Validators.required]],
    customer: [],
  });

  constructor(
    protected addressService: AddressService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ address }) => {
      this.updateForm(address);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const address = this.createFromForm();
    if (address.id !== undefined) {
      this.subscribeToSaveResponse(this.addressService.update(address));
    } else {
      this.subscribeToSaveResponse(this.addressService.create(address));
    }
  }

  trackCustomerById(_index: number, item: ICustomer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAddress>>): void {
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

  protected updateForm(address: IAddress): void {
    this.editForm.patchValue({
      id: address.id,
      street1: address.street1,
      street2: address.street2,
      city: address.city,
      state: address.state,
      zipCode: address.zipCode,
      customer: address.customer,
    });

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing(
      this.customersSharedCollection,
      address.customer
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

  protected createFromForm(): IAddress {
    return {
      ...new Address(),
      id: this.editForm.get(['id'])!.value,
      street1: this.editForm.get(['street1'])!.value,
      street2: this.editForm.get(['street2'])!.value,
      city: this.editForm.get(['city'])!.value,
      state: this.editForm.get(['state'])!.value,
      zipCode: this.editForm.get(['zipCode'])!.value,
      customer: this.editForm.get(['customer'])!.value,
    };
  }
}
