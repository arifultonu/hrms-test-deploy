import { Component, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DataTableDirective } from 'angular-datatables';
import { data } from 'jquery';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subject } from 'rxjs';
import { AllModulesService } from 'src/app/all-modules/all-modules.service';
import { AllOrgMst } from 'src/app/all-modules/employees/model/AllOrgMst';
import { IrecruitmentService } from 'src/app/all-modules/i-recruitment/service/irecruitment.service';
import { environment } from 'src/environments/environment';
import { CommonService } from '../../../services/common.service';

declare const $: any;
@Component({
  selector: 'app-all-org-mst',
  templateUrl: './all-org-mst.component.html',
  styleUrls: ['./all-org-mst.component.css']
})
export class AllOrgMstComponent implements OnInit {

  @ViewChild(DataTableDirective, { static: false })
  public dtElement: DataTableDirective;
  public dtOptions: DataTables.Settings = {};
  public dtTrigger: Subject<any> = new Subject();
  public url: any = "leaveType";

  public baseUrl = environment.baseUrl;

  public allLeaveType: any = [];
  public allOrgMst!: AllOrgMst[];
  public parentData!:AllOrgMst;
  public parentAllOrgMst!:AllOrgMst[];
  public addOrgFormGroup:FormGroup;


  public addLeaveType: FormGroup;
  public editLeaveType: FormGroup;
  public editId: any;
  public tempId: any;
  constructor(
    private allModuleService: AllModulesService,
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private commonService: CommonService,
    private irecservice: IrecruitmentService,
    private spinnerService: NgxSpinnerService,
    private route: ActivatedRoute,
    private router: Router,
  ) {}

  ngOnInit() {
    this.getLeaveType();
    this.getAllOrgMst();

    // for data table configuration
    this.dtOptions = {
      // ... skipped ...
      pageLength: 10,
      dom: "lrtip",
    };

    // Add Provident Form Validation And Getting Values

    this.addOrgFormGroup = this.formBuilder.group({
      orgType: ["", [Validators.required]],
      title: ["", [Validators.required]],
      approvalStatus:["",Validators.required],
    });




    // Edit Provident Form Validation And Getting Values

    this.editLeaveType = this.formBuilder.group({
      editLeave: ["", [Validators.required]],
      editLeaveDays: ["", [Validators.required]],
    });
  }

  addAllOrgMst(){

    if(this.addOrgFormGroup.valid){
      let obj: AllOrgMst = Object.assign(this.addOrgFormGroup.value);
      this.commonService.saveOrgMst(obj).subscribe((data)=>{
        this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
          dtInstance.destroy();
          this.getAllOrgMst();
          this.toastr.success("Item is added", "Success");
        });
      },(error)=>{this.toastr.error("error "+error.error.message, "Success");});
      $("#add_organization").modal("hide");
      this.addOrgFormGroup.reset();
    }


  }




  getAllParentOrgMst(){

  }

  getAllOrgMst(){
    this.commonService.getAllOrgMst().subscribe((data:any)=>{
      this.allOrgMst=data;
    });
  }





  getLeaveType() {
    this.allModuleService.get(this.url).subscribe((data) => {
      this.allLeaveType = data;
      this.dtTrigger.next();
    });
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


public deleteEnityData( dataId ){

  let apiURL = this.baseUrl + "/allOrgMst/remove/" + dataId;
  console.log(apiURL);

  let formData: any = {};

  this.spinnerService.show();
  this.irecservice.sendDeleteRequest(apiURL, formData).subscribe(
    (response: any) => {
      console.log(response);
      this.spinnerService.hide();
      $("#delete_entity").modal("hide");
      this.toastr.success("Successfully item is deleted", "Success");
      this.getAllOrgMst();
    },
    (error) => {
      console.log(error);
      this.spinnerService.hide();
    }
  );

}
  // for unsubscribe datatable
  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event
    this.dtTrigger.unsubscribe();
  }
}
