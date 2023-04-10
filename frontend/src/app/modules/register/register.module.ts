import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {Router, RouterModule, Routes} from "@angular/router";
import {RegistrationFormComponent} from "../../components/registration-form/registration-form.component";

const routes: Routes = [
  {
    path: '', component:RegistrationFormComponent
  }
]

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forChild(routes),
    CommonModule
  ]
})
export class RegisterModule { }
