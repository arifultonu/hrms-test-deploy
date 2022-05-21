import { EmployeeProfileComponent } from './../employees/components/all-employees/employee-profile/employee-profile.component';
import { EmployeeLeaveRptComponent } from './employee-leave-rpt/employee-leave-rpt.component';
import { PayslipJsrRptCompnent } from './payslip-jsr-rpt/payslip-jsr-rpt.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ReportsComponent } from './reports.component';
import { ExpenseReportComponent } from './expense-report/expense-report.component';
import { InvoiceReportComponent } from './invoice-report/invoice-report.component';
import { EditInvoiceReportComponent } from './edit-invoice-report/edit-invoice-report.component';
import { AttendanceJsrRptCompnent } from './attendance-jsr-rpt/attendance-jsr-rpt.component';
import { BirthdayReportComponent } from './birthday-report/birthday-report.component';

import { EmployeeTopsheetComponent } from './employee-topsheet/employee-topsheet.component';
import { EmployeeMonthlyRptComponent } from './employee-monthly-rpt/employee-monthly-rpt/employee-monthly-rpt.component';
import { EmployeeSalaryRptComponent } from './employee-salary-rpt/employee-salary-rpt/employee-salary-rpt.component';
import { IrregularAttendanceComponent } from './Irregular-Attendance/irregular-attendance/irregular-attendance.component';
import { EmployeeProfileRptComponent } from './employee-profile-rpt/employee-profile-rpt.component';

const routes: Routes = [
  {
    path:"",
    component:ReportsComponent,
    children:[
      {
        path:"expense-report",
        component:ExpenseReportComponent
      },
      {
        path:"invoice-report",
        component:InvoiceReportComponent
      },
      {
        path:"edit-invoice-report",
        component:EditInvoiceReportComponent
      },
      {
        path: "attendance-jsr-rpt",
        component: AttendanceJsrRptCompnent
      },
      {
        path: "payslip-report",
        component: PayslipJsrRptCompnent
      },
      {
        path: "birthday-report",
        component: BirthdayReportComponent
      },


      {
        path: "employee-topsheet",
        component: EmployeeTopsheetComponent
      },

      {
        path: "employee-monthly-rpt",
        component: EmployeeMonthlyRptComponent
      },

      {
        path: "employee-salary-rpt",
        component: EmployeeSalaryRptComponent
      },
      {
        path: "Irregular-Attendance",
        component:IrregularAttendanceComponent

      },
      {
        path: "employee-leave-rpt",
        component: EmployeeLeaveRptComponent
      },

      {
        path: "employee-profile-rpt",
        component: EmployeeProfileRptComponent
      },

    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReportsRoutingModule { }
