import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import { HttpClientModule } from '@angular/common/http';
import {NavBarComponent} from './components/nav-bar/nav-bar.component';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from "@angular/material/button";
import {RouterLink} from "@angular/router";
import {MatGridListModule} from "@angular/material/grid-list";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatFormFieldModule, MAT_FORM_FIELD_DEFAULT_OPTIONS} from "@angular/material/form-field";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AppRoutingModule} from "./app-routing.module";
import {MatInputModule} from '@angular/material/input';
import {HomePageComponent} from './components/home-page/home-page.component';
import {RegistrationFormComponent} from './components/registration-form/registration-form.component';
import {ProfileInfoComponent} from './components/profile-info/profile-info.component';
import {IncomePageComponent} from './components/income-page/income-page.component';
import {LoginFormComponent} from './components/login-form/login-form.component';
import {WalletComponent} from './components/wallet/wallet.component';
import {MatIconModule} from "@angular/material/icon";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {BrowserModule} from "@angular/platform-browser";
import { ChartComponent } from './components/chart/chart.component';
import {NgChartsModule} from "ng2-charts";
import {MatSelectModule} from "@angular/material/select";
import {MatMenuModule} from "@angular/material/menu";
import { DepositWithdrawComponent } from './components/deposit-withdraw/deposit-withdraw.component';
import {MatDialogModule} from "@angular/material/dialog";

@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent,
    HomePageComponent,
    RegistrationFormComponent,
    ProfileInfoComponent,
    IncomePageComponent,
    LoginFormComponent,
    WalletComponent,
    ChartComponent,
    DepositWithdrawComponent,
  ],
  imports: [
    MatToolbarModule,
    MatButtonModule,
    HttpClientModule,
    RouterLink,
    MatGridListModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatIconModule,
    FormsModule,
    AppRoutingModule,
    MatInputModule,
    MatProgressBarModule,
    ReactiveFormsModule,
    NgChartsModule,
    BrowserModule,
    MatSelectModule,
    MatMenuModule,
    MatDialogModule
  ],
  providers: [{provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: {appearance: 'outline'}}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
