import {SubscriptionPlan} from "./SubscriptionPlan";

export class RegistrationPerson {
  id:number;
  name: string;
  email: string;
  password:string;
  isSubscriptionEnabled: boolean;
  subscriptionPlan: SubscriptionPlan;
}
