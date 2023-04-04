import {ChartData, ChartOptions} from "chart.js";

export class ChartSchm{
  options: ChartOptions<any>
  data: ChartData

  constructor(bronze: number, silver: number, gold: number, plat: number) {
    this.data = {
      labels: ['Bronze', 'Silver', 'Gold', 'Platinum'],
      datasets: [
        {label: 'Income/Percent', data: [bronze, silver, gold, plat]}
      ],
    };

    this.options = {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: 'Percent Income',
        },
      },
    }
  }
}
