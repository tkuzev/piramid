import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, Validators} from "@angular/forms";
import {BinaryService} from "../../services/binary.service";
import {BinPerson} from "../../models/bin-person";
import {ActivatedRoute, Router} from "@angular/router";
import {RegistrationPerson} from "../../models/registration-person";
import {BinaryPerson} from "../../models/binary-person";

@Component({
  selector: 'app-binary-registration',
  templateUrl: './binary-registration.component.html',
  styleUrls: ['./binary-registration.component.css']
})
export class BinaryRegistrationComponent implements OnInit{
  private parent: BinPerson;
  private child: RegistrationPerson;
  public subTree: Set<BinPerson>;

  childId:number;

  nqkwoDTO:BinaryPerson=new BinaryPerson();
  direction: boolean;
  selectedPerson: BinPerson;
  fatherId: number


  constructor(private fb: FormBuilder, private binaryService: BinaryService, private route: ActivatedRoute, private router:Router) {
  }

  registerForm = this.fb.group({
    direction: new FormControl('Left', [Validators.required]),
    person: new FormControl('', [Validators.required])
  });

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.fatherId = params['fatherId'];
      this.childId = params['childId'];
      this.binaryService.getBinaryPersonById(this.fatherId).subscribe(value =>this.parent = value)
      this.binaryService.getRegistrationPersonById(this.childId).subscribe(value => this.child = value)
      this.binaryService.getTree(this.fatherId).subscribe(value => this.subTree = value);
    })
  }

  onSubmit() {
      this.nqkwoDTO.preferredDirection=this.direction
      this.nqkwoDTO.binaryPersonToPutItOnId=this.selectedPerson.id
      this.binaryService.postBinaryPerson(this.childId,this.nqkwoDTO).subscribe(
        () => {
          // Handle successful response
          this.router.navigate(['/']);
        },
        () => {
          // Handle error response
          this.router.navigate(['register/binary/'+this.fatherId+'/'+this.childId])
        }
      )

  }
}
