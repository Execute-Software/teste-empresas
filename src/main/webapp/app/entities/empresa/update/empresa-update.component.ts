import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEmpresa, Empresa } from '../empresa.model';
import { EmpresaService } from '../service/empresa.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-empresa-update',
  templateUrl: './empresa-update.component.html',
})
export class EmpresaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required]],
    sobre: [],
    cnpj: [null, []],
    razaoSocial: [],
    dataConstituicao: [],
    logoUrl: [],
    linkFacebook: [],
    linkInstagram: [],
    linkLinkedin: [],
    linkTwitter: [],
    site: [],
    ativo: [],
    criado: [null, [Validators.required]],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected empresaService: EmpresaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empresa }) => {
      if (empresa.id === undefined) {
        const today = dayjs().startOf('day');
        empresa.criado = today;
      }

      this.updateForm(empresa);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('empresasApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const empresa = this.createFromForm();
    if (empresa.id !== undefined) {
      this.subscribeToSaveResponse(this.empresaService.update(empresa));
    } else {
      this.subscribeToSaveResponse(this.empresaService.create(empresa));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpresa>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(empresa: IEmpresa): void {
    this.editForm.patchValue({
      id: empresa.id,
      nome: empresa.nome,
      sobre: empresa.sobre,
      cnpj: empresa.cnpj,
      razaoSocial: empresa.razaoSocial,
      dataConstituicao: empresa.dataConstituicao,
      logoUrl: empresa.logoUrl,
      linkFacebook: empresa.linkFacebook,
      linkInstagram: empresa.linkInstagram,
      linkLinkedin: empresa.linkLinkedin,
      linkTwitter: empresa.linkTwitter,
      site: empresa.site,
      ativo: empresa.ativo,
      criado: empresa.criado ? empresa.criado.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IEmpresa {
    return {
      ...new Empresa(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      sobre: this.editForm.get(['sobre'])!.value,
      cnpj: this.editForm.get(['cnpj'])!.value,
      razaoSocial: this.editForm.get(['razaoSocial'])!.value,
      dataConstituicao: this.editForm.get(['dataConstituicao'])!.value,
      logoUrl: this.editForm.get(['logoUrl'])!.value,
      linkFacebook: this.editForm.get(['linkFacebook'])!.value,
      linkInstagram: this.editForm.get(['linkInstagram'])!.value,
      linkLinkedin: this.editForm.get(['linkLinkedin'])!.value,
      linkTwitter: this.editForm.get(['linkTwitter'])!.value,
      site: this.editForm.get(['site'])!.value,
      ativo: this.editForm.get(['ativo'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
