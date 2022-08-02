import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaymentInfo } from '../payment-info.model';

@Component({
  selector: 'jhi-payment-info-detail',
  templateUrl: './payment-info-detail.component.html',
})
export class PaymentInfoDetailComponent implements OnInit {
  paymentInfo: IPaymentInfo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentInfo }) => {
      this.paymentInfo = paymentInfo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
