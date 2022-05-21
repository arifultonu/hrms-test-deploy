import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { HrCrEmp } from 'src/app/all-modules/employees/model/HrCrEmp';
import { EmployeeService } from 'src/app/all-modules/employees/services/employee.service';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { environment } from 'src/environments/environment';
import { IrecruitmentService } from '../../service/irecruitment.service';

@Component({
  selector: 'app-ivbmbcreate',
  templateUrl: './ivbmbcreate.component.html',
  styleUrls: ['./ivbmbcreate.component.css']
})
export class IvbmbcreateComponent implements OnInit {

  //public dropdownList: HrCrEmp[] = [];
  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  vacc: any;
  aplc: any;
  public listvacData: any = [];
  configDDL: any;
  configPgn: any;


//pagination config for all emp
pageNum = 1;
pageSize = 10;
pageSizes = [3, 5, 10, 25, 50, 100, 200, 500, 1000];
totalItem = 50;
pngDiplayLastSeq = 10;
pngConfig: any;

//pagination config for all leave Assign
pageNum2 = 1;
pageSize2 = 3;
pageSizes2 = [3, 5, 10, 25, 50, 100, 200, 500, 1000];
totalItem2 = 50;
pngDiplayLastSeq2 = 10;
pngConfig2: any;

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private irecservice: IrecruitmentService,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private empServicec: EmployeeService,
    private commonService : CommonService,
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

   }

  ngOnInit(): void {

    this.initializeForm();
    this.setFormDefaultValues();
    this.initButtonsRippleEffect();

    this.getBoard();
   this.getMember();



  }
  // add(){
  //   let row = document.createElement('div');
  //     row.className = 'row';
  //     row.innerHTML = `
  //     <br>
  //     <input type="text" class="form-control" formControlName="empIds">`;
  //     document.querySelector('.showInputField').appendChild(row);
  // }

  initializeForm(){

    this.myForm = this.formBuilder.group({

      interviewBoard:[""],
      empIds:[""],
      active:[""],
      externalMember:[""],
      externalMemberAddr:[""],
      externalMemberPhn:[""],
      externalMemberEmail:[""]
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

    let apiURL = this.baseUrl + "/api/interviewBoardMember/create";

    const formData : any = Object.assign(this.myForm.value, {
      interviewBoard: this.getBrd.value ? { id: this.getBrd.value} : null,
      empIds: this.getEmp.value ? { id: this.getEmp.value} : null,
    });

    this.spinnerService.show();
    console.log(formData.empIds);

    this.irecservice.sendPostRequest(apiURL,formData).subscribe(
      (response: any) => {

        console.log(response);
        this.spinnerService.hide();
        this.router.navigate(["/irecruitment/vivaboardmember/list"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.spinnerService.hide();
      }
    );
  }

 get getBrd(){
    return this.myForm.get("interviewBoard");
  }

 get getEmp(){
    return this.myForm.get("empIds");
  }

  getBoard() {


    let apiURL = this.baseUrl + "/api/interviewBoard/getAllBrd";

    let queryParams: any = {};

    this.irecservice.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.vacc = response;
      },
      (error) => {
        console.log(error)
      }
    );
  }


  getUserQueryParams(page: number, pageSize: number): any {

    let params: any = {};

    if (page) {
      params[`pageNum`] = page - 0;
    }
    if (pageSize) {
      params[`pageSize`] = pageSize;
    }

    return params;

  }

  showDivv = {
    previous : false,
    current : false,
  }

  getMember() {

    // this.pageNum++;
    // let apiURL = this.baseUrl + "/api/common/getEmp";

    // let queryParams: any = {};

    // // this.commonService.sendGetRequest(apiURL, queryParams).subscribe(
    // //   (response: any) => {
    // //     this.aplc = response.objectList;
    // //   },
    // //   (error) => {
    // //     console.log(error)
    // //   }
    // // );

    // const params = this.getUserQueryParams(this.pageNum, this.pageSize);
    // queryParams = params;

    // this.commonService.sendGetRequest(apiURL, queryParams).subscribe(
    //   (response: any) => {
    //     this.aplc = response.objectList;
    //     //console.log("length=" + this.dropdownList.length);
    //   },
    //   (error) => {
    //     console.log(error)
    //   }
    // );
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







}
