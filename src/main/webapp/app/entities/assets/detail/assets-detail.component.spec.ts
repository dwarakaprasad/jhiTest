import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AssetsDetailComponent } from './assets-detail.component';

describe('Assets Management Detail Component', () => {
  let comp: AssetsDetailComponent;
  let fixture: ComponentFixture<AssetsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssetsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ assets: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AssetsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssetsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load assets on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.assets).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
