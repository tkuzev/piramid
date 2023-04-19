import {Component, Inject, Input, Optional, Output} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Deposit} from "../../models/deposit";
import {PersonService} from "../../services/person.service";
import {Withdraw} from "../../models/withdraw";

@Component({
  selector: 'app-deposit-withdraw',
  templateUrl: './deposit-withdraw.component.html',
  styleUrls: ['./deposit-withdraw.component.css']
})
export class DepositWithdrawComponent {

  money: number
  displayedButton !: number;
  constructor(@Optional() @Inject(MAT_DIALOG_DATA) public data: any, private personService: PersonService) {
    this.displayedButton = data.buttonValue;
  }
  deposit() {
    let deposit = new Deposit()
    this.personService.personGetId().subscribe(
      value => {
        deposit.id = value
        deposit.money = this.money
        this.personService.deposit(deposit).subscribe()
      }
    )
  }
  withdraw(){
    let withdraw = new Withdraw()
    this.personService.personGetId().subscribe(
      value => {
        withdraw.id = value
        withdraw.money = this.money
        this.personService.withdraw(withdraw).subscribe()
      }
    )
  }
}
