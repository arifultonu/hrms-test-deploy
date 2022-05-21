import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SimRoutingModule } from './sim-routing.module';

import { ReactiveFormsModule } from '@angular/forms';
import { DataTablesModule } from 'angular-datatables';
import { BsDatepickerModule } from 'ngx-bootstrap';
import { NgxPaginationModule } from 'ngx-pagination';
import { NgxSpinnerModule } from 'ngx-spinner';
import { PickListModule } from 'primeng/picklist';
import { SharingModule } from 'src/app/sharing/sharing.module';
import { SimComponent } from './sim.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { BillFormComponent } from './components/bill/bill-form/bill-form.component';
import { BillListComponent } from './components/bill/bill-list/bill-list.component';
import { ManagementFormComponent } from './components/management/management-form/management-form.component';
import { ManagementListComponent } from './components/management/management-list/management-list.component';
import { RequisitionFormComponent } from './components/requisition/requisition-form/requisition-form.component';
import { RequisitionListComponent } from './components/requisition/requisition-list/requisition-list.component';




@NgModule({
  declarations: [RequisitionFormComponent, RequisitionListComponent, SimComponent, ManagementListComponent, ManagementFormComponent, BillFormComponent, BillListComponent],
  imports: [
    CommonModule,
    SimRoutingModule,
    DataTablesModule,
    BsDatepickerModule.forRoot(),
    SharingModule,
    ReactiveFormsModule,
    PickListModule,
    NgxPaginationModule,
    NgxSpinnerModule,
    NgSelectModule,

  ]
})
export class SimModule { }
