import { SubscriptionPlan } from "./SubscriptionPlan";

export class kur{
    id:number;
    name:string;
    email:string;
    parentId:number;
    money:number;
    password:string;
    subscriptionPlan:SubscriptionPlan;
    subscriptionExpirationDate:Date;
}
