import {Component} from '@angular/core';
import {FormBuilder, FormControl, Validators} from "@angular/forms";

@Component({
  selector: 'app-binary-registration',
  templateUrl: './binary-registration.component.html',
  styleUrls: ['./binary-registration.component.css']
})
export class BinaryRegistrationComponent{

  constructor(private fb: FormBuilder) {
  }

  registerForm = this.fb.group({
    direction: new FormControl('Left', [Validators.required])
  });

}
