import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ApplicantComponent } from '../list/applicant.component';
import { ApplicantDetailComponent } from '../detail/applicant-detail.component';
import { ApplicantUpdateComponent } from '../update/applicant-update.component';
import { ApplicantRoutingResolveService } from './applicant-routing-resolve.service';

const applicantRoute: Routes = [
  {
    path: '',
    component: ApplicantComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApplicantDetailComponent,
    resolve: {
      applicant: ApplicantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApplicantUpdateComponent,
    resolve: {
      applicant: ApplicantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApplicantUpdateComponent,
    resolve: {
      applicant: ApplicantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(applicantRoute)],
  exports: [RouterModule],
})
export class ApplicantRoutingModule {}
