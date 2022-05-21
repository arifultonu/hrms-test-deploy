import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import baseUrl from 'src/app/_helper';
import { retry } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class IrecruitmentService {
  dob: string;
  requiredWithin: string;

  public baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) { }




  public sendGetRequest(apiURL, queryParams){

    console.log("@sendGetRequest");
    return this.http.get(apiURL, {params: queryParams}).pipe( retry(3));

  }
  public sendPostRequest(apiURL, formData){

    console.log(formData);
    return this.http.post(apiURL, formData);
   // return  this.http.post<any>(apiURL, formData);

  }
  public sendPutRequest(apiURL, formData){

    console.log("@sendPutRequest");
    return this.http.put(apiURL, formData);

  }
  public sendDeleteRequest(apiURL, formData){

    console.log("@sendDeleteRequest");
    return this.http.delete(apiURL, formData);

  }


  // Get PayrollElement Assignment By EmpId
  public getPayrollElementAssignmentByEmpId(empId){
    return this.http.get(`${baseUrl}/empPayrollAssignment/get/${empId}`);
  }

 //upload profile image file
 public uploadProfileImage(id,formData){
  return  this.http.post<any>(`${baseUrl}/multimedia/${id}`,formData);

}

//upload Nominee image file
public uploadApplicantImage(id,formData){
  return  this.http.post<any>(`${this.baseUrl}/multimedia/aplcImg/${id}`,formData);

}

    // get vacancy
    public getVac(){
      return this.http.get(`${baseUrl}/api/applicant/getList`);
    }

    public sendPostReq(apiURL,appInst){

      console.log("@sendPostReq-new");
      return this.http.post(apiURL,appInst);


    }

    public getAllApplicant(){
      return this.http.get(`${this.baseUrl}/api/applicant/getApplicantLoader`);
    }


    public getAllVacancy(){
      return this.http.get(`${this.baseUrl}/api/vacancy/getVacancyLoader`);
    }

    public getAllUserGrp(){
      return this.http.get(`${this.baseUrl}/user/getGroupUser`);
    }



    public getSearchAPLC(apiURL, queryParams)
    {
      return this.http.get(apiURL, {params: queryParams}).pipe( retry(3));
    }




}
