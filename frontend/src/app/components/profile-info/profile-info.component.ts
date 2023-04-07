import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {ThemePalette} from "@angular/material/core";
import { PersonService } from 'src/app/services/person.service';
import {RegistrationPerson} from "../../models/registration-person";
import {Router} from "@angular/router";

@Component({
  selector: 'app-profile-info',
  templateUrl: './profile-info.component.html',
  styleUrls: ['./profile-info.component.css']
})
export class ProfileInfoComponent implements OnInit {
  color: ThemePalette = "primary";
  profileData: RegistrationPerson = new RegistrationPerson();
  enableEdit: boolean = true;
  enableSave: boolean = false;
  profileForm: FormGroup;
  status: string;
  exp_date: Date;
  plan: string;
  currentDate: Date = new Date();
  formattedExpDate: string;
  autoPay: boolean;

  constructor(private fb: FormBuilder, private  personService: PersonService) { }

  ngOnInit() {
    this.profileForm = this.fb.group({
      name: new FormControl({value: '', disabled: true}, [Validators.required, Validators.pattern('[a-zA-Z]+'), Validators.maxLength(25)]),
      email: new FormControl({value: '', disabled: true},[Validators.required, Validators.email]),
      subscriptionEnabled: new FormControl({value: true, disabled: true},[Validators.required])
    });

    this.personService.getProfileInfo().subscribe(data=>{
      this.profileData = data;

      this.profileForm.setValue({
        name: this.profileData.name,
        email: this.profileData.email,
        subscriptionEnabled: this.profileData.subscriptionEnabled
      });

      this.plan = this.profileData.subscriptionPlan.name;
      this.exp_date = new Date(this.profileData.subscriptionExpirationDate);
      this.formattedExpDate = `${this.exp_date.getDate()}.${this.exp_date.getMonth() + 1}.${this.exp_date.getFullYear()}`;
      this.autoPay = this.profileData.subscriptionEnabled;

      if(this.currentDate > this.exp_date){
        this.status = "Expired";
      } else {
        this.status = "Active";
      }
    });
  }

  toggleEditMode(): void {
    this.enableEdit = !this.enableEdit;
    this.enableSave = !this.enableSave;

    if (this.enableSave) {
      this.profileForm.get('name').enable();
      this.profileForm.get('email').enable();
      this.profileForm.get('subscriptionEnabled').enable();
    } else {
      this.profileForm.get('name').disable();
      this.profileForm.get('email').disable();
      this.profileForm.get('subscriptionEnabled').disable();
    }
  }

  edit() {
    this.profileData.name = this.profileForm.get('name').value;
    this.profileData.email = this.profileForm.get('email').value;
    this.profileData.subscriptionEnabled = this.profileForm.get('subscriptionEnabled').value;

    this.personService.editProfile(this.profileData).subscribe(data => {
      //popup success
      //refresh page
    });

    this.toggleEditMode();
  }
}

