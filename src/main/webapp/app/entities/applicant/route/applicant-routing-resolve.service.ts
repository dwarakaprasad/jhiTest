import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApplicant, Applicant } from '../applicant.model';
import { ApplicantService } from '../service/applicant.service';

@Injectable({ providedIn: 'root' })
export class ApplicantRoutingResolveService implements Resolve<IApplicant> {
  constructor(protected service: ApplicantService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApplicant> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((applicant: HttpResponse<Applicant>) => {
          if (applicant.body) {
            return of(applicant.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Applicant());
  }
}
