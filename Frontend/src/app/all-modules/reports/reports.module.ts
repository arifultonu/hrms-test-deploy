import { PayslipJsrRptCompnent } from './payslip-jsr-rpt/payslip-jsr-rpt.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReportsRoutingModule } from './reports-routing.module';
import { ReportsComponent } from './reports.component';
import { ExpenseReportComponent } from './expense-report/expense-report.component';
import { DataTablesModule } from 'angular-datatables';
import { InvoiceReportComponent } from './invoice-report/invoice-report.component';
import { SharingModule } from 'src/app/sharing/sharing.module';
import { EditInvoiceReportComponent } from './edit-invoice-report/edit-invoice-report.component';
import { BsDatepickerModule } from 'ngx-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AttendanceJsrRptCompnent } from './attendance-jsr-rpt/attendance-jsr-rpt.component';
import { BirthdayReportComponent } from './birthday-report/birthday-report.component';


import { EmployeeTopsheetComponent } from './employee-topsheet/employee-topsheet.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { HttpClientModule } from '@angular/common/http';
import { NgxPaginationModule } from 'ngx-pagination';
import { NgxSpinnerModule } from 'ngx-spinner';
import { PickListModule } from 'primeng/picklist';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/compiler';
import { EmployeeMonthlyRptComponent } from './employee-monthly-rpt/employee-monthly-rpt/employee-monthly-rpt.component';
import { EmployeeSalaryRptComponent } from './employee-salary-rpt/employee-salary-rpt/employee-salary-rpt.component';
import { IrregularAttendanceComponent } from './Irregular-Attendance/irregular-attendance/irregular-attendance.component';
import { EmployeeLeaveRptComponent } from './employee-leave-rpt/employee-leave-rpt.component';
import { EmployeeProfileRptComponent } from './employee-profile-rpt/employee-profile-rpt.component';


@NgModule({
  declarations: [ReportsComponent, ExpenseReportComponent, InvoiceReportComponent, EditInvoiceReportComponent, AttendanceJsrRptCompnent, PayslipJsrRptCompnent, BirthdayReportComponent, EmployeeTopsheetComponent, EmployeeMonthlyRptComponent, EmployeeSalaryRptComponent, IrregularAttendanceComponent, EmployeeLeaveRptComponent, EmployeeProfileRptComponent],
  imports: [
    CommonModule,
    ReportsRoutingModule,
    DataTablesModule,
    FormsModule,
    NgSelectModule,
    ReactiveFormsModule,
    SharingModule,
    HttpClientModule,
    FormsModule,

    NgxPaginationModule,
    NgxSpinnerModule,
    NgSelectModule,
    PickListModule,
    BsDatepickerModule.forRoot(),
  ],
  //schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class ReportsModule { }
