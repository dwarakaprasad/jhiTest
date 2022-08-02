import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaymentInfo, PaymentInfo } from '../payment-info.model';
import { PaymentInfoService } from '../service/payment-info.service';

@Injectable({ providedIn: 'root' })
export class PaymentInfoRoutingResolveService implements Resolve<IPaymentInfo> {
  constructor(protected service: PaymentInfoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaymentInfo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((paymentInfo: HttpResponse<PaymentInfo>) => {
          if (paymentInfo.body) {
            return of(paymentInfo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PaymentInfo());
  }
}
