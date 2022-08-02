import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { IncomeSourceService } from '../service/income-source.service';

import { IncomeSourceComponent } from './income-source.component';

describe('IncomeSource Management Component', () => {
  let comp: IncomeSourceComponent;
  let fixture: ComponentFixture<IncomeSourceComponent>;
  let service: IncomeSourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [IncomeSourceComponent],
    })
      .overrideTemplate(IncomeSourceComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IncomeSourceComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(IncomeSourceService);

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
    expect(comp.incomeSources?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
