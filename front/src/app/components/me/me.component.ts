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
    const newEmail = this.userForm!.get('email')?.value;
  
    if (this.user && newEmail !== this.user.email) {
      const confirmMessage = 'En changeant d\'email, vous serez déconnecté de l\'application et vous devrez vous connecter avec votre nouvel email. Continuer ?';
      if (confirm(confirmMessage)) {
        const userData: UserResponse = {
          userName: this.userForm!.get('userName')?.value,
          email: newEmail,
        };
        this.authService.update(this.user.id, userData).subscribe(
          (result: UserResponse) => {
            this.openSnackBar("Profil modifié avec succès", result ? 'Succès' : 'Erreur');
            this.logout();
          },
          (error) => {
            console.error("Erreur lors de l'appel au backend:", error);
          }
        );
      }
    } else {
      const userData: UserResponse = {
        userName: this.userForm!.get('userName')?.value,
        email: newEmail,
      };
     if(this.user) {
      this.authService.update(this.user.id, userData).subscribe(
        (result: UserResponse) => {
          this.openSnackBar("Profil modifié avec succès", result ? 'Succès' : 'Erreur');
        },
        (error) => {
          console.error("Erreur lors de l'appel au backend:", error);
        }
      );
     }
      
    }
  }


  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 3000, 
    });
  }

  unsubscribeTheme(themeId: number) {
    this.authService.unsubscribeToTheme(themeId).subscribe((result: MessageResponse) => {
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
