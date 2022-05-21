import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { environment } from 'src/environments/environment';
import { ShortLeaveService } from '../../../service/short-leave.service';

@Component({
  selector: 'app-edit-short-leave',
  templateUrl: './edit-short-leave.component.html',
  styleUrls: ['./edit-short-leave.component.css']
})
export class EditShortLeaveComponent implements OnInit {

  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  public myFormData: any = {};


  configDDL: any;


  constructor(
    private commonService:CommonService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private shortLeaveService: ShortLeaveService,
    private datePipe: DatePipe,
    private toastr: ToastrService,
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
      // hrCrEmp:[{}],
      id:[""],
      hrCrEmpResponsible: [{}, [Validators.required]],
      onDate: ["", [Validators.required]],
      startTime: ["", [Validators.required]],
      // leaveDays: [""],
      duration: ["", [Validators.required]],
      addressDuringLeave: ["", [Validators.required]],
      reason: ["", [Validators.required]],
      remarks: [""],
     
      

    });

  }

  getFormData(){

    let id =  this.route.snapshot.params.id;
    let apiURL = this.baseUrl + "/short-leave/get/" + id;

    let queryParams: any = {};
   

    this.spinnerService.show();
    this.shortLeaveService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.myFormData = response;
        console.log("ookk");
       console.log(this.myFormData);
       
        this.spinnerService.hide();
       
       

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
        this.myFormData.onDate = this.datePipe.transform(response.onDate,"MM-dd-yyyy").toString().slice(0, 10) ;
     
        this.myFormData.startTime =this.datePipe.transform(response.onDateStartTime,"HH:mm").toString();
        
        this.myForm.patchValue(this.myFormData); 


      },
      (error) => {
        console.log(error)
      } 
    );


  }
  resetFormValues()
  {
    //this.myForm.reset();
    this.getFormData()
  }

  saveUpdatedFormData()
  {
    if (this.myForm.invalid) {
      this.toastr.info("Please insert valid data")
      return;
    }
    
   

    const shortLeave: any = Object.assign(this.myForm.value, {
    
      hrCrEmpResponsible:this.getHrCrResponsibleEmp.value ? { id: this.getHrCrResponsibleEmp.value } : null,
    });
    
    
  
    let apiURL = this.baseUrl + "/short-leave/edit" ;
    //alert(this.myForm.value.id)
    console.log(apiURL);

    let formData: any = {};
    formData = shortLeave

    // process date
    formData.onDate = (formData.onDate) ? this.datePipe.transform(formData.onDate,"yyyy-MM-dd").toString().slice(0, 10) : null;

    
    this.spinnerService.show();
    this.shortLeaveService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.spinnerService.hide();  
        this.toastr.success("Successfully item is updated", "Success");
        this.router.navigate(["/sefl-service/emp-short-leave"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.toastr.warning(error.error.message);
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
  get getHrCrResponsibleEmp() {
    return this.myForm.get("hrCrEmpResponsible");
  }

}
