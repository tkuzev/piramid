import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {BinPerson} from "../models/bin-person";

@Injectable({
  providedIn: 'root'
})
export class IncomeService{
  private readonly incomeUrl:string


  constructor(private http:HttpClient) {
    this.incomeUrl = "http://localhost:8080/income/";
  }

  // public getIncome(id:number):Observable<Map<string,number>>{
  //
  //   let requestParams = new HttpParams();
  //
  //   requestParams = requestParams.append("id", id)
  //
  // }
}
