import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaymentInfoDetailComponent } from './payment-info-detail.component';

describe('PaymentInfo Management Detail Component', () => {
  let comp: PaymentInfoDetailComponent;
  let fixture: ComponentFixture<PaymentInfoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaymentInfoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ paymentInfo: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PaymentInfoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PaymentInfoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load paymentInfo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.paymentInfo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
