import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INCOMETYPE } from 'app/entities/enumerations/incometype.model';
import { IIncomeSource, IncomeSource } from '../income-source.model';

import { IncomeSourceService } from './income-source.service';

describe('IncomeSource Service', () => {
  let service: IncomeSourceService;
  let httpMock: HttpTestingController;
  let elemDefault: IIncomeSource;
  let expectedResult: IIncomeSource | IIncomeSource[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IncomeSourceService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      incomeType: INCOMETYPE.JOB,
      incomeAmount: 0,
      duration: 0,
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

    it('should create a IncomeSource', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new IncomeSource()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IncomeSource', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          incomeType: 'BBBBBB',
          incomeAmount: 1,
          duration: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IncomeSource', () => {
      const patchObject = Object.assign(
        {
          incomeAmount: 1,
          duration: 1,
        },
        new IncomeSource()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IncomeSource', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          incomeType: 'BBBBBB',
          incomeAmount: 1,
          duration: 1,
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

    it('should delete a IncomeSource', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addIncomeSourceToCollectionIfMissing', () => {
      it('should add a IncomeSource to an empty array', () => {
        const incomeSource: IIncomeSource = { id: 123 };
        expectedResult = service.addIncomeSourceToCollectionIfMissing([], incomeSource);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(incomeSource);
      });

      it('should not add a IncomeSource to an array that contains it', () => {
        const incomeSource: IIncomeSource = { id: 123 };
        const incomeSourceCollection: IIncomeSource[] = [
          {
            ...incomeSource,
          },
          { id: 456 },
        ];
        expectedResult = service.addIncomeSourceToCollectionIfMissing(incomeSourceCollection, incomeSource);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IncomeSource to an array that doesn't contain it", () => {
        const incomeSource: IIncomeSource = { id: 123 };
        const incomeSourceCollection: IIncomeSource[] = [{ id: 456 }];
        expectedResult = service.addIncomeSourceToCollectionIfMissing(incomeSourceCollection, incomeSource);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(incomeSource);
      });

      it('should add only unique IncomeSource to an array', () => {
        const incomeSourceArray: IIncomeSource[] = [{ id: 123 }, { id: 456 }, { id: 9413 }];
        const incomeSourceCollection: IIncomeSource[] = [{ id: 123 }];
        expectedResult = service.addIncomeSourceToCollectionIfMissing(incomeSourceCollection, ...incomeSourceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const incomeSource: IIncomeSource = { id: 123 };
        const incomeSource2: IIncomeSource = { id: 456 };
        expectedResult = service.addIncomeSourceToCollectionIfMissing([], incomeSource, incomeSource2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(incomeSource);
        expect(expectedResult).toContain(incomeSource2);
      });

      it('should accept null and undefined values', () => {
        const incomeSource: IIncomeSource = { id: 123 };
        expectedResult = service.addIncomeSourceToCollectionIfMissing([], null, incomeSource, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(incomeSource);
      });

      it('should return initial array if no IncomeSource is added', () => {
        const incomeSourceCollection: IIncomeSource[] = [{ id: 123 }];
        expectedResult = service.addIncomeSourceToCollectionIfMissing(incomeSourceCollection, undefined, null);
        expect(expectedResult).toEqual(incomeSourceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
