import { Component, OnDestroy, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { AllModulesService } from "../../../all-modules.service";
import { ToastrService } from "ngx-toastr";
import { DataTableDirective } from "angular-datatables";
import { Subject } from "rxjs";
import { DatePipe } from "@angular/common";
import { HrTlShiftDtl } from "../../model/HrTlShftDtl";
import { ShiftService } from "../../services/shift.service";
import { Pipe, PipeTransform } from '@angular/core';
declare const $: any;
@Component({
  selector: 'app-shift-list',
  templateUrl: './shift-list.component.html',
  styleUrls: ['./shift-list.component.css']
})

export class ShiftListComponent implements OnInit, OnDestroy  {
  @ViewChild(DataTableDirective, { static: false })
  public dtElement: DataTableDirective;
  public dtOptions: DataTables.Settings = {};
  public dtTrigger: Subject<any> = new Subject();
  public pipe = new DatePipe("en-US");
 
  public addShiftForm: FormGroup;
  public editShiftForm: FormGroup;

  public hrTlShiftDtl:HrTlShiftDtl;
 
 
  constructor(
  	private formBuilder: FormBuilder,
    private shiftService:ShiftService,
    private toastr: ToastrService
  	) { }

  ngOnInit() {
   

 
    this.loadAllShift()
  
  	this.addShiftForm = this.formBuilder.group({
      title: ["", [Validators.required]],
      code: ["", [Validators.required]],
      startTime: ["", [Validators.required]],
      endTime: ["", [Validators.required]],
      remarks: ["", [Validators.required]],
     
    });
     this.editShiftForm = this.formBuilder.group({
      Title: ["", [Validators.required]],
      Code: ["", [Validators.required]],
      StartTime: ["", [Validators.required]],
      EndTtme: ["", [Validators.required]],
      Remarks: ["", [Validators.required]],
    });
  }
  
  loadAllShift()
  {
    this.shiftService.getAllShift().subscribe((data: any)=>
    {
     
      this.hrTlShiftDtl=data;
 
    });
  }
 

  addShift()
  {
    if (this.addShiftForm.invalid) {
      this.toastr.info("Please insert valid data")
      return;
    }
   
    this.hrTlShiftDtl=Object.assign(this.addShiftForm.value) 
    this.shiftService.createShift(this.hrTlShiftDtl).subscribe(() => {

      $("#add_shift").modal("hide");
      this.addShiftForm.reset();
      this.loadAllShift();
      this.toastr.success("Successfully Added Shift");
     },
     (error) => {
      this.toastr.error("Error in creating shift ");
     
    });

   
  }
  


    
   ngOnDestroy(): void {
    // Do not forget to unsubscribe the event
    this.dtTrigger.unsubscribe();
  }

  

}
 

