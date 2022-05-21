import { CommonService } from './../../../../settings/common/services/common.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { DatePipe } from "@angular/common";
import { ActivatedRoute, Router } from "@angular/router";
import { environment } from 'src/environments/environment';
import { NgxSpinnerService } from 'ngx-spinner';
import { PayrollService } from '../../../service/payroll.service';
import { ToastrService } from 'ngx-toastr';

declare const $: any;
@Component({
  selector: 'app-payroll-element-value-create',
  templateUrl: './payroll-element-value-create.component.html',
  styleUrls: ['./payroll-element-value-create.component.css']
})
export class PayrollElementValueCreateComponent implements OnInit {

  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  configDDL: any;
  configPgn: any;
  payrollElmData: any=[];

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private payrollService: PayrollService,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private commonService : CommonService,
    private toastr: ToastrService,
    ) {
      this.configPgn = {
        pageNum: 1,
        pageSize: 10,
        pageSizes: [3, 5, 10, 25, 50, 100, 200, 500, 1000],
        totalItem: 50,
        pngDiplayLastSeq: 10,
        entityName: "",
      };

      this._initConfigDDL();
      this._customInitLoadData();
    }


  ngOnInit(): void {

    this.initializeForm();
    this.setFormDefaultValues();
    this.initButtonsRippleEffect();
    this.loadData();
    this._getPrlElmDef();

  }


  initializeForm(){

    this.myForm = this.formBuilder.group({
      empTitle: [""],
     // emp: {},
      emp:[{},Validators.required],
      elementTitle: [""],
      //payrollElement: {},
      payrollElement:[{},Validators.required],
      elementAmount: ["",Validators.required],
      activeStartDate: ["",Validators.required],
      activeEndDate: ["",Validators.required],
      isActive: [""],
    });

  }

  setFormDefaultValues(){

    var dt = new Date();
    var year = dt.getFullYear();

    // // multiple
    // this.myForm.patchValue({
    //   year: year,
    // });
    // // single
    // this.myForm.controls.year.setValue(year);

  }


  resetFormValues(){

    this.myForm.reset();
    this.setFormDefaultValues();

  }


  initButtonsRippleEffect(){
    var createRipple = function(e){

      const button = e.currentTarget;

      let x = e.clientX - e.target.getBoundingClientRect().left;
      let y = e.clientY - e.target.getBoundingClientRect().top;

        // Create span element
        let ripple = document.createElement("span");
        // Position the span element
        ripple.style.cssText = "position: absolute; background: #fff; transform: translate(-50%, -50%); pointer-events: none; border-radius: 50%; animation: animate 1s linear infinite;";
        ripple.style.left = `${x}px`;
        ripple.style.top = `${y}px`;

        // Add span to the button
        button.appendChild(ripple);

        // Remove span after 0.3s
        setTimeout(() => {
              ripple.remove();
        }, 1000);

    }

    const elements = document.querySelectorAll('.btn-ripple') as any as Array<HTMLElement>
    elements.forEach(element => {
      element.addEventListener('click', function(e){
        createRipple(e);
      });
    });

  }




  myFormSubmit() {

    let apiURL = this.baseUrl + "/api/payrollElValue/create";

    if(this.myForm.invalid){
      this.toastr.warning("Please fillup all required fields")
      return;
    }

    let formData: any = Object.assign(this.myForm.value, {
      emp: this.getEmployee.value
        ? { id: this.getEmployee.value }
        : null,
      payrollElement: this.getPayrollElement.value ? { id: this.getPayrollElement.value } : null,

    });

  //  let formData: any = {};
    formData = this.myForm.value
    formData.rActiveOperation = "Create";
    // process date
    formData.activeStartDate = (formData.activeStartDate) ? this.datePipe.transform(formData.activeStartDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
    formData.activeEndDate = (formData.activeEndDate) ? this.datePipe.transform(formData.activeEndDate,"yyyy-MM-dd").toString().slice(0, 10) : null;

    console.log(formData);

    this.spinnerService.show();
    this.payrollService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.toastr.success("Created Successfully");
        this.spinnerService.hide();
        this.router.navigate(["/payroll/element-value/list"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.spinnerService.hide();
      }
    );


  }



  loadData(){

    let apiURL = this.baseUrl + "/salaryProcess/start";

    let queryParams: any = {};
    queryParams.abc = "OK";

    // this.payrollService.sendGetRequest(apiURL, queryParams).subscribe(
    //   (response: any) => {
    //   console.log(response);
    //   },
    //   (error) => console.log(error)
    // );


  }


  _getPrlElmDef(){

    let apiURL = this.baseUrl + "/api/common/getPrlElmDef";

    this.spinnerService.show();
    this.payrollService.sendGetRequest(apiURL,{}).subscribe(
      (response: any) => {
        this.payrollElmData = response.objectList;
        console.log(response);
        this.spinnerService.hide();
      },
      (error) => {
        console.log(error);
        this.spinnerService.hide();
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

initSysParamsDDL( event, activeFieldNameDDL, dataGetApiPathDDL, apiQueryFieldNameDDL) {

  console.log("...");
  console.log("ddlActiveFieldName:" + activeFieldNameDDL);
  console.log("dataGetApiPathDDL:" + dataGetApiPathDDL);
  console.log(event.target);

  if ( this.configDDL.activeFieldName && this.configDDL.activeFieldName != activeFieldNameDDL) {
    this.setDefaultParamsDDL();
  }

  this.configDDL.activeFieldName = activeFieldNameDDL;
  this.configDDL.dataGetApiPath = dataGetApiPathDDL;
  this.configDDL.apiQueryFieldName = apiQueryFieldNameDDL;
  this.getListDataDDL();
}
// --------------------------- DDL (Dinamic Dropdown List) Methods End -------------------------------------


get getEmployee() {
  return this.myForm.get("emp");
}

get getPayrollElement(){
  return this.myForm.get("payrollElement");
}



}
