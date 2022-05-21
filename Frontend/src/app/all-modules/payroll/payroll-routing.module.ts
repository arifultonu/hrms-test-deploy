import { PayrollElementValueUploadComponent } from './components/element-value/upload/payroll-element-value-upload/payroll-element-value-upload.component';
import { PayrollElementDefCreateComponent } from './components/element-def/create/payroll-element-def-create/payroll-element-def-create.component';
import { PayrollElementDefListComponent } from './components/element-def/list/payroll-element-def-list/payroll-element-def-list.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PayrollComponent } from './payroll.component';
import { EmployeeSalaryComponent } from './components/employee-salary/employee-salary.component';
import { SalaryViewComponent } from './components/salary-view/salary-view.component';
import { PayrollItemsComponent } from './components/payroll-items/payroll-items.component';
import { PayrollSalaryProcessComponent } from './components/salary-process/salary-process-parameter.component';
import { SalaryProcessJobListComponent } from './components/salary-process/batch-job/list/salary-process-job-list.component';
import { PayrollElementValueListComponent } from './components/element-value/list/payroll-element-value-list.component';
import { PayrollElementValueCreateComponent } from './components/element-value/create/payroll-element-value-create.component';
import { PayrollElementValueEditComponent } from './components/element-value/edit/payroll-element-value-edit.component';
import { PayrollElementValueShowComponent } from './components/element-value/show/payroll-element-value-show.component';
import { OffDayBillsComponent } from './components/off-day-bill/off-day-bills/off-day-bills.component';
import { OffDayBillCreateComponent } from './components/off-day-bill/off-day-bill-create/off-day-bill-create.component';
import { OffDayBillShowComponent } from './components/off-day-bill/off-day-bill-show/off-day-bill-show.component';
import { OffDayBillEditComponent } from './components/off-day-bill/off-day-bill-edit/off-day-bill-edit.component';
const routes: Routes = [
  {
    path:"",
    component:PayrollComponent,
    children:[
      {
        path:"employee-salary",
        component:EmployeeSalaryComponent
      },
      {
        path:"payslip/:id",
        component:SalaryViewComponent
      },
      {
        path:"payroll-items",
        component:PayrollItemsComponent
      },
      {
        path:"salary-process",
        component:PayrollSalaryProcessComponent
      },
      {
        path:"salary-process-list",
        component:SalaryProcessJobListComponent
      },
      {
        path:"element-def/list",
        component:PayrollElementDefListComponent
      },
      {
        path:"element-def/create",
        component:PayrollElementDefCreateComponent
      },
      {
        path:"element-value/list",
        component:PayrollElementValueListComponent
      },
      {
        path:"element-value/create",
        component:PayrollElementValueCreateComponent
      },
      {
        path:"element-value/upload",
        component:PayrollElementValueUploadComponent
      },
      {
        path:"element-value/edit/:id",
        component:PayrollElementValueEditComponent
      },
      {
        path:"element-value/show/:id", // payroll/element-xyz-value/
        component:PayrollElementValueShowComponent
      },
      {
        path:"off-day-bill",
        component:OffDayBillsComponent
      },
      {
        path:"off-day-bill/create",
        component:OffDayBillCreateComponent
      },
      {
        path:"off-day-bill/show/:id",
        component:OffDayBillShowComponent
      },
      {
        path:"off-day-bill/edit/:id",
        component:OffDayBillEditComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PayrollRoutingModule { }
