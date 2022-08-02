import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIncomeSource } from '../income-source.model';
import { IncomeSourceService } from '../service/income-source.service';

@Component({
  templateUrl: './income-source-delete-dialog.component.html',
})
export class IncomeSourceDeleteDialogComponent {
  incomeSource?: IIncomeSource;

  constructor(protected incomeSourceService: IncomeSourceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.incomeSourceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
