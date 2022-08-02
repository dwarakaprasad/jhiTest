import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IApplication, Application } from '../application.model';
import { ApplicationService } from '../service/application.service';
import { APPLICATIONTYPE } from 'app/entities/enumerations/applicationtype.model';

@Component({
  selector: 'jhi-application-update',
  templateUrl: './application-update.component.html',
})
export class ApplicationUpdateComponent implements OnInit {
  isSaving = false;
  aPPLICATIONTYPEValues = Object.keys(APPLICATIONTYPE);

  editForm = this.fb.group({
    id: [],
    applicationType: [null, [Validators.required]],
    initiationdate: [null, [Validators.required]],
    submissionDate: [null, [Validators.required]],
    finalizationdate: [null, [Validators.required]],
    applicationIdentifier: [],
  });

  constructor(protected applicationService: ApplicationService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ application }) => {
      this.updateForm(application);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const application = this.createFromForm();
    if (application.id !== undefined) {
      this.subscribeToSaveResponse(this.applicationService.update(application));
    } else {
      this.subscribeToSaveResponse(this.applicationService.create(application));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplication>>): void {
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

  protected updateForm(application: IApplication): void {
    this.editForm.patchValue({
      id: application.id,
      applicationType: application.applicationType,
      initiationdate: application.initiationdate,
      submissionDate: application.submissionDate,
      finalizationdate: application.finalizationdate,
      applicationIdentifier: application.applicationIdentifier,
    });
  }

  protected createFromForm(): IApplication {
    return {
      ...new Application(),
      id: this.editForm.get(['id'])!.value,
      applicationType: this.editForm.get(['applicationType'])!.value,
      initiationdate: this.editForm.get(['initiationdate'])!.value,
      submissionDate: this.editForm.get(['submissionDate'])!.value,
      finalizationdate: this.editForm.get(['finalizationdate'])!.value,
      applicationIdentifier: this.editForm.get(['applicationIdentifier'])!.value,
    };
  }
}
