import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaymentInfo } from '../payment-info.model';
import { PaymentInfoService } from '../service/payment-info.service';

@Component({
  templateUrl: './payment-info-delete-dialog.component.html',
})
export class PaymentInfoDeleteDialogComponent {
  paymentInfo?: IPaymentInfo;

  constructor(protected paymentInfoService: PaymentInfoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paymentInfoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
