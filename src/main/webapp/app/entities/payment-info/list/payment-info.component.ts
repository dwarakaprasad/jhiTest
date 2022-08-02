import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaymentInfo } from '../payment-info.model';
import { PaymentInfoService } from '../service/payment-info.service';
import { PaymentInfoDeleteDialogComponent } from '../delete/payment-info-delete-dialog.component';

@Component({
  selector: 'jhi-payment-info',
  templateUrl: './payment-info.component.html',
})
export class PaymentInfoComponent implements OnInit {
  paymentInfos?: IPaymentInfo[];
  isLoading = false;

  constructor(protected paymentInfoService: PaymentInfoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.paymentInfoService.query().subscribe({
      next: (res: HttpResponse<IPaymentInfo[]>) => {
        this.isLoading = false;
        this.paymentInfos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPaymentInfo): number {
    return item.id!;
  }

  delete(paymentInfo: IPaymentInfo): void {
    const modalRef = this.modalService.open(PaymentInfoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.paymentInfo = paymentInfo;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
