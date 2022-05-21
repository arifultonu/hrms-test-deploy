import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class AdminDashboardService {

  baseUrl = environment.baseUrl;

  constructor(private http:HttpClient) {}

  public getTotalEmployees(){
    return this.http.get(`${this.baseUrl}/api/v1/adminDashboard/getTotalEmployees`);
  }

  public getTotalEmployeesJoinedThisMonth(){
    return this.http.get(`${this.baseUrl}/api/v1/adminDashboard/totalEmployeesJoinedLastMonth`);
  }

  public getTotalEmployeesPresentToday(){
    return this.http.get(`${this.baseUrl}/api/v1/adminDashboard/getPresentEmployeesToday`);
  }

  public getTotalEmployeesAbsentToday(){
    return this.http.get(`${this.baseUrl}/api/v1/adminDashboard/getAbsentEmployeesToday`);
  }
  
  public getTotalLateEmployeesToday(){
    return this.http.get(`${this.baseUrl}/api/v1/adminDashboard/getLateEmployeesToday`);
  }

  public getEarlyGoneEmployeesToday(){
    return this.http.get(`${this.baseUrl}/api/v1/adminDashboard/getEarlyGoneEmployeesToday`);
  }

  public getOnTourEmployeesToday(){
    return this.http.get(`${this.baseUrl}/api/v1/adminDashboard/getOnTourEmployeesToday`);
  }

  public getOnLeaveEmployeesToday(){
    return this.http.get(`${this.baseUrl}/api/v1/adminDashboard/getOnLeaveEmployeesToday`);
  }
  public getDepWiseHeadCount(){
    return this.http.get(`${this.baseUrl}/api/v1/adminDashboard/getNumberOfEmployeeEachDepartment`);
  }
  public getGenderWiseHeadCount()
  {
    return this.http.get(`${this.baseUrl}/api/v1/adminDashboard/getNumberOfEmployeeEachGender`);
  }

  public getDestrictWiseHeadCount()
  {
    return this.http.get(`${this.baseUrl}/api/v1/adminDashboard/getNumberOfEmployeeEachDistrict`);
  }
  public getAgeGroupWiseHeadCount()
  {
    return this.http.get(`${this.baseUrl}/api/v1/adminDashboard/getNumberOfEmployeeEachAgeGroup`);
  }

  public getThisMonthAttnWiseHeadCountChartData()
  {
    return this.http.get(`${this.baseUrl}/api/v1/adminDashboard/getNumberOfEmployeeAttnThisMonth`);
  }
  public getExperienceWiseHeadCount()
  {
    return this.http.get(`${this.baseUrl}/api/v1/adminDashboard/getNumberOfEmployeeEachExperienceGroup`);
  }
}
