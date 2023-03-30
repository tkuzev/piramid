import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn:'root'
})
export class BinaryService{
  private treeUrl: string


  constructor(http: HttpClient) {
    this.treeUrl = 'http://localhost:8080/getKids/{id}';
  }

  private getId(id:number){

  }
}
