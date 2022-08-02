import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssets } from '../assets.model';
import { AssetsService } from '../service/assets.service';

@Component({
  templateUrl: './assets-delete-dialog.component.html',
})
export class AssetsDeleteDialogComponent {
  assets?: IAssets;

  constructor(protected assetsService: AssetsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
