import {Component, OnInit} from '@angular/core';
import {BinaryService} from "../../services/binary.service";
import {ActivatedRoute} from "@angular/router";
import {BinPerson} from "../../models/bin-person";

@Component({
  selector: 'app-binary-registration',
  templateUrl: './binary-registration.component.html',
  styleUrls: ['./binary-registration.component.css']
})
export class BinaryRegistrationComponent implements OnInit {
  public parent: BinPerson;
  public child: BinPerson;
  public subTree: Array<BinPerson>;


  constructor(private binaryService: BinaryService, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const fatherId: number = params['fatherId'];
      const childId = params['childId'];
      this.binaryService.getBinaryPersonById(fatherId).subscribe(value => this.parent = value)
      this.binaryService.getBinaryPersonById(childId).subscribe(value => this.child = value)
      this.binaryService.getTree(fatherId).subscribe(value => this.subTree = value);
    })
  }
}
