import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AddEmployeeComponent } from './components/all-employees/add-employee/add-employee.component';
import { EmployeeListComponent } from './components/all-employees/employee-list/employee-list.component';
import { EmployeePageContentComponent } from './components/all-employees/employee-page-content/employee-page-content.component';
import { EmployeeProfileComponent } from './components/all-employees/employee-profile/employee-profile.component';
import { AttendanceAdminComponent } from './components/attendance-admin/attendance-admin.component';

import { AttendanceEmployeeComponent } from './components/attendance-employee/attendance-employee.component';
import { AttendanceRowAdminComponent } from './components/attendance-row-admin/attendance-row-admin.component';
import { DepartmentsComponent } from './components/departments/departments.component';
import { DesignationComponent } from './components/designation/designation.component';
import { HolidaysComponent } from './components/holidays/holidays.component';
import { LeaveSettingsComponent } from './components/leave-settings/leave-settings.component';
import { LeavesAdminComponent } from './components/leaves-admin/leaves-admin.component';
import { LeavesEmployeeComponent } from '../self-service/components/leaves/leaves-employee/leaves-employee.component';
import { OvertimeComponent } from './components/overtime/overtime.component';
import { TimesheetComponent } from './components/timesheet/timesheet.component';
import { EmployeesComponent } from './employees.component';
import { EmpProfileEditGuard } from 'src/app/guard/emp-profile/emp-profile-edit-guard';

const routes: Routes = [
  {
    path: '',
    component: EmployeesComponent,
    children: [
      {
        path: 'employeepage',
        component: EmployeePageContentComponent
      },
      {
        path: 'add-employee',
        component: AddEmployeeComponent
      },
      {
        path: 'edit-employee/:id',
        component: AddEmployeeComponent,
       // canActivate: [EmpProfileEditGuard]
      },
      {
        path: 'employeelist',
        component: EmployeeListComponent
      },
      {
        path: 'employeeprofile/:id',
        component: EmployeeProfileComponent
      },
      {
        path: 'holidays',
        component: HolidaysComponent
      },
      {
        path: 'adminleaves',
        component: LeavesAdminComponent
      },

      {
        path: 'leavesettings',
        component: LeaveSettingsComponent
      },
      {
        path: 'attendanceadmin',
        component: AttendanceAdminComponent
      },
      {
        path: 'attendancerowadmin',
        component: AttendanceRowAdminComponent
      },
      {
        path: 'attendanceemployee',
        component: AttendanceEmployeeComponent
      },
      {
        path: 'departments',
        component: DepartmentsComponent
      },
      {
        path: 'designation',
        component: DesignationComponent
      },
      {
        path: 'timesheet',
        component: TimesheetComponent
      },
      {
        path: 'overtime',
        component: OvertimeComponent
      },

    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EmployeesRoutingModule { }
