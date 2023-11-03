import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { AuthSuccess  } from '../interfaces/authSuccess.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { User } from 'src/app/interfaces/user.interface';
import { UserResponse } from 'src/app/interfaces/user.response.interface';
import { MessageResponse } from '../../articles/interfaces/api/messageResponse.interface';
import { environment } from 'src/environments/environment.prod';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private pathService = 'api/auth';

  constructor(private httpClient: HttpClient) { }

  public register(registerRequest: RegisterRequest): Observable<AuthSuccess> {
    console.log("registerRequest", registerRequest);
    const url = `${this.pathService}/register`;
    console.log("url", url);
    return this.httpClient.post<AuthSuccess>(url, registerRequest);
  }

  public login(loginRequest: LoginRequest): Observable<AuthSuccess> {
    return this.httpClient.post<AuthSuccess>(`${this.pathService}/login`, loginRequest);
  }

  public me(): Observable<User> {
    console.log("in me");
    return this.httpClient.get<User>(`${this.pathService}/me`);
  }

  public subscribeToTheme(id: number): Observable<MessageResponse> {
    return this.httpClient.get<MessageResponse>(`${this.pathService}/themes/${id}`);
  }

  public unsubscribeToTheme(id: number): Observable<MessageResponse> {
    console.log("in service unsubToTheme");
    return this.httpClient.put<MessageResponse>(`${this.pathService}/themes/${id}`, null);
  }

  public update(id: number, form: UserResponse): Observable<UserResponse> {
    console.log("in service update");
    return this.httpClient.put<UserResponse>(`${this.pathService}/user/${id}`, form);
  }
}
