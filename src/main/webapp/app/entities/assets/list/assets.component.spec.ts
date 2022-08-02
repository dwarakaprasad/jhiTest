import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AssetsService } from '../service/assets.service';

import { AssetsComponent } from './assets.component';

describe('Assets Management Component', () => {
  let comp: AssetsComponent;
  let fixture: ComponentFixture<AssetsComponent>;
  let service: AssetsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AssetsComponent],
    })
      .overrideTemplate(AssetsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AssetsService);

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
    expect(comp.assets?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
