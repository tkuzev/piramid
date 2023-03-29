import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomePageComponent} from "./components/home-page/home-page.component";
import {WalletComponent} from "./components/wallet/wallet.component";
import {ProfileInfoComponent} from "./components/profile-info/profile-info.component";
import {RegistrationFormComponent} from "./components/registration-form/registration-form.component";
import {IncomePageComponent} from "./components/income-page/income-page.component";
import {LoginFormComponent} from "./components/login-form/login-form.component";
import {ChartComponent} from "./components/chart/chart.component";
import {BinaryRegistrationComponent} from "./components/binary-registration/binary-registration.component";


const routes: Routes = [
  {path: '', component: HomePageComponent},
  {path:'wallet', component: WalletComponent},
  {path:'profile', component: ProfileInfoComponent},
  {path:'register', component: RegistrationFormComponent},
  {path:'income', component: IncomePageComponent},
  {path:'login', component: LoginFormComponent},
  {path: 'chart', component: ChartComponent},
  {path:'register/binary',component: BinaryRegistrationComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
