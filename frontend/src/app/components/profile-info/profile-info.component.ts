import {Component} from '@angular/core';
import {FormBuilder, FormControl, Validators} from "@angular/forms";
import {ThemePalette} from "@angular/material/core";

@Component({
  selector: 'app-profile-info',
  templateUrl: './profile-info.component.html',
  styleUrls: ['./profile-info.component.css']
})
export class ProfileInfoComponent {
  color: ThemePalette = "primary";
  public showPassword: boolean = false;
  constructor(private fb: FormBuilder) {
  }

  profileForm = this.fb.group({
    name: new FormControl('', [Validators.required, Validators.pattern('[a-zA-Z]+'), Validators.maxLength(25)]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.pattern('^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$')]),
    plan: new FormControl('Bronze', [Validators.required])
  });

  public togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }
}

