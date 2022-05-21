import { Component, OnInit } from "@angular/core";
import { Router, Event, NavigationEnd } from "@angular/router";
import { environment } from "src/environments/environment";
import { AllModulesService } from "../all-modules/all-modules.service";
import { EmployeeDashboardService } from "../all-modules/dashboard/services/employee-dashboard.service";
import { HeaderService } from "../header/header.service";

@Component({
  selector: "app-sidebar",
  templateUrl: "./sidebar.component.html",
  styleUrls: ["./sidebar.component.css"],
})
export class SidebarComponent implements OnInit {

  baseUrl = environment.baseUrl;

  menuData : any = [];

  urlComplete = {
    mainUrl: "",
    subUrl: "",
    childUrl: "",
    fullUrl: "",
  };

  sidebarMenus = {
    default: true,
    chat: false,
    settings: false,
  };

  members = {};
  groups = {};

  constructor(
    private router: Router,
    private allModulesService: AllModulesService,
    private headerService: HeaderService,
    private employeeDashboardService: EmployeeDashboardService
  ) {
    this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationEnd) {
        $(".main-wrapper").removeClass('slide-nav');
        $(".sidebar-overlay").removeClass('opened');
        const url = event.url.split("/");
        this.urlComplete.mainUrl = url[1];
        this.urlComplete.subUrl = url[2];
        this.urlComplete.childUrl = url[3];
        if (url[1] === "") {
          this.urlComplete.mainUrl = "dashboard";
          this.urlComplete.subUrl = "admin";
        }

        if (url[2] === "chat" || url[2] === "calls") {
          this.sidebarMenus.chat = true;
          this.sidebarMenus.default = false;
        } else {
          this.sidebarMenus.chat = false;
          this.sidebarMenus.default = true;
        }
        // make full url
        this.urlComplete.fullUrl = url[1]
        if ( url.length> 3 ) this.urlComplete.fullUrl = url[2] + "/" + url[3];
        console.log(this.urlComplete.fullUrl);
      }
    });

    this.groups = { ...this.allModulesService.groups };
    this.members = { ...this.allModulesService.members };
  }

  ngOnInit() {

    this._getMenuesAuth();
    // Slide up and down of menus
    $(document).on("click", "#sidebar-menu a", function (e) {
      e.stopImmediatePropagation();

      if ($(this).parent().hasClass("submenu")) {
        e.preventDefault();
      }

      if (!$(this).hasClass("subdrop")) {
        $("ul", $(this).parents("ul:first")).slideUp(350);
        $("a", $(this).parents("ul:first")).removeClass("subdrop");
        $(this).next("ul").slideDown(350);
        $(this).addClass("subdrop");

      } else if ($(this).hasClass("subdrop")) {
        $(this).removeClass("subdrop");
        $(this).next("ul").slideUp(350);

      }
    });

  }



  _getMenuesAuth(){
    this.employeeDashboardService.sendGetRequestForMenusAuth().subscribe((data) => {
      this.menuData = data;
      console.log('From Sidebar :: '+data);
      //console.log(this.listData);
      console.log(this.menuData);

      for(const item in this.menuData){

        const auth = this.menuData[item];
        console.log(auth);

        if(auth != true){
          $('#'+item).css({
            "display": "none",

          });
        }

      }


    });
  }

  setActive(member) {
    this.allModulesService.members.active = member;
  }

}
