import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {  FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { EMPTY } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Vacancy } from '../../models/vacancy';
import { IrecruitmentService } from '../../service/irecruitment.service';
import { AJListComponent } from '../list/list.component';
import * as $ from 'jquery';
@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class AJCreateComponent implements OnInit {

  public preVivaMarks : number;
  public vivaMarks : number;
  public finalVivaMarks: number;

  public mcqMarks : number;
  public writtenMarks : number;
  public apTestMarks: number;

  public marks: number;


  public vacancies: Vacancy[];

  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;

  public listvacData: any = [];
  configDDL: any;
  configPgn: any;

  //vac: any;
  public vac: any=[];
  addBankAndPayrollForm: any;
  cal: number;
 public vacc= [];

  aplc: any;
  result: number;
  sh:number;
  id: number;
  apwc: any;
  //public vacancy: object;
  //public applicant: object;

  public pngDisConfig: any;
  districts: any;
  board: any;



  // addition(event: any) {

  //   let preVivaMarks =  this.myForm.get('preVivaMarks').value;
  //   let mcqMarks=this.myForm.get('mcqMarks').value;

  //   let writtenMarks=this.myForm.get('writtenMarks').value;
  //   let vivaMarks = this.myForm.get('vivaMarks').value;

  //   let apTestMarks=this.myForm.get('apTestMarks').value;
  //   let finalVivaMarks = this.myForm.get('finalVivaMarks').value;


  //   this.result = preVivaMarks + mcqMarks + writtenMarks + vivaMarks + finalVivaMarks + apTestMarks;



  // }




  constructor( private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private irecservice: IrecruitmentService,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService)
    {
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
    this.loadData();
    this.getVac();
    this.getAplc();
    this.getBoard();

    //this.findapp(str);



  }


  initializeForm(){

    this.myForm = this.formBuilder.group({

      marks:[""],
      statusdrop:["",[Validators.required]],

      vivaMarks:[""],
      finalVivaMarks: [""],
      preVivaMarks: [""],
      apTestMarks:[""],
      writtenMarks:[""],
      mcqMarks:[""],

      outOfPreVivaMarks:[""],
      outOfMcqMarks:[""],
      outOfWrittenMarks:[""],
      outOfVivaMarks:[""],
      outOfApTestMarks:[""],
      outOfFinalVivaMarks:[""],

      remarks: ["",[Validators.required]],
      source: [""],
      comments: [""],
      shortlist: false,
      HR_IR_APLC_ID:[""],
      HR_IR_VCNCY_ID:[""],
      num1:[""],
      num2:[""],
      total:[""],




     vacancy:["",[Validators.required]],
     applicant:["",[Validators.required]],
     iboard:["",[Validators.required]]
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








  findapp($event){

    let vacId = $event;
    console.log(vacId);

     let apiURL = this.baseUrl + "/api/applicant/getList" ;

    let queryParams: any= {};
    queryParams.vacId = vacId;

     this.irecservice.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.aplc = response.objectList;

      },
      (error) => {
        console.log(error)
      }
    );


    //  const formData : any = Object.assign(this.myForm.value, {
    //   applicant: this.getVacancy.value ? { applicant: this.getVacancy.value} : null,
    // });



  }


  myFormSubmit() {

    let apiURL = this.baseUrl + "/api/appliedjob/create";
   // let formData: any = {};
    //let kop = formData.marks = this.result;
    // formData.rActiveOperation = "Create";

    const formData : any = Object.assign(this.myForm.value, {
      //vacancy:  { id: this.getVacancy.value } ,
      vacancy: this.getVacancy.value ? { id: this.getVacancy.value} : null,
      applicant: this.getApplc.value ? { id: this.getApplc.value} : null,
      iboard: this.getBrd.value ? { id: this.getBrd.value} : null,

      //marks:this.preVivaMarks + this.mcqMarks + this.writtenMarks + this.vivaMarks + this.finalVivaMarks + this.apTestMarks,


      //applicant: { id:this.getApplc.value },
      //formData.marks= this.result;
     // marks:this.result,
    });

    //console.log(this.marks);

    formData.rActiveOperation = "Create";

    this.spinnerService.show();
    console.log(formData);

    // this.irecservice.(appInst).subscribe(

    this.irecservice.sendPostRequest(apiURL,formData).subscribe(
      (response: any) => {

        console.log(response);
        this.spinnerService.hide();
        this.router.navigate(["/irecruitment/appliedjob/list"], {relativeTo: this.route});
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

    // this.irecservice.sendGetRequest(apiURL, queryParams).subscribe(
    //   (response: any) => {
    //   console.log(response);
    //   },
    //   (error) => console.log(error)
    // );

}


getBoard() {


  let apiURL = this.baseUrl + "/api/interviewBoard/getList";

  let queryParams: any = {};

  this.irecservice.sendGetRequest(apiURL, queryParams).subscribe(
    (response: any) => {
      this.board = response.objectList;


    },
    (error) => {
      console.log(error)
    }
  );
}

getVac(){


  this.irecservice.getAllVacancy().subscribe(
    (data: any) => {
      this.vacc = data;
    },
    (error) => {
     alert('error');
    }
  );


}

getAplc() {

  this.irecservice.getAllApplicant().subscribe(
    (response: any) => {
      this.aplc = response;


    },
    (error) => {
      console.log(error)
    }
  );
}

get getVacancy() {
 return this.myForm.get("vacancy");
}

get getApplc() {
  return this.myForm.get("applicant");
}

get getBrd() {
  return this.myForm.get("iboard");
}

get remarks() {
  return this.myForm.get("remarks");
}

get statusdrop() {
  return this.myForm.get("iboard");
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
  this.configDDL.dataGetApiPath = "/api/applicant/getList";
  this.configDDL.apiQueryFieldName = "applicant";
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

  this.irecservice.sendGetRequest(apiURL, queryParams).subscribe(
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
// function str(str: any) {
//   throw new Error('Function not implemented.');
// }

