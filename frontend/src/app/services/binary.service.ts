import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {BinPerson} from "../models/bin-person";
import {BinaryPerson} from "../models/binary-person";
import {RegistrationPerson} from "../models/registration-person";

@Injectable({
  providedIn: 'root'
})
export class BinaryService {
  private readonly treeUrl: string
  private readonly getBinaryById: string
  private readonly getRegistrationById: string
  private readonly postBinPerson: string;


  constructor(private http: HttpClient) {
    this.treeUrl = 'http://localhost:8080/getTree';
    this.getBinaryById = 'http://localhost:8080/binary/getById';
    this.getRegistrationById = 'http://localhost:8080/registration/getById';
    this.postBinPerson = 'http://localhost:8080/user/register/binary/';
  }

  public getTree(id: number): Observable<Set<BinPerson>> {
    let requestParams = new HttpParams();
    requestParams = requestParams.append("id", id)
    return this.http.get<Set<BinPerson>>(this.treeUrl, {params: requestParams})
  }

  public getBinaryPersonById(id: number): Observable<BinPerson> {
    let requestParams = new HttpParams()
    requestParams = requestParams.append("id", id);
    return this.http.get<BinPerson>(this.getBinaryById, {params: requestParams})
  }

  public getRegistrationPersonById(id: number): Observable<RegistrationPerson> {
    let requestParams = new HttpParams()
    requestParams = requestParams.append("id", id);
    return this.http.get<RegistrationPerson>(this.getRegistrationById, {params: requestParams})
  }

  public postBinaryPerson(childId: number, binPerson: BinaryPerson):Observable<any> {
    console.log(this.postBinPerson + childId)
    console.log(binPerson)
    return this.http.post<BinaryPerson>(this.postBinPerson+childId, binPerson);
  }

}
