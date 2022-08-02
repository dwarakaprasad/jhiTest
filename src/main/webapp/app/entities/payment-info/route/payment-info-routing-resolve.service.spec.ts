import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPaymentInfo, PaymentInfo } from '../payment-info.model';
import { PaymentInfoService } from '../service/payment-info.service';

import { PaymentInfoRoutingResolveService } from './payment-info-routing-resolve.service';

describe('PaymentInfo routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PaymentInfoRoutingResolveService;
  let service: PaymentInfoService;
  let resultPaymentInfo: IPaymentInfo | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(PaymentInfoRoutingResolveService);
    service = TestBed.inject(PaymentInfoService);
    resultPaymentInfo = undefined;
  });

  describe('resolve', () => {
    it('should return IPaymentInfo returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPaymentInfo = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPaymentInfo).toEqual({ id: 123 });
    });

    it('should return new IPaymentInfo if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPaymentInfo = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPaymentInfo).toEqual(new PaymentInfo());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PaymentInfo })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPaymentInfo = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPaymentInfo).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
