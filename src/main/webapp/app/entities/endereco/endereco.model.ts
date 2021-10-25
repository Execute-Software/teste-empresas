import { IEmpresa } from 'app/entities/empresa/empresa.model';

export interface IEndereco {
  id?: number;
  nome?: string;
  cep?: string;
  logradouro?: string | null;
  numero?: string | null;
  complemento?: string | null;
  bairro?: string | null;
  cidade?: string | null;
  estado?: string | null;
  empresa?: IEmpresa | null;
}

export class Endereco implements IEndereco {
  constructor(
    public id?: number,
    public nome?: string,
    public cep?: string,
    public logradouro?: string | null,
    public numero?: string | null,
    public complemento?: string | null,
    public bairro?: string | null,
    public cidade?: string | null,
    public estado?: string | null,
    public empresa?: IEmpresa | null
  ) {}
}

export function getEnderecoIdentifier(endereco: IEndereco): number | undefined {
  return endereco.id;
}
