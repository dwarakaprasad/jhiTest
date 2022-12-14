import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployerService } from '../service/employer.service';
import { IEmployer, Employer } from '../employer.model';

import { EmployerUpdateComponent } from './employer-update.component';

describe('Employer Management Update Component', () => {
  let comp: EmployerUpdateComponent;
  let fixture: ComponentFixture<EmployerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employerService: EmployerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployerUpdateComponent],
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
      .overrideTemplate(EmployerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employerService = TestBed.inject(EmployerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const employer: IEmployer = { id: 456 };

      activatedRoute.data = of({ employer });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(employer));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Employer>>();
      const employer = { id: 123 };
      jest.spyOn(employerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employer }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(employerService.update).toHaveBeenCalledWith(employer);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Employer>>();
      const employer = new Employer();
      jest.spyOn(employerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employer }));
      saveSubject.complete();

      // THEN
      expect(employerService.create).toHaveBeenCalledWith(employer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Employer>>();
      const employer = { id: 123 };
      jest.spyOn(employerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employerService.update).toHaveBeenCalledWith(employer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
