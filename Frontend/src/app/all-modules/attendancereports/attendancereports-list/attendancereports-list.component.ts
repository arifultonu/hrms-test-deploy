import { DatePipe } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { environment } from "src/environments/environment";
import { ReportBetweenDateDTO } from "../model/ReportBetweenDateDTO";
import { AttendanceReportService } from "../services/attendance-report.service";

declare const $: any;
@Component({
  selector: "app-attendancereports-list",
  templateUrl: "./attendancereports-list.component.html",
  styleUrls: ["./attendancereports-list.component.css"],
})
export class AttendancereportsListComponent implements OnInit {
  public reportBetweenDateForm: FormGroup;

  contextPth = environment.contextPath;

  constructor(
    private formBuilder: FormBuilder,
    private attnReport: AttendanceReportService,
    private datePipe: DatePipe,
    private router: Router,
    private spinnerService: NgxSpinnerService,
  ) {}

  AttnReport = [];
  startdate;
  todate;

  ngOnInit() {
    if ($(".floating").length > 0) {
      $(".floating")
        .on("focus blur", function (e) {
          $(this)
            .parents(".form-focus")
            .toggleClass(
              "focused",
              e.type === "focus" || this.value.length > 0
            );
        })
        .trigger("blur");
    }

    this.initializeForm();
    this.loadAttnDada(); //lode defort data for this month
    this.selInitialDefaultValues();
  }

  initializeForm() {
    this.reportBetweenDateForm = this.formBuilder.group({
      fromDate: ["", [Validators.required]],
      toDate: ["", [Validators.required]],
    });
  }

  selInitialDefaultValues() {
    let today = new Date();
    let fromDate_ = new Date(today.getFullYear(), today.getMonth(), 1);
    let fromDate = fromDate_.toLocaleDateString("en-US");
    let toDate = today.toLocaleDateString("en-US");
    this.reportBetweenDateForm = this.formBuilder.group({
      fromDate: [fromDate],
      toDate: [toDate],
    });

    // const element = document.querySelector('.fromDate') as any as HTMLElement
    // element.focus();
    // element.blur();
    setTimeout(() => {
      const element = document.querySelector(".fromDate") as any as HTMLElement;
      element.focus();
      element.blur();
    }, 500);
    setTimeout(() => {
      const element = document.querySelector(".toDate") as any as HTMLElement;
      element.focus();
      element.blur();
    }, 700);
  }

  formSubmit() {
    let ReportBetweenDate: ReportBetweenDateDTO = Object.assign(
      this.reportBetweenDateForm.value
    );

    let fDate = ReportBetweenDate.fromDate;
    let convertfDate = this.datePipe
      .transform(fDate, "yyyy-MM-dd")
      .toString()
      .slice(0, 10);
    ReportBetweenDate.fromDate = convertfDate;

    let tDate = ReportBetweenDate.toDate;
    let converttDate = this.datePipe
      .transform(tDate, "yyyy-MM-dd")
      .toString()
      .slice(0, 10);
    ReportBetweenDate.toDate = converttDate;

    this.spinnerService.show();
    this.attnReport
      .getReportBetweenDate(ReportBetweenDate)
      .subscribe((data: any) => {
        this.AttnReport = data;
        this.spinnerService.hide();
      });
  }

  loadAttnDada() {
    let ReportBetweenDate: ReportBetweenDateDTO = Object.assign(
      this.reportBetweenDateForm.value
    );
    ReportBetweenDate.fromDate = null;
    ReportBetweenDate.toDate = null;

    this.todate = new Date();
    this.startdate = new Date(
      this.todate.getFullYear(),
      this.todate.getMonth(),
      1
    );

    this.todate = this.todate.toLocaleDateString("en-US");
    this.startdate = this.startdate.toLocaleDateString("en-US");

    // var today  = new Date();
    // console.log(today.toLocaleDateString("en-US")); // 9/17/2016

    this.spinnerService.show();
    this.attnReport
      .getReportBetweenDate(ReportBetweenDate)
      .subscribe((data: any) => {
        this.AttnReport = data;
        this.spinnerService.hide();
      });
  }

  

  renderJasperReport() {
    // data processing
    let formData = this.reportBetweenDateForm.value;
    let fromDate_ = formData.fromDate;
    let toDate_ = formData.toDate;
    let fromDate = this.datePipe
      .transform(fromDate_, "yyyy-MM-dd")
      .toString()
      .slice(0, 10);
    let toDate = this.datePipe
      .transform(toDate_, "yyyy-MM-dd")
      .toString()
      .slice(0, 10);

    let queryParams = { fromDate: fromDate, toDate: toDate };

    // self
    // this.router.navigateByUrl('/reports/attendance-jsr-rpt');

    // new tab
    const url = this.router.serializeUrl(
      this.router.createUrlTree(
        [this.contextPth + "reports/attendance-jsr-rpt"],
        { queryParams: queryParams }
      )
      // this.router.createUrlTree([`/reports/attendance-jsr-rpt`],{queryParams:queryParams})
    );

    window.open(url, "_blank");
  }
}
