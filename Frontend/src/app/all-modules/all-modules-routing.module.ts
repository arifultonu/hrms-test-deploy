import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SettingsGuard } from '../guard/settings.guard';
import { UserGuard } from '../guard/user.guard';
import { AllModulesComponent } from './all-modules.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: '',
    component: AllModulesComponent,
    children: [
      {
        path: 'dashboard',
        loadChildren: () => import('./dashboard/dashboard.module').then(m => m.DashboardModule)
      },
      {
        path: 'employees',
        loadChildren: () => import('./employees/employees.module').then(m => m.EmployeesModule),
        canActivate:[UserGuard],

      },
      {
        path: 'payroll',
        loadChildren: () => import('./payroll/payroll.module').then(m => m.PayrollModule)
      },
      {
        path: 'reports',
        loadChildren: () => import('./reports/reports.module').then(m => m.ReportsModule)
      },
      {
        path: 'assets',
        loadChildren: () => import('./assets/assets.module').then(m => m.AssetsModule)
      },
      {
        path: 'users',
        loadChildren: () => import('./users/users.module').then(m => m.UsersModule)
      },
      {
        path: 'settings',
        loadChildren: () => import('./settings/settings.module').then(m => m.SettingsModule),
        canActivate:[SettingsGuard],
      },
      {
        path: 'shift',
        loadChildren: () => import('./shift/shift.module').then(m => m.ShiftModule)
      },
      {
        path: 'employeereports',
        loadChildren: () => import('./employeereports/employeereports.module').then(m => m.EmployeereportsModule)
      },
      {
        path: 'attendancereports',
        loadChildren: () => import('./attendancereports/attendancereports.module').then(m => m.AttendancereportsModule)
      },
       {
        path: 'leavereports',
        loadChildren: () => import('./leavereports/leavereports.module').then(m => m.LeavereportsModule)
      },
      {
        path: 'approval',
        loadChildren: () => import('./approval/approval.module').then(m => m.ApprovalModule)
      },
      {
        path: 'broadcast',
        loadChildren: () => import('./broadcastx/broadcastx.module').then(m => m.BroadcastxModule)
      },
      {
        path: 'sim',
        loadChildren: () => import('./sim/sim.module').then(m => m.SimModule)
      },
      {
        path: 'sefl-service',
        loadChildren: () => import('./self-service/self-service.module').then(m => m.SelfServiceModule)
      },
      {
        path: 'irecruitment',
        loadChildren: () => import('./i-recruitment/i-recruitment.module').then(m => m.IRecruitmentModule)
      },
      {
        path: 'broadcastx',
        loadChildren: () => import('./broadcastx/broadcastx.module').then(m => m.BroadcastxModule)
      },
      {
        path: 'xyzyourmodule',
        loadChildren: () => import('./xyzyourmodule/xyzyourmodule.module').then(m => m.XyzyourmoduleModule)
      },
      {
        path: 'logs',
        loadChildren: () => import('./visitorlog/vislog.module').then(m => m.VislogModule)
      },
      {
        path: 'taskmanagement',
        loadChildren: () => import('./taskmanagement/taskmanagement.module').then(m => m.TaskmanagementModule)
      },

    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AllModulesRoutingModule { }
