import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { environment } from 'src/environments/environment';
import { OffDayBillService } from '../../../service/off-day-bill.service';

@Component({
  selector: 'app-off-day-bill-show',
  templateUrl: './off-day-bill-show.component.html',
  styleUrls: ['./off-day-bill-show.component.css']
})
export class OffDayBillShowComponent implements OnInit {

  public baseUrl = environment.baseUrl;
  public myData: any = {};

  constructor(
    private route: ActivatedRoute,
    private spinnerService: NgxSpinnerService,
    private offDayBillService:OffDayBillService,
  ) { }

  ngOnInit(): void {
    this.getFormData();
  }
  getFormData(){

    let id =  this.route.snapshot.params.id;
    let apiURL = this.baseUrl + "/offDayBill/get/" + id;

    let queryParams: any = {};
   

    this.spinnerService.show();
    this.offDayBillService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.myData = response;
       console.log(this.myData);
       
        this.spinnerService.hide();
     
      },
      (error) => {
        console.log(error)
      } 
    );


  }
}
