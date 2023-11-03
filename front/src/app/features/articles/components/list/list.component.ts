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
    );;
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
          response.articles.sort((a, b) => b.createdAt.getTime() - a.createdAt.getTime());
        } else {
          // Trier les articles du plus ancien au plus récent
          response.articles.sort((a, b) => a.createdAt.getTime() - b.createdAt.getTime());
        }
        return response;
      })
    );
  }

  navigateToDetail(articleId  : number) {
    console.log("in navigate :", articleId)
    this.router.navigate(['/articles/detail', articleId]);
  }

  sortArticles(response: ArticlesResponse): void {
    if (this.newersFirst) {
      // Trier les articles du plus récent au plus ancien
      response.articles.sort((a, b) => b.createdAt.getTime() - a.createdAt.getTime());
    } else {
      // Trier les articles du plus ancien au plus récent
      response.articles.sort((a, b) => a.createdAt.getTime() - b.createdAt.getTime());
    }
  }
}
