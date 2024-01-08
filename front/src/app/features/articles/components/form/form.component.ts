import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ArticleResponseCreate } from '../../interfaces/api/articleResponse.create.interface';
import { Theme } from '../../../themes/interfaces/theme.interface';
import { ArticlesService } from '../../services/articles.service';
import { ThemesService } from 'src/app/features/themes/services/themes.service';
import { Observable } from 'rxjs';
import { ThemesResponse } from 'src/app/features/themes/interfaces/api/themesResponse.interface';
import { ArticleCreate } from '../../interfaces/article.create.interface';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss']
})
export class FormComponent implements OnInit {

  public articleForm: FormGroup | undefined;
  public themes$!: Observable<ThemesResponse>;
  constructor(
    private fb: FormBuilder,
    private matSnackBar: MatSnackBar,
    private articlesService: ArticlesService,
    private router: Router,
    private themeService : ThemesService
  ) {
  }

  public ngOnInit(): void {
    this.themes$ = this.themeService.all();  
    this.initForm();
  }

  public submit(): void {
    let selectedTheme : Theme | undefined;; 
    this.themes$.subscribe((themesResponse: ThemesResponse) => {
      selectedTheme = themesResponse.themes.find((theme) => theme.title === this.articleForm?.get('theme')?.value);
           if (selectedTheme) {
        const articleData: ArticleCreate = {
          title: this.articleForm!.get('title')?.value,
          theme_id: selectedTheme.id, 
          content: this.articleForm!.get('content')?.value
        };
        this.articlesService
          .create(articleData)
          .subscribe((articleResponse: ArticleResponseCreate) => this.exitPage(articleResponse));
      }
    });
    
  }

  private initForm(): void {
    this.articleForm = this.fb.group({
      title: ['', [Validators.required]],
      theme: ['', [Validators.required]],
      content: ['', [Validators.required]]
    });
  }

  private exitPage(articleResponse: ArticleResponseCreate): void {
    this.matSnackBar.open("Article créé avec succès", "Close", { duration: 3000 });
    this.router.navigate(['articles']);
  }
}
