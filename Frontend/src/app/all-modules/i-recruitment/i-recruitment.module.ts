import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { IRecruitmentRoutingModule } from './i-recruitment-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { IRecruitmentComponent } from './i-recruitment.component';
import { CreateVacancyComponent } from './vacancy/create/create.component';
import { VacancyListComponent } from './vacancy/list/vacancy.component';

import { CUSTOM_ELEMENTS_SCHEMA  } from '@angular/core';
import { DataTablesModule } from 'angular-datatables';
import { BsDatepickerModule } from 'ngx-bootstrap';
import { SharingModule } from 'src/app/sharing/sharing.module';
import { ReactiveFormsModule } from '@angular/forms';
import { PickListModule } from 'primeng/picklist';
import {NgxPaginationModule} from 'ngx-pagination';
import { NgxSpinnerModule } from "ngx-spinner";
import { EditComponent } from './vacancy/edit/edit.component';
import { ShowComponent } from './vacancy/show/show.component';

import { ListComponent } from './applicant/list/list.component';
import { CreateComponent } from './applicant/create/create.component';
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
import { NgSelectModule } from '@ng-select/ng-select';
import { BrdShowComponent } from './interviewBoard/show/show.component';
import { CKEditorModule } from '@ckeditor/ckeditor5-angular';
import { TpvListComponent } from './Tele-Phone-Viva/tpv-list/tpv-list.component';
import { TpvCreateComponent } from './Tele-Phone-Viva/tpv-create/tpv-create.component';
import { TpvEditComponent } from './Tele-Phone-Viva/tpv-edit/tpv-edit.component';
import { TpvShowComponent } from './Tele-Phone-Viva/tpv-show/tpv-show.component';
import {NgxPrintModule} from 'ngx-print';


@NgModule({
  declarations: [ IRecruitmentComponent, CreateVacancyComponent,  VacancyListComponent, EditComponent, ShowComponent, ListComponent, CreateComponent,ShowComponentt,EditComponentt,AJListComponent,AJCreateComponent,AJEditComponentt,AJShowComponent,VivaListComponent,VivaCreateComponent,VivaEditComponent, IvbmblistComponent, IvbmbcreateComponent,BrdShowComponent, TpvListComponent, TpvCreateComponent, TpvEditComponent, TpvShowComponent],
  imports: [
    CommonModule,
    IRecruitmentRoutingModule,
    HttpClientModule,
    FormsModule,
    SharingModule,
    CKEditorModule,
    ReactiveFormsModule,
    NgxPaginationModule,
    NgxSpinnerModule,
    NgSelectModule,
    PickListModule,
    NgxPrintModule,
    BsDatepickerModule.forRoot(),
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class IRecruitmentModule { }
