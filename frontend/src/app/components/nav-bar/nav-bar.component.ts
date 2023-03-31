import {AfterViewInit, Component, Input, OnInit} from '@angular/core';
import {DepositWithdrawComponent} from "../deposit-withdraw/deposit-withdraw.component";
import {MatDialog} from "@angular/material/dialog";
import {PersonService} from "../../services/person.service";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements AfterViewInit{

  balance: number[];

  buttonValue !: number
  constructor(public dialog: MatDialog, private personService: PersonService) {
  }
  withdraw():void{
    this.buttonValue = 1;
    this.dialog.open(DepositWithdrawComponent, {
      height: '250px',
      width: '400px',
      data: {buttonValue: this.buttonValue}
    });
  }
  deposit():void{
    this.buttonValue = 2;
    this.dialog.open(DepositWithdrawComponent, {
      height: '250px',
      width: '400px',
      data: {buttonValue: this.buttonValue}
    });
  }
  ngAfterViewInit(): void{
    this.personService.getRegisteredPersonBalance().then(value => {value.subscribe(data=>this.balance[0]=data)}
    )
  }
}
