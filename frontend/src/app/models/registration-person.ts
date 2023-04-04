import {SubscriptionPlan} from "./SubscriptionPlan";

export class RegistrationPerson {
  name: string;
  email: string;
  parentId: number;
  money: number;
  password:string;
  isSubscriptionEnabled: boolean;
  subscriptionPlan: SubscriptionPlan;
}
