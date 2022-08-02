import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIncomeSource, IncomeSource } from '../income-source.model';
import { IncomeSourceService } from '../service/income-source.service';

@Injectable({ providedIn: 'root' })
export class IncomeSourceRoutingResolveService implements Resolve<IIncomeSource> {
  constructor(protected service: IncomeSourceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIncomeSource> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((incomeSource: HttpResponse<IncomeSource>) => {
          if (incomeSource.body) {
            return of(incomeSource.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new IncomeSource());
  }
}
