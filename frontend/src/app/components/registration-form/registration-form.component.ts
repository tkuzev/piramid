import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, Validators} from "@angular/forms";
import { ActivatedRoute, Router } from '@angular/router';
import { PersonService } from 'src/app/services/person.service';
import { kur } from 'src/app/models/kur';

@Component({
  selector: 'app-registration-form',
  templateUrl: './registration-form.component.html',
  styleUrls: ['./registration-form.component.css']
})
export class RegistrationFormComponent implements OnInit{

  private registrationPerson: kur

  constructor(private fb:FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private personService: PersonService) {
  }
  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }
  newReg =this.fb.group({
    name: new FormControl('', [Validators.required, Validators.pattern('[a-zA-Z]+')]),
    email: new FormControl('', [Validators.required, Validators.email]),
    money: new FormControl('', [Validators.required, Validators.pattern('[0-9]+')]),
    password: new FormControl('', [Validators.required, Validators.pattern('^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$')])
  });
  
  onSubmit() {
      this.registrationPerson.email = this.newReg.get("email").value
      this.registrationPerson.name = this.newReg.get("name").value
      this.registrationPerson.money = parseInt(this.newReg.get("money").value)
      this.registrationPerson.password = this.newReg.get("password").value
      this.personService.personGetId().subscribe(value => this.registrationPerson.parentId = value)

      this.personService.registerPerson(this.registrationPerson);
  }
}
