import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';

import { environment } from 'src/environments/environment';
import { OffDayBillService } from '../../../service/off-day-bill.service';

@Component({
  selector: 'app-off-day-bill-edit',
  templateUrl: './off-day-bill-edit.component.html',
  styleUrls: ['./off-day-bill-edit.component.css']
})
export class OffDayBillEditComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  public myFormData: any = {};
 

  configDDL: any;


  constructor(
    private formBuilder: FormBuilder,
   private datePipe: DatePipe,
   private route: ActivatedRoute,
   private router: Router,
   private offDayBillService:OffDayBillService,
   private toastr: ToastrService,
   private commonService:CommonService,
   private spinnerService: NgxSpinnerService
  ) {
    this._initConfigDDL();
      this._customInitLoadData();
   }

  ngOnInit(): void {
    this.initializeForm()
    this.getFormData()
  }
  initializeForm(){

    this.myForm = this.formBuilder.group({
      id:[""],
      hrCrEmp:["", Validators.required],
      contactNo: [""],
      hrCrEmpResponsible: ["",Validators.required],
     
      startDate: [""],
      endDate: [""],
     
      remarks: [""],

    });

  }
  getFormData(){

    let id =  this.route.snapshot.params.id;
    let apiURL = this.baseUrl + "/offDayBill/get/" + id;

    let queryParams: any = {};
   

    this.spinnerService.show();
    this.offDayBillService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.myFormData = response;
       console.log(this.myFormData);
       
        this.spinnerService.hide();
       
        // field 1
        let hrCrEmpVal = [
          {
            ddlCode: response.hrCrEmp.id,
            ddlDescription: response.hrCrEmp.loginCode + "-" + response.hrCrEmp.displayName,
          },
        ];
        this.configDDL.listData = hrCrEmpVal;
        this.myFormData.hrCrEmp =  response.hrCrEmp.id;

       // field 2
        let hrCrEmpResponsibleVal = [
          {
            ddlCode: response.hrCrEmpResponsible.id,
            ddlDescription: response.hrCrEmpResponsible.loginCode + "-" + response.hrCrEmpResponsible.displayName,
          },
        ];
        this.configDDL.listData2 = hrCrEmpResponsibleVal;
        this.myFormData.hrCrEmpResponsible =  response.hrCrEmpResponsible.id;

        //set date
        this.myFormData.startDate = this.datePipe.transform(response.startDate,"MM-dd-yyyy").toString().slice(0, 10) ;
        this.myFormData.endDate = this.datePipe.transform(response.endDate,"MM-dd-yyyy").toString().slice(0, 10) ;;
        

        
        this.myForm.patchValue(this.myFormData); 


      },
      (error) => {
        console.log(error)
      } 
    );


  }
  saveUpdatedFormData()
  {
    // alert("okok")
  
    //let diffInDays =((this.myForm.value.endDate.getTime()-this.myForm.value.startDate.getTime())/ (1000 * 3600 * 24))+1
 

    // if(diffInDays<1)
    // {
    //   this.toastr.error("End Date must be equal or greater")
    //   return;
    // }

    if (this.myForm.invalid) {
      
      return;
    }
   

   
    let obj  = Object.assign(this.myForm.value, {
      hrCrEmp: this.getHrCrEmp.value ? { id: this.getHrCrEmp.value } : null,
      hrCrEmpResponsible:this.getHrCrResponsibleEmp.value ? { id: this.getHrCrResponsibleEmp.value } : null,
    });
   
    
  
    let apiURL = this.baseUrl + "/offDayBill/edit" ;
    //alert(this.myForm.value.id)
   

    let formData: any = {};
    formData = obj

    // process date
    formData.startDate = (formData.startDate) ? this.datePipe.transform(formData.startDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
    formData.endDate = (formData.endDate) ? this.datePipe.transform(formData.endDate,"yyyy-MM-dd").toString().slice(0, 10) : null;

    
    this.spinnerService.show();
    this.offDayBillService.sendPutRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.spinnerService.hide();  
        this.router.navigate(["/payroll/off-day-bill"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.spinnerService.hide(); 
        this.toastr.error(error.error.message) 
      }
    );
  }


  resetFormValues()
  {
    //this.myForm.reset();
    this.getFormData()
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
          this.configDDL.listData2 = this.configDDL.listData2.concat(
            response.objectList
          );
        } else {
          this.configDDL.listData = response.objectList;
          this.configDDL.listData2 = response.objectList;
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
      listData2: [],
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
