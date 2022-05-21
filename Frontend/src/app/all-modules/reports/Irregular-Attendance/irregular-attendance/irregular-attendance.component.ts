import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { environment } from 'src/environments/environment';
import { ReportsService } from '../../service/reports.service';

@Component({
  selector: 'app-irregular-attendance',
  templateUrl: './irregular-attendance.component.html',
  styleUrls: ['./irregular-attendance.component.css']
})
export class IrregularAttendanceComponent implements OnInit {

  public myForm: FormGroup;
  dataLocalUrl: any;

public baseUrl = environment.baseUrl;

public listvacData: any = [];
configDDL: any;
configPgn: any;


  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private reportsService: ReportsService,
    private domSanitizer: DomSanitizer,
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

  }


  initializeForm(){
      this.myForm = this.formBuilder.group({
        fromDate: [""],
        toDate: [""],
        empCode:[""]
      });
  }

  loadReport(){

    let reportParams = {};
    reportParams['rptFileName'] = "Irregular_Attandence_Final_Version2.4";
    reportParams['outputFileName'] = "Irregular_Attandence_Final_Version2.4Report";

    reportParams['Emp_Code']=this.myForm.get('empCode').value;
console.log("empode"+this.myForm.get('empCode').value);


    reportParams['From_Date'] = this.datePipe.transform((this.myForm.get('fromDate').value), 'yyyy-MM-dd');
    reportParams['To_Date'] = this.datePipe.transform((this.myForm.get('toDate').value), 'yyyy-MM-dd');

    this.reportsService.irrattnRpt(reportParams).subscribe((response:any)=>{

      const file = new Blob([response], { type: 'application/pdf' });
      const fileURL = URL.createObjectURL(file);
      this.dataLocalUrl = this.domSanitizer.bypassSecurityTrustResourceUrl(fileURL);

      $('body').css('height', '100%');
      $('.main-wrapper').css('height', '100%');
      $('.main-wrapper .page-wrapper').css('height', '100%');
      $([document.documentElement, document.body]).animate({
        scrollTop: $("#idReportDisplay").offset().top
      }, 2000);

    });

  }

  showReport(){
    this.loadReport();
  }


  resetFormValues(){
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
