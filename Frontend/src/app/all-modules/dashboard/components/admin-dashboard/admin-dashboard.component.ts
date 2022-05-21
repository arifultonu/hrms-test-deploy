import { Component, OnInit } from "@angular/core";
import { AdminDashboardService } from "../../services/admin-dashboard.service";
import { Chart, registerables } from 'chart.js';
import { NgxSpinnerService } from "ngx-spinner";


Chart.register(...registerables);

@Component({
  selector: "app-admin-dashboard",
  templateUrl: "./admin-dashboard.component.html",
  styleUrls: ["./admin-dashboard.component.css"],
})
export class AdminDashboardComponent implements OnInit {

  public empTotal : any;
  public empThisMonth : any;
  public empPresent: any;
  public empAbsent:any;
  public lateEmployee: any;
  public earlyGoneEmployee: any;
  public onTourEmployee: any;
  public onLeaveEmployee: any;
  
  public depWiseHeadCountChartData: any;
  public genderWiseHeadCountChartData: any;
  public destrictWiseHeadCountChartData: any;
  public ageGroupWiseHeadCountChartData: any;
  public thisMonthAttnWiseHeadCountChartData: any;
  public experienceWiseHeadCountChartData:any;
  public chartOptions: any;
  public barColors = {
    a: "#667eea"
  };


  constructor(
    private _dashboardService: AdminDashboardService,
    private spinnerService: NgxSpinnerService,
     ) {
    }


  ngOnInit() {
    

    this._countTotalEmployees();
    this._getEmployeesJoinedThisMonth();
    this._getTotalEmployeesPresentToday();
    this._getTotalEmployeesAbsentToday();
    this._getTotalLateEmployeesToday();
    this._getEarlyGoneEmployeesToday();
    this._getOnTourEmployeesToday();
    this._getOnLeaveEmployeesToday();

    //chart 

    this._getDepWiseHeadCountChartData();
    this._getGenderWiseHeadCountChartData();
    this._getDisrtictWiseHeadCountChartData();
    this._getAgeGrouptWiseHeadCountChartData();
    this._getThisMonthAttnWiseHeadCountChartData();
    this._getThisMonthAttnWiseHeadCountChartData2();
    this._getExperiencetWiseHeadCountChartData()

    
  
 
  


  

  

  }

  _countTotalEmployees(){
    this._dashboardService.getTotalEmployees().subscribe((data:any)=>{
      this.empTotal = data;
    })
  }

  _getEmployeesJoinedThisMonth(){
    this._dashboardService.getTotalEmployeesJoinedThisMonth().subscribe((data:any)=>{
      this.empThisMonth = data;
    })
  }

  _getTotalEmployeesPresentToday(){
    this._dashboardService.getTotalEmployeesPresentToday().subscribe((data:any)=>{
      this.empPresent = data;
    })
  }

  _getTotalEmployeesAbsentToday(){
    this._dashboardService.getTotalEmployeesAbsentToday().subscribe((data:any)=>{
      this.empAbsent = data;
    })
  }

  _getTotalLateEmployeesToday(){
    this._dashboardService.getTotalLateEmployeesToday().subscribe((data:any)=>{
      this.lateEmployee = data;
    })
  }

  _getEarlyGoneEmployeesToday(){
    this._dashboardService.getEarlyGoneEmployeesToday().subscribe((data:any)=>{
      this.earlyGoneEmployee = data;
    })
  }

  _getOnTourEmployeesToday(){
    this._dashboardService.getOnTourEmployeesToday().subscribe((data:any)=>{
      this.onTourEmployee = data;
    })
  }

  _getOnLeaveEmployeesToday(){
    this._dashboardService.getOnLeaveEmployeesToday().subscribe((data:any)=>{
      this.onLeaveEmployee = data;
    })
  }
  _getDepWiseHeadCountChartData()
  {
    var cd = []

    // this.depWiseHeadCountChartData = {
    //   type: 'ColumnChart',
    // };
 

    this.spinnerService.show();
    var i = 0;
    this._dashboardService.getDepWiseHeadCount().subscribe((data:any)=>{
     data.objectList.forEach(element => {
        cd[i++] = [element.dept_name, element.emp_head_count];
     });
     //console.log(cd);
     this.depWiseHeadCountChartData = {
       type: 'ColumnChart',
       data:cd ,
       chartColumns: ['Departments Nanme', 'Emp'],
       options : {
        colors: [ '#764BA2'],
        //title: 'Department wise headcount',
      },
       width: 560,
       height: 343,
     };

      
    })
   
  }

