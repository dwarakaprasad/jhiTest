import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPaymentInfo, getPaymentInfoIdentifier } from '../payment-info.model';

export type EntityResponseType = HttpResponse<IPaymentInfo>;
export type EntityArrayResponseType = HttpResponse<IPaymentInfo[]>;

@Injectable({ providedIn: 'root' })
export class PaymentInfoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payment-infos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(paymentInfo: IPaymentInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentInfo);
    return this.http
      .post<IPaymentInfo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(paymentInfo: IPaymentInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentInfo);
    return this.http
      .put<IPaymentInfo>(`${this.resourceUrl}/${getPaymentInfoIdentifier(paymentInfo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(paymentInfo: IPaymentInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentInfo);
    return this.http
      .patch<IPaymentInfo>(`${this.resourceUrl}/${getPaymentInfoIdentifier(paymentInfo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPaymentInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPaymentInfo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPaymentInfoToCollectionIfMissing(
    paymentInfoCollection: IPaymentInfo[],
    ...paymentInfosToCheck: (IPaymentInfo | null | undefined)[]
  ): IPaymentInfo[] {
    const paymentInfos: IPaymentInfo[] = paymentInfosToCheck.filter(isPresent);
    if (paymentInfos.length > 0) {
      const paymentInfoCollectionIdentifiers = paymentInfoCollection.map(paymentInfoItem => getPaymentInfoIdentifier(paymentInfoItem)!);
      const paymentInfosToAdd = paymentInfos.filter(paymentInfoItem => {
        const paymentInfoIdentifier = getPaymentInfoIdentifier(paymentInfoItem);
        if (paymentInfoIdentifier == null || paymentInfoCollectionIdentifiers.includes(paymentInfoIdentifier)) {
          return false;
        }
        paymentInfoCollectionIdentifiers.push(paymentInfoIdentifier);
        return true;
      });
      return [...paymentInfosToAdd, ...paymentInfoCollection];
    }
    return paymentInfoCollection;
  }

  protected convertDateFromClient(paymentInfo: IPaymentInfo): IPaymentInfo {
    return Object.assign({}, paymentInfo, {
      expiry: paymentInfo.expiry?.isValid() ? paymentInfo.expiry.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.expiry = res.body.expiry ? dayjs(res.body.expiry) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((paymentInfo: IPaymentInfo) => {
        paymentInfo.expiry = paymentInfo.expiry ? dayjs(paymentInfo.expiry) : undefined;
      });
    }
    return res;
  }
}
