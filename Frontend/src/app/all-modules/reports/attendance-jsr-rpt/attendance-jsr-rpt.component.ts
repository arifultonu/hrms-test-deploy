import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ReportsService } from "../service/reports.service";
import {DomSanitizer,SafeResourceUrl} from '@angular/platform-browser'
import { ActivatedRoute, Router, NavigationEnd  } from '@angular/router';

declare const $: any;
@Component({
  selector: "app-basic-inputs",
  templateUrl: "./attendance-jsr-rpt.component.html",
  styleUrls: ["./attendance-jsr-rpt.component.css"],
})
export class AttendanceJsrRptCompnent implements OnInit {

  public basicForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private _reportService : ReportsService,
    private domSanitizer: DomSanitizer,
    private activatedRoute: ActivatedRoute
    ) {}


    dataLocalUrl: any;
    urlSafe: SafeResourceUrl;

    empAttnData={
      rptFileName: 'EmpDailyAttendanceRpt',
      newWindow: true,
      userId:'xxxx',
      startDate:'',
      endDate:'',
    }


  ngOnInit() {

    this.basicForm = this.formBuilder.group({
      txt: [""],
    });

    this.empAttnData.startDate = this.activatedRoute.snapshot.queryParamMap.get('fromDate');
    this.empAttnData.endDate = this.activatedRoute.snapshot.queryParamMap.get('toDate');

    this.loadReport();
  }

  basicFormSubmit() {
    console.log("");
  }

  loadReport(){


    this._reportService.employeeAttendanceRpt(this.empAttnData.rptFileName, this.empAttnData.userId,this.empAttnData.startDate,this.empAttnData.endDate).subscribe((response:any)=>{
      // this.dataLocalUrl = this.domSanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(file3));
      const file = new Blob([response], { type: 'application/pdf' });
      const fileURL = URL.createObjectURL(file);
      if(this.empAttnData.newWindow && false){
        window.open(fileURL);
      } else {
        this.dataLocalUrl = this.domSanitizer.bypassSecurityTrustResourceUrl(fileURL);
        // this.urlSafe= this.domSanitizer.bypassSecurityTrustResourceUrl('https://angular.io/api/router/RouterLink');

        $('body').css('height', '100%');
        $('.main-wrapper').css('height', '100%');
        $('.main-wrapper .page-wrapper').css('height', '100%');

      }
    })

  }


}
