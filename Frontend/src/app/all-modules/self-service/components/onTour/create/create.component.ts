import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { HrCrEmp } from 'src/app/all-modules/employees/model/HrCrEmp';
import { EmployeeService } from 'src/app/all-modules/employees/services/employee.service';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { LoginService } from 'src/app/login/services/login.service';
import { environment } from 'src/environments/environment';
import { OnTour } from '../../../models/onTour';
import { OnTourService } from '../../../service/on-tour.service';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class CreateComponent implements OnInit {


  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  
  

 
  configDDL: any;

  user!: HrCrEmp;
  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private login: LoginService,
  
    private route: ActivatedRoute,
    private router: Router,
    private onTourService:OnTourService,
 
    private toastr: ToastrService,
   
    private commonService:CommonService
    
    
    ) {
      this._initConfigDDL();
      this._customInitLoadData();
      
    }

  ngOnInit(): void {
    this.initializeForm();
    this.liginUser();
    this.setValue();
   
  }

  initializeForm(){

    this.myForm = this.formBuilder.group({
      hrCrEmp:{},
      contactNo: ["", [Validators.required]],
      hrCrEmpResponsible:[{}, [Validators.required]],
      tourType: ["", [Validators.required]],
      startDate: ["", [Validators.required]],
      endDate: ["", [Validators.required]],
    
      addressDuringTour: ["", [Validators.required]],
      reasonForTour: ["", [Validators.required]],
      remarks: [""],
     
      

    });

  }
  liginUser()
  {
    this.user = this.login.getUser();
    console.log(this.user);
  }
  setValue()
  {
    this.myForm.get('hrCrEmp').setValue( this.user.firstName+" "+this.user.lastName);
    this.myForm.get('contactNo').setValue( this.user.mobCode);

      
  }

  myFormSubmit()
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
     // hrCrEmp: this.getHrCrEmp.value ? { id: this.getHrCrEmp.value } : null,
      hrCrEmp:  { id: this.user.id } ,
      hrCrEmpResponsible:this.getHrCrResponsibleEmp.value ? { id: this.getHrCrResponsibleEmp.value } : null,
    });
    
    let apiURL = this.baseUrl + "/onTourTnx/save";

    let formData: any = {};
    formData = onTour

    console.log(formData);
    
    
    
    
    // process date
    formData.startDate = (formData.startDate) ? this.datePipe.transform(formData.startDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
    formData.endDate = (formData.endDate) ? this.datePipe.transform(formData.endDate,"yyyy-MM-dd").toString().slice(0, 10) : null;


    this.onTourService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
     
        this.router.navigate(["/sefl-service/onTour"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
    
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
  
 

  contactFind(code)
  {
    alert(code)
  }
  resetFormValues()
  {
    
    this.myForm.reset();
    //this.setFormDefaultValues();
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
