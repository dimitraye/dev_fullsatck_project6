import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ArticlesResponse } from '../interfaces/api/articlesResponse.interface';
import { ArticleView } from '../interfaces/article.view.interface';
import { ArticleResponseCreate } from '../interfaces/api/articleResponse.create.interface';
import { ArticleCreate } from '../interfaces/article.create.interface';


@Injectable({
  providedIn: 'root'
})
export class ArticlesService {

  private pathService = 'api/articles';

  constructor(private httpClient: HttpClient) { }

  public all(): Observable<ArticlesResponse> {
    return this.httpClient.get<ArticlesResponse>(this.pathService);
  }

  public detail(id: string): Observable<ArticleView> {
    return this.httpClient.get<ArticleView>(`${this.pathService}/${id}`);
  }

  public create(form: ArticleCreate): Observable<ArticleResponseCreate> {
    return this.httpClient.post<ArticleResponseCreate>(this.pathService, form);
  }
}
