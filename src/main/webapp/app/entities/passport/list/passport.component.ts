import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPassport } from '../passport.model';
import { PassportService } from '../service/passport.service';
import { PassportDeleteDialogComponent } from '../delete/passport-delete-dialog.component';

@Component({
  selector: 'jhi-passport',
  templateUrl: './passport.component.html',
})
export class PassportComponent implements OnInit {
  passports?: IPassport[];
  isLoading = false;

  constructor(protected passportService: PassportService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.passportService.query().subscribe({
      next: (res: HttpResponse<IPassport[]>) => {
        this.isLoading = false;
        this.passports = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPassport): number {
    return item.id!;
  }

  delete(passport: IPassport): void {
    const modalRef = this.modalService.open(PassportDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.passport = passport;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
