import {Component} from '@angular/core';
import {FormBuilder, FormControl, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {RegistrationPerson} from "../../models/registration-person";
import {PersonService} from "../../services/person.service";
import {LoginPerson} from "../../models/login-person";

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent {



  constructor(private fb: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private personService: PersonService) {
  }

  onSubmit(){
    let loginPerson = new LoginPerson()
    loginPerson.email = this.newLog.get("email").value
    loginPerson.password = this.newLog.get("password").value
    this.personService.login(loginPerson).subscribe(
      () =>this.gotoHomePage()
    );
  }

  gotoHomePage(){
    this.router.navigate(['/']);
  };

  newLog = this.fb.group({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.pattern('^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$')])
  });
}
