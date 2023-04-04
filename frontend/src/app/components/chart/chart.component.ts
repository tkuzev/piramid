import {Component, OnDestroy, OnInit} from '@angular/core';
import {Chart, ChartData, ChartOptions} from "chart.js";
import {PersonService} from "../../services/person.service";
import {ChartSchm} from "./chart-schm";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {Observable, Subscription} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})
export class ChartComponent implements OnInit, OnDestroy {

  map: Map<string, number> = new Map()
  bronze: number = 0
  silver: number = 0
  gold: number = 0
  plat: number = 0
  chart: ChartSchm = new ChartSchm(0,0,0,0)

  chartSubscription
  constructor(private personService: PersonService, private router: Router) {
  }

  monthlyIncome() {
    this.personService.monthlyIncome().then(
        value => {
         this.chartSubscription = value.subscribe(
          data => {
              this.map = data
              console.log(data)
              this.bronze = this.map['Bronze']
              this.gold = this.map['Gold']
              this.silver = this.map['Silver']
              this.plat = this.map['Platinum']
              this.chart = new ChartSchm(this.bronze, this.silver, this.gold, this.plat)
          }
        )
          console.log(this.chartSubscription)
      }
    ).catch(()=>{
      this.chart = new ChartSchm(0, 0, 0, 0)
    })
  }

  ngOnInit() {
    this.monthlyIncome()
  }
  ngOnDestroy(){
    if(this.chartSubscription!=null){
      this.chartSubscription.unsubscribe()
    }
    console.log(this.chartSubscription)
  }
}
