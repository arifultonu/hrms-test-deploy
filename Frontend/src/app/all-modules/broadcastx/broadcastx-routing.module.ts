import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BroadcastxComponent } from './broadcastx.component';
import { AttendanceMailxCreateComponent } from './components/attendance-mail/attendance-mailx-create/attendance-mailx-create.component';
import { AttendanceMailxComponent } from './components/attendance-mail/attendance-mailx/attendance-mailx.component';
import { CustomSchedulerCreateComponent } from './components/custom-scheduler/custom-scheduler-create/custom-scheduler-create.component';
import { CustomSchedulersComponent } from './components/custom-scheduler/custom-schedulers/custom-schedulers.component';
import { DailySchedulerCreateComponent } from './components/daily-scheduler/daily-scheduler-create/daily-scheduler-create.component';
import { DailySchedulersComponent } from './components/daily-scheduler/daily-schedulers/daily-schedulers.component';



const routes: Routes = [
  {
    path: "",
    component: BroadcastxComponent,
    children: [
      {
        path: "attendance-mail",
        component: AttendanceMailxComponent
      },
      {
        path: "attendance-mail/create",
        component: AttendanceMailxCreateComponent
      },
      {
        path: "custom-job",
        component: CustomSchedulersComponent
      },
      {
        path: "custom-job/create",
        component: CustomSchedulerCreateComponent
      },
      {
        path: "daily-scheduler",
        component:DailySchedulersComponent
      },
      {
        path: "daily-scheduler/create",
        component:DailySchedulerCreateComponent
      },
    ]
  } 
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BroadcastxRoutingModule { }
