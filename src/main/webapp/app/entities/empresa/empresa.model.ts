import * as dayjs from 'dayjs';

export interface IEmpresa {
  id?: number;
  nome?: string;
  sobre?: string | null;
  cnpj?: string | null;
  razaoSocial?: string | null;
  dataConstituicao?: dayjs.Dayjs | null;
  logoUrl?: string | null;
  linkFacebook?: string | null;
  linkInstagram?: string | null;
  linkLinkedin?: string | null;
  linkTwitter?: string | null;
  site?: string | null;
  ativo?: boolean | null;
  criado?: dayjs.Dayjs;
}

export class Empresa implements IEmpresa {
  constructor(
    public id?: number,
    public nome?: string,
    public sobre?: string | null,
    public cnpj?: string | null,
    public razaoSocial?: string | null,
    public dataConstituicao?: dayjs.Dayjs | null,
    public logoUrl?: string | null,
    public linkFacebook?: string | null,
    public linkInstagram?: string | null,
    public linkLinkedin?: string | null,
    public linkTwitter?: string | null,
    public site?: string | null,
    public ativo?: boolean | null,
    public criado?: dayjs.Dayjs
  ) {
    this.ativo = this.ativo ?? false;
  }
}

export function getEmpresaIdentifier(empresa: IEmpresa): number | undefined {
  return empresa.id;
}
