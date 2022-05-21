import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { EmployeeDashboardComponent } from './components/employee-dashboard/employee-dashboard.component';
import { MorrisJsModule } from 'angular-morris-js';
import { GoogleChartsModule } from 'angular-google-charts';


@NgModule({
  declarations: [DashboardComponent, AdminDashboardComponent, EmployeeDashboardComponent],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    MorrisJsModule,
    GoogleChartsModule

  ]
})
export class DashboardModule { }
