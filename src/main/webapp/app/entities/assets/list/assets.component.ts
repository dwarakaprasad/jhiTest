import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssets } from '../assets.model';
import { AssetsService } from '../service/assets.service';
import { AssetsDeleteDialogComponent } from '../delete/assets-delete-dialog.component';

@Component({
  selector: 'jhi-assets',
  templateUrl: './assets.component.html',
})
export class AssetsComponent implements OnInit {
  assets?: IAssets[];
  isLoading = false;

  constructor(protected assetsService: AssetsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.assetsService.query().subscribe({
      next: (res: HttpResponse<IAssets[]>) => {
        this.isLoading = false;
        this.assets = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IAssets): number {
    return item.id!;
  }

  delete(assets: IAssets): void {
    const modalRef = this.modalService.open(AssetsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.assets = assets;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
