import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IApplicant } from '../applicant.model';
import { ApplicantService } from '../service/applicant.service';
import { ApplicantDeleteDialogComponent } from '../delete/applicant-delete-dialog.component';

@Component({
  selector: 'jhi-applicant',
  templateUrl: './applicant.component.html',
})
export class ApplicantComponent implements OnInit {
  applicants?: IApplicant[];
  isLoading = false;

  constructor(protected applicantService: ApplicantService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.applicantService.query().subscribe({
      next: (res: HttpResponse<IApplicant[]>) => {
        this.isLoading = false;
        this.applicants = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IApplicant): number {
    return item.id!;
  }

  delete(applicant: IApplicant): void {
    const modalRef = this.modalService.open(ApplicantDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.applicant = applicant;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
