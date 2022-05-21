import { NgModule, CUSTOM_ELEMENTS_SCHEMA  } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PayrollRoutingModule } from './payroll-routing.module';
import { PayrollComponent } from './payroll.component';
import { EmployeeSalaryComponent } from './components/employee-salary/employee-salary.component';
import { SalaryViewComponent } from './components/salary-view/salary-view.component';
import { DataTablesModule } from 'angular-datatables';
import { PayrollItemsComponent } from './components/payroll-items/payroll-items.component';
import { BsDatepickerModule } from 'ngx-bootstrap';
import { SharingModule } from 'src/app/sharing/sharing.module';
import { ReactiveFormsModule } from '@angular/forms';
import { PickListModule } from 'primeng/picklist';
import { NgxPaginationModule } from 'ngx-pagination';
import { NgxSpinnerModule } from "ngx-spinner";
import { SalaryProcessJobListComponent } from './components/salary-process/batch-job/list/salary-process-job-list.component';
import { SalaryProcessJobCreateComponent } from './components/salary-process/batch-job/create/salary-process-job-create.component';
import { SalaryProcessJobEditComponent } from './components/salary-process/batch-job/edit/salary-process-job-edit.component';
import { SalaryProcessJobShowComponent } from './components/salary-process/batch-job/show/salary-process-job-show.component';
import { PayrollElementValueListComponent } from './components/element-value/list/payroll-element-value-list.component';
import { PayrollElementValueShowComponent } from './components/element-value/show/payroll-element-value-show.component';
import { PayrollElementValueCreateComponent } from './components/element-value/create/payroll-element-value-create.component';
import { PayrollElementValueEditComponent } from './components/element-value/edit/payroll-element-value-edit.component';
import { PayrollSalaryProcessComponent } from './components/salary-process/salary-process-parameter.component';
import { OffDayBillsComponent } from './components/off-day-bill/off-day-bills/off-day-bills.component';

import { OffDayBillCreateComponent } from './components/off-day-bill/off-day-bill-create/off-day-bill-create.component';
import { OffDayBillEditComponent } from './components/off-day-bill/off-day-bill-edit/off-day-bill-edit.component';
import { OffDayBillShowComponent } from './components/off-day-bill/off-day-bill-show/off-day-bill-show.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { PayrollElementDefCreateComponent } from './components/element-def/create/payroll-element-def-create/payroll-element-def-create.component';
import { PayrollElementDefListComponent } from './components/element-def/list/payroll-element-def-list/payroll-element-def-list.component';
import { PayrollElementDefShowComponent } from './components/element-def/show/payroll-element-def-show/payroll-element-def-show.component';
import { PayrollElementDefEditComponent } from './components/element-def/edit/payroll-element-def-edit/payroll-element-def-edit.component';
import { PayrollElementValueUploadComponent } from './components/element-value/upload/payroll-element-value-upload/payroll-element-value-upload.component';




@NgModule({
  declarations: [PayrollComponent, EmployeeSalaryComponent, SalaryViewComponent, PayrollItemsComponent, PayrollSalaryProcessComponent, SalaryProcessJobListComponent, SalaryProcessJobCreateComponent, SalaryProcessJobEditComponent, SalaryProcessJobShowComponent, PayrollElementValueListComponent, PayrollElementValueShowComponent, PayrollElementValueCreateComponent, PayrollElementValueEditComponent, OffDayBillsComponent, OffDayBillCreateComponent, OffDayBillEditComponent, OffDayBillShowComponent, PayrollElementDefCreateComponent, PayrollElementDefListComponent, PayrollElementDefShowComponent, PayrollElementDefEditComponent, PayrollElementValueUploadComponent],
  imports: [
    CommonModule,
    PayrollRoutingModule,
    DataTablesModule,
    BsDatepickerModule.forRoot(),
    SharingModule,
    ReactiveFormsModule,
    PickListModule,
    NgxPaginationModule,
    NgxSpinnerModule,
    NgSelectModule
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class PayrollModule { }
