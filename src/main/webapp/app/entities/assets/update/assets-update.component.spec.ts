import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AssetsService } from '../service/assets.service';
import { IAssets, Assets } from '../assets.model';
import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';

import { AssetsUpdateComponent } from './assets-update.component';

describe('Assets Management Update Component', () => {
  let comp: AssetsUpdateComponent;
  let fixture: ComponentFixture<AssetsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetsService: AssetsService;
  let addressService: AddressService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AssetsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AssetsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetsService = TestBed.inject(AssetsService);
    addressService = TestBed.inject(AddressService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call address query and add missing value', () => {
      const assets: IAssets = { id: 456 };
      const address: IAddress = { id: 43120 };
      assets.address = address;

      const addressCollection: IAddress[] = [{ id: 32637 }];
      jest.spyOn(addressService, 'query').mockReturnValue(of(new HttpResponse({ body: addressCollection })));
      const expectedCollection: IAddress[] = [address, ...addressCollection];
      jest.spyOn(addressService, 'addAddressToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assets });
      comp.ngOnInit();

      expect(addressService.query).toHaveBeenCalled();
      expect(addressService.addAddressToCollectionIfMissing).toHaveBeenCalledWith(addressCollection, address);
      expect(comp.addressesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const assets: IAssets = { id: 456 };
      const address: IAddress = { id: 8657 };
      assets.address = address;

      activatedRoute.data = of({ assets });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(assets));
      expect(comp.addressesCollection).toContain(address);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Assets>>();
      const assets = { id: 123 };
      jest.spyOn(assetsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assets });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assets }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetsService.update).toHaveBeenCalledWith(assets);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Assets>>();
      const assets = new Assets();
      jest.spyOn(assetsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assets });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assets }));
      saveSubject.complete();

      // THEN
      expect(assetsService.create).toHaveBeenCalledWith(assets);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Assets>>();
      const assets = { id: 123 };
      jest.spyOn(assetsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assets });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetsService.update).toHaveBeenCalledWith(assets);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAddressById', () => {
      it('Should return tracked Address primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAddressById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
