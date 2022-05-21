import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { retry } from 'rxjs/operators';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class PayrollService {

  public baseUrl = environment.baseUrl;

  constructor(private http:HttpClient) { }



  public sendGetRequest(apiURL, queryParams){

    console.log("@sendGetRequest");
    return this.http.get(apiURL, {params: queryParams}).pipe( retry(3));

  }
  public sendPostRequest(apiURL, formData){

    console.log("@sendPostRequest");
    return this.http.post(apiURL, formData);
  }

  public  sendPostRequestWithParam(apiURL, formData, queryParams){

      console.log("@sendPostRequestWithParam");
      return this.http.post(apiURL, formData, {params: queryParams});
  
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
    console.log("@sendGetRequest For PayrollElementAssignment By EmpId");
    return this.http.get(`${this.baseUrl}/empPayrollAssignment/get/${empId}`);
  }


}
