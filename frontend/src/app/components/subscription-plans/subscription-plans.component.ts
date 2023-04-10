import { Component } from '@angular/core';

@Component({
  selector: 'app-subscription-plans',
  templateUrl: './subscription-plans.component.html',
  styleUrls: ['./subscription-plans.component.css']
})
export class SubscriptionPlansComponent {
  name: string = 'Platinum';
  flatPercent: number = 5;
  price: number = 500;
  level1: number = 5;
  level2: number = 4;
  level3: number = 3;
  level4: number = 2;
  isEligibleForBinary: boolean = true;
}
