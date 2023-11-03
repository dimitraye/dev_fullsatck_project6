import { MeComponent } from './components/me/me.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { AuthGuard } from './guards/auth.guard';
import { UnauthGuard } from './guards/unauth.guard';
import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes



const routes: Routes = [
  {
    path: 'articles',
    canActivate: [AuthGuard],
    loadChildren: () => import('./features/articles/articles.module').then(m => m.ArticlesModule)
  },
  {
    path: 'themes',
    canActivate: [AuthGuard],
    loadChildren: () => import('./features/themes/themes.module').then(m => m.ThemesModule)
  },
  {
    path: '',
    canActivate: [UnauthGuard],
    loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: 'me',
    canActivate: [AuthGuard], 
    component: MeComponent
  },
  { path: '404', component: NotFoundComponent },
  { path: '**', redirectTo: '404' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
