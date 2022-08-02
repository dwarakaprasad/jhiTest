import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ASSETTYPE } from 'app/entities/enumerations/assettype.model';
import { IAssets, Assets } from '../assets.model';

import { AssetsService } from './assets.service';

describe('Assets Service', () => {
  let service: AssetsService;
  let httpMock: HttpTestingController;
  let elemDefault: IAssets;
  let expectedResult: IAssets | IAssets[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssetsService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      assetType: ASSETTYPE.MOVABLE,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Assets', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Assets()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Assets', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          assetType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Assets', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new Assets()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Assets', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          assetType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Assets', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAssetsToCollectionIfMissing', () => {
      it('should add a Assets to an empty array', () => {
        const assets: IAssets = { id: 123 };
        expectedResult = service.addAssetsToCollectionIfMissing([], assets);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assets);
      });

      it('should not add a Assets to an array that contains it', () => {
        const assets: IAssets = { id: 123 };
        const assetsCollection: IAssets[] = [
          {
            ...assets,
          },
          { id: 456 },
        ];
        expectedResult = service.addAssetsToCollectionIfMissing(assetsCollection, assets);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Assets to an array that doesn't contain it", () => {
        const assets: IAssets = { id: 123 };
        const assetsCollection: IAssets[] = [{ id: 456 }];
        expectedResult = service.addAssetsToCollectionIfMissing(assetsCollection, assets);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assets);
      });

      it('should add only unique Assets to an array', () => {
        const assetsArray: IAssets[] = [{ id: 123 }, { id: 456 }, { id: 85652 }];
        const assetsCollection: IAssets[] = [{ id: 123 }];
        expectedResult = service.addAssetsToCollectionIfMissing(assetsCollection, ...assetsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assets: IAssets = { id: 123 };
        const assets2: IAssets = { id: 456 };
        expectedResult = service.addAssetsToCollectionIfMissing([], assets, assets2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assets);
        expect(expectedResult).toContain(assets2);
      });

      it('should accept null and undefined values', () => {
        const assets: IAssets = { id: 123 };
        expectedResult = service.addAssetsToCollectionIfMissing([], null, assets, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assets);
      });

      it('should return initial array if no Assets is added', () => {
        const assetsCollection: IAssets[] = [{ id: 123 }];
        expectedResult = service.addAssetsToCollectionIfMissing(assetsCollection, undefined, null);
        expect(expectedResult).toEqual(assetsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
