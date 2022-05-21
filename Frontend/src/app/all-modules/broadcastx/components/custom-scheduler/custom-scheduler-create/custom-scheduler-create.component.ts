import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { environment } from 'src/environments/environment';
import { BroadcastxService } from '../../../services/broadcastx.service';

@Component({
  selector: 'app-custom-scheduler-create',
  templateUrl: './custom-scheduler-create.component.html',
  styleUrls: ['./custom-scheduler-create.component.css']
})
export class CustomSchedulerCreateComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  configDDL: any;
  configPgn: any;
  selected:0;


  constructor(
    private spinnerService: NgxSpinnerService,
    private commonService : CommonService,
    private datePipe: DatePipe,
    private formBuilder: FormBuilder,
    private broadcastxService:BroadcastxService,
    private router: Router,
    private route: ActivatedRoute,
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
   // this._customInitLoadData();
   }

  ngOnInit(): void {
    this.initializeForm();
  }
  initializeForm(){

    this.myForm = this.formBuilder.group({
  
      jobTitle: ["", Validators.required],
      isAllEmp: [""],
      empIds: [""],  
      procFromDate: ["", Validators.required],
      procToDate: ["", Validators.required],
    
    });

  }
  formSubmit()
  {
    
    console.log(this.myForm.value.empIds);
   
    
    if (this.myForm.invalid) {
      return;
    }
    if(this.myForm.value.isAllEmp==true&&this.myForm.value.empIds!=[])
    {  
     this.myForm.reset();
     return;
    }
    if(this.myForm.value.isAllEmp!=true&&this.myForm.value.empIds==[])
    {  
     this.myForm.reset();
     return;
    }
    
    let  stringEmpIds;
    if(this.myForm.value.empIds!=[])
    {
      stringEmpIds=this.myForm.value.empIds.join(',');
    }
   
    

   
   
    let obj  = Object.assign(this.myForm.value, 
      
       { empIds: stringEmpIds}
      );

      

    let apiURL = this.baseUrl + "/commonJobProc/save";

    let formData: any = {};
    formData = obj
    // process date
    formData.procFromDate = (formData.procFromDate) ? this.datePipe.transform(formData.procFromDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
    formData.procToDate = (formData.procToDate) ? this.datePipe.transform(formData.procToDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
    formData.jobType="CUSTOM_SCHEDULER"
    console.log(formData);
    
    
    
    this.broadcastxService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
     
        this.router.navigate(["/broadcastx/custom-job"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
    
      }
    );
   
    
  }
 

  resetFormValues()
  {
    this.myForm.reset();
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

// _customInitLoadData() {
//   this.configDDL.activeFieldName = "ddlDescription";
//   this.configDDL.dataGetApiPath = "/api/common/getEmp";
//   this.configDDL.apiQueryFieldName = "hrCrEmp";
//   // this.getListDataDDL();
// }

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


}
