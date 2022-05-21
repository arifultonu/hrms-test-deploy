import { NgSelectModule } from '@ng-select/ng-select';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ShiftRoutingModule } from './shift-routing.module';
import { ShiftComponent } from './shift.component';
import { ShiftListComponent } from './components/shift-list/shift-list.component';
import { DataTablesModule } from 'angular-datatables';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BsDatepickerModule } from 'ngx-bootstrap';
import { SharingModule } from 'src/app/sharing/sharing.module';
import { PickListModule } from 'primeng/picklist';
import { ShiftAssignComponent } from './components/shiftAssign/shift-assign/shift-assign.component';
import { NgxSpinnerModule } from 'ngx-spinner';
import { NgxPaginationModule } from 'ngx-pagination';
@NgModule({
  declarations: [ShiftComponent, ShiftListComponent, ShiftAssignComponent],
  imports: [
    CommonModule,
    ShiftRoutingModule,
     ReactiveFormsModule,
    FormsModule,
      DataTablesModule,
      PickListModule,
    BsDatepickerModule.forRoot(),
    SharingModule,
    NgxSpinnerModule,
    NgxPaginationModule,
    NgSelectModule
  ]
})
export class ShiftModule { }
