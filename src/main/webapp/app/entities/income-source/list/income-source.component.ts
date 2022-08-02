import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IIncomeSource } from '../income-source.model';
import { IncomeSourceService } from '../service/income-source.service';
import { IncomeSourceDeleteDialogComponent } from '../delete/income-source-delete-dialog.component';

@Component({
  selector: 'jhi-income-source',
  templateUrl: './income-source.component.html',
})
export class IncomeSourceComponent implements OnInit {
  incomeSources?: IIncomeSource[];
  isLoading = false;

  constructor(protected incomeSourceService: IncomeSourceService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.incomeSourceService.query().subscribe({
      next: (res: HttpResponse<IIncomeSource[]>) => {
        this.isLoading = false;
        this.incomeSources = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IIncomeSource): number {
    return item.id!;
  }

  delete(incomeSource: IIncomeSource): void {
    const modalRef = this.modalService.open(IncomeSourceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.incomeSource = incomeSource;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
