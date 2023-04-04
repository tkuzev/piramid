import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {ThemePalette} from "@angular/material/core";
import { PersonService } from 'src/app/services/person.service';
import {RegistrationPerson} from "../../models/registration-person";
import {exhaustMap} from "rxjs";

@Component({
  selector: 'app-profile-info',
  templateUrl: './profile-info.component.html',
  styleUrls: ['./profile-info.component.css']
})
export class ProfileInfoComponent implements OnInit {
  color: ThemePalette = "primary";
  showPassword: boolean = false;
  profileData: RegistrationPerson;
  editingMode: boolean = false;
  profileForm: FormGroup;
  constructor(private fb: FormBuilder, private  personService: PersonService) { }

  ngOnInit() {
    console.log(localStorage.getItem('currentUserEmail'))
    this.profileForm = this.fb.group({
      name: new FormControl({value: '', disabled: true}, [Validators.required, Validators.pattern('[a-zA-Z]+'), Validators.maxLength(25)]),
      email: new FormControl({value: '', disabled: true},[Validators.required, Validators.email]),
      password: new FormControl({value: '', disabled: true},[Validators.required, Validators.pattern('^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$')]),
      subscriptionPlan: new FormControl({value: '', disabled: true}, [Validators.required]),
      // isSubscriptionEnabled: new FormControl({value:'', disabled: true})
    });

    this.personService.getProfileInfo().subscribe(data=>{
      this.profileData = data;

      this.profileForm.setValue({
        name: this.profileData.name,
        email: this.profileData.email,
        password: this.profileData.password,
        subscriptionPlan: this.profileData.subscriptionPlan.name,
        isSubscriptionEnabled: this.profileData.isSubscriptionEnabled
      });
    });
  }

  toggleEditMode(): void {
    this.editingMode = !this.editingMode;
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  edit() {
    this.personService.editProfile(this.profileForm.value).subscribe(data=>{
      //popup success
    })
  }
}

