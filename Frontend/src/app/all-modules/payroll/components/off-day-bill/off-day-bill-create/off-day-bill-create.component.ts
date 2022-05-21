import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import * as moment from 'moment';
import { ToastrService } from 'ngx-toastr';
import { HrCrEmp } from 'src/app/all-modules/employees/model/HrCrEmp';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { LoginService } from 'src/app/login/services/login.service';
import { environment } from 'src/environments/environment';
import { OffDayBillService } from '../../../service/off-day-bill.service';

@Component({
  selector: 'app-off-day-bill-create',
  templateUrl: './off-day-bill-create.component.html',
  styleUrls: ['./off-day-bill-create.component.css']
})
export class OffDayBillCreateComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;


  configDDL: any;



  constructor(
    private formBuilder: FormBuilder,
   private datePipe: DatePipe,
   private route: ActivatedRoute,
   private router: Router,
   private offDayBillService:OffDayBillService,
   private toastr: ToastrService,
   private commonService:CommonService


   ) {
     this._initConfigDDL();
     this._customInitLoadData();
   }

   ngOnInit(): void {
    this.initializeForm();

  }

  initializeForm(){

    this.myForm = this.formBuilder.group({
      hrCrEmp:[{}, Validators.required],
      contactNo: [""],
      hrCrEmpResponsible: [{},Validators.required],

      startDate: ["",Validators.required],
      endDate: ["",Validators.required],

      remarks: ["",Validators.required],

    });

  }

  resetFormValues()
  {

    this.myForm.reset();
    //this.setFormDefaultValues();
  }

  myFormSubmit()
  {

    if (this.myForm.invalid) {
      this.toastr.warning("Please fillup all required fields");
      return;
    }

    let diffInDays =((this.myForm.value.endDate.getTime()-this.myForm.value.startDate.getTime())/ (1000 * 3600 * 24))+1

    if(diffInDays<1)
    {
      this.toastr.error("End Date must be equal or greater");
      return;
    }





    // let obj = {
    //   contactNo: this.myForm.value.contactNo,
    //   startDate: this.myForm.value.startDate,
    //   endDate: this.myForm.value.endDate,
    //   hrCrEmp: this.getHrCrEmp.value ? { id: this.getHrCrEmp.value } : null,
    //   hrCrEmpResponsible:this.getHrCrResponsibleEmp.value ? { id: this.getHrCrResponsibleEmp.value } : null,
    //   remarks: this.myForm.value.remarks,

    // }
    let obj  = Object.assign(this.myForm.value, {
      hrCrEmp: this.getHrCrEmp.value ? { id: this.getHrCrEmp.value } : null,
      hrCrEmpResponsible:this.getHrCrResponsibleEmp.value ? { id: this.getHrCrResponsibleEmp.value } : null,
    });

    let apiURL = this.baseUrl + "/offDayBill/save";

    let formData: any = {};
    formData = obj



    // process date
    formData.startDate = (formData.startDate) ? this.datePipe.transform(formData.startDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
    formData.endDate = (formData.endDate) ? this.datePipe.transform(formData.endDate,"yyyy-MM-dd").toString().slice(0, 10) : null;


    this.offDayBillService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);

        this.router.navigate(["/payroll/off-day-bill"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);

      }
    );
  }


  // --------------------------- DDL (Dinamic Dropdown List) Methods Start -----------------------------------
  searchDDL(event: any) {
    let q = event.term;
    this.configDDL.q = q;
    this.configDDL.pageNum = 1;
    this.configDDL.append = false;
    this.getListDataDDL();
  }

  scrollToEndDDL() {
    this.configDDL.pageNum++;
    this.configDDL.append = true;
    this.getListDataDDL();
  }

  _customInitLoadData() {
    this.configDDL.activeFieldName = "ddlDescription";
    this.configDDL.dataGetApiPath = "/api/common/getEmp";
    this.configDDL.apiQueryFieldName = "hrCrEmp";
    // this.getListDataDDL();
  }

  clearDDL() {
    this.configDDL.q = "";
  }

  private getListDataDDL() {
    let apiURL = this.baseUrl + this.configDDL.dataGetApiPath;

    let queryParams: any = {};
    queryParams.pageNum = this.configDDL.pageNum;
    queryParams.pageSize = this.configDDL.pageSize;
    if (this.configDDL.q && this.configDDL.q != null) {
      queryParams[this.configDDL.apiQueryFieldName] = this.configDDL.q;
    }

    this.commonService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        if (this.configDDL.append) {
          this.configDDL.listData = this.configDDL.listData.concat(
            response.objectList
          );
        } else {
          this.configDDL.listData = response.objectList;
        }
        this.configDDL.totalItem = response.totalItems;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  setDefaultParamsDDL() {
    this._initConfigDDL();
  }

  _initConfigDDL() {
    this.configDDL = {
      pageNum: 1,
      pageSize: 10,
      totalItem: 50,
      listData: [],
      append: false,
      q: "",
      activeFieldName: "xxxFieldName",
      dataGetApiPath: "",
      apiQueryFieldName: "xxxFieldName",
    };
  }

  initSysParamsDDL(
    event,
    activeFieldNameDDL,
    dataGetApiPathDDL,
    apiQueryFieldNameDDL
  ) {
    console.log("...");
    console.log("ddlActiveFieldName:" + activeFieldNameDDL);
    console.log("dataGetApiPathDDL:" + dataGetApiPathDDL);
    console.log(event.target);

    if (
      this.configDDL.activeFieldName &&
      this.configDDL.activeFieldName != activeFieldNameDDL
    ) {
      this.setDefaultParamsDDL();
    }

    this.configDDL.activeFieldName = activeFieldNameDDL;
    this.configDDL.dataGetApiPath = dataGetApiPathDDL;
    this.configDDL.apiQueryFieldName = apiQueryFieldNameDDL;
    this.getListDataDDL();
  }
  // --------------------------- DDL (Dinamic Dropdown List) Methods End -------------------------------------

//-----------Get Relational Object Id ------------------
get getHrCrEmp() {
  return this.myForm.get("hrCrEmp");
}
get getHrCrResponsibleEmp() {
  return this.myForm.get("hrCrEmpResponsible");
}

}
