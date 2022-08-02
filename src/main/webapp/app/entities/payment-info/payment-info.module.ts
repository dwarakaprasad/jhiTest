import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaymentInfoComponent } from './list/payment-info.component';
import { PaymentInfoDetailComponent } from './detail/payment-info-detail.component';
import { PaymentInfoUpdateComponent } from './update/payment-info-update.component';
import { PaymentInfoDeleteDialogComponent } from './delete/payment-info-delete-dialog.component';
import { PaymentInfoRoutingModule } from './route/payment-info-routing.module';

@NgModule({
  imports: [SharedModule, PaymentInfoRoutingModule],
  declarations: [PaymentInfoComponent, PaymentInfoDetailComponent, PaymentInfoUpdateComponent, PaymentInfoDeleteDialogComponent],
  entryComponents: [PaymentInfoDeleteDialogComponent],
})
export class PaymentInfoModule {}
