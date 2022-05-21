import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SettingsRoutingModule } from './settings-routing.module';
import { SettingsComponent } from './settings.component';
import { CompanySettingsComponent } from './company-settings/company-settings.component';
import { LocalizationComponent } from './localization/localization.component';
import { ThemeSettingsComponent } from './theme-settings/theme-settings.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { LeaveTypeComponent } from './leave-type/leave-type.component';
import { DataTablesModule } from 'angular-datatables';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CreateUserComponent } from './systems/components/users/create-user/create-user.component';
import { ShowUsersComponent } from './systems/components/users/show-users/show-users.component';
import { CreateAlkpComponent } from './common/components/alkp/create-alkp/create-alkp.component';
import { ShowAlkpComponent } from './common/components/alkp/show-alkp/show-alkp.component';
import { AllOrgMstComponent } from './common/components/all-org-mst/all-org-mst/all-org-mst.component';
import { LeaveConfigComponent } from './leave/components/leave-config/leave-config.component';
import {NgxPaginationModule} from 'ngx-pagination';
import { CreateEditBasAddressComponent } from './common/components/bas-address/create-edit-bas-address/create-edit-bas-address.component';
import { NgxSpinnerModule } from "ngx-spinner";
import { NgSelectModule } from '@ng-select/ng-select';
import { LeaveAssignComponent } from './leave/components/leave-assign/leave-assign.component';
import { SysResDefComponent } from './systems/components/sys-res-def/sys-res-def.component';
import { ListSysResDefComponent } from './systems/components/list-sys-res-def/list-sys-res-def.component';
import { UserListComponent } from './systems/components/users/user-list/user-list.component';
import { ChatComponent } from './common/components/chat/chat.component';
import { ApiConfigComponent } from './apiConfig/api-config/api-config.component';
import { ApiConfigCreateComponent } from './apiConfig/api-config-create/api-config-create.component';
import { GovtHolidayComponent } from './companyCalander/components/govt-holiday/govt-holiday.component';
import { GovtHolidayEditComponent } from './companyCalander/components/govt-holiday-edit/govt-holiday-edit.component';
import { GovtHolidayViewComponent } from './companyCalander/components/govt-holiday-view/govt-holiday-view.component';
import { GovtHolidayCreateComponent } from './companyCalander/components/govt-holiday-create/govt-holiday-create.component';
import { BsDatepickerModule } from 'ngx-bootstrap';
import { EditOrgMstComponent } from './common/components/all-org-mst/edit-org-mst/edit-org-mst.component';
import { HttpClientModule } from '@angular/common/http';
import { PickListModule } from 'primeng/picklist';

import { CUSTOM_ELEMENTS_SCHEMA  } from '@angular/core';



@NgModule({
  declarations: [SettingsComponent, CompanySettingsComponent, LocalizationComponent, ThemeSettingsComponent, ChangePasswordComponent, LeaveTypeComponent, CreateUserComponent, ShowUsersComponent, CreateAlkpComponent, ShowAlkpComponent, AllOrgMstComponent, LeaveConfigComponent, CreateEditBasAddressComponent, LeaveAssignComponent, SysResDefComponent, ListSysResDefComponent, UserListComponent, ChatComponent, ApiConfigComponent, ApiConfigCreateComponent, GovtHolidayComponent, GovtHolidayEditComponent, GovtHolidayViewComponent, GovtHolidayCreateComponent, EditOrgMstComponent],
  imports: [
    CommonModule,
    SettingsRoutingModule,
    BsDatepickerModule.forRoot(),
    DataTablesModule,
    FormsModule,
    ReactiveFormsModule,
    NgxPaginationModule,
    NgxSpinnerModule,
    NgSelectModule,
    HttpClientModule,
    PickListModule,

  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class SettingsModule { }
