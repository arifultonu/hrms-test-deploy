import { GovtHolidayEditComponent } from './companyCalander/components/govt-holiday-edit/govt-holiday-edit.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SettingsComponent } from './settings.component';
import { CompanySettingsComponent } from './company-settings/company-settings.component';
import { LocalizationComponent } from './localization/localization.component';
import { ThemeSettingsComponent } from './theme-settings/theme-settings.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { ShowUsersComponent } from './systems/components/users/show-users/show-users.component';
import { ShowAlkpComponent } from './common/components/alkp/show-alkp/show-alkp.component';
import { AllOrgMstComponent } from './common/components/all-org-mst/all-org-mst/all-org-mst.component';
import { LeaveConfigComponent } from './leave/components/leave-config/leave-config.component';
import { CreateEditBasAddressComponent } from './common/components/bas-address/create-edit-bas-address/create-edit-bas-address.component';
import { LeaveAssignComponent } from './leave/components/leave-assign/leave-assign.component';
import { SysResDefComponent } from './systems/components/sys-res-def/sys-res-def.component';
import { ListSysResDefComponent } from './systems/components/list-sys-res-def/list-sys-res-def.component';
import { CreateUserComponent } from './systems/components/users/create-user/create-user.component';
import { UserListComponent } from './systems/components/users/user-list/user-list.component';
import { ChatComponent } from './common/components/chat/chat.component';
import { ApiConfigComponent } from './apiConfig/api-config/api-config.component';
import { ApiConfigCreateComponent } from './apiConfig/api-config-create/api-config-create.component';
import { GovtHolidayComponent } from './companyCalander/components/govt-holiday/govt-holiday.component';
import { GovtHolidayCreateComponent } from './companyCalander/components/govt-holiday-create/govt-holiday-create.component';
import { EditOrgMstComponent } from './common/components/all-org-mst/edit-org-mst/edit-org-mst.component';





const routes: Routes = [
  {
    path:"",
    component:SettingsComponent,
    children:[
      {
        path:"company-settings",
        component:CompanySettingsComponent
      },
      {
        path:"system-users",
        component:ShowUsersComponent
      },
      {
        path:"system-user/create",
        component:CreateUserComponent
      },
      {
        path:"system-user/edit/:id",
        component:CreateUserComponent
      },
      {
        path:"system-user/show/:id",
        component:CreateUserComponent
      },
      {
        path:"system-user/lists",
        component:UserListComponent
      },
      {
        path:"alkp",
        component:ShowAlkpComponent
      },
      {
        path:"all-org-mst",
        component:AllOrgMstComponent
      },
      {
        path:"edit-org-mst/:id",
        component:EditOrgMstComponent
      },
      {
        path:"bas-address",
        component:CreateEditBasAddressComponent,
      },
      {
        path:"bas-address/:id",
        component:CreateEditBasAddressComponent,
      },
      {
        path:"sys-resDef",
        component:SysResDefComponent,
      },
      {
        path:"list-sys-resDef",
        component:ListSysResDefComponent,
      },
      {
        path:"sys-resDef/:id",
        component:SysResDefComponent,
      },
      {
        path:"localization",
        component:LocalizationComponent
      },
      {
        path:"theme-settings",
        component:ThemeSettingsComponent
      },
      {
        path:"change-password",
        component:ChangePasswordComponent
      },
      {
        path:"leave-assign",
        component:LeaveAssignComponent
      },
      {
        path:"leave-config",
        component:LeaveConfigComponent
      },
      {
        path:"chat",
        component:ChatComponent
      },
      {
        path:"api-config",
        component:ApiConfigComponent
      },
      {
        path:"api-config/create",
        component:ApiConfigCreateComponent
      },
      {
        path:"holiday-config",
        component:GovtHolidayComponent
      },
      {
        path:"holiday-config/create",
        component:GovtHolidayCreateComponent
      },
      {
        path:"holiday-config/edit/:id",
        component:GovtHolidayEditComponent
      },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SettingsRoutingModule { }