  _getGenderWiseHeadCountChartData()
  {
    var cd = []
    var i = 0;
    this._dashboardService.getGenderWiseHeadCount().subscribe((data:any)=>{
    
     data.objectList.forEach(element => {  
        cd[i++] = [element.gender, element.count];
     });
     //console.log(cd);
     this.genderWiseHeadCountChartData = {
       type: 'PieChart',
       data:cd ,
       chartColumns: ['Gender', 'Emp'],
       options : {    
        is3D:false,
       // title: 'Gender Wise Head Count',
       },
       width: 550,
       height: 300,
     };

      
    })
   
  }
  _getDisrtictWiseHeadCountChartData()
  {
    var cd = []
    var i = 0;
    this._dashboardService.getDestrictWiseHeadCount().subscribe((data:any)=>{
    
     data.objectList.forEach(element => {  
        cd[i++] = [element.district, element.count];
     });
     //console.log(cd);
     this.destrictWiseHeadCountChartData = {
       type: 'PieChart',
       data:cd ,
       chartColumns: ['District', 'Emp'],
       width: 550,
       height: 300,
     };

      
    })
  }

  _getAgeGrouptWiseHeadCountChartData()
  {
    var cd = []
    var i = 0;
    this._dashboardService.getAgeGroupWiseHeadCount().subscribe((data:any)=>{
    
     data.objectList.forEach(element => {  
        cd[i++] = [element.ageGroup, element.count];
     });
    // console.log(cd);
     this.ageGroupWiseHeadCountChartData = {
       type: 'PieChart',
       data:cd ,
       chartColumns: ['Age Group', 'Emp'],
       options : {    
        is3D:true,
       // title: 'Age Group Wise Head Count',
       },
       width: 550,
       height: 300,
     };

      
    })
  }

  _getThisMonthAttnWiseHeadCountChartData()
  {
    var cd = []
    
    var i = 0;
    
    this._dashboardService.getThisMonthAttnWiseHeadCountChartData().subscribe((data:any)=>{
    
   // console.log(data);
      
     data.objectList.forEach(element => {  
      //  cd[0]=  ['Month', 'Absent','Present','Late','Early Gone','Late And Early Gone'],
        cd[i++] = [element.date, element.data.Absent,element.data.Present,element.data.Late,element.data.Early_Gone,element.data.Late_And_Early_Gone,element.data.Weekend,element.data.On_Tour];
        
     });
    //  console.log(cd);
     this.thisMonthAttnWiseHeadCountChartData = {
     
   type : 'ColumnChart',
   data :cd,
   chartColumns : ['Date', 'Absent','Present','Late','Early Gone','Late And Early Gone','Weekend','On Tour'],
   options : {
    colors: ['#A11320', '#109618', '#403375', '#5B2A5E', '#6E244D','#213D91','#961629'],
   // title: 'Last Seven Days Attendance Wise Headcount',
  },
  
  //  width : 1200,
  //  height : 400,
  width: 560,
  height: 343,
  };

    })
  }


  public lineData;
  public lineOption;
  _getThisMonthAttnWiseHeadCountChartData2()
  {
    var cd = []
    
    var i = 0;

    this._dashboardService.getThisMonthAttnWiseHeadCountChartData().subscribe((data:any)=>{
    
      // console.log(data);
      // console.log("y:"+data.objectList[0].date);
      // console.log("a:"+data.objectList[0].data.Late);
       
      data.objectList.forEach(element => {  
       //  cd[0]=  ['Month', 'Absent','Present','Late','Early Gone','Late And Early Gone'],
         cd[i++] = {y:element.date, a:element.data.Absent, b:element.data.Present ,c:element.data.Late, d:element.data.Early_Gone,e:element.data.Late_And_Early_Gone,f:element.data.Weekend,g:element.data.On_Tour };
         
      });
     
       console.log(cd);
      var lineColors = {
        abs: "#667eea",
        pre: "#764ba2",
        lt: "#23705c",
        eg: "#0d4033",
        lteg: "#372e75",
        wk: "#872652",
        ontr: "#9bbd17",
      };
     this. lineOption = {
        xkey: "y",
        ykeys: ["a", "b","c","d","e","f","g"],
        labels: ["Total Absent", "Total Present","Total Late","Total Early Gone",
        "Total Late & Early Gone", "Total Weekend","Total Ontour",],
        resize: true,
        lineColors: [lineColors.abs, lineColors.pre,lineColors.lt,lineColors.eg,
          lineColors.lteg,lineColors.wk,lineColors.ontr],
      };
  
       this.lineData = cd
 
     })

  }
   


  _getExperiencetWiseHeadCountChartData()
  {var cd = []
    var i = 0;
    this._dashboardService.getExperienceWiseHeadCount().subscribe((data:any)=>{
    
     data.objectList.forEach(element => {  
        cd[i++] = [element.type_of_ex, element.emp_head_count];
     });
    // console.log(cd);
     this.experienceWiseHeadCountChartData = {
       type: 'PieChart',
       data:cd ,
       chartColumns: ['Experience', 'Emp'],
       options : {    
        is3D:true,
       // title: 'Age Group Wise Head Count',
       },
        width: 550,
        height: 300,
     };

      
    })

  }








}
