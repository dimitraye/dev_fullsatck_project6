import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FormComponent } from './components/form/form.component';
import { DetailComponent } from './components/detail/detail.component';
import { ListComponent } from './components/list/list.component';


const routes: Routes = [
  { title: 'Themes', path: '', component: ListComponent },
  { title: 'Themes - detail', path: 'detail/:id', component: DetailComponent },
  { title: 'Themes - update', path: 'update/:id', component: FormComponent },
  { title: 'Themes - create', path: 'create', component: FormComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ThemeRoutingModule { }
