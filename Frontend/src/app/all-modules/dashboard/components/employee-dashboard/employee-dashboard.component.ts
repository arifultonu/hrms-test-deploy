
import { DatePipe } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HrCrEmp } from 'src/app/all-modules/employees/model/HrCrEmp';
import { HeaderService } from 'src/app/header/header.service';
import { LoginService } from 'src/app/login/services/login.service';
import { environment } from 'src/environments/environment';



import { EmployeeDashboardService } from '../../services/employee-dashboard.service';


@Component({
  selector: 'app-employee-dashboard',
  templateUrl: './employee-dashboard.component.html',
  styleUrls: ['./employee-dashboard.component.css']
})
export class EmployeeDashboardComponent implements OnInit {
  @ViewChild('hrHand',{static:false})hrHand:ElementRef;
  @ViewChild('minHand',{static:false})minHand:ElementRef;
  @ViewChild('secHand',{static:false})secHand:ElementRef;

  private daysArray=['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday']
  private monthArray=['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec']
  private date= new Date();
  public hour:any;
  public minute:string;
  public second:string;
  public ampm:string;
  public day:string;
  public dateCount:any;
  public month:any;
  public year;any;

  //last 7 day attn
  public last7DaysAttn;

  user!:HrCrEmp;
dob!:HrCrEmp;
  profileImageUrl!:any;


  baseUrl = environment.baseUrl;



  onTourNotifications:any = [];
  leaveNotifications:any = [];
  simNotifications:any = [];
  transportNotifications:any = [];



  constructor(private empDeshbrd:EmployeeDashboardService,private login:LoginService,private headerService:HeaderService,private datepipe:DatePipe) { }



  ngOnInit() {
    setInterval(()=>
    {
      const date= new Date();
      this.updateClock(date);
    },1000);
   this.day=this.daysArray[this.date.getDay()];
   this.dateCount=this.date.getDate();
   this.month=this.monthArray[this.date.getMonth()]
   this.year=this.date.getFullYear();

   this.lastSevenDaysAttn();
   this.lodeLoginUser();
   this.onTourNotification();
   this.leaveNotification();
   this.simNotification();
   this.transportNotification();
   //this.refreshData()
  //  this.empDeshbrd.refreshNeed
  //  .subscribe(()=>
  //  {
  //    this.onTourNotification();
  //  });


  }




  updateClock(date)
  {
    //for analog
    this.secHand.nativeElement.style.transform='rotate('+ date.getSeconds()*6+'deg)';
    this.minHand.nativeElement.style.transform='rotate('+ date.getMinutes()*6 +'deg)';
    this.hrHand.nativeElement.style.transform='rotate('+ (date.getHours()*30 + date.getMinutes() * 0.5)+'deg)';

    //for digital
    const hours=date.getHours();
    this.ampm= hours>=12? 'PM':'AM';
    this.hour=hours%12;//make hour in 12 hour formate
    this.hour=this.hour?this.hour:12;// if the hour is 0 thn 12 is assign
    this.hour=this.hour<10?'0'+this.hour:this.hour; // if houtr is single digite thn add 0 before it

    const minutes=date.getMinutes();
    this.minute=minutes<10?'0'+minutes:minutes.toString();

    const seconds= date.getSeconds();
    this.second=seconds<10?'0'+seconds:seconds.toString();

  }
  lastSevenDaysAttn()
  {
    this.empDeshbrd.getLastSevenDaysAttn() .subscribe((data:any) =>{
      this.last7DaysAttn=data;
      console.log(this.last7DaysAttn);
    })

  }
  currentDate = new Date();


  lodeLoginUser()
  {
    this.dob=this.login.getUser();

    this.user = this.login.getUser();
    this.profileImageUrl=this.baseUrl+this.user.pic_;
  }

  //for notification
  onTourNotification()
  {
    let apiURL = this.baseUrl + "/notification/onTourApproval";

    this.empDeshbrd.sendGetSelfRequest(apiURL).subscribe(
      (response: any) => {
        this.onTourNotifications = response;
        console.log(this.onTourNotifications);


      },
      (error) => {
        console.log(error)
      }
    );

  }
  leaveNotification()
  {
    let apiURL = this.baseUrl + "/notification/leaveApproval";

    this.empDeshbrd.sendGetSelfRequest(apiURL).subscribe(
      (response: any) => {
        this.leaveNotifications = response;
        console.log(this.leaveNotifications);


      },
      (error) => {
        console.log(error)
      }
    );

  }
  simNotification()
  {
    let apiURL = this.baseUrl + "/notification/simApproval";

    this.empDeshbrd.sendGetSelfRequest(apiURL).subscribe(
      (response: any) => {
        this.simNotifications = response;
        console.log(this.simNotifications);


      },
      (error) => {
        console.log(error)
      }
    );

  }
  transportNotification()
  {
  let apiURL = this.baseUrl + "/notification/transportApproval";

  this.empDeshbrd.sendGetSelfRequest(apiURL).subscribe(
    (response: any) => {
      this.transportNotifications = response;
      console.log(this.transportNotifications);


    },
    (error) => {
      console.log(error)
    }
  );

  }

  refreshData(){
    this.onTourNotifications =
      setInterval(() => {
        this.onTourNotification();

      }, 60000);
      this.leaveNotifications =
      setInterval(() => {
        this.leaveNotification();

      }, 60000);
      this.simNotifications =
      setInterval(() => {
        this.simNotification();

      }, 60000);
      this.transportNotifications =
      setInterval(() => {
        this.transportNotification();

      }, 60000);
  }


}
