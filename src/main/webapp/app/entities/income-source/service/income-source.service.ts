import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIncomeSource, getIncomeSourceIdentifier } from '../income-source.model';

export type EntityResponseType = HttpResponse<IIncomeSource>;
export type EntityArrayResponseType = HttpResponse<IIncomeSource[]>;

@Injectable({ providedIn: 'root' })
export class IncomeSourceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/income-sources');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(incomeSource: IIncomeSource): Observable<EntityResponseType> {
    return this.http.post<IIncomeSource>(this.resourceUrl, incomeSource, { observe: 'response' });
  }

  update(incomeSource: IIncomeSource): Observable<EntityResponseType> {
    return this.http.put<IIncomeSource>(`${this.resourceUrl}/${getIncomeSourceIdentifier(incomeSource) as number}`, incomeSource, {
      observe: 'response',
    });
  }

  partialUpdate(incomeSource: IIncomeSource): Observable<EntityResponseType> {
    return this.http.patch<IIncomeSource>(`${this.resourceUrl}/${getIncomeSourceIdentifier(incomeSource) as number}`, incomeSource, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIncomeSource>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIncomeSource[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIncomeSourceToCollectionIfMissing(
    incomeSourceCollection: IIncomeSource[],
    ...incomeSourcesToCheck: (IIncomeSource | null | undefined)[]
  ): IIncomeSource[] {
    const incomeSources: IIncomeSource[] = incomeSourcesToCheck.filter(isPresent);
    if (incomeSources.length > 0) {
      const incomeSourceCollectionIdentifiers = incomeSourceCollection.map(
        incomeSourceItem => getIncomeSourceIdentifier(incomeSourceItem)!
      );
      const incomeSourcesToAdd = incomeSources.filter(incomeSourceItem => {
        const incomeSourceIdentifier = getIncomeSourceIdentifier(incomeSourceItem);
        if (incomeSourceIdentifier == null || incomeSourceCollectionIdentifiers.includes(incomeSourceIdentifier)) {
          return false;
        }
        incomeSourceCollectionIdentifiers.push(incomeSourceIdentifier);
        return true;
      });
      return [...incomeSourcesToAdd, ...incomeSourceCollection];
    }
    return incomeSourceCollection;
  }
}
