import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { retry, tap } from 'rxjs/operators';
import { HeaderService } from 'src/app/header/header.service';

import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EmployeeDashboardService {
  baseUrl = environment.baseUrl;



  constructor(private http:HttpClient,private headerService:HeaderService) { }

  // private _refreshNeed= new Subject<void>();
  // get refreshNeed()
  // {
  //   return this._refreshNeed;
  // }
  // Get last 7 day attn  
  public getLastSevenDaysAttn(){
    return this.http.get(`${this.baseUrl}/attnProc/lastSevenDaysAttn`);
  }

  sendGetRequestForMenusAuth(){
    console.log("sendGetRequestForMenusAuth");
    return this.http.get(`${this.baseUrl}/menusAuth`);
  }

  public sendGetSelfRequest(apiURL){
    
    console.log("@sendGetRequest");
    return this.http.get(apiURL)
    // .pipe(
    //   tap(()=>
    //   {
    //     this._refreshNeed.next();
    //   })
    // );

  }
  
  
}
