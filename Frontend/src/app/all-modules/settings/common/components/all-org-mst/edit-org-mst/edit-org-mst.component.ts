import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { EmployeeService } from 'src/app/all-modules/employees/services/employee.service';
import { IrecruitmentService } from 'src/app/all-modules/i-recruitment/service/irecruitment.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-edit-org-mst',
  templateUrl: './edit-org-mst.component.html',
  styleUrls: ['./edit-org-mst.component.css']
})
export class EditOrgMstComponent implements OnInit {

  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;

  public myFormData: any = {};

  departments :any;
  org:any;
  dept:any;
  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private payrollService: IrecruitmentService,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private employeeService: EmployeeService,
    private toastr: ToastrService,
  ) { }

  ngOnInit(): void {
    this.initializeForm();
    this.getFormData();
  }

  initializeForm(){

    this.myForm = this.formBuilder.group({
      id:[""],

      title:[""],
      orgType:[""],
      sequence:[""],
      approvalStatus:[""],


    });

  }
  setFormDefaultValues(){
    //
  }

  resetFormValues(){

    this.myForm.reset();
    this.setFormDefaultValues();

  }


  getFormData(){

    let id =  this.route.snapshot.params.id;
    let apiURL = this.baseUrl + "/allOrgMst/get/" + id;
console.log("id"+id);


    let queryParams: any = {};
    queryParams.rEntityName = "allOrgMst";
    queryParams.rActiveOpetation = "read";

    this.spinnerService.show();
    this.employeeService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {

        this.spinnerService.hide();
        this.myForm.patchValue(response);
        this.myFormData = response;
console.log(this.myFormData);

      },
      (error) => {
        console.log(error)
      }
    );


  }
  // getAllOrgMstID(){
  //   this.commonService.getAllOrgMst().subscribe((data:any)=>{
  //     this.allOrgMst=data;
  //   });
  // }


  saveUpdatedFormData() {

    let apiURL = this.baseUrl + "/allOrgMst/edit/" + this.myForm.value.id;
    console.log(apiURL);

    let formData: any = {};
    formData = this.myForm.value;

    this.spinnerService.show();
    this.payrollService.sendPutRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.toastr.success('Successfully Updated');
        this.spinnerService.hide();
        this.router.navigate(["/settings/all-org-mst"], {relativeTo: this.route});
      },
      (error) => {
        this.router.navigate(["/allOrgMst/edit/"+this.myForm.value.id], {relativeTo: this.route});
        this.toastr.error("Error!!");
        console.log(error);
        this.spinnerService.hide();
      }
    );

  }

}
