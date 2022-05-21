import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { retry } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ApprovalProcessService {

  constructor(private http:HttpClient) { }


  public sendGetRequest(apiURL, queryParams){

    console.log("@sendGetRequest");
    return this.http.get(apiURL, {params: queryParams}).pipe( retry(3));

  }

  public sendPostRequest(apiURL, formData)
  {
    console.log("@sendPostRequest");
    return this.http.post(apiURL, formData);
  }
  public sendPutRequest(apiURL, formData){

    console.log("@sendPutRequest");
    return this.http.put(apiURL, formData);

  }

  public sendDeleteRequest(apiURL, formData){

    console.log("@sendDeleteRequest");
    return this.http.delete(apiURL, formData);

  }

  
}
