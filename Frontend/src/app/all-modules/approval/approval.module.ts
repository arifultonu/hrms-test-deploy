import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CUSTOM_ELEMENTS_SCHEMA  } from '@angular/core';
import { DataTablesModule } from 'angular-datatables';
import { BsDatepickerModule } from 'ngx-bootstrap';
import { SharingModule } from 'src/app/sharing/sharing.module';
import { ReactiveFormsModule } from '@angular/forms';
import { PickListModule } from 'primeng/picklist';
import {NgxPaginationModule} from 'ngx-pagination';
import { NgxSpinnerModule } from "ngx-spinner";

import { ApprovalRoutingModule } from './approval-routing.module';
import { ApprovalProcessComponent } from './components/approvalProcess/approval-process/approval-process.component';
import { ApprovalStepComponent } from './components/approvalStep/approval-step/approval-step.component';
import { ApprovalComponent } from './approval.component';
import { CreateComponent } from './components/approvalProcess/create/create.component';
import { EditComponent } from './components/approvalProcess/edit/edit.component';
import { ApprovalStepEditComponent } from './components/approvalStep/approval-step-edit/approval-step-edit.component';
import { ApprovalStepCreateComponent } from './components/approvalStep/approval-step-create/approval-step-create.component';
import { ApprovalStepApproverComponent } from './components/approvalStepApprover/approval-step-approver/approval-step-approver.component';
import { ApprovalStepApproverCreateComponent } from './components/approvalStepApprover/approval-step-approver-create/approval-step-approver-create.component';
import { ApprovalStepApproverEditComponent } from './components/approvalStepApprover/approval-step-approver-edit/approval-step-approver-edit.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { ApprovalStepActionComponent } from './components/approvalStepAction/approval-step-action/approval-step-action.component';
import { ApprovalStepActionCreateComponent } from './components/approvalStepAction/approval-step-action-create/approval-step-action-create.component';
import { ApprovalStepActionEditComponent } from './components/approvalStepAction/approval-step-action-edit/approval-step-action-edit.component';
import { ApprovalProcessTnxHistoryComponent } from './components/ApprovalProcessTnxHistory/approval-process-tnx-history/approval-process-tnx-history.component';


@NgModule({
  declarations: [ApprovalComponent, ApprovalProcessComponent, ApprovalStepComponent, CreateComponent, EditComponent, ApprovalStepEditComponent, ApprovalStepCreateComponent, ApprovalStepApproverComponent, ApprovalStepApproverCreateComponent, ApprovalStepApproverEditComponent, ApprovalStepActionComponent, ApprovalStepActionCreateComponent, ApprovalStepActionEditComponent, ApprovalProcessTnxHistoryComponent],
  imports: [
    CommonModule,
    ApprovalRoutingModule,
    DataTablesModule,
    BsDatepickerModule.forRoot(),
    SharingModule,
    ReactiveFormsModule,
    PickListModule,
    NgxPaginationModule,
    NgxSpinnerModule,
    NgSelectModule

  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class ApprovalModule { }
