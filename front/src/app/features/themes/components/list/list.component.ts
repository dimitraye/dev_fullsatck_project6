import { Component } from '@angular/core';
import { User } from 'src/app/interfaces/user.interface';
import { SessionService } from 'src/app/services/session.service';
import { ThemesService } from '../../services/themes.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { MessageResponse } from 'src/app/features/articles/interfaces/api/messageResponse.interface';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent {

  public themes$ = this.themesService.all();

  constructor(
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private themesService: ThemesService
  ) 
  { 
    this.themes$ = this.themesService.all();
  }

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 3000, // Durée d'affichage de la notification en millisecondes (3 secondes dans cet exemple)
    });
  }

  subscribeToTheme(themeId: number) {
    this.authService.subscribeToTheme(themeId).subscribe((result: MessageResponse) => {
      this.openSnackBar(result.message, result.message ? 'Succès' : 'Erreur');
    });
  }
}
