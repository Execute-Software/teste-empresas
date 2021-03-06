import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmpresa, getEmpresaIdentifier } from '../empresa.model';

export type EntityResponseType = HttpResponse<IEmpresa>;
export type EntityArrayResponseType = HttpResponse<IEmpresa[]>;

@Injectable({ providedIn: 'root' })
export class EmpresaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/empresas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(empresa: IEmpresa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(empresa);
    return this.http
      .post<IEmpresa>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(empresa: IEmpresa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(empresa);
    return this.http
      .put<IEmpresa>(`${this.resourceUrl}/${getEmpresaIdentifier(empresa) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(empresa: IEmpresa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(empresa);
    return this.http
      .patch<IEmpresa>(`${this.resourceUrl}/${getEmpresaIdentifier(empresa) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEmpresa>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmpresa[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEmpresaToCollectionIfMissing(empresaCollection: IEmpresa[], ...empresasToCheck: (IEmpresa | null | undefined)[]): IEmpresa[] {
    const empresas: IEmpresa[] = empresasToCheck.filter(isPresent);
    if (empresas.length > 0) {
      const empresaCollectionIdentifiers = empresaCollection.map(empresaItem => getEmpresaIdentifier(empresaItem)!);
      const empresasToAdd = empresas.filter(empresaItem => {
        const empresaIdentifier = getEmpresaIdentifier(empresaItem);
        if (empresaIdentifier == null || empresaCollectionIdentifiers.includes(empresaIdentifier)) {
          return false;
        }
        empresaCollectionIdentifiers.push(empresaIdentifier);
        return true;
      });
      return [...empresasToAdd, ...empresaCollection];
    }
    return empresaCollection;
  }

  protected convertDateFromClient(empresa: IEmpresa): IEmpresa {
    return Object.assign({}, empresa, {
      dataConstituicao: empresa.dataConstituicao?.isValid() ? empresa.dataConstituicao.format(DATE_FORMAT) : undefined,
      criado: empresa.criado?.isValid() ? empresa.criado.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataConstituicao = res.body.dataConstituicao ? dayjs(res.body.dataConstituicao) : undefined;
      res.body.criado = res.body.criado ? dayjs(res.body.criado) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((empresa: IEmpresa) => {
        empresa.dataConstituicao = empresa.dataConstituicao ? dayjs(empresa.dataConstituicao) : undefined;
        empresa.criado = empresa.criado ? dayjs(empresa.criado) : undefined;
      });
    }
    return res;
  }
}
