import { Component } from '@angular/core';
import { User } from 'src/app/interfaces/user.interface';
import { SessionService } from 'src/app/services/session.service';
import { ArticlesService } from '../../services/articles.service';
import { Article } from '../../interfaces/article.interface';
import { ArticlesResponse } from '../../interfaces/api/articlesResponse.interface';
import { Observable, map, of } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent {

  public articles$: Observable<ArticlesResponse>;
  public newersFirst : boolean = true;


  constructor(
    private sessionService: SessionService,
    private articlesService: ArticlesService,
    private router: Router
  ) {
    
    this.articles$ = this.articlesService.all().pipe(
      map((response: ArticlesResponse) => {
        console.log("response", response);
        //this.sortArticles(response);
        return response;
      })
    );
   }

  get user(): User | undefined {
    return this.sessionService.user;
  }

  toggleIconClick() {
    this.newersFirst = !this.newersFirst;
    this.articles$ = this.articles$.pipe(
      map((response: ArticlesResponse) => {
        if (this.newersFirst) {
          // Trier les articles du plus récent au plus ancien
          this.sortArticlesByDate(response.articles, false);
        } else {
          // Trier les articles du plus ancien au plus récent
          this.sortArticlesByDate(response.articles, true);
        }
        return response;
      })
    );
  }

  navigateToDetail(articleId  : number) {
    console.log("in navigate :", articleId)
    this.router.navigate(['/articles/detail', articleId]);
  }

  sortArticlesByDate(articles: Article[], ascending: boolean = true): Article[] {
    // Utilisez la méthode `sort` pour trier le tableau d'articles
    return articles.sort((a, b) => {
      const dateA = new Date(a.createdAt).getTime();
      const dateB = new Date(b.createdAt).getTime();
  
      if (ascending) {
        // Tri croissant (du plus ancien au plus récent)
        return dateA - dateB;
      } else {
        // Tri décroissant (du plus récent au plus ancien)
        return dateB - dateA;
      }
    });
  }
}
