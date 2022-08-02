import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApplicantDetailComponent } from './applicant-detail.component';

describe('Applicant Management Detail Component', () => {
  let comp: ApplicantDetailComponent;
  let fixture: ComponentFixture<ApplicantDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ApplicantDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ applicant: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ApplicantDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ApplicantDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load applicant on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.applicant).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
