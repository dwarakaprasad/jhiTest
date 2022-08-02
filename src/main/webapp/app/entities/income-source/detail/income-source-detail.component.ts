import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIncomeSource } from '../income-source.model';

@Component({
  selector: 'jhi-income-source-detail',
  templateUrl: './income-source-detail.component.html',
})
export class IncomeSourceDetailComponent implements OnInit {
  incomeSource: IIncomeSource | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ incomeSource }) => {
      this.incomeSource = incomeSource;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
