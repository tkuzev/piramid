import {Component, Inject, Input, Optional, Output} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-deposit-withdraw',
  templateUrl: './deposit-withdraw.component.html',
  styleUrls: ['./deposit-withdraw.component.css']
})
export class DepositWithdrawComponent {
   displayedButton !: number;
   input = document.getElementById("input")
  constructor(@Optional() @Inject(MAT_DIALOG_DATA) public data: any) {
    this.displayedButton = data.buttonValue;
  }
  submitForm(){

  }
}
