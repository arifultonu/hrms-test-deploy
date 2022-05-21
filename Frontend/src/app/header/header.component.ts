import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { environment } from "src/environments/environment";
import { HrCrEmp } from "../all-modules/employees/model/HrCrEmp";
import { LoginService } from "../login/services/login.service";
import { HeaderService } from "./header.service";


declare const $: any;
@Component({
  selector: "app-header",
  templateUrl: "./header.component.html",
  styleUrls: ["./header.component.css"],
})
export class HeaderComponent implements OnInit {

  baseUrl = environment.baseUrl;

  isLoggedIn = false;
  user!:HrCrEmp;
  profileImageUrl!:any;
roles:any;

  currOrg:any;


  jsonData: any = {
    notification: [],
    message: [],
  };
  notifications: any;
  messagesData: any;

  constructor(private headerService: HeaderService, private router: Router,private login:LoginService) {}

  ngOnInit() {

    // this.getDatas("notification");
    // this.getDatas("message");

    this.currOrg = this.login.getOrg();
    //alert(this.currOrg);

    this.isLoggedIn = this.login.isLoggedIn();
    this.user = this.login.getUser();
    this.roles = this.login.getLoginUserRole();

console.log("rol :"+this.roles);

    this.profileImageUrl = this.baseUrl+this.user.pic_;
  //  if(this.profileImageUrl == null || this.profileImageUrl == "") this.profileImageUrl = "assets/img/u-sq-pic.jpg";



    this.login.loginStatusSubject.asObservable().subscribe((data) => {
      this.isLoggedIn = this.login.isLoggedIn();
    });


    this.notifications = [
      {
        message: "Patient appointment booking",
        author: "John Doe",
        function: "added new task",
        time: "4 mins ago",
      },
      {
        message: "Patient appointment booking",
        author: "John Doe",
        function: "added new task",
        time: "1 hour ago",
      },
      {
        message: "Patient appointment booking",
        author: "John Doe",
        function: "added new task",
        time: "4 mins ago",
      },
      {
        message: "Patient appointment booking",
        author: "John Doe",
        function: "added new task",
        time: "1 hour ago",
      },
      {
        message: "Patient appointment booking",
        author: "John Doe",
        function: "added new task",
        time: "4 mins ago",
      },
      {
        message: "Patient appointment booking",
        author: "John Doe",
        function: "added new task",
        time: "1 hour ago",
      },
    ];

    this.messagesData = [
      {
        message: "Lorem ipsum dolor sit amet, consectetur adipiscing",
        author: "Mike Litorus",
        time: "4 mins ago",
      },
      {
        message: "Lorem ipsum dolor sit amet, consectetur adipiscing",
        author: "Mike Litorus",
        time: "1 hour ago",
      },
      {
        message: "Lorem ipsum dolor sit amet, consectetur adipiscing",
        author: "Mike Litorus",
        time: "4 mins ago",
      }
    ];
  }

  public logout() {
    this.login.logout();
  }

  getDatas(section) {
    this.headerService.getDataFromJson(section).subscribe((data) => {
      this.jsonData[section] = data;
    });
  }

  clearData(section) {
    this.jsonData[section] = [];
  }
  onSubmit() {
    this.router.navigate(["/pages/search"]);
  }

  toggleCompanyLogo(){
    console.log("toggleCompanyLogo");
    let dataFlag = $("#companyLogo").attr("data-flag");
    if(dataFlag == "1"){

      if(this.currOrg === 'ODCL'){
        $("#companyLogo").attr("data-flag", "0");
        $("#companyLogo").attr('src', "assets/img/one_direction_logo.png");
      }
      if(this.currOrg === 'Majesto'){
        $("#companyLogo").attr("data-flag", "0");
        $("#companyLogo").attr('src', "assets/img/majesto_logo_login.jpg");
      }
      // if(this.currOrg == 'Digi-Tech'){
      //   $("#companyLogo").attr("data-flag", "0");
      //   // $("#companyLogo").attr('src', "assets/img/one_direction_logo.png");
      //   $("#companyLogo").attr('src', "assets/img/majesto_logo_login.jpg");
      // }


    } else {

      if(this.currOrg === 'ODCL'){
        $("#companyLogo").attr("data-flag", "1");
        $("#companyLogo").attr('src', "assets/img/White_w_logo-2.png");
      }

      if(this.currOrg === 'Majesto'){
        $("#companyLogo").attr("data-flag", "1");
        $("#companyLogo").attr('src', "assets/img/majesto_logo_login.jpg");
      }
      // if(this.currOrg == 'Digi-Tech'){
      //   $("#companyLogo").attr("data-flag", "1");
      //   // $("#companyLogo").attr('src', "assets/img/one_direction_logo.png");
      //   $("#companyLogo").attr('src', "assets/img/majesto_logo_login.jpg");
      // }


    }

  }


}
