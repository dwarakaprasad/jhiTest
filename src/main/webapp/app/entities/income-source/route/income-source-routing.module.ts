import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IncomeSourceComponent } from '../list/income-source.component';
import { IncomeSourceDetailComponent } from '../detail/income-source-detail.component';
import { IncomeSourceUpdateComponent } from '../update/income-source-update.component';
import { IncomeSourceRoutingResolveService } from './income-source-routing-resolve.service';

const incomeSourceRoute: Routes = [
  {
    path: '',
    component: IncomeSourceComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IncomeSourceDetailComponent,
    resolve: {
      incomeSource: IncomeSourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IncomeSourceUpdateComponent,
    resolve: {
      incomeSource: IncomeSourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IncomeSourceUpdateComponent,
    resolve: {
      incomeSource: IncomeSourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(incomeSourceRoute)],
  exports: [RouterModule],
})
export class IncomeSourceRoutingModule {}
