import { Component } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-registration-form',
  templateUrl: './registration-form.component.html',
  styleUrls: ['./registration-form.component.css']
})
export class RegistrationFormComponent {
  constructor(private fb:FormBuilder) {
  }
  newReg =this.fb.group({

    name: new FormControl('', [Validators.required, Validators.pattern('[a-zA-Z]+')]
    ),

    email: new FormControl('', [Validators.required, Validators.email]),
    parentId: new FormControl('', [Validators.required, Validators.pattern('[0-9]')]),
    money: new FormControl('', [Validators.required, Validators.pattern('[0-9]')]),
    password: new FormControl('', [Validators.required, Validators.pattern('^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$')])
  });

}
