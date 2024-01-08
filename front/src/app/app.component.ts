import { Component, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { AuthService } from './features/auth/services/auth.service';
import { User } from './interfaces/user.interface';
import { SessionService } from './services/session.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent implements OnInit {
  constructor(
    private authService: AuthService,
    private router: Router,
    private sessionService: SessionService) {
  }

  public ngOnInit(): void {
    this.autoLog();
  }

  public $isLogged(): Observable<boolean> {
    return this.sessionService.$isLogged();
  }

  public logout(): void {
    this.sessionService.logOut();
    this.router.navigate([''])
  }

  public autoLog(): void {
    this.authService.me().subscribe(
      (user: User) => {
        this.sessionService.logIn(user);
      },
      (_) => {
        this.sessionService.logOut();
      }
    )
  }

  isAuthPage(): Observable<boolean> {
    return new Observable(observer => {
      observer.next(this.router.url === '/login' || this.router.url === '/register');
      observer.complete();
    });
  }
}
