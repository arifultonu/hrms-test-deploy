
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ShowAlkpComponent } from '../settings/common/components/alkp/show-alkp/show-alkp.component';
import { CreateComponent } from './applicant/create/create.component';
import { ListComponent } from './applicant/list/list.component';
import { IRecruitmentComponent } from './i-recruitment.component';
import { CreateVacancyComponent } from './vacancy/create/create.component';
import { EditComponent } from './vacancy/edit/edit.component';
import { VacancyListComponent } from './vacancy/list/vacancy.component';
import { ShowComponent } from './vacancy/show/show.component';
import {  ShowComponentt } from './applicant/show/show.component';
import {  EditComponentt } from './applicant/edit/edit.component';
import { AJListComponent } from './appliedjob/list/list.component';
import { AJCreateComponent } from './appliedjob/create/create.component';
import { AJEditComponentt } from './appliedjob/edit/edit.component';
import { AJShowComponent } from './appliedjob/show/show.component';
import { VivaListComponent } from './interviewBoard/list/list.component';
import { VivaCreateComponent } from './interviewBoard/create/create.component';
import { VivaEditComponent } from './interviewBoard/edit/edit.component';
import { IvbmblistComponent } from './interviewBoardMember/ivbmblist/ivbmblist.component';
import { IvbmbcreateComponent } from './interviewBoardMember/ivbmbcreate/ivbmbcreate.component';
import { BrdShowComponent } from './interviewBoard/show/show.component';
import { TpvListComponent } from './Tele-Phone-Viva/tpv-list/tpv-list.component';
import { TpvCreateComponent } from './Tele-Phone-Viva/tpv-create/tpv-create.component';


const routes: Routes = [
  {
    path:"",
    component:IRecruitmentComponent,
    children:[
      {
        path:"vacancy/list",
        component:VacancyListComponent
      },
      {
        path:"vacancy/create",
        component:CreateVacancyComponent
      },
      {
        path:"vacancy/edit/:id",
        component:EditComponent
      },
      {
        path:"vacancy/show/:id",
        component:ShowComponent
      },
      {
        path:"applicant/list",
        component:ListComponent
      },

      {
        path:"applicant/create",
        component:CreateComponent
      },

      {
        path:"applicant/show/:id",
        component:ShowComponentt
      },
      {
        path:"applicant/edit/:id",
        component:EditComponentt
      },

      {
        path:"telePhoneViva/list",
        component:TpvListComponent
      },

      {
        path:"telePhoneViva/create",
        component:TpvCreateComponent
      },


      {
        path:"appliedjob/list",
        component:AJListComponent
      },
      {
        path:"appliedjob/create",
        component:AJCreateComponent
      },
      {
        path:"appliedjob/edit/:id",
        component:AJEditComponentt
      },

      {
        path:"appliedjob/show/:id",
        component:AJShowComponent
      },

      {
        path:"vivaboard/list",
        component:VivaListComponent
      },
      {
        path:"vivaboard/create",
        component:VivaCreateComponent
      },
      {
        path:"vivaboard/edit/:id",
        component:VivaEditComponent
      },

      {
        path:"vivaboardmember/list",
        component:IvbmblistComponent
      },

      {
        path:"vivaboardmember/create",
        component:IvbmbcreateComponent
      },
      {
        path:"vivaboard/show/:id",
        component:BrdShowComponent
      },

    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class IRecruitmentRoutingModule { }
