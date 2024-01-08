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
          this.sortArticlesByDate(response.articles, false);
        } else {
          this.sortArticlesByDate(response.articles, true);
        }
        return response;
      })
    );
  }

  navigateToDetail(articleId  : number) {
    this.router.navigate(['/articles/detail', articleId]);
  }

  sortArticlesByDate(articles: Article[], ascending: boolean = true): Article[] {
    return articles.sort((a, b) => {
      const dateA = new Date(a.createdAt).getTime();
      const dateB = new Date(b.createdAt).getTime();
  
      if (ascending) {
        return dateA - dateB;
      } else {
        return dateB - dateA;
      }
    });
  }
}
