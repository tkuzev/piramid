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
  profileData: RegistrationPerson;
  enableEdit: boolean = true;
  enableSave: boolean = false;
  profileForm: FormGroup;
  status: string;
  exp_date: string;
  plan: string;
  id: number;

  constructor(private fb: FormBuilder, private  personService: PersonService, private router: Router) { }

  ngOnInit() {
    this.profileForm = this.fb.group({
      name: new FormControl({value: '', disabled: true}, [Validators.required, Validators.pattern('[a-zA-Z]+'), Validators.maxLength(25)]),
      email: new FormControl({value: '', disabled: true},[Validators.required, Validators.email]),
    });

    this.personService.getProfileInfo().subscribe(data=>{
      this.profileData = data;

      this.profileForm.setValue({
        name: this.profileData.name,
        email: this.profileData.email,
      });
    });

  }

  toggleEditMode(): void {

    console.log(this.profileData.id);
    this.enableEdit = !this.enableEdit;
    this.enableSave = !this.enableSave;

    if (this.enableSave) {
      this.profileForm.get('name').enable();
      this.profileForm.get('email').enable();
    } else {
      this.profileForm.get('name').disable();
      this.profileForm.get('email').disable();
    }
  }
  edit() {
    this.toggleEditMode();

    this.personService.editProfile(this.profileData).subscribe(data=>{

      //popup success
      //refresh page
    })
  }


}

