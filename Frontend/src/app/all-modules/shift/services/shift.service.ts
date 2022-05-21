import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { retry } from 'rxjs/operators';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class ShiftService {

  baseUrl = environment.baseUrl;

  constructor(private http:HttpClient) { }

  public createShift (shift){
    return this.http.post(`${this.baseUrl}/shft/save`,shift);
  }
  public getAllShift (){
    return this.http.get(`${this.baseUrl}/shft/findAll`);
  }

  public getAllActiveAssignShift()
  {
    return this.http.get(`${this.baseUrl}/shftAssign/findAll`);
  }
  public sendGetSelfRequest(apiURL, queryParams){

    console.log("@sendGetSelfRequest");
    return this.http.get(apiURL, {params: queryParams}).pipe( retry(3));

  }
  public assignShift(value)
  {
    return this.http.post(`${this.baseUrl}/shftAssign/save`,value);
  }
  public editnShift(value)
  {
    return this.http.put(`${this.baseUrl}/shftAssign/edit`,value);
  }

  public sendDeleteRequest(value)
  {
    return this.http.delete(`${this.baseUrl}/shftAssign/delete_by_id/${value}`);
  }

}
