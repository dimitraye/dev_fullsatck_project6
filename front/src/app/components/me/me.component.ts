import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { User } from 'src/app/interfaces/user.interface';
import { UserResponse } from 'src/app/interfaces/user.response.interface';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MessageResponse } from 'src/app/features/themes/interfaces/api/messageResponse.interface';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss']
})
export class MeComponent implements OnInit {
  public userForm: FormGroup | undefined;
  public user: User | undefined;

  constructor(
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private fb: FormBuilder,
    private router: Router,
    private sessionService: SessionService) { }

  public ngOnInit(): void {    
    this.authService.me().subscribe(
      (user: User) => {
        this.user = user;
        this.initForm(this.user);
      },
      (error) => {
        console.error("Erreur lors de l'appel au backend:", error);
      }
    );
  }


  public submit(): void {
    console.log("this.userForm :", this.userForm); 
    const userData : UserResponse = {
      userName: this.userForm!.get('userName')?.value,
      email: this.userForm!.get('email')?.value
    }; 
    console.log("userData :", userData); 
    if(this.user) {
      this.authService.update(this.user.id , userData).subscribe((result: UserResponse) => {
        console.log("auth service update return", result);
        this.openSnackBar("Profil modifié avec succès", result ? 'Succès' : 'Erreur');
      },
      (error) => {
        console.error("Erreur lors de l'appel au backend:", error);
      }
    );
    }
  }


  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 3000, // Durée d'affichage de la notification en millisecondes (3 secondes dans cet exemple)
    });
  }

  unsubscribeTheme(themeId: number) {
    this.authService.unsubscribeToTheme(themeId).subscribe((result: MessageResponse) => {
      console.log("result", result);
      this.openSnackBar(result.message, result.message ? 'Succès' : 'Erreur');
    },
    (error) => {
      console.error("Erreur lors de l'appel au backend:", error);
    }
  );
  }

  public back() {
    window.history.back();
  }

  private initForm(user?: User): void {
    this.userForm = this.fb.group({
      userName: [user ? user.userName : '', [Validators.required]],
      email: [user ? user.email : '', [Validators.required]]
    });
  }

  public logout(): void {
    this.sessionService.logOut();
    this.router.navigate([''])
  }
}
