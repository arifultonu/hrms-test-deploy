import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TaskmanagementRoutingModule } from './taskmanagement-routing.module';
import { TaskCreateComponent } from './task/components/create/task-create.component';
import { TaskListComponent } from './task/components/list/task-list.component';
import { TaskmanagementComponent } from './taskmanagement.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { DataTablesModule } from 'angular-datatables';
import { BsDatepickerModule } from 'ngx-bootstrap';
import { NgxPaginationModule } from 'ngx-pagination';
import { NgxSpinnerModule } from 'ngx-spinner';
import { TaskShowComponent } from './task/components/show/task-show.component';
import { TaskEditComponent } from './task/components/edit/task-edit.component';


@NgModule({
  declarations: [TaskCreateComponent, TaskListComponent, TaskmanagementComponent, TaskShowComponent, TaskEditComponent],
  imports: [
    CommonModule,
    TaskmanagementRoutingModule,
    DataTablesModule,
    FormsModule,
    ReactiveFormsModule,
    NgxSpinnerModule,
    NgxPaginationModule,
    NgSelectModule,
    BsDatepickerModule.forRoot(),
  ]
})
export class TaskmanagementModule { }
