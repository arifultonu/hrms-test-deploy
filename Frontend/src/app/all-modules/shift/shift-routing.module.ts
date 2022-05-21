import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ShiftComponent } from './shift.component';
import { ShiftListComponent } from './components/shift-list/shift-list.component';
import { ShiftAssignComponent } from './components/shiftAssign/shift-assign/shift-assign.component';


const routes: Routes = [
  {
    path:"",
    component:ShiftComponent,
    children:[
     {
      path:"shift-list",
      component:ShiftListComponent
     },
     {
      path:"shift-assign",
      component:ShiftAssignComponent
     }

    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ShiftRoutingModule { }
