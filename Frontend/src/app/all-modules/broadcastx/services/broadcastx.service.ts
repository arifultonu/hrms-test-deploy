import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { retry } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BroadcastxService {

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
}
