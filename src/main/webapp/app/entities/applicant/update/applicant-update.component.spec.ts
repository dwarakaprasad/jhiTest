import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ApplicantService } from '../service/applicant.service';
import { IApplicant, Applicant } from '../applicant.model';
import { IAssets } from 'app/entities/assets/assets.model';
import { AssetsService } from 'app/entities/assets/service/assets.service';
import { IApplication } from 'app/entities/application/application.model';
import { ApplicationService } from 'app/entities/application/service/application.service';

import { ApplicantUpdateComponent } from './applicant-update.component';

describe('Applicant Management Update Component', () => {
  let comp: ApplicantUpdateComponent;
  let fixture: ComponentFixture<ApplicantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let applicantService: ApplicantService;
  let assetsService: AssetsService;
  let applicationService: ApplicationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ApplicantUpdateComponent],
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
      .overrideTemplate(ApplicantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApplicantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    applicantService = TestBed.inject(ApplicantService);
    assetsService = TestBed.inject(AssetsService);
    applicationService = TestBed.inject(ApplicationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Assets query and add missing value', () => {
      const applicant: IApplicant = { id: 456 };
      const assets: IAssets[] = [{ id: 62442 }];
      applicant.assets = assets;

      const assetsCollection: IAssets[] = [{ id: 49783 }];
      jest.spyOn(assetsService, 'query').mockReturnValue(of(new HttpResponse({ body: assetsCollection })));
      const additionalAssets = [...assets];
      const expectedCollection: IAssets[] = [...additionalAssets, ...assetsCollection];
      jest.spyOn(assetsService, 'addAssetsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ applicant });
      comp.ngOnInit();

      expect(assetsService.query).toHaveBeenCalled();
      expect(assetsService.addAssetsToCollectionIfMissing).toHaveBeenCalledWith(assetsCollection, ...additionalAssets);
      expect(comp.assetsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Application query and add missing value', () => {
      const applicant: IApplicant = { id: 456 };
      const application: IApplication = { id: 95925 };
      applicant.application = application;

      const applicationCollection: IApplication[] = [{ id: 8463 }];
      jest.spyOn(applicationService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationCollection })));
      const additionalApplications = [application];
      const expectedCollection: IApplication[] = [...additionalApplications, ...applicationCollection];
      jest.spyOn(applicationService, 'addApplicationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ applicant });
      comp.ngOnInit();

      expect(applicationService.query).toHaveBeenCalled();
      expect(applicationService.addApplicationToCollectionIfMissing).toHaveBeenCalledWith(applicationCollection, ...additionalApplications);
      expect(comp.applicationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const applicant: IApplicant = { id: 456 };
      const assets: IAssets = { id: 55299 };
      applicant.assets = [assets];
      const application: IApplication = { id: 49943 };
      applicant.application = application;

      activatedRoute.data = of({ applicant });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(applicant));
      expect(comp.assetsSharedCollection).toContain(assets);
      expect(comp.applicationsSharedCollection).toContain(application);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Applicant>>();
      const applicant = { id: 123 };
      jest.spyOn(applicantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: applicant }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(applicantService.update).toHaveBeenCalledWith(applicant);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Applicant>>();
      const applicant = new Applicant();
      jest.spyOn(applicantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: applicant }));
      saveSubject.complete();

      // THEN
      expect(applicantService.create).toHaveBeenCalledWith(applicant);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Applicant>>();
      const applicant = { id: 123 };
      jest.spyOn(applicantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(applicantService.update).toHaveBeenCalledWith(applicant);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAssetsById', () => {
      it('Should return tracked Assets primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAssetsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackApplicationById', () => {
      it('Should return tracked Application primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApplicationById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedAssets', () => {
      it('Should return option if no Assets is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedAssets(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Assets for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedAssets(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Assets is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedAssets(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
