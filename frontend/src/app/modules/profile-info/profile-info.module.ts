import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {Router, RouterModule, Routes} from "@angular/router";
import {ProfileInfoComponent} from "../../components/profile-info/profile-info.component";

const routes: Routes = [
  {
    path: '',
    component: ProfileInfoComponent
  }
]
@NgModule({
  declarations: [],
  imports: [
    RouterModule.forChild(routes),
    CommonModule
  ]
})
export class ProfileInfoModule { }
