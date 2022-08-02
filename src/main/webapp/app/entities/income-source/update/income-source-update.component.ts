import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IIncomeSource, IncomeSource } from '../income-source.model';
import { IncomeSourceService } from '../service/income-source.service';
import { IEmployer } from 'app/entities/employer/employer.model';
import { EmployerService } from 'app/entities/employer/service/employer.service';
import { IApplicant } from 'app/entities/applicant/applicant.model';
import { ApplicantService } from 'app/entities/applicant/service/applicant.service';
import { INCOMETYPE } from 'app/entities/enumerations/incometype.model';

@Component({
  selector: 'jhi-income-source-update',
  templateUrl: './income-source-update.component.html',
})
export class IncomeSourceUpdateComponent implements OnInit {
  isSaving = false;
  iNCOMETYPEValues = Object.keys(INCOMETYPE);

  employersCollection: IEmployer[] = [];
  applicantsSharedCollection: IApplicant[] = [];

  editForm = this.fb.group({
    id: [],
    incomeType: [null, [Validators.required]],
    incomeAmount: [null, [Validators.required]],
    duration: [null, [Validators.required]],
    employer: [],
    applicant: [],
  });

  constructor(
    protected incomeSourceService: IncomeSourceService,
    protected employerService: EmployerService,
    protected applicantService: ApplicantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ incomeSource }) => {
      this.updateForm(incomeSource);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const incomeSource = this.createFromForm();
    if (incomeSource.id !== undefined) {
      this.subscribeToSaveResponse(this.incomeSourceService.update(incomeSource));
    } else {
      this.subscribeToSaveResponse(this.incomeSourceService.create(incomeSource));
    }
  }

  trackEmployerById(_index: number, item: IEmployer): number {
    return item.id!;
  }

  trackApplicantById(_index: number, item: IApplicant): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIncomeSource>>): void {
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

  protected updateForm(incomeSource: IIncomeSource): void {
    this.editForm.patchValue({
      id: incomeSource.id,
      incomeType: incomeSource.incomeType,
      incomeAmount: incomeSource.incomeAmount,
      duration: incomeSource.duration,
      employer: incomeSource.employer,
      applicant: incomeSource.applicant,
    });

    this.employersCollection = this.employerService.addEmployerToCollectionIfMissing(this.employersCollection, incomeSource.employer);
    this.applicantsSharedCollection = this.applicantService.addApplicantToCollectionIfMissing(
      this.applicantsSharedCollection,
      incomeSource.applicant
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employerService
      .query({ filter: 'incomesource-is-null' })
      .pipe(map((res: HttpResponse<IEmployer[]>) => res.body ?? []))
      .pipe(
        map((employers: IEmployer[]) =>
          this.employerService.addEmployerToCollectionIfMissing(employers, this.editForm.get('employer')!.value)
        )
      )
      .subscribe((employers: IEmployer[]) => (this.employersCollection = employers));

    this.applicantService
      .query()
      .pipe(map((res: HttpResponse<IApplicant[]>) => res.body ?? []))
      .pipe(
        map((applicants: IApplicant[]) =>
          this.applicantService.addApplicantToCollectionIfMissing(applicants, this.editForm.get('applicant')!.value)
        )
      )
      .subscribe((applicants: IApplicant[]) => (this.applicantsSharedCollection = applicants));
  }

  protected createFromForm(): IIncomeSource {
    return {
      ...new IncomeSource(),
      id: this.editForm.get(['id'])!.value,
      incomeType: this.editForm.get(['incomeType'])!.value,
      incomeAmount: this.editForm.get(['incomeAmount'])!.value,
      duration: this.editForm.get(['duration'])!.value,
      employer: this.editForm.get(['employer'])!.value,
      applicant: this.editForm.get(['applicant'])!.value,
    };
  }
}
