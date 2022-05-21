import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VislogRoutingModule } from './vislog-routing.module';

import { CUSTOM_ELEMENTS_SCHEMA  } from '@angular/core';
import { vislog } from './vislog.component';
import { VislistComponent } from './vislist/vislist.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharingModule } from 'src/app/sharing/sharing.module';
import { NgxSpinnerModule } from 'ngx-spinner';
import { NgSelectModule } from '@ng-select/ng-select';
import { PickListModule } from 'primeng/picklist';
import { BsDatepickerModule } from 'ngx-bootstrap';

@NgModule({
  declarations: [vislog,VislistComponent],
  imports: [
    CommonModule,
    VislogRoutingModule,
    NgxPaginationModule,
    HttpClientModule,
    FormsModule,
    SharingModule,
    ReactiveFormsModule,
    NgxSpinnerModule,
    NgSelectModule,
    PickListModule,
    BsDatepickerModule.forRoot(),
   
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class VislogModule { }
