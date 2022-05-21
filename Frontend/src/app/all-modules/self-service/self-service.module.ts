import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SelfServiceRoutingModule } from './self-service-routing.module';

import { SelfServiceComponent } from './sefl-service.component';


import { ReactiveFormsModule } from '@angular/forms';
import { DataTablesModule } from 'angular-datatables';
import { BsDatepickerModule } from 'ngx-bootstrap';
import { NgxPaginationModule } from 'ngx-pagination';
import { NgxSpinnerModule } from 'ngx-spinner';
import { PickListModule } from 'primeng/picklist';
import { SharingModule } from 'src/app/sharing/sharing.module';
import { NgSelectModule } from '@ng-select/ng-select';
import { OnTourComponent } from './components/onTour/on-tour/on-tour.component';
import { OnTourHrAdminComponent } from './components/onTourHrAdmin/on-tour-hr-admin/on-tour.component';
import { CreateComponent } from './components/onTour/create/create.component';
import { ViewComponent } from './components/onTour/view/view.component';
import { EditComponent } from './components/onTour/edit/edit.component';
import { LeavesEmployeeComponent } from './components/leaves/leaves-employee/leaves-employee.component';
import { CreateLeaveComponent } from './components/leaves/create-leave/create-leave.component';
import { EditLeaveComponent } from './components/leaves/edit-leave/edit-leave.component';
import { ViewLeaveComponent } from './components/leaves/view-leave/view-leave.component';
import { CreateHrAdminComponent } from './components/onTourHrAdmin/create-hr-Admin/create.component';
import { ViewHrAdminComponent } from './components/onTourHrAdmin/view-hr-admin/view.component';
import { EditHrAdminComponent } from './components/onTourHrAdmin/edit-hr-admin/edit.component';
import { LeavesEmployeeHrAdminComponent } from './components/leavesHrAdmin/leaves-employee/leaves-employee.component';
import { CreateLeaveHrAdminComponent } from './components/leavesHrAdmin/create-leave/create-leave.component';
import { EditLeaveHrAdminComponent } from './components/leavesHrAdmin/edit-leave/edit-leave.component';
import { ViewLeaveHrAdminComponent } from './components/leavesHrAdmin/view-leave/view-leave.component';
import { ShortLeaveComponent } from './components/short-leave/short-leave/short-leave.component';
import { CreateShortLeaveComponent } from './components/short-leave/create-short-leave/create-short-leave.component';
import { ViewShortLeaveComponent } from './components/short-leave/view-short-leave/view-short-leave.component';
import { EditShortLeaveComponent } from './components/short-leave/edit-short-leave/edit-short-leave.component';
import { CreateTransportComponent } from './components/transport/create-transport/create-transport.component';
import { EditTransportComponent } from './components/transport/edit-transport/edit-transport.component';
import { ViewTransportComponent } from './components/transport/view-transport/view-transport.component';
import { ListTransportComponent } from './components/transport/list-transport/list-transport.component';
import { IdcardrequsnCreateComponent } from './components/idcardreqsn/create/idcardrequsn-create.component';






@NgModule({
  declarations: [SelfServiceComponent, OnTourComponent, CreateComponent, ViewComponent, EditComponent,LeavesEmployeeComponent, CreateLeaveComponent, EditLeaveComponent, ViewLeaveComponent,OnTourHrAdminComponent,CreateHrAdminComponent,ViewHrAdminComponent,EditHrAdminComponent,LeavesEmployeeHrAdminComponent,CreateLeaveHrAdminComponent,EditLeaveHrAdminComponent,ViewLeaveHrAdminComponent, ShortLeaveComponent, CreateShortLeaveComponent, ViewShortLeaveComponent, EditShortLeaveComponent, CreateTransportComponent, EditTransportComponent, ViewTransportComponent, ListTransportComponent, IdcardrequsnCreateComponent],
  imports: [
    CommonModule,
    SelfServiceRoutingModule,
    BsDatepickerModule.forRoot(),
     SharingModule,
     ReactiveFormsModule,
     PickListModule,
     NgxPaginationModule,
     NgxSpinnerModule,
     NgSelectModule,
  ]
})
export class SelfServiceModule { }
