import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {RegistrationPerson} from "../models/registration-person";
import {Sell} from "../models/sell";
import {BinaryPerson} from "../models/binary-person";
import {LoginPerson} from "../models/login-person";
import {map, Observable} from "rxjs";
import { JwtHelperService } from '@auth0/angular-jwt';
import jwt_decode from 'jwt-decode';


@Injectable({
  providedIn: 'root'
})
export class PersonService {

  private readonly usersUrl: string;
  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8080/auth';
  }

  public login(loginPerson: LoginPerson):Observable<any>{
      return this.http.post<LoginPerson>(this.usersUrl+"/login",loginPerson).pipe(map(response =>{
        const token = response;
        if(token){
          localStorage.setItem('currentUserEmail',loginPerson.email)
          localStorage.setItem('currentUser',JSON.stringify({email:loginPerson.email,token}))
        }
        return response;
      }));
  }


  public getLoggedPersonId(){
    const helper = new JwtHelperService();
    const token = localStorage.getItem('CurrentUser')
    const decodedToken = helper.decodeToken(token);
    console.log(decodedToken)
  }

  public logout(){
    localStorage.removeItem('currentUser');
  }

  isLoggedIn() {
    console.log("current users email is:"+localStorage.getItem('currentUserEmail'))
    console.log(localStorage.getItem('currentUser'));
    console.log(JSON.parse(localStorage.getItem("email")))
    console.log(!!localStorage.getItem('currentUser')); // Check if the auth_token exists in localStorage
  }
  public makeSell(sell: Sell){
    return this.http.post<Sell>(this.usersUrl+"/sell",sell);
  }

  public registerBinaryPerson(binaryPerson: BinaryPerson){
    return this.http.post<BinaryPerson>(this.usersUrl+'/register/binary/${this.binaryPersonId}',binaryPerson);
  }
}
