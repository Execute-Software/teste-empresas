import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmpresa, Empresa } from '../empresa.model';

import { EmpresaService } from './empresa.service';

describe('Empresa Service', () => {
  let service: EmpresaService;
  let httpMock: HttpTestingController;
  let elemDefault: IEmpresa;
  let expectedResult: IEmpresa | IEmpresa[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmpresaService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nome: 'AAAAAAA',
      sobre: 'AAAAAAA',
      cnpj: 'AAAAAAA',
      razaoSocial: 'AAAAAAA',
      dataConstituicao: currentDate,
      logoUrl: 'AAAAAAA',
      linkFacebook: 'AAAAAAA',
      linkInstagram: 'AAAAAAA',
      linkLinkedin: 'AAAAAAA',
      linkTwitter: 'AAAAAAA',
      site: 'AAAAAAA',
      ativo: false,
      criado: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dataConstituicao: currentDate.format(DATE_FORMAT),
          criado: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Empresa', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dataConstituicao: currentDate.format(DATE_FORMAT),
          criado: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataConstituicao: currentDate,
          criado: currentDate,
        },
        returnedFromService
      );

      service.create(new Empresa()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Empresa', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nome: 'BBBBBB',
          sobre: 'BBBBBB',
          cnpj: 'BBBBBB',
          razaoSocial: 'BBBBBB',
          dataConstituicao: currentDate.format(DATE_FORMAT),
          logoUrl: 'BBBBBB',
          linkFacebook: 'BBBBBB',
          linkInstagram: 'BBBBBB',
          linkLinkedin: 'BBBBBB',
          linkTwitter: 'BBBBBB',
          site: 'BBBBBB',
          ativo: true,
          criado: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataConstituicao: currentDate,
          criado: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Empresa', () => {
      const patchObject = Object.assign(
        {
          nome: 'BBBBBB',
          sobre: 'BBBBBB',
          dataConstituicao: currentDate.format(DATE_FORMAT),
          logoUrl: 'BBBBBB',
          linkFacebook: 'BBBBBB',
          linkInstagram: 'BBBBBB',
          linkLinkedin: 'BBBBBB',
          site: 'BBBBBB',
          ativo: true,
        },
        new Empresa()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dataConstituicao: currentDate,
          criado: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Empresa', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nome: 'BBBBBB',
          sobre: 'BBBBBB',
          cnpj: 'BBBBBB',
          razaoSocial: 'BBBBBB',
          dataConstituicao: currentDate.format(DATE_FORMAT),
          logoUrl: 'BBBBBB',
          linkFacebook: 'BBBBBB',
          linkInstagram: 'BBBBBB',
          linkLinkedin: 'BBBBBB',
          linkTwitter: 'BBBBBB',
          site: 'BBBBBB',
          ativo: true,
          criado: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataConstituicao: currentDate,
          criado: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Empresa', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEmpresaToCollectionIfMissing', () => {
      it('should add a Empresa to an empty array', () => {
        const empresa: IEmpresa = { id: 123 };
        expectedResult = service.addEmpresaToCollectionIfMissing([], empresa);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(empresa);
      });

      it('should not add a Empresa to an array that contains it', () => {
        const empresa: IEmpresa = { id: 123 };
        const empresaCollection: IEmpresa[] = [
          {
            ...empresa,
          },
          { id: 456 },
        ];
        expectedResult = service.addEmpresaToCollectionIfMissing(empresaCollection, empresa);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Empresa to an array that doesn't contain it", () => {
        const empresa: IEmpresa = { id: 123 };
        const empresaCollection: IEmpresa[] = [{ id: 456 }];
        expectedResult = service.addEmpresaToCollectionIfMissing(empresaCollection, empresa);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(empresa);
      });

      it('should add only unique Empresa to an array', () => {
        const empresaArray: IEmpresa[] = [{ id: 123 }, { id: 456 }, { id: 93482 }];
        const empresaCollection: IEmpresa[] = [{ id: 123 }];
        expectedResult = service.addEmpresaToCollectionIfMissing(empresaCollection, ...empresaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const empresa: IEmpresa = { id: 123 };
        const empresa2: IEmpresa = { id: 456 };
        expectedResult = service.addEmpresaToCollectionIfMissing([], empresa, empresa2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(empresa);
        expect(expectedResult).toContain(empresa2);
      });

      it('should accept null and undefined values', () => {
        const empresa: IEmpresa = { id: 123 };
        expectedResult = service.addEmpresaToCollectionIfMissing([], null, empresa, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(empresa);
      });

      it('should return initial array if no Empresa is added', () => {
        const empresaCollection: IEmpresa[] = [{ id: 123 }];
        expectedResult = service.addEmpresaToCollectionIfMissing(empresaCollection, undefined, null);
        expect(expectedResult).toEqual(empresaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
