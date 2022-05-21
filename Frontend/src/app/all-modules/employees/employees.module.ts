import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

import { EmployeesRoutingModule } from './employees-routing.module';
import { EmployeesComponent } from './employees.component';
import { DataTablesModule } from 'angular-datatables';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BsDatepickerModule } from 'ngx-bootstrap';
import { SharingModule } from 'src/app/sharing/sharing.module';
import { PickListModule } from 'primeng/picklist';
import { AllEmployeesComponent } from './components/all-employees/all-employees.component';
import { EmployeePageContentComponent } from './components/all-employees/employee-page-content/employee-page-content.component';
import { EmployeeListComponent } from './components/all-employees/employee-list/employee-list.component';
import { HolidaysComponent } from './components/holidays/holidays.component';
import { EmployeeProfileComponent } from './components/all-employees/employee-profile/employee-profile.component';
import { AttendanceAdminComponent } from './components/attendance-admin/attendance-admin.component';
import { AttendanceEmployeeComponent } from './components/attendance-employee/attendance-employee.component';
import { DepartmentsComponent } from './components/departments/departments.component';
import { DesignationComponent } from './components/designation/designation.component';
import { LeaveSettingsComponent } from './components/leave-settings/leave-settings.component';
import { LeavesAdminComponent } from './components/leaves-admin/leaves-admin.component';

import { OvertimeComponent } from './components/overtime/overtime.component';
import { TimesheetComponent } from './components/timesheet/timesheet.component';
import { AddEmployeeComponent } from './components/all-employees/add-employee/add-employee.component';
import { AttendanceRowAdminComponent } from './components/attendance-row-admin/attendance-row-admin.component';

import { NgxSpinnerModule } from "ngx-spinner";
import {NgSelectModule, NgOption} from '@ng-select/ng-select';
import {NgxPaginationModule} from 'ngx-pagination';


@NgModule({
  declarations: [EmployeesComponent, AllEmployeesComponent, EmployeePageContentComponent, EmployeeListComponent, EmployeeProfileComponent, HolidaysComponent, LeavesAdminComponent, LeaveSettingsComponent, AttendanceAdminComponent, AttendanceEmployeeComponent, DepartmentsComponent, DesignationComponent, TimesheetComponent, OvertimeComponent, AddEmployeeComponent, AttendanceRowAdminComponent],
  imports: [
    CommonModule,
    FormsModule,
    SharingModule,
    ReactiveFormsModule,
    PickListModule,
    EmployeesRoutingModule, PickListModule,
    BsDatepickerModule.forRoot(),
    DataTablesModule,
    HttpClientModule,
    NgxSpinnerModule,
    NgSelectModule,
    NgxPaginationModule,



  ]
})

export class EmployeesModule { }
