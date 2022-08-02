import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IncomeSourceService } from '../service/income-source.service';
import { IIncomeSource, IncomeSource } from '../income-source.model';
import { IEmployer } from 'app/entities/employer/employer.model';
import { EmployerService } from 'app/entities/employer/service/employer.service';
import { IApplicant } from 'app/entities/applicant/applicant.model';
import { ApplicantService } from 'app/entities/applicant/service/applicant.service';

import { IncomeSourceUpdateComponent } from './income-source-update.component';

describe('IncomeSource Management Update Component', () => {
  let comp: IncomeSourceUpdateComponent;
  let fixture: ComponentFixture<IncomeSourceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let incomeSourceService: IncomeSourceService;
  let employerService: EmployerService;
  let applicantService: ApplicantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IncomeSourceUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(IncomeSourceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IncomeSourceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    incomeSourceService = TestBed.inject(IncomeSourceService);
    employerService = TestBed.inject(EmployerService);
    applicantService = TestBed.inject(ApplicantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call employer query and add missing value', () => {
      const incomeSource: IIncomeSource = { id: 456 };
      const employer: IEmployer = { id: 93794 };
      incomeSource.employer = employer;

      const employerCollection: IEmployer[] = [{ id: 6617 }];
      jest.spyOn(employerService, 'query').mockReturnValue(of(new HttpResponse({ body: employerCollection })));
      const expectedCollection: IEmployer[] = [employer, ...employerCollection];
      jest.spyOn(employerService, 'addEmployerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ incomeSource });
      comp.ngOnInit();

      expect(employerService.query).toHaveBeenCalled();
      expect(employerService.addEmployerToCollectionIfMissing).toHaveBeenCalledWith(employerCollection, employer);
      expect(comp.employersCollection).toEqual(expectedCollection);
    });

    it('Should call Applicant query and add missing value', () => {
      const incomeSource: IIncomeSource = { id: 456 };
      const applicant: IApplicant = { id: 8214 };
      incomeSource.applicant = applicant;

      const applicantCollection: IApplicant[] = [{ id: 23358 }];
      jest.spyOn(applicantService, 'query').mockReturnValue(of(new HttpResponse({ body: applicantCollection })));
      const additionalApplicants = [applicant];
      const expectedCollection: IApplicant[] = [...additionalApplicants, ...applicantCollection];
      jest.spyOn(applicantService, 'addApplicantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ incomeSource });
      comp.ngOnInit();

      expect(applicantService.query).toHaveBeenCalled();
      expect(applicantService.addApplicantToCollectionIfMissing).toHaveBeenCalledWith(applicantCollection, ...additionalApplicants);
      expect(comp.applicantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const incomeSource: IIncomeSource = { id: 456 };
      const employer: IEmployer = { id: 6194 };
      incomeSource.employer = employer;
      const applicant: IApplicant = { id: 53489 };
      incomeSource.applicant = applicant;

      activatedRoute.data = of({ incomeSource });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(incomeSource));
      expect(comp.employersCollection).toContain(employer);
      expect(comp.applicantsSharedCollection).toContain(applicant);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IncomeSource>>();
      const incomeSource = { id: 123 };
      jest.spyOn(incomeSourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incomeSource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: incomeSource }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(incomeSourceService.update).toHaveBeenCalledWith(incomeSource);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IncomeSource>>();
      const incomeSource = new IncomeSource();
      jest.spyOn(incomeSourceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incomeSource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: incomeSource }));
      saveSubject.complete();

      // THEN
      expect(incomeSourceService.create).toHaveBeenCalledWith(incomeSource);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IncomeSource>>();
      const incomeSource = { id: 123 };
      jest.spyOn(incomeSourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incomeSource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(incomeSourceService.update).toHaveBeenCalledWith(incomeSource);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEmployerById', () => {
      it('Should return tracked Employer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEmployerById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackApplicantById', () => {
      it('Should return tracked Applicant primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApplicantById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
