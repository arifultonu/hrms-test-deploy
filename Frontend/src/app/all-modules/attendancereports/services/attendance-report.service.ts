import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AttendanceReportService {
  baseUrl = environment.baseUrl;

  constructor(private http:HttpClient) { }

  // call Report between date function
   public getReportBetweenDate (data){
    
   return this.http.post(`${this.baseUrl}/attnReport/reportBetweenDate`,data);
  
   }
   // call Report between date function All Emp
   public getReportBetweenDateAllEmp (data){
     console.log(data);
     
    
    return this.http.post(`${this.baseUrl}/attnReport/reportBetweenDateAllEmp`,data);
   
    }



}
