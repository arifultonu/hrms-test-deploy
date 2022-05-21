import { vislog } from './vislog.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VislistComponent } from './vislist/vislist.component';
import { RouterModule, Routes } from '@angular/router';


const routes: Routes = [
  {
    path:"",
    component:vislog,
    children:[
      {
        path:"vislog/list",
        component:VislistComponent
      },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class VislogRoutingModule { }
