import { TaskListComponent } from './task/components/list/task-list.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TaskCreateComponent } from './task/components/create/task-create.component';
import { TaskmanagementComponent } from './taskmanagement.component';
import { TaskShowComponent } from './task/components/show/task-show.component';
import { TaskEditComponent } from './task/components/edit/task-edit.component';

const routes: Routes = [
  {
    path: '',
    component:TaskmanagementComponent,
    children:[
      {
        path: 'task/create',
        component:TaskCreateComponent
      },
      {
        path: 'task/list',
        component:TaskListComponent
      },
      {
        path: 'task/show/:id',
        component:TaskShowComponent
      },
      {
        path: 'task/edit/:id',
        component:TaskEditComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TaskmanagementRoutingModule { }
