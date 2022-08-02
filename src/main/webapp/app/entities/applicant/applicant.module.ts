import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ApplicantComponent } from './list/applicant.component';
import { ApplicantDetailComponent } from './detail/applicant-detail.component';
import { ApplicantUpdateComponent } from './update/applicant-update.component';
import { ApplicantDeleteDialogComponent } from './delete/applicant-delete-dialog.component';
import { ApplicantRoutingModule } from './route/applicant-routing.module';

@NgModule({
  imports: [SharedModule, ApplicantRoutingModule],
  declarations: [ApplicantComponent, ApplicantDetailComponent, ApplicantUpdateComponent, ApplicantDeleteDialogComponent],
  entryComponents: [ApplicantDeleteDialogComponent],
})
export class ApplicantModule {}
