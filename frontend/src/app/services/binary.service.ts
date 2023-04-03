import {Injectable, OnInit} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {map, Observable, of} from "rxjs";
import {BinPerson} from "../models/bin-person";
import {ActivatedRoute} from "@angular/router";
import {BinaryPerson} from "../models/binary-person";

@Injectable({
  providedIn: 'root'
})
export class BinaryService{
  private readonly treeUrl: string
  private readonly getBinaryById:string

  constructor(private http: HttpClient) {
    this.treeUrl = 'http://localhost:8080/getTree';
    this.getBinaryById='http://localhost:8080/binary/getById';
  }

  public getTree(id: number): Observable<BinPerson[]> {

    let requestParams = new HttpParams();

    requestParams = requestParams.append("id", id)

    return this.http.get<BinPerson[]>(this.treeUrl, {params: requestParams}).pipe(map(response =>{
      if (response){
        return Object.values(response); //This will return the array of object values.
      }
      return [];
    }))
  }

  public getBinaryPersonById(id:number):Observable<BinPerson>{
    let requestParams=new HttpParams()
    requestParams=requestParams.append("id",id);
    return this.http.get<BinPerson>(this.getBinaryById,{params:requestParams})
  }

}
