import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ApprovalComponent } from './approval.component';
import { ApprovalProcessComponent } from './components/approvalProcess/approval-process/approval-process.component';
import { CreateComponent } from './components/approvalProcess/create/create.component';
import { EditComponent } from './components/approvalProcess/edit/edit.component';

import { ApprovalProcessTnxHistoryComponent } from './components/ApprovalProcessTnxHistory/approval-process-tnx-history/approval-process-tnx-history.component';
import { ApprovalStepCreateComponent } from './components/approvalStep/approval-step-create/approval-step-create.component';
import { ApprovalStepEditComponent } from './components/approvalStep/approval-step-edit/approval-step-edit.component';
import { ApprovalStepComponent } from './components/approvalStep/approval-step/approval-step.component';
import { ApprovalStepActionCreateComponent } from './components/approvalStepAction/approval-step-action-create/approval-step-action-create.component';
import { ApprovalStepActionEditComponent } from './components/approvalStepAction/approval-step-action-edit/approval-step-action-edit.component';
import { ApprovalStepActionComponent } from './components/approvalStepAction/approval-step-action/approval-step-action.component';
import { ApprovalStepApproverCreateComponent } from './components/approvalStepApprover/approval-step-approver-create/approval-step-approver-create.component';
import { ApprovalStepApproverEditComponent } from './components/approvalStepApprover/approval-step-approver-edit/approval-step-approver-edit.component';
import { ApprovalStepApproverComponent } from './components/approvalStepApprover/approval-step-approver/approval-step-approver.component';


const routes: Routes = [
  {
    path:'',
    component:ApprovalComponent,
    children:[
      {
        path:"approval-process",
        component:ApprovalProcessComponent
      },
      {
        path:"approval-process/create",
        component:CreateComponent
      },
      {
        path:"approval-process/edit/:id",
        component:EditComponent
      },
      {
        path:"approval-step",
        component:ApprovalStepComponent
      },
      {
        path:"approval-step/create",
        component:ApprovalStepCreateComponent
      },
      {
        path:"approval-step/edit/:id",
        component:ApprovalStepEditComponent
      },
      {
        path:"approval-step-approver",
        component:ApprovalStepApproverComponent
      },
      {
        path:"approval-step-approver/create",
        component:ApprovalStepApproverCreateComponent
      },
      {
        path:"approval-step-approver/edit/:id",
        component:ApprovalStepApproverEditComponent
      },
      {
        path:"approval-step-action",
        component:ApprovalStepActionComponent
      },
      {
        path:"approval-step-action/create",
        component:ApprovalStepActionCreateComponent
      },
      {
        path:"approval-step-action/edit/:id",
        component:ApprovalStepActionEditComponent
      },
      {
        path:"approval-process-tnx-history",
        component:ApprovalProcessTnxHistoryComponent
      }, 
      
      
      
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ApprovalRoutingModule { }
