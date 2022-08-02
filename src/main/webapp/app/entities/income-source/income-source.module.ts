import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IncomeSourceComponent } from './list/income-source.component';
import { IncomeSourceDetailComponent } from './detail/income-source-detail.component';
import { IncomeSourceUpdateComponent } from './update/income-source-update.component';
import { IncomeSourceDeleteDialogComponent } from './delete/income-source-delete-dialog.component';
import { IncomeSourceRoutingModule } from './route/income-source-routing.module';

@NgModule({
  imports: [SharedModule, IncomeSourceRoutingModule],
  declarations: [IncomeSourceComponent, IncomeSourceDetailComponent, IncomeSourceUpdateComponent, IncomeSourceDeleteDialogComponent],
  entryComponents: [IncomeSourceDeleteDialogComponent],
})
export class IncomeSourceModule {}
