import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssetsComponent } from './list/assets.component';
import { AssetsDetailComponent } from './detail/assets-detail.component';
import { AssetsUpdateComponent } from './update/assets-update.component';
import { AssetsDeleteDialogComponent } from './delete/assets-delete-dialog.component';
import { AssetsRoutingModule } from './route/assets-routing.module';

@NgModule({
  imports: [SharedModule, AssetsRoutingModule],
  declarations: [AssetsComponent, AssetsDetailComponent, AssetsUpdateComponent, AssetsDeleteDialogComponent],
  entryComponents: [AssetsDeleteDialogComponent],
})
export class AssetsModule {}
