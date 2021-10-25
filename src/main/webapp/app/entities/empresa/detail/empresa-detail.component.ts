import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmpresa } from '../empresa.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-empresa-detail',
  templateUrl: './empresa-detail.component.html',
})
export class EmpresaDetailComponent implements OnInit {
  empresa: IEmpresa | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empresa }) => {
      this.empresa = empresa;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
