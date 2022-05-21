import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';
import { IrecruitmentService } from '../../service/irecruitment.service';


declare const $: any;
@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class VivaEditComponent implements OnInit {


  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;

  public myFormData: any = {};
  uploadForm: FormGroup;
  
  activeTabCnf: any;

  imageSrc: any;
  profileImageUrl!: any;
  
  id: any;

  
  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private irecservice: IrecruitmentService,
    private toastr: ToastrService,
  ) { }

  ngOnInit(): void {
    this.initializeForm();
    this.setFormDefaultValues();
    this.initButtonsRippleEffect();
    this.getFormData();
    this.getMember();
  }

  initializeForm(){
    this.myForm = this.formBuilder.group({
      title: [""],
      code:[""],
      id:[""],
      empCode:[""]  
    });
  }

  setFormDefaultValues(){

    var dt = new Date();
    var year = dt.getFullYear();

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


  
  /**
   * Read form data from API
   */
   getFormData(){

    let id =  this.route.snapshot.params.id;
    let apiURL = this.baseUrl + "/api/interviewBoard/get/" + id;

    let queryParams: any = {};
    queryParams.rEntityName = "interviewBoard";
    queryParams.rActiveOpetation = "read";

    this.spinnerService.show();
    this.irecservice.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.myFormData = response;

        console.log(this.myFormData);
        
        // process date fields
        this.myFormData.activeStartDate = (this.myFormData.activeStartDate) ? this.datePipe.transform(this.myFormData.activeStartDate,"dd-MM-yyyy") : null;
        this.myFormData.activeEndDate = (this.myFormData.activeEndDate) ? this.datePipe.transform(this.myFormData.activeEndDate,"dd-MM-yyyy") : null;
        this.spinnerService.hide();
        this.myForm.patchValue(this.myFormData);  
      },
      (error) => {
        console.log(error)
      } 
    );


  }

  getMember(){

    // let id =  this.route.snapshot.params.id;
    
    // let apiURL = this.baseUrl + "/api/interviewBoard/getBoardMembers/" + id;
  
    // let queryParams: any = {};
    // queryParams.rEntityName = "interviewBoardMember";
    // queryParams.rActiveOpetation = "read";
  
    // this.spinnerService.show();
    // this.irecservice.sendGetRequest(apiURL, queryParams).subscribe(
    //   (response: any) => {
    //     this.myFormData = response;
        
        
  
    //    // process date fields
    //     this.myFormData.activeStartDate = (this.myFormData.activeStartDate) ? this.datePipe.transform(this.myFormData.activeStartDate,"dd-MM-yyyy") : null;
    //     this.myFormData.activeEndDate = (this.myFormData.activeEndDate) ? this.datePipe.transform(this.myFormData.activeEndDate,"dd-MM-yyyy") : null;
    //     this.spinnerService.hide();
    //     this.myForm.patchValue(this.myFormData);  
  
       
        
    //     this.spinnerService.hide();
       
    //   },
    //   (error) => {
    //     console.log(error)
    //   } 
    // );
    
  }

  convertToISODateString(dateStr_DD_MM_YYYY) {
    let dateComponents = dateStr_DD_MM_YYYY.split('-');
    return dateComponents[2] + "-" + dateComponents[1] + dateComponents[0];
}


  saveUpdatedFormData() {
    
    let apiURL = this.baseUrl + "/api/interviewBoard/update/" + this.myForm.value.id;
    console.log(apiURL);

    let formData: any = {};
    formData = this.myForm.value;

    formData.rEntityName = "interviewBoard";
    formData.rActiveOperation = "update";
    // process date fields
    formData.activeStartDate = (formData.activeStartDate) ? this.datePipe.transform( this.convertToISODateString(formData.activeStartDate),"yyyy-MM-dd") : null;
    formData.activeEndDate = (formData.activeEndDate) ? this.datePipe.transform( this.convertToISODateString(formData.activeEndDate), "yyyy-MM-dd") : null;

    this.spinnerService.show();
    this.irecservice.sendPutRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.spinnerService.hide();  
        this.toastr.success("Update successfully", "Success"); 
        this.router.navigate(["/irecruitment/vivaboard/list"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.spinnerService.hide();  
      }
    );


  }

}
