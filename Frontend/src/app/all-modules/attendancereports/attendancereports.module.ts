import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AttendancereportsRoutingModule } from './attendancereports-routing.module';
import { AttendancereportsComponent } from './attendancereports.component';
import { AttendancereportsListComponent } from './attendancereports-list/attendancereports-list.component';
import { BsDatepickerModule } from 'ngx-bootstrap';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DataTablesModule } from 'angular-datatables';
import { PickListModule } from 'primeng/picklist';
import { SharingModule } from 'src/app/sharing/sharing.module';
import { EmployeesRoutingModule } from '../employees/employees-routing.module';
import { AttendancereportsHrAdminListComponent } from './attendancereport-hr-admin-list/attendancereports-list.component';
import { NgxSpinnerModule } from 'ngx-spinner';


@NgModule({
  declarations: [AttendancereportsComponent, AttendancereportsListComponent,AttendancereportsHrAdminListComponent],
  imports: [
    CommonModule,
    AttendancereportsRoutingModule,
    CommonModule,
    FormsModule,
    SharingModule,
    ReactiveFormsModule,
    PickListModule,
    EmployeesRoutingModule,
    BsDatepickerModule.forRoot(),
    DataTablesModule,
    HttpClientModule,
    NgxSpinnerModule
  ]
})
export class AttendancereportsModule { }
