import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { XyzyourmoduleRoutingModule } from './xyzyourmodule-routing.module';
import { XyzyourmoduleComponent } from './xyzyourmodule.component';
import { XyzyourmodulePage1Component } from './components/xyzyourmodule-page1/xyzyourmodule-page1.component';


@NgModule({
  declarations: [XyzyourmoduleComponent, XyzyourmodulePage1Component],
  imports: [
    CommonModule,
    XyzyourmoduleRoutingModule
  ]
})
export class XyzyourmoduleModule { }
