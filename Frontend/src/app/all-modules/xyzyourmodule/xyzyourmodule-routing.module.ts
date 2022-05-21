import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { XyzyourmodulePage1Component } from './components/xyzyourmodule-page1/xyzyourmodule-page1.component';
import { XyzyourmoduleComponent } from './xyzyourmodule.component';

const routes: Routes = [
  {
    path: "",
    component: XyzyourmoduleComponent,
    children: [
      {
        path: "page1",
        component: XyzyourmodulePage1Component
      }
    ]
  }
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class XyzyourmoduleRoutingModule { }
