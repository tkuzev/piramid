import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomePageComponent} from "./components/home-page/home-page.component";
import {ProfileInfoComponent} from "./components/profile-info/profile-info.component";
import {RegistrationFormComponent} from "./components/registration-form/registration-form.component";
import {IncomePageComponent} from "./components/income-page/income-page.component";
import {LoginFormComponent} from "./components/login-form/login-form.component";
import {ChartComponent} from "./components/chart/chart.component";
import {BinaryRegistrationComponent} from "./components/binary-registration/binary-registration.component";
import {NavBarComponent} from "./components/nav-bar/nav-bar.component";
import {ChangePasswordComponent} from "./components/change-password/change-password.component";
import {SubscriptionPlansComponent} from "./components/subscription-plans/subscription-plans.component";


const routes: Routes = [
  {
    path: 'wrapper', component: NavBarComponent,
    children:[
      {path:'income', component: IncomePageComponent},
      {path:'home', component: HomePageComponent},
      {path:'profile', component: ProfileInfoComponent},
      {path:'register', component: RegistrationFormComponent},
      {path:'login', component: LoginFormComponent},
      {
        path:'profile',
        loadChildren: ()=>import('src/app/modules/profile-info/profile-info.module').then(m=>m.ProfileInfoModule)
      },
      {
        path:'register',
        loadChildren: ()=>import('src/app/modules/register/register.module').then(m=>m.RegisterModule)
      },
      {
        path:'login',
        loadChildren: ()=>import('src/app/modules/login/login.module').then(m=>m.LoginModule)
      },
      {path: 'chart', component: ChartComponent},
      {path:'register/binary',component: BinaryRegistrationComponent},
      {path:'changePassword',component: ChangePasswordComponent},
      {path:'plans',component: SubscriptionPlansComponent},
    ]
  },
  {path: "", component: NavBarComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
