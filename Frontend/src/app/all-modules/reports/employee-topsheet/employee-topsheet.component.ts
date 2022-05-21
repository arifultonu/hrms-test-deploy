import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { ReportsService } from '../service/reports.service';

@Component({
  selector: 'app-employee-topsheet',
  templateUrl: './employee-topsheet.component.html',
  styleUrls: ['./employee-topsheet.component.css']
})
export class EmployeeTopsheetComponent implements OnInit {
  
  public basicForm: FormGroup;
  dataLocalUrl: any;


  constructor( 
    
    private formBuilder: FormBuilder,
   
    private domSanitizer: DomSanitizer,
    private activatedRoute: ActivatedRoute,
    private reportsService: ReportsService,

    ) { }

  
  ngOnInit() {

    this.basicForm = this.formBuilder.group({
      txt: [""],
    });

    this.loadReport();
  }


  loadReport(){


    let reportParams = {};
    reportParams['rptFileName'] = "Final_EMP_TOPSHEET";
    reportParams['outputFileName'] = "Final_EMP_TOPSHEETReport";


    this.reportsService.emptopsheet(reportParams).subscribe((response:any)=>{

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

}
