import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAssets, getAssetsIdentifier } from '../assets.model';

export type EntityResponseType = HttpResponse<IAssets>;
export type EntityArrayResponseType = HttpResponse<IAssets[]>;

@Injectable({ providedIn: 'root' })
export class AssetsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/assets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(assets: IAssets): Observable<EntityResponseType> {
    return this.http.post<IAssets>(this.resourceUrl, assets, { observe: 'response' });
  }

  update(assets: IAssets): Observable<EntityResponseType> {
    return this.http.put<IAssets>(`${this.resourceUrl}/${getAssetsIdentifier(assets) as number}`, assets, { observe: 'response' });
  }

  partialUpdate(assets: IAssets): Observable<EntityResponseType> {
    return this.http.patch<IAssets>(`${this.resourceUrl}/${getAssetsIdentifier(assets) as number}`, assets, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAssets>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAssets[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAssetsToCollectionIfMissing(assetsCollection: IAssets[], ...assetsToCheck: (IAssets | null | undefined)[]): IAssets[] {
    const assets: IAssets[] = assetsToCheck.filter(isPresent);
    if (assets.length > 0) {
      const assetsCollectionIdentifiers = assetsCollection.map(assetsItem => getAssetsIdentifier(assetsItem)!);
      const assetsToAdd = assets.filter(assetsItem => {
        const assetsIdentifier = getAssetsIdentifier(assetsItem);
        if (assetsIdentifier == null || assetsCollectionIdentifiers.includes(assetsIdentifier)) {
          return false;
        }
        assetsCollectionIdentifiers.push(assetsIdentifier);
        return true;
      });
      return [...assetsToAdd, ...assetsCollection];
    }
    return assetsCollection;
  }
}
