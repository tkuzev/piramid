import {Component} from '@angular/core';
import {FormBuilder, FormControl, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {PersonService} from "../../services/person.service";
import {LoginPerson} from "../../models/login-person";

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent {

  loading = false;
  error = '';

  constructor(private fb: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private personService: PersonService) {
  }

  onSubmit() {
    this.loading = true;
    let loginPerson = new LoginPerson()
    loginPerson.email = this.newLog.get("email").value
    loginPerson.password = this.newLog.get("password").value
    this.personService.login(loginPerson).subscribe(
      data => {
        this.gotoHomePage()
      },
      error => {
        this.error = error;
        this.loading = false;
      },
      ()=>{this.router.navigate(['/'])}
    )
  }

  gotoHomePage() {
    this.router.navigate(['/']).then(()=>{window.location.reload()})
  };

  newLog = this.fb.group({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.pattern('^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$')])
  });
}
