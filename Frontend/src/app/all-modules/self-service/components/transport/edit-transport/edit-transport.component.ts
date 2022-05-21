import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { environment } from 'src/environments/environment';
import { OnTourService } from '../../../service/on-tour.service';
import { TransportRequsitionService } from '../../../service/transport-requsition.service';

@Component({
  selector: 'app-edit-transport',
  templateUrl: './edit-transport.component.html',
  styleUrls: ['./edit-transport.component.css']
})
export class EditTransportComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  public myFormData: any = {};


  configDDL: any;

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,

    private route: ActivatedRoute,
    private router: Router,
    private transportRequsitionService: TransportRequsitionService,

    private toastr: ToastrService,

    private commonService:CommonService,
    private spinnerService: NgxSpinnerService
  ) {
    this._initConfigDDL();
      this._customInitLoadData();
   }

  ngOnInit(): void {
    this.initializeForm()
    this.getFormData();
  }
  initializeForm(){

    this.myForm = this.formBuilder.group({
       id:[""],
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

  getFormData(){
   

    let id =  this.route.snapshot.params.id;
    let apiURL = this.baseUrl + "/transportRequisition/getById/" + id;

    let queryParams: any = {};


    this.spinnerService.show();
    this.transportRequsitionService.sendGetRequest(apiURL, queryParams).subscribe(
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
        let hrCrEmpD1Val = [
          {
            ddlCode: response.hrCrEmpD1?.id,
            ddlDescription: response.hrCrEmpD1?.loginCode + "-" + response.hrCrEmpD1?.displayName,
          },
        ];
        this.configDDL.listData2 = hrCrEmpD1Val;
        this.myFormData.hrCrEmpD1 =  response.hrCrEmpD1?.id;

          // field 3
          let hrCrEmpD2Val = [
            {
              ddlCode: response.hrCrEmpD2?.id,
              ddlDescription: response.hrCrEmpD2?.loginCode + "-" + response.hrCrEmpD2?.displayName,
            },
          ];
          this.configDDL.listData3 = hrCrEmpD2Val;
          this.myFormData.hrCrEmpD2 =  response.hrCrEmpD2?.id;

        //set date
        // this.myFormData.startDate = this.datePipe.transform(response.startDate,"MM-dd-yyyy").toString().slice(0, 10) ;
        // this.myFormData.endDate = this.datePipe.transform(response.endDate,"MM-dd-yyyy").toString().slice(0, 10) ;


       //set date
        this.myFormData.visitTime =this.datePipe.transform(response.visitDateTime,"HH:mm").toString();

        this.myForm.patchValue(this.myFormData);


      },
      (error) => {
        console.log(error)
      }
    );


  }

  myFormSubmit()
  {
    if (this.myForm.invalid) {
      this.toastr.info("Please insert valid data")
      return;
    }



    let transportRequisition: any = Object.assign(this.myForm.value, {
      hrCrEmp: this.getHrCrEmp.value ? { id: this.getHrCrEmp.value } : null,

      hrCrEmpD1:this.getHrCrD1.value ? { id: this.getHrCrD1.value } : null,

       hrCrEmpD2:this.getHrCrD2.value ? { id: this.getHrCrD2.value } : null,
    });



    let apiURL = this.baseUrl + "/transportRequisition/update" ;
    //alert(this.myForm.value.id)
    console.log(apiURL);

    let formData: any = {};
    formData = transportRequisition;




    this.spinnerService.show();
    this.transportRequsitionService.sendPutRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.spinnerService.hide();
        this.toastr.success("Successfully item is updated", "Success");
        this.router.navigate(["/sefl-service/transportRequisition"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.toastr.warning(error.error.message);
        this.spinnerService.hide();
      }
    );
  }

  resetFormValues()
  {
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
          this.configDDL.listData3 = this.configDDL.listData3.concat(
            response.objectList
          );
        } else {
          this.configDDL.listData = response.objectList;
          this.configDDL.listData2 = response.objectList;
          this.configDDL.listData3 = response.objectList;
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
      listData3: [],
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
