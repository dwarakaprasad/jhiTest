import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PassportService } from '../service/passport.service';

import { PassportComponent } from './passport.component';

describe('Passport Management Component', () => {
  let comp: PassportComponent;
  let fixture: ComponentFixture<PassportComponent>;
  let service: PassportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PassportComponent],
    })
      .overrideTemplate(PassportComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PassportComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PassportService);

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
    expect(comp.passports?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
