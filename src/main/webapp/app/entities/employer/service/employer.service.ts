import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmployer, getEmployerIdentifier } from '../employer.model';

export type EntityResponseType = HttpResponse<IEmployer>;
export type EntityArrayResponseType = HttpResponse<IEmployer[]>;

@Injectable({ providedIn: 'root' })
export class EmployerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employer: IEmployer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employer);
    return this.http
      .post<IEmployer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(employer: IEmployer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employer);
    return this.http
      .put<IEmployer>(`${this.resourceUrl}/${getEmployerIdentifier(employer) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(employer: IEmployer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employer);
    return this.http
      .patch<IEmployer>(`${this.resourceUrl}/${getEmployerIdentifier(employer) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEmployer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmployer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEmployerToCollectionIfMissing(employerCollection: IEmployer[], ...employersToCheck: (IEmployer | null | undefined)[]): IEmployer[] {
    const employers: IEmployer[] = employersToCheck.filter(isPresent);
    if (employers.length > 0) {
      const employerCollectionIdentifiers = employerCollection.map(employerItem => getEmployerIdentifier(employerItem)!);
      const employersToAdd = employers.filter(employerItem => {
        const employerIdentifier = getEmployerIdentifier(employerItem);
        if (employerIdentifier == null || employerCollectionIdentifiers.includes(employerIdentifier)) {
          return false;
        }
        employerCollectionIdentifiers.push(employerIdentifier);
        return true;
      });
      return [...employersToAdd, ...employerCollection];
    }
    return employerCollection;
  }

  protected convertDateFromClient(employer: IEmployer): IEmployer {
    return Object.assign({}, employer, {
      started: employer.started?.isValid() ? employer.started.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.started = res.body.started ? dayjs(res.body.started) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((employer: IEmployer) => {
        employer.started = employer.started ? dayjs(employer.started) : undefined;
      });
    }
    return res;
  }
}
