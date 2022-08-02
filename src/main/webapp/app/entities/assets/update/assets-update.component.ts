import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAssets, Assets } from '../assets.model';
import { AssetsService } from '../service/assets.service';
import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';
import { ASSETTYPE } from 'app/entities/enumerations/assettype.model';

@Component({
  selector: 'jhi-assets-update',
  templateUrl: './assets-update.component.html',
})
export class AssetsUpdateComponent implements OnInit {
  isSaving = false;
  aSSETTYPEValues = Object.keys(ASSETTYPE);

  addressesCollection: IAddress[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    assetType: [null, [Validators.required]],
    address: [],
  });

  constructor(
    protected assetsService: AssetsService,
    protected addressService: AddressService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assets }) => {
      this.updateForm(assets);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assets = this.createFromForm();
    if (assets.id !== undefined) {
      this.subscribeToSaveResponse(this.assetsService.update(assets));
    } else {
      this.subscribeToSaveResponse(this.assetsService.create(assets));
    }
  }

  trackAddressById(_index: number, item: IAddress): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssets>>): void {
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

  protected updateForm(assets: IAssets): void {
    this.editForm.patchValue({
      id: assets.id,
      name: assets.name,
      assetType: assets.assetType,
      address: assets.address,
    });

    this.addressesCollection = this.addressService.addAddressToCollectionIfMissing(this.addressesCollection, assets.address);
  }

  protected loadRelationshipsOptions(): void {
    this.addressService
      .query({ filter: 'assets-is-null' })
      .pipe(map((res: HttpResponse<IAddress[]>) => res.body ?? []))
      .pipe(
        map((addresses: IAddress[]) => this.addressService.addAddressToCollectionIfMissing(addresses, this.editForm.get('address')!.value))
      )
      .subscribe((addresses: IAddress[]) => (this.addressesCollection = addresses));
  }

  protected createFromForm(): IAssets {
    return {
      ...new Assets(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      assetType: this.editForm.get(['assetType'])!.value,
      address: this.editForm.get(['address'])!.value,
    };
  }
}
