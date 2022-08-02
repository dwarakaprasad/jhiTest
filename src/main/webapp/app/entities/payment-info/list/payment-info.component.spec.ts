import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PaymentInfoService } from '../service/payment-info.service';

import { PaymentInfoComponent } from './payment-info.component';

describe('PaymentInfo Management Component', () => {
  let comp: PaymentInfoComponent;
  let fixture: ComponentFixture<PaymentInfoComponent>;
  let service: PaymentInfoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PaymentInfoComponent],
    })
      .overrideTemplate(PaymentInfoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaymentInfoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PaymentInfoService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.paymentInfos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
