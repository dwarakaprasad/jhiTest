import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IApplication } from '../application.model';
import { ApplicationService } from '../service/application.service';
import { ApplicationDeleteDialogComponent } from '../delete/application-delete-dialog.component';

@Component({
  selector: 'jhi-application',
  templateUrl: './application.component.html',
})
export class ApplicationComponent implements OnInit {
  applications?: IApplication[];
  isLoading = false;

  constructor(protected applicationService: ApplicationService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.applicationService.query().subscribe({
      next: (res: HttpResponse<IApplication[]>) => {
        this.isLoading = false;
        this.applications = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IApplication): number {
    return item.id!;
  }

  delete(application: IApplication): void {
    const modalRef = this.modalService.open(ApplicationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.application = application;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
