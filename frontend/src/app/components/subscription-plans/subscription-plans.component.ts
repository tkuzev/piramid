import {Component, OnInit} from '@angular/core';
import { SubscriptionPlanService } from 'src/app/services/subscription-plan.service';
import {SubscriptionPlan} from "../../models/SubscriptionPlan";

@Component({
  selector: 'app-subscription-plans',
  templateUrl: './subscription-plans.component.html',
  styleUrls: ['./subscription-plans.component.css']
})

export class SubscriptionPlansComponent implements OnInit {
  name: string;
  flatPercent: number = 5;
  plans: SubscriptionPlan[];

  constructor(private subscriptionPlanService: SubscriptionPlanService) {
  }
  ngOnInit(): void {
    this.subscriptionPlanService.getPlans().subscribe((plans => {
      this.plans = plans;
    }));
  }
}
