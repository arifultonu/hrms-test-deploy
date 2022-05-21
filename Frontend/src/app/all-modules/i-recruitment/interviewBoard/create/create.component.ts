import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';;
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { id } from 'src/assets/all-modules-data/id';
import { environment } from 'src/environments/environment';
import { IrecruitmentService } from '../../service/irecruitment.service';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css'],
  providers: [DatePipe]
})
export class VivaCreateComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  public vac: any=[];
  public alet;

  myDate=new Date();
result ="VB"+this.datePipe.transform(this.myDate, 'yyyyMMddHHmmss');
  
  

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private irecservice: IrecruitmentService,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private toastr: ToastrService,
  ) { }

  ngOnInit(): void {

    this.initializeForm();
    this.setFormDefaultValues();
    this.initButtonsRippleEffect();
    //this.LoadVacancy();
  }



  initializeForm(){

    this.myForm = this.formBuilder.group({
      title: ["",[Validators.required]],
      firstName:["",[Validators.required]]
      
      
      
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

  get gettitle() {
    return this.myForm.get("title");
   }

  myFormSubmita(){
    if(!this.gettitle.value ){
      this.toastr.error("Must filled all the field ", "Error");
    }
    else{
      this.myFormSubmit();
    }
  }

  


  myFormSubmit() {
    
    let apiURL = this.baseUrl + "/api/interviewBoard/create";

    let formData: any = {};
    formData = this.myForm.value;
    
    formData.code=this.result;

    formData.rActiveOperation = "Create";
    // process date
    formData.activeStartDate = (formData.activeStartDate) ? this.datePipe.transform(formData.activeStartDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
    formData.activeEndDate = (formData.activeEndDate) ? this.datePipe.transform(formData.activeEndDate,"yyyy-MM-dd").toString().slice(0, 10) : null;

    this.spinnerService.show();
    this.irecservice.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.spinnerService.hide(); 
        this.toastr.success("Board has been created successfully", "Success"); 
        this.router.navigate(["/irecruitment/vivaboard/list"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.spinnerService.hide();  
      }
    );


  }
  

  
//   LoadVacancy(){
//       let apiURL = this.baseUrl + "/api/vacancy/getList";

//       let queryParams: any = {};

//     this.irecservice.sendGetRequest(apiURL, queryParams).subscribe(
//       (response: any) => {
//         this.vac = response.objectList;
//         console.log(this.vac);
      
//       },
//       (error) => {
//         console.log(error)
//       } 
//     );
// }


  }



