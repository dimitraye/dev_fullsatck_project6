import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { User } from 'src/app/interfaces/user.interface';
import { UserResponse } from 'src/app/interfaces/user.response.interface';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MessageResponse } from 'src/app/features/themes/interfaces/api/messageResponse.interface';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';
import { fi } from 'date-fns/locale';

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


  /* public submit(): void {
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
  } */
  
  public submit(): void {
    const newEmail = this.userForm!.get('email')?.value;
  
    if (this.user && newEmail !== this.user.email) {
      // L'e-mail a été modifié, affichez la boîte de dialogue de confirmation
      const confirmMessage = 'En changeant d\'email, vous serez déconnecté de l\'application et vous devrez vous connecter avec votre nouvel email. Continuer ?';
      if (confirm(confirmMessage)) {
        const userData: UserResponse = {
          userName: this.userForm!.get('userName')?.value,
          email: newEmail,
        };
        this.authService.update(this.user.id, userData).subscribe(
          (result: UserResponse) => {
            this.openSnackBar("Profil modifié avec succès", result ? 'Succès' : 'Erreur');
            // Déconnectez l'utilisateur après la mise à jour de l'e-mail
            this.logout();
          },
          (error) => {
            console.error("Erreur lors de l'appel au backend:", error);
          }
        );
      }
    } else {
      // L'e-mail n'a pas été modifié, procédez normalement
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
