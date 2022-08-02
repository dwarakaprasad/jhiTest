import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PaymentInfoService } from '../service/payment-info.service';
import { IPaymentInfo, PaymentInfo } from '../payment-info.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';

import { PaymentInfoUpdateComponent } from './payment-info-update.component';

describe('PaymentInfo Management Update Component', () => {
  let comp: PaymentInfoUpdateComponent;
  let fixture: ComponentFixture<PaymentInfoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paymentInfoService: PaymentInfoService;
  let customerService: CustomerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PaymentInfoUpdateComponent],
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
      .overrideTemplate(PaymentInfoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaymentInfoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paymentInfoService = TestBed.inject(PaymentInfoService);
    customerService = TestBed.inject(CustomerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Customer query and add missing value', () => {
      const paymentInfo: IPaymentInfo = { id: 456 };
      const customer: ICustomer = { id: 5228 };
      paymentInfo.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 81349 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paymentInfo });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(customerCollection, ...additionalCustomers);
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const paymentInfo: IPaymentInfo = { id: 456 };
      const customer: ICustomer = { id: 90859 };
      paymentInfo.customer = customer;

      activatedRoute.data = of({ paymentInfo });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(paymentInfo));
      expect(comp.customersSharedCollection).toContain(customer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PaymentInfo>>();
      const paymentInfo = { id: 123 };
      jest.spyOn(paymentInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paymentInfo }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(paymentInfoService.update).toHaveBeenCalledWith(paymentInfo);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PaymentInfo>>();
      const paymentInfo = new PaymentInfo();
      jest.spyOn(paymentInfoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paymentInfo }));
      saveSubject.complete();

      // THEN
      expect(paymentInfoService.create).toHaveBeenCalledWith(paymentInfo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PaymentInfo>>();
      const paymentInfo = { id: 123 };
      jest.spyOn(paymentInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paymentInfoService.update).toHaveBeenCalledWith(paymentInfo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCustomerById', () => {
      it('Should return tracked Customer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCustomerById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
