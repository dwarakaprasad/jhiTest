import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AssetsComponent } from '../list/assets.component';
import { AssetsDetailComponent } from '../detail/assets-detail.component';
import { AssetsUpdateComponent } from '../update/assets-update.component';
import { AssetsRoutingResolveService } from './assets-routing-resolve.service';

const assetsRoute: Routes = [
  {
    path: '',
    component: AssetsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssetsDetailComponent,
    resolve: {
      assets: AssetsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssetsUpdateComponent,
    resolve: {
      assets: AssetsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssetsUpdateComponent,
    resolve: {
      assets: AssetsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assetsRoute)],
  exports: [RouterModule],
})
export class AssetsRoutingModule {}
