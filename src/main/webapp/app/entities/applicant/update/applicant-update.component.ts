import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IApplicant, Applicant } from '../applicant.model';
import { ApplicantService } from '../service/applicant.service';
import { IAssets } from 'app/entities/assets/assets.model';
import { AssetsService } from 'app/entities/assets/service/assets.service';
import { IApplication } from 'app/entities/application/application.model';
import { ApplicationService } from 'app/entities/application/service/application.service';
import { GENDER } from 'app/entities/enumerations/gender.model';

@Component({
  selector: 'jhi-applicant-update',
  templateUrl: './applicant-update.component.html',
})
export class ApplicantUpdateComponent implements OnInit {
  isSaving = false;
  gENDERValues = Object.keys(GENDER);

  assetsSharedCollection: IAssets[] = [];
  applicationsSharedCollection: IApplication[] = [];

  editForm = this.fb.group({
    id: [],
    firstName: [null, [Validators.required]],
    middleName: [],
    lastName: [null, [Validators.required]],
    dob: [null, [Validators.required]],
    gender: [null, [Validators.required]],
    assets: [],
    application: [],
  });

  constructor(
    protected applicantService: ApplicantService,
    protected assetsService: AssetsService,
    protected applicationService: ApplicationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicant }) => {
      this.updateForm(applicant);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const applicant = this.createFromForm();
    if (applicant.id !== undefined) {
      this.subscribeToSaveResponse(this.applicantService.update(applicant));
    } else {
      this.subscribeToSaveResponse(this.applicantService.create(applicant));
    }
  }

  trackAssetsById(_index: number, item: IAssets): number {
    return item.id!;
  }

  trackApplicationById(_index: number, item: IApplication): number {
    return item.id!;
  }

  getSelectedAssets(option: IAssets, selectedVals?: IAssets[]): IAssets {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplicant>>): void {
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

  protected updateForm(applicant: IApplicant): void {
    this.editForm.patchValue({
      id: applicant.id,
      firstName: applicant.firstName,
      middleName: applicant.middleName,
      lastName: applicant.lastName,
      dob: applicant.dob,
      gender: applicant.gender,
      assets: applicant.assets,
      application: applicant.application,
    });

    this.assetsSharedCollection = this.assetsService.addAssetsToCollectionIfMissing(
      this.assetsSharedCollection,
      ...(applicant.assets ?? [])
    );
    this.applicationsSharedCollection = this.applicationService.addApplicationToCollectionIfMissing(
      this.applicationsSharedCollection,
      applicant.application
    );
  }

  protected loadRelationshipsOptions(): void {
    this.assetsService
      .query()
      .pipe(map((res: HttpResponse<IAssets[]>) => res.body ?? []))
      .pipe(
        map((assets: IAssets[]) => this.assetsService.addAssetsToCollectionIfMissing(assets, ...(this.editForm.get('assets')!.value ?? [])))
      )
      .subscribe((assets: IAssets[]) => (this.assetsSharedCollection = assets));

    this.applicationService
      .query()
      .pipe(map((res: HttpResponse<IApplication[]>) => res.body ?? []))
      .pipe(
        map((applications: IApplication[]) =>
          this.applicationService.addApplicationToCollectionIfMissing(applications, this.editForm.get('application')!.value)
        )
      )
      .subscribe((applications: IApplication[]) => (this.applicationsSharedCollection = applications));
  }

  protected createFromForm(): IApplicant {
    return {
      ...new Applicant(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      middleName: this.editForm.get(['middleName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      dob: this.editForm.get(['dob'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      assets: this.editForm.get(['assets'])!.value,
      application: this.editForm.get(['application'])!.value,
    };
  }
}
