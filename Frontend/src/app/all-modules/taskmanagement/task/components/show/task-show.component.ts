import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { environment } from 'src/environments/environment';
import { TaskService } from '../../service/task.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-task-show',
  templateUrl: './task-show.component.html',
  styleUrls: ['./task-show.component.css']
})
export class TaskShowComponent implements OnInit {

  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  public myFormData: any = {};

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private taskService: TaskService,
    private toastr : ToastrService,
  ) { }

  ngOnInit(): void {
    this._getFormData();
  }

  _getFormData(){

    const id =  this.route.snapshot.params.id;
    const apiURL = this.baseUrl + '/task/getTaskById/' + id;
    const queryParams: any = {};
    queryParams.rEntityName = 'Task';
    queryParams.rActiveOperation = 'read';
    this.spinnerService.show();
    this.taskService.sendGetRequest(apiURL, queryParams)
      .subscribe(
        response => {
          if(response.status === true){
            this.spinnerService.hide();
            this.myFormData = response.data;
          }else{
            this.spinnerService.hide();
            this.toastr.error(response.message, 'Error');
          }
        },
        error => {
          this.spinnerService.hide();
          console.log('Error: ', error);
          this.toastr.error(error.error.message);
        }
      );
}

}
