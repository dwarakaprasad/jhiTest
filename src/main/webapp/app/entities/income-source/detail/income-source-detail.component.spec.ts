import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IncomeSourceDetailComponent } from './income-source-detail.component';

describe('IncomeSource Management Detail Component', () => {
  let comp: IncomeSourceDetailComponent;
  let fixture: ComponentFixture<IncomeSourceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IncomeSourceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ incomeSource: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IncomeSourceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IncomeSourceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load incomeSource on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.incomeSource).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
