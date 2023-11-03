import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ThemesResponse } from '../interfaces/api/themesResponse.interface';


@Injectable({
  providedIn: 'root'
})
export class ThemesService {

  private pathService = 'api/themes';

  constructor(private httpClient: HttpClient) { }

  public all(): Observable<ThemesResponse> {
    return this.httpClient.get<ThemesResponse>(this.pathService);
  }

}
