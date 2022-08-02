import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssets, Assets } from '../assets.model';
import { AssetsService } from '../service/assets.service';

@Injectable({ providedIn: 'root' })
export class AssetsRoutingResolveService implements Resolve<IAssets> {
  constructor(protected service: AssetsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAssets> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((assets: HttpResponse<Assets>) => {
          if (assets.body) {
            return of(assets.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Assets());
  }
}
