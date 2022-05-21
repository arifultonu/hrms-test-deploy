import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BillFormComponent } from './components/bill/bill-form/bill-form.component';
import { BillListComponent } from './components/bill/bill-list/bill-list.component';
import { ManagementFormComponent } from './components/management/management-form/management-form.component';
import { ManagementListComponent } from './components/management/management-list/management-list.component';
import { RequisitionFormComponent } from './components/requisition/requisition-form/requisition-form.component';
import { RequisitionListComponent } from './components/requisition/requisition-list/requisition-list.component';
import { SimComponent } from './sim.component';

const routes: Routes = [
  {
    path:"",
    component:SimComponent,
    children:[
      {
        path:"requisition/create",
        component:RequisitionFormComponent
      },
      {
        path:"requisition/edit/:id",
        component:RequisitionFormComponent
      }, 
      {
        path:"requisition/show/:id",
        component:RequisitionFormComponent
      },
      {
        path:"requisition/list",
        component:RequisitionListComponent
      }, 
      {
        path:"management/create",
        component:ManagementFormComponent
      },
      {
        path:"management/edit/:id",
        component:ManagementFormComponent
      },
      {
        path:"management/show/:id",
        component:ManagementFormComponent
      },
      {
        path:"management/list",
        component:ManagementListComponent
      },
      {
        path:"billUpload/create",
        component:BillFormComponent
      },
      {
        path:"billUpload/edit/:id",
        component:BillFormComponent
      },
      {
        path:"billUpload/list",
        component:BillListComponent
      },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SimRoutingModule { }
