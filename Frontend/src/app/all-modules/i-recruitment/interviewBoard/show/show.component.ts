import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { environment } from 'src/environments/environment';
import { IrecruitmentService } from '../../service/irecruitment.service';

@Component({
  selector: 'app-show',
  templateUrl: './show.component.html',
  styleUrls: ['./show.component.css']
})
export class BrdShowComponent implements OnInit {

   
  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  public listData: any = [];
  public myFormData: any = {};
  data: any;
  
  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private payrollService: IrecruitmentService,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService
  ) { }

  ngOnInit(): void {
    this.initializeForm();
    this.setFormDefaultValues();
    this.initButtonsRippleEffect();
    this.getMemberData();
    this.getBoardData();
  }


  initializeForm(){
    this.myForm = this.formBuilder.group({
      title: [""],
      code:[""],
      id:[""],
      boardMemberName:[""],
      boardMemberId:[""]  
    });
  }



  setFormDefaultValues(){
    //
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
 getMemberData(){

  let id =  this.route.snapshot.params.id;
    
    let apiURL = this.baseUrl + "/api/interviewBoard/getBoardMembers/" + id;
  
    let queryParams: any = {};
    queryParams.rEntityName = "interviewBoardMember";
    queryParams.rActiveOpetation = "read";
  
    this.spinnerService.show();
    this.payrollService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.myFormData = response;
        
        
  
       // process date fields
        this.myFormData.activeStartDate = (this.myFormData.activeStartDate) ? this.datePipe.transform(this.myFormData.activeStartDate,"dd-MM-yyyy") : null;
        this.myFormData.activeEndDate = (this.myFormData.activeEndDate) ? this.datePipe.transform(this.myFormData.activeEndDate,"dd-MM-yyyy") : null;
        this.spinnerService.hide();
        this.myForm.patchValue(this.myFormData);  
  
       
        
        this.spinnerService.hide();
       
      },
      (error) => {
        console.log(error)
      } 
    );
  
}

getBoardData(){

  let id =  this.route.snapshot.params.id;
    
    let apiURL = this.baseUrl + "/api/interviewBoard/get/" + id;
  
    let queryParams: any = {};
    queryParams.rEntityName = "interviewBoard";
    queryParams.rActiveOpetation = "read";
  
    this.spinnerService.show();
    this.payrollService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.data = response;
        
        
  
       // process date fields
        this.data.activeStartDate = (this.myFormData.activeStartDate) ? this.datePipe.transform(this.myFormData.activeStartDate,"dd-MM-yyyy") : null;
        this.data.activeEndDate = (this.myFormData.activeEndDate) ? this.datePipe.transform(this.myFormData.activeEndDate,"dd-MM-yyyy") : null;
        this.spinnerService.hide();
        this.myForm.patchValue(this.data);  
  
       
        
        this.spinnerService.hide();
       
      },
      (error) => {
        console.log(error)
      } 
    );
  
}

}
