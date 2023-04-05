import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Sell} from "../models/sell";
import {BinaryPerson} from "../models/binary-person";
import {LoginPerson} from "../models/login-person";
import {firstValueFrom, map, Observable, Subscription} from "rxjs";
import {RegistrationPerson} from "../models/registration-person";


@Injectable({
  providedIn: 'root'
})
export class PersonService {

  private readonly usersUrl: string;
  private readonly authUrl: string;

  id: number
  email: string = localStorage.getItem('currentUserEmail')
  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8080/user';
    this.authUrl = 'http://localhost:8080/auth';
  }

  public login(loginPerson: LoginPerson):Observable<any>{
      return this.http.post<LoginPerson>(this.authUrl+"/login",loginPerson).pipe(map(response =>{
        const token = response;
        if(token){
          localStorage.setItem('currentUserEmail',loginPerson.email)
          localStorage.setItem('currentUser',JSON.stringify({email:loginPerson.email,token}))
        }
        return response;
      }));
  }
  personGetId(): Observable<number>{
    let requestParams = new HttpParams();
    requestParams = requestParams.append("email", this.email)
    return this.http.get<number>(this.usersUrl+'/getPersonId', {params:requestParams})
  }

  // public getLoggedPersonId(){
  //   return this.http.post(this.usersUrl+"/getPersonId",) localStorage.getItem("currentUserEmail")
  // }

  public registerPerson(registerPerson: RegistrationPerson){
    return this.http.post<RegistrationPerson>(this.usersUrl+"register",registerPerson);
  }

  public logout(){
    localStorage.clear();
  }

  isLoggedIn(): boolean {
    return localStorage.getItem('currentUser') != null;
  }
  public makeSell(sell: Sell){
    return this.http.post<Sell>(this.usersUrl+"/sell",sell);
  }

  public registerBinaryPerson(binaryPerson: BinaryPerson){
    return this.http.post<BinaryPerson>(this.usersUrl+'/register/binary/${this.binaryPersonId}',binaryPerson);
  }

  async getRegisteredPersonBalance(){
    let requestParams = new HttpParams();
    this.id = await firstValueFrom(this.personGetId());
    requestParams = requestParams.append('id', this.id);
    // console.log(requestParams)
    return this.http.get<any>(this.usersUrl + '/wallet/balance', {params: requestParams});
  }

  async monthlyIncome(){
    let requestParams = new HttpParams();
    this.id = await firstValueFrom(this.personGetId());
    requestParams = requestParams.append('id', this.id);
    // this.chartSubscription = this.http.get<any>(this.usersUrl + '/income', {params: requestParams}).subscribe(
    //   value => (console.log())
    // )
    return this.http.get<any>(this.usersUrl + '/income', {params: requestParams})
  }


  getProfileInfo(): Observable<any>{
    let requestParams = new HttpParams();
    requestParams = requestParams.append('email', this.email);
    console.log(requestParams)
    return this.http.get<any>(this.usersUrl + '/getPersonDetails', {params: requestParams});
  }

  editProfile(personData: any): Observable<any>{
    return this.http.put<any>(this.usersUrl + '/edit', personData);
  }
}
