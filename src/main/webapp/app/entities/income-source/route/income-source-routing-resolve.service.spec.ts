import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IIncomeSource, IncomeSource } from '../income-source.model';
import { IncomeSourceService } from '../service/income-source.service';

import { IncomeSourceRoutingResolveService } from './income-source-routing-resolve.service';

describe('IncomeSource routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: IncomeSourceRoutingResolveService;
  let service: IncomeSourceService;
  let resultIncomeSource: IIncomeSource | undefined;

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
    routingResolveService = TestBed.inject(IncomeSourceRoutingResolveService);
    service = TestBed.inject(IncomeSourceService);
    resultIncomeSource = undefined;
  });

  describe('resolve', () => {
    it('should return IIncomeSource returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIncomeSource = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultIncomeSource).toEqual({ id: 123 });
    });

    it('should return new IIncomeSource if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIncomeSource = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultIncomeSource).toEqual(new IncomeSource());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as IncomeSource })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIncomeSource = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultIncomeSource).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
