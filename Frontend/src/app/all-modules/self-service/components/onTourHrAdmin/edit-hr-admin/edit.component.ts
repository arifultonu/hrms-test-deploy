import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { HrCrEmp } from 'src/app/all-modules/employees/model/HrCrEmp';
import { EmployeeService } from 'src/app/all-modules/employees/services/employee.service';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { LoginService } from 'src/app/login/services/login.service';
import { environment } from 'src/environments/environment';
import { OnTour } from '../../../models/onTour';
import { OnTourService } from '../../../service/on-tour.service';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class EditHrAdminComponent implements OnInit {

  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  public myFormData: any = {};


  configDDL: any;


  user!: HrCrEmp;
  constructor(
     private formBuilder: FormBuilder,
    private datePipe: DatePipe,

    private route: ActivatedRoute,
    private router: Router,
    private onTourService:OnTourService,

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
      hrCrEmp:["",[Validators.required]],
      //contactNo: ["",[Validators.required]],
      hrCrEmpResponsible: ["",[Validators.required]],
      tourType: ["",[Validators.required]],
      startDate: ["",[Validators.required]],
      endDate: ["",[Validators.required]],
     // tourDays: [""],
      addressDuringTour: ["",[Validators.required]],
      reasonForTour: ["",[Validators.required]],
      remarks: [""],



    });

  }



  getFormData(){

    let id =  this.route.snapshot.params.id;
    let apiURL = this.baseUrl + "/onTourTnx/get/" + id;

    let queryParams: any = {};


    this.spinnerService.show();
    this.onTourService.sendGetRequest(apiURL, queryParams).subscribe(
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
    if (this.myForm.invalid) {
      this.toastr.info("Please insert valid data")
      return;
    }
    if(this.checkSomeCondition())
    {

      return;
    }


    const onTour: OnTour = Object.assign(this.myForm.value, {
      hrCrEmp: this.getHrCrEmp.value ? { id: this.getHrCrEmp.value } : null,
      hrCrEmpResponsible:this.getHrCrResponsibleEmp.value ? { id: this.getHrCrResponsibleEmp.value } : null,
    });



    let apiURL = this.baseUrl + "/onTourTnx/save" ;
    //alert(this.myForm.value.id)
    console.log(apiURL);

    let formData: any = {};
    formData = onTour

    // process date
    formData.startDate = (formData.startDate) ? this.datePipe.transform(formData.startDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
    formData.endDate = (formData.endDate) ? this.datePipe.transform(formData.endDate,"yyyy-MM-dd").toString().slice(0, 10) : null;


    this.spinnerService.show();
    this.onTourService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.spinnerService.hide();
        this.router.navigate(["/sefl-service/onTourHrAdmin"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.toastr.warning(error.error.message);
        this.spinnerService.hide();
      }
    );
  }
  checkSomeCondition()
  {


    //check end date getter thn start date
    if(this.myForm.value.startDate>this.myForm.value.endDate)
    {
      this.toastr.error("End Date must be equal or greater")
      return true;
    }
    else{
      return false;
    }
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
