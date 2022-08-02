import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'customer',
        data: { pageTitle: 'myApp.customer.home.title' },
        loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule),
      },
      {
        path: 'payment-info',
        data: { pageTitle: 'myApp.paymentInfo.home.title' },
        loadChildren: () => import('./payment-info/payment-info.module').then(m => m.PaymentInfoModule),
      },
      {
        path: 'address',
        data: { pageTitle: 'myApp.address.home.title' },
        loadChildren: () => import('./address/address.module').then(m => m.AddressModule),
      },
      {
        path: 'passport',
        data: { pageTitle: 'myApp.passport.home.title' },
        loadChildren: () => import('./passport/passport.module').then(m => m.PassportModule),
      },
      {
        path: 'application',
        data: { pageTitle: 'myApp.application.home.title' },
        loadChildren: () => import('./application/application.module').then(m => m.ApplicationModule),
      },
      {
        path: 'applicant',
        data: { pageTitle: 'myApp.applicant.home.title' },
        loadChildren: () => import('./applicant/applicant.module').then(m => m.ApplicantModule),
      },
      {
        path: 'assets',
        data: { pageTitle: 'myApp.assets.home.title' },
        loadChildren: () => import('./assets/assets.module').then(m => m.AssetsModule),
      },
      {
        path: 'income-source',
        data: { pageTitle: 'myApp.incomeSource.home.title' },
        loadChildren: () => import('./income-source/income-source.module').then(m => m.IncomeSourceModule),
      },
      {
        path: 'employer',
        data: { pageTitle: 'myApp.employer.home.title' },
        loadChildren: () => import('./employer/employer.module').then(m => m.EmployerModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
