import { TransportRequsitionService } from './../../../service/transport-requsition.service';
import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { HrCrEmp } from 'src/app/all-modules/employees/model/HrCrEmp';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-create-transport',
  templateUrl: './create-transport.component.html',
  styleUrls: ['./create-transport.component.css']
})
export class CreateTransportComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  configDDL: any;
  user!: HrCrEmp;
  public myForm: FormGroup;


  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
    private commonService:CommonService,
    private transportRequsitionService: TransportRequsitionService
  ) {
    this._initConfigDDL();
      this._customInitLoadData();
  }

  ngOnInit(): void {
    this.initializeForm();
  }
  initializeForm(){

    this.myForm = this.formBuilder.group({
      hrCrEmp:[null,[Validators.required]],
      vehicleType:["",[Validators.required]],
      visitFrom:["",[Validators.required]],
      numberOfPassenger:["",[Validators.required]],
      visitReason:["",[Validators.required]],
      visitTo:["",[Validators.required]],
      numberOfVehicle:["",[Validators.required]],
      visitStartDate:["",[Validators.required]],
      descriptionOfVisit:["",[Validators.required]],
      visitTime:["",[Validators.required]],
      visitEndDate:["",[Validators.required]],
      remarks: [""],
      sanctionVehicleType:[""],
      vehicleUseDate:[""],
      vehicleRegNumber:[""],
      meterRidingBeforeJourney:[""],
      hrCrEmpD1:[null],
      meterRidingAfterJourney:[""],
      hrCrEmpD2:[null],
      meterRidingDuration:[""],
      remarks2:[""],



    });

  }

  myFormSubmit(){
    if (this.myForm.invalid) {
      this.toastr.info("Please insert valid data")
      return;
    }

    let transportRequisition: any = Object.assign(this.myForm.value, {
      hrCrEmp: this.getHrCrEmp.value ? { id: this.getHrCrEmp.value } : null,

      hrCrEmpD1:this.getHrCrD1.value ? { id: this.getHrCrD1.value } : null,
     
       hrCrEmpD2:this.getHrCrD2.value ? { id: this.getHrCrD2.value } : null,
    });

    let apiURL = this.baseUrl + "/transportRequisition/save";

    let formData: any = {};
    formData = transportRequisition

    console.log("formData");
    console.log(formData); 

     // process date
    //  formData.visitStartDate = (formData.visitStartDate) ? this.datePipe.transform(formData.visitStartDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
    //  formData.visitEndDate = (formData.visitEndDate) ? this.datePipe.transform(formData.visitEndDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
    //  formData.vehicleUseDate = (formData.vehicleUseDate) ? this.datePipe.transform(formData.vehicleUseDate,"yyyy-MM-dd").toString().slice(0, 10) : null;


     this.transportRequsitionService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);

        this.router.navigate(["/sefl-service/transportRequisition"], {relativeTo: this.route});
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

  get getHrCrEmp() {
    return this.myForm.get("hrCrEmp");
  }
  get getHrCrD1() {
    return this.myForm.get("hrCrEmpD1");
  }
  get getHrCrD2() {
    return this.myForm.get("hrCrEmpD2");
  }

}
