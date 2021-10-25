import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'empresa',
        data: { pageTitle: 'empresasApp.empresa.home.title' },
        loadChildren: () => import('./empresa/empresa.module').then(m => m.EmpresaModule),
      },
      {
        path: 'endereco',
        data: { pageTitle: 'empresasApp.endereco.home.title' },
        loadChildren: () => import('./endereco/endereco.module').then(m => m.EnderecoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
