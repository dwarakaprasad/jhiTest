import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApplication, getApplicationIdentifier } from '../application.model';

export type EntityResponseType = HttpResponse<IApplication>;
export type EntityArrayResponseType = HttpResponse<IApplication[]>;

@Injectable({ providedIn: 'root' })
export class ApplicationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/applications');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(application: IApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(application);
    return this.http
      .post<IApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(application: IApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(application);
    return this.http
      .put<IApplication>(`${this.resourceUrl}/${getApplicationIdentifier(application) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(application: IApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(application);
    return this.http
      .patch<IApplication>(`${this.resourceUrl}/${getApplicationIdentifier(application) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IApplication>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IApplication[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addApplicationToCollectionIfMissing(
    applicationCollection: IApplication[],
    ...applicationsToCheck: (IApplication | null | undefined)[]
  ): IApplication[] {
    const applications: IApplication[] = applicationsToCheck.filter(isPresent);
    if (applications.length > 0) {
      const applicationCollectionIdentifiers = applicationCollection.map(applicationItem => getApplicationIdentifier(applicationItem)!);
      const applicationsToAdd = applications.filter(applicationItem => {
        const applicationIdentifier = getApplicationIdentifier(applicationItem);
        if (applicationIdentifier == null || applicationCollectionIdentifiers.includes(applicationIdentifier)) {
          return false;
        }
        applicationCollectionIdentifiers.push(applicationIdentifier);
        return true;
      });
      return [...applicationsToAdd, ...applicationCollection];
    }
    return applicationCollection;
  }

  protected convertDateFromClient(application: IApplication): IApplication {
    return Object.assign({}, application, {
      initiationdate: application.initiationdate?.isValid() ? application.initiationdate.format(DATE_FORMAT) : undefined,
      submissionDate: application.submissionDate?.isValid() ? application.submissionDate.format(DATE_FORMAT) : undefined,
      finalizationdate: application.finalizationdate?.isValid() ? application.finalizationdate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.initiationdate = res.body.initiationdate ? dayjs(res.body.initiationdate) : undefined;
      res.body.submissionDate = res.body.submissionDate ? dayjs(res.body.submissionDate) : undefined;
      res.body.finalizationdate = res.body.finalizationdate ? dayjs(res.body.finalizationdate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((application: IApplication) => {
        application.initiationdate = application.initiationdate ? dayjs(application.initiationdate) : undefined;
        application.submissionDate = application.submissionDate ? dayjs(application.submissionDate) : undefined;
        application.finalizationdate = application.finalizationdate ? dayjs(application.finalizationdate) : undefined;
      });
    }
    return res;
  }
}
