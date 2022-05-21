import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { retry } from 'rxjs/operators';

import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LeaveConfigService {
  baseUrl = environment.baseUrl;

  constructor(private http:HttpClient) { }

  //create leave config
  public createLeaveCnfg (leaveCnfg){
    return this.http.post(`${this.baseUrl}/leaveConf/save`,leaveCnfg);
  }

  //get all leave config
  public getAllLeaveConfig (queryParams){
    return this.http.get(`${this.baseUrl}/leaveConf/findAll`,{params: queryParams}).pipe( retry(3));
  }
  //get all leave prd
  public getLeavePrd (){
    return this.http.get(`${this.baseUrl}/leavePrd/findAll`);
  }

  //get all self leave 
  public getselfLeave (){
    return this.http.get(`${this.baseUrl}/leaveAssign/selfLeave`);
  }

  getselfLeaveByType(type){
    return this.http.get(`${this.baseUrl}/leaveAssign/selfLeave/${type}`);
  }

  getselfLeaveByTypeAndEmp(apiURL, queryParams){
    console.log("@sendGetRequest");
    return this.http.get(apiURL, {params: queryParams}).pipe( retry(3));
  } 

  //create leave assign
  public createLeaveAssign (leaveAssign){
    return this.http.post(`${this.baseUrl}/leaveAssign/save`,leaveAssign);
  }
  //create Cmpnstry leave assign
  public createCmLeaveAssign (cmLeaveAssign){
    return this.http.post(`${this.baseUrl}/leaveAssign/saveCmpnstry`,cmLeaveAssign);
  }

  //get all leave assign
  public getAllLeaveAssign (queryParams){
    return this.http.get(`${this.baseUrl}/leaveAssign/findAll`,{params: queryParams}).pipe( retry(3));
  }
}
