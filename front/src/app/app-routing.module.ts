import { HomeComponent } from './pages/home/home.component';


import { MeComponent } from './components/me/me.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { AuthGuard } from './guards/auth.guard';
import { UnauthGuard } from './guards/unauth.guard';
import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { ListComponent } from './features/articles/components/list/list.component';
import { LoginComponent } from './features/auth/components/login/login.component';
// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes



const routes: Routes = [
  { path: '', component: ListComponent },
  {
    path: 'rentals',
    //canActivate: [AuthGuard],
    loadChildren: () => import('./features/rentals/rentals.module').then(m => m.RentalsModule)
  },
  {
    path: 'articles',
    //canActivate: [AuthGuard],
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
    //canActivate: [AuthGuard], 
    component: MeComponent
  },
  { path: '404', component: NotFoundComponent },
  { path: '**', redirectTo: '404' }
];

//const routes: Routes = [{ path: '', component: HomeComponent }];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
