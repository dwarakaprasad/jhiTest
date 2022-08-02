import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaymentInfoComponent } from '../list/payment-info.component';
import { PaymentInfoDetailComponent } from '../detail/payment-info-detail.component';
import { PaymentInfoUpdateComponent } from '../update/payment-info-update.component';
import { PaymentInfoRoutingResolveService } from './payment-info-routing-resolve.service';

const paymentInfoRoute: Routes = [
  {
    path: '',
    component: PaymentInfoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaymentInfoDetailComponent,
    resolve: {
      paymentInfo: PaymentInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaymentInfoUpdateComponent,
    resolve: {
      paymentInfo: PaymentInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentInfoUpdateComponent,
    resolve: {
      paymentInfo: PaymentInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paymentInfoRoute)],
  exports: [RouterModule],
})
export class PaymentInfoRoutingModule {}
