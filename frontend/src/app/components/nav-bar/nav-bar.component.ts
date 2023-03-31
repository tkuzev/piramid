import {AfterViewInit, ChangeDetectorRef, Component, Inject, Input, OnInit} from '@angular/core';
import {DepositWithdrawComponent} from "../deposit-withdraw/deposit-withdraw.component";
import {MatDialog} from "@angular/material/dialog";
import {PersonService} from "../../services/person.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit{

  money: Array<number>;
  balance: number = 0;
  leftC: number = 0;
  rightC: number = 0;

  buttonValue : number

  constructor(public dialog: MatDialog,
              private personService: PersonService,
              private router: Router,
              private changeDetector: ChangeDetectorRef) {
  }

  withdraw(): void {
    this.buttonValue = 1;
    this.dialog.open(DepositWithdrawComponent, {
      height: '250px',
      width: '400px',
      data: {buttonValue: this.buttonValue}
    });
  }

  deposit(): void {
    this.buttonValue = 2;
    this.dialog.open(DepositWithdrawComponent, {
      height: '250px',
      width: '400px',
      data: {buttonValue: this.buttonValue}
    });
  }
  logout(){
    this.personService.logout()
    this.balance = 0;
    this.leftC = 0;
    this.rightC = 0;
    this.router.navigate(['/'])

  }

  refresh(){
  }
  ngOnInit(): void {
    this.method()
  }

  method(){
    this.personService.getRegisteredPersonBalance().then(value => {
        value.subscribe(
          data => {
            this.money = data
            if(this.money[0] != null) {
              this.balance = this.money[0]
            }
            if(this.money[1] != null) {
              this.leftC = this.money[1]
            }
            if(this.money[1] != null) {
              this.rightC=this.money[2]
            }
          }
        )
      }
  )

  }
}
