import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BroadcastxRoutingModule } from './broadcastx-routing.module';
import { BroadcastxComponent } from './broadcastx.component';
import { AttendanceMailxComponent } from './components/attendance-mail/attendance-mailx/attendance-mailx.component';
import { AttendanceMailxCreateComponent } from './components/attendance-mail/attendance-mailx-create/attendance-mailx-create.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { NgxSpinnerModule } from 'ngx-spinner';
import { NgSelectModule } from '@ng-select/ng-select';
import { BsDatepickerModule } from 'ngx-bootstrap';
import { ReactiveFormsModule } from '@angular/forms';
import { CustomSchedulersComponent } from './components/custom-scheduler/custom-schedulers/custom-schedulers.component';
import { CustomSchedulerCreateComponent } from './components/custom-scheduler/custom-scheduler-create/custom-scheduler-create.component';
import { DailySchedulersComponent } from './components/daily-scheduler/daily-schedulers/daily-schedulers.component';
import { DailySchedulerCreateComponent } from './components/daily-scheduler/daily-scheduler-create/daily-scheduler-create.component';




@NgModule({
  declarations: [BroadcastxComponent, AttendanceMailxComponent, AttendanceMailxCreateComponent, CustomSchedulersComponent, CustomSchedulerCreateComponent, DailySchedulersComponent, DailySchedulerCreateComponent],
  imports: [
    CommonModule,
    BroadcastxRoutingModule,
    NgxPaginationModule,
    NgxSpinnerModule,
    NgSelectModule,
    BsDatepickerModule.forRoot(),
    ReactiveFormsModule
  ]
})
export class BroadcastxModule { }
