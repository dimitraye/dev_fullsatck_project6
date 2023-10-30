import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Rental } from 'src/app/features/rentals/interfaces/rental.interface';
import { RentalResponse } from '../interfaces/api/rentalResponse.interface';
import { RentalsResponse } from '../interfaces/api/rentalsResponse.interface';
import { ArticleResponse } from '../interfaces/api/articleResponse.interface';
import { Article } from '../interfaces/article.interface';
import { ArticlesResponse } from '../interfaces/api/articlesResponse.interface';


@Injectable({
  providedIn: 'root'
})
export class ArticlesService {

  private pathService = 'api/articles';

  constructor(private httpClient: HttpClient) { }

  public all(): Observable<ArticlesResponse> {
    return this.httpClient.get<ArticlesResponse>(this.pathService);
  }

  public detail(id: string): Observable<Article> {
    return this.httpClient.get<Article>(`${this.pathService}/${id}`);
  }

  public create(form: FormData): Observable<ArticleResponse> {
    return this.httpClient.post<ArticleResponse>(this.pathService, form);
  }

  /*public update(id: string, form: FormData): Observable<RentalResponse> {
    return this.httpClient.put<RentalResponse>(`${this.pathService}/${id}`, form);
  }*/
}
