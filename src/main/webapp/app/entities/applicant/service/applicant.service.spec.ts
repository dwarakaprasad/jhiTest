import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { GENDER } from 'app/entities/enumerations/gender.model';
import { IApplicant, Applicant } from '../applicant.model';

import { ApplicantService } from './applicant.service';

describe('Applicant Service', () => {
  let service: ApplicantService;
  let httpMock: HttpTestingController;
  let elemDefault: IApplicant;
  let expectedResult: IApplicant | IApplicant[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ApplicantService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      firstName: 'AAAAAAA',
      middleName: 'AAAAAAA',
      lastName: 'AAAAAAA',
      dob: currentDate,
      gender: GENDER.MALE,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dob: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Applicant', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dob: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dob: currentDate,
        },
        returnedFromService
      );

      service.create(new Applicant()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Applicant', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          firstName: 'BBBBBB',
          middleName: 'BBBBBB',
          lastName: 'BBBBBB',
          dob: currentDate.format(DATE_FORMAT),
          gender: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dob: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Applicant', () => {
      const patchObject = Object.assign(
        {
          firstName: 'BBBBBB',
          middleName: 'BBBBBB',
          lastName: 'BBBBBB',
          dob: currentDate.format(DATE_FORMAT),
        },
        new Applicant()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dob: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Applicant', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          firstName: 'BBBBBB',
          middleName: 'BBBBBB',
          lastName: 'BBBBBB',
          dob: currentDate.format(DATE_FORMAT),
          gender: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dob: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Applicant', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addApplicantToCollectionIfMissing', () => {
      it('should add a Applicant to an empty array', () => {
        const applicant: IApplicant = { id: 123 };
        expectedResult = service.addApplicantToCollectionIfMissing([], applicant);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(applicant);
      });

      it('should not add a Applicant to an array that contains it', () => {
        const applicant: IApplicant = { id: 123 };
        const applicantCollection: IApplicant[] = [
          {
            ...applicant,
          },
          { id: 456 },
        ];
        expectedResult = service.addApplicantToCollectionIfMissing(applicantCollection, applicant);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Applicant to an array that doesn't contain it", () => {
        const applicant: IApplicant = { id: 123 };
        const applicantCollection: IApplicant[] = [{ id: 456 }];
        expectedResult = service.addApplicantToCollectionIfMissing(applicantCollection, applicant);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(applicant);
      });

      it('should add only unique Applicant to an array', () => {
        const applicantArray: IApplicant[] = [{ id: 123 }, { id: 456 }, { id: 4541 }];
        const applicantCollection: IApplicant[] = [{ id: 123 }];
        expectedResult = service.addApplicantToCollectionIfMissing(applicantCollection, ...applicantArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const applicant: IApplicant = { id: 123 };
        const applicant2: IApplicant = { id: 456 };
        expectedResult = service.addApplicantToCollectionIfMissing([], applicant, applicant2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(applicant);
        expect(expectedResult).toContain(applicant2);
      });

      it('should accept null and undefined values', () => {
        const applicant: IApplicant = { id: 123 };
        expectedResult = service.addApplicantToCollectionIfMissing([], null, applicant, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(applicant);
      });

      it('should return initial array if no Applicant is added', () => {
        const applicantCollection: IApplicant[] = [{ id: 123 }];
        expectedResult = service.addApplicantToCollectionIfMissing(applicantCollection, undefined, null);
        expect(expectedResult).toEqual(applicantCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
