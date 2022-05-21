import { EditTransportComponent } from './components/transport/edit-transport/edit-transport.component';
import { ViewTransportComponent } from './components/transport/view-transport/view-transport.component';
import { CreateTransportComponent } from './components/transport/create-transport/create-transport.component';
import { ListTransportComponent } from './components/transport/list-transport/list-transport.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateLeaveComponent } from './components/leaves/create-leave/create-leave.component';
import { EditLeaveComponent } from './components/leaves/edit-leave/edit-leave.component';
import { LeavesEmployeeComponent } from './components/leaves/leaves-employee/leaves-employee.component';
import { ViewLeaveComponent } from './components/leaves/view-leave/view-leave.component';
import { CreateLeaveHrAdminComponent } from './components/leavesHrAdmin/create-leave/create-leave.component';
import { EditLeaveHrAdminComponent } from './components/leavesHrAdmin/edit-leave/edit-leave.component';
import { LeavesEmployeeHrAdminComponent } from './components/leavesHrAdmin/leaves-employee/leaves-employee.component';
import { ViewLeaveHrAdminComponent } from './components/leavesHrAdmin/view-leave/view-leave.component';
import { CreateComponent } from './components/onTour/create/create.component';

import { EditComponent } from './components/onTour/edit/edit.component';
import { OnTourComponent } from './components/onTour/on-tour/on-tour.component';

import { ViewComponent } from './components/onTour/view/view.component';
import { CreateHrAdminComponent } from './components/onTourHrAdmin/create-hr-Admin/create.component';
import { EditHrAdminComponent } from './components/onTourHrAdmin/edit-hr-admin/edit.component';
import { OnTourHrAdminComponent } from './components/onTourHrAdmin/on-tour-hr-admin/on-tour.component';
import { ViewHrAdminComponent } from './components/onTourHrAdmin/view-hr-admin/view.component';
import { CreateShortLeaveComponent } from './components/short-leave/create-short-leave/create-short-leave.component';
import { EditShortLeaveComponent } from './components/short-leave/edit-short-leave/edit-short-leave.component';
import { ShortLeaveComponent } from './components/short-leave/short-leave/short-leave.component';
import { ViewShortLeaveComponent } from './components/short-leave/view-short-leave/view-short-leave.component';


import { SelfServiceComponent } from './sefl-service.component';
import { IdcardrequsnCreateComponent } from './components/idcardreqsn/create/idcardrequsn-create.component';

const routes: Routes = [
  {
    path:'',
    component:SelfServiceComponent,
    children:[
      {
        path:"onTour",
        component:OnTourComponent
      },
      {
        path:"create",
        component:CreateComponent
      },
      {
        path:"onTour/view/:id",
        component:ViewComponent
      },
      {
        path:"onTour/edit/:id",
        component:EditComponent
      },
      {
        path: 'employeeleaves',
        component: LeavesEmployeeComponent
      },
      {
        path: 'employeeleaves/create',
        component: CreateLeaveComponent
      },
      {
        path: 'employeeleaves/edit/:id',
        component: EditLeaveComponent
      },
      {
        path: 'employeeleaves/view/:id',
        component: ViewLeaveComponent
      },
      {
        path: 'employeeleaves-hr-admin',
        component: LeavesEmployeeHrAdminComponent
      },
      {
        path: 'employeeleaves-hr-admin/create',
        component: CreateLeaveHrAdminComponent
      },
      {
        path: 'employeeleaves-hr-admin/edit/:id',
        component: EditLeaveHrAdminComponent
      },
      {
        path: 'employeeleaves-hr-admin/view/:id',
        component: ViewLeaveHrAdminComponent
      },
      {
        path:"onTourHrAdmin",
        component:OnTourHrAdminComponent
      },
      {
        path:"onTourHrAdmin/create",
        component:CreateHrAdminComponent
      },
      {
        path:"onTourHrAdmin/view/:id",
        component:ViewHrAdminComponent
      },
      {
        path:"onTourHrAdmin/edit/:id",
        component:EditHrAdminComponent
      },
      {
        path:"emp-short-leave",
        component:ShortLeaveComponent
      },
      {
        path:"emp-short-leave/create",
        component:CreateShortLeaveComponent
      },
      {
        path:"emp-short-leave/view/:id",
        component:ViewShortLeaveComponent
      },
      {
        path:"emp-short-leave/edit/:id",
        component:EditShortLeaveComponent
      },
      {
        path:"transportRequisition",
        component:ListTransportComponent
      },
      {
        path:"transportRequisition/create",
        component:CreateTransportComponent
      },
      {
        path:"transportRequisition/view/:id",
        component:ViewTransportComponent
      },
      {
        path:"transportRequisition/edit/:id",
        component:EditTransportComponent
      },
      {
        path:"idcardrequsn/create",
        component:IdcardrequsnCreateComponent
      }




    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SelfServiceRoutingModule { }
