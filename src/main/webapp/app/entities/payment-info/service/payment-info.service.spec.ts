import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { PAYMENTTYPE } from 'app/entities/enumerations/paymenttype.model';
import { IPaymentInfo, PaymentInfo } from '../payment-info.model';

import { PaymentInfoService } from './payment-info.service';

describe('PaymentInfo Service', () => {
  let service: PaymentInfoService;
  let httpMock: HttpTestingController;
  let elemDefault: IPaymentInfo;
  let expectedResult: IPaymentInfo | IPaymentInfo[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PaymentInfoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      paymentType: PAYMENTTYPE.CREDIT,
      number: 0,
      expiry: currentDate,
      security: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          expiry: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PaymentInfo', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          expiry: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          expiry: currentDate,
        },
        returnedFromService
      );

      service.create(new PaymentInfo()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PaymentInfo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          paymentType: 'BBBBBB',
          number: 1,
          expiry: currentDate.format(DATE_FORMAT),
          security: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          expiry: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PaymentInfo', () => {
      const patchObject = Object.assign(
        {
          paymentType: 'BBBBBB',
        },
        new PaymentInfo()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          expiry: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PaymentInfo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          paymentType: 'BBBBBB',
          number: 1,
          expiry: currentDate.format(DATE_FORMAT),
          security: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          expiry: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PaymentInfo', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPaymentInfoToCollectionIfMissing', () => {
      it('should add a PaymentInfo to an empty array', () => {
        const paymentInfo: IPaymentInfo = { id: 123 };
        expectedResult = service.addPaymentInfoToCollectionIfMissing([], paymentInfo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paymentInfo);
      });

      it('should not add a PaymentInfo to an array that contains it', () => {
        const paymentInfo: IPaymentInfo = { id: 123 };
        const paymentInfoCollection: IPaymentInfo[] = [
          {
            ...paymentInfo,
          },
          { id: 456 },
        ];
        expectedResult = service.addPaymentInfoToCollectionIfMissing(paymentInfoCollection, paymentInfo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PaymentInfo to an array that doesn't contain it", () => {
        const paymentInfo: IPaymentInfo = { id: 123 };
        const paymentInfoCollection: IPaymentInfo[] = [{ id: 456 }];
        expectedResult = service.addPaymentInfoToCollectionIfMissing(paymentInfoCollection, paymentInfo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paymentInfo);
      });

      it('should add only unique PaymentInfo to an array', () => {
        const paymentInfoArray: IPaymentInfo[] = [{ id: 123 }, { id: 456 }, { id: 81522 }];
        const paymentInfoCollection: IPaymentInfo[] = [{ id: 123 }];
        expectedResult = service.addPaymentInfoToCollectionIfMissing(paymentInfoCollection, ...paymentInfoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const paymentInfo: IPaymentInfo = { id: 123 };
        const paymentInfo2: IPaymentInfo = { id: 456 };
        expectedResult = service.addPaymentInfoToCollectionIfMissing([], paymentInfo, paymentInfo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paymentInfo);
        expect(expectedResult).toContain(paymentInfo2);
      });

      it('should accept null and undefined values', () => {
        const paymentInfo: IPaymentInfo = { id: 123 };
        expectedResult = service.addPaymentInfoToCollectionIfMissing([], null, paymentInfo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paymentInfo);
      });

      it('should return initial array if no PaymentInfo is added', () => {
        const paymentInfoCollection: IPaymentInfo[] = [{ id: 123 }];
        expectedResult = service.addPaymentInfoToCollectionIfMissing(paymentInfoCollection, undefined, null);
        expect(expectedResult).toEqual(paymentInfoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
