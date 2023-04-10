import { SubscriptionPlan } from "./SubscriptionPlan";

export class kur{
    id:number;
    name:string;
    email:string;
    parentId:number;
    money:number;
    password:string;
    subscriptionEnabled:Boolean;
    subscriptionPlan:SubscriptionPlan;
    subscriptionExpirationDate:Date;
}