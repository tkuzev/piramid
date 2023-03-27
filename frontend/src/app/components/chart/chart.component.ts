import { Component } from '@angular/core';
import {ChartData, ChartOptions} from "chart.js";
@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})
export class ChartComponent {
  salesData: ChartData<'bar'> = {
    labels: ['Bronze', 'Silver', 'Gold', 'Platinum'],
    datasets: [
      { label: 'Income/Percent', data: [1000, 1200, 1050, 2000] },
    ],
  };
  chartOptions: ChartOptions = {
    responsive: true,
    plugins: {
      title: {
        display: true,
        text: 'Percent Income',
      },
    },
  };
}
