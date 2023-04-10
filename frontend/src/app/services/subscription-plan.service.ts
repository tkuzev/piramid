import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {SubscriptionPlan} from "../models/SubscriptionPlan";

@Injectable({
  providedIn: 'root'
})
export class SubscriptionPlanService {
  private readonly usersUrl: string;
  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8080/user';
  }

  getPlans(): Observable<SubscriptionPlan[]> {
    return this.http.get<SubscriptionPlan[]>(this.usersUrl + '/getAllSubscriptionPlans');
  }
}
