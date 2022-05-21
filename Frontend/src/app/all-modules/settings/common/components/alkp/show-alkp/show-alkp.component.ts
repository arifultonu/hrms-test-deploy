import { Component, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { DataTableDirective } from 'angular-datatables';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subject } from 'rxjs';
import { first } from 'rxjs/operators';
import { AllModulesService } from 'src/app/all-modules/all-modules.service';
import { IrecruitmentService } from 'src/app/all-modules/i-recruitment/service/irecruitment.service';
import { environment } from 'src/environments/environment';
import { ALKP } from '../../../models/alkp';
import { CommonService } from '../../../services/common.service';

declare const $: any;
@Component({
  selector: 'app-show-alkp',
  templateUrl: './show-alkp.component.html',
  styleUrls: ['./show-alkp.component.css']
})
export class ShowAlkpComponent implements OnInit {
  @ViewChild(DataTableDirective, { static: false })
  public dtElement: DataTableDirective;
  public dtOptions: DataTables.Settings = {};
  public dtTrigger: Subject<any> = new Subject();
  public url: any = "leaveType";
  public baseUrl = environment.baseUrl;
  public allLeaveType: any = [];
  public alkp!:ALKP[];

  public parentAlkp:ALKP[];

  public addLeaveType: FormGroup;
  public editLeaveType: FormGroup;


  public addAlkp: FormGroup;
  public editAlkp: FormGroup;

  public editId: any;
  public tempId: any;
  constructor(
    private allModuleService: AllModulesService,
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private commonService: CommonService,
    private spinnerService: NgxSpinnerService,
    private irecservice: IrecruitmentService,
  ) {}

  ngOnInit() {
    this.getAlkp();
    this.getParentAlkp();
    this.getLeaveType();
    // for data table configuration
    this.dtOptions = {
      // ... skipped ...
      pageLength: 10,
      dom: "lrtip",
    };

    // Add Provident Form Validation And Getting Values

    this.addLeaveType = this.formBuilder.group({
      addLeaveType: ["", [Validators.required]],
      addLeaveDays: ["", [Validators.required]],
    });

     // Add Provident Form Validation And Getting Values
     this.addAlkp=this.formBuilder.group({
       title: ["", [Validators.required]],
       keyword: [],
       sequence: ["", [Validators.required]],
       code: ["", [Validators.required]],
       parentId:[]
     });

    // Edit Provident Form Validation And Getting Values

    this.editLeaveType = this.formBuilder.group({
      editLeave: ["", [Validators.required]],
      editLeaveDays: ["", [Validators.required]],
    });
  }

  getLeaveType() {
    this.allModuleService.get(this.url).subscribe((data) => {
      this.allLeaveType = data;
      this.dtTrigger.next();
    });
  }

  //get alkp
  getAlkp(){
    this.commonService.getAlkp().subscribe((data:any) =>{
      this.alkp=data;
      console.log('@SendGetRequest'+this.alkp);
    })

  }
  // Get parent Alkp
  getParentAlkp(){
    this.commonService.getParentAlkp().subscribe((data:any) =>{
      this.parentAlkp=data;
    })
  }

  // Add Provident Modal Api Call

  addLeave() {
    if (this.addLeaveType.valid) {
      let obj = {
        leaveType: this.addLeaveType.value.addLeaveType,
        leaveDays: this.addLeaveType.value.addLeaveDays,
      };
      this.allModuleService.add(obj, this.url).subscribe((data) => {
        this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
          dtInstance.destroy();
        });
      });
      this.getLeaveType();
      $("#add_leavetype").modal("hide");
      this.addLeaveType.reset();
      this.toastr.success("Leave type is added", "Success");
    }
  }

    // Add Provident Modal Api Call
    saveAlkp(){
      if(this.addAlkp.valid){
        let obj : ALKP=Object.assign(this.addAlkp.value);

        this.commonService.saveAlkp(obj).subscribe((data) => {
          this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
            dtInstance.destroy();
            this.getAlkp();
            this.toastr.success("Alkp type is added", "Success");
          });
        },(error)=>{this.toastr.error("error "+error.error.message, "Success");});
      $("#add_leavetype").modal("hide");
      this.addAlkp.reset();

      }
    }

  // Edit Provident Modal Api Call

  editLeave() {
    let obj = {
      leaveType: this.editLeaveType.value.editLeave,
      leaveDays: this.editLeaveType.value.editLeaveDays,
      id: this.editId,
    };
    this.allModuleService.update(obj, this.url).subscribe((data1) => {
      this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
        dtInstance.destroy();
      });
    });
    this.getLeaveType();
    $("#edit_leavetype").modal("hide");
    this.toastr.success("Leave type is edited", "Success");
  }

  edit(value) {
    this.editId = value;
    const index = this.allLeaveType.findIndex((item) => {
      return item.id === value;
    });
    let toSetValues = this.allLeaveType[index];
    this.editLeaveType.setValue({
      editLeave: toSetValues.leaveType,
      editLeaveDays: toSetValues.leaveDays,
    });
  }


  // Delete Provident Modal Api Call

  deleteLeave() {
    this.allModuleService.delete(this.tempId, this.url).subscribe((data) => {
      this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
        dtInstance.destroy();
      });
      this.getLeaveType();
      $("#delete_leavetype").modal("hide");
      this.toastr.success("Leave type is deleted", "Success");
    });
  }
  // for unsubscribe datatable
  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event
    this.dtTrigger.unsubscribe();
  }


}
