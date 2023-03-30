import {Component, Input, OnInit} from '@angular/core';
import {Observable, Observer} from "rxjs";
import {DepositWithdrawComponent} from "../deposit-withdraw/deposit-withdraw.component";
import {MatDialog} from "@angular/material/dialog";
import {Person} from "../../models/person";
import {PersonService} from "../../services/person.service";
import {RegistrationPerson} from "../../models/registration-person";
@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit{

  person: RegistrationPerson;

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
  ngOnInit(): void{
    this.personService.getRegisteredPerson().subscribe(
      person=>this.person = person
    )
  }
}
