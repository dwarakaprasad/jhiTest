import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CustomerService } from '../service/customer.service';
import { ICustomer, Customer } from '../customer.model';
import { IPassport } from 'app/entities/passport/passport.model';
import { PassportService } from 'app/entities/passport/service/passport.service';

import { CustomerUpdateComponent } from './customer-update.component';

describe('Customer Management Update Component', () => {
  let comp: CustomerUpdateComponent;
  let fixture: ComponentFixture<CustomerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let customerService: CustomerService;
  let passportService: PassportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CustomerUpdateComponent],
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
      .overrideTemplate(CustomerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CustomerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    customerService = TestBed.inject(CustomerService);
    passportService = TestBed.inject(PassportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call passport query and add missing value', () => {
      const customer: ICustomer = { id: 456 };
      const passport: IPassport = { id: 8105 };
      customer.passport = passport;

      const passportCollection: IPassport[] = [{ id: 99066 }];
      jest.spyOn(passportService, 'query').mockReturnValue(of(new HttpResponse({ body: passportCollection })));
      const expectedCollection: IPassport[] = [passport, ...passportCollection];
      jest.spyOn(passportService, 'addPassportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ customer });
      comp.ngOnInit();

      expect(passportService.query).toHaveBeenCalled();
      expect(passportService.addPassportToCollectionIfMissing).toHaveBeenCalledWith(passportCollection, passport);
      expect(comp.passportsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const customer: ICustomer = { id: 456 };
      const passport: IPassport = { id: 10164 };
      customer.passport = passport;

      activatedRoute.data = of({ customer });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(customer));
      expect(comp.passportsCollection).toContain(passport);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Customer>>();
      const customer = { id: 123 };
      jest.spyOn(customerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customer }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(customerService.update).toHaveBeenCalledWith(customer);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Customer>>();
      const customer = new Customer();
      jest.spyOn(customerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customer }));
      saveSubject.complete();

      // THEN
      expect(customerService.create).toHaveBeenCalledWith(customer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Customer>>();
      const customer = { id: 123 };
      jest.spyOn(customerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(customerService.update).toHaveBeenCalledWith(customer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPassportById', () => {
      it('Should return tracked Passport primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPassportById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
