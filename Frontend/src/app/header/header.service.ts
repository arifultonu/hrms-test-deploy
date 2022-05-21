import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginService } from '../login/services/login.service';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class HeaderService   {


    baseUrl = environment.baseUrl;

  constructor(private http: HttpClient,private loginService: LoginService) {
  }

  

  getDataFromJson(section) {
    return this.http.get(`assets/json/${section}.json`);
  }

  

  sendGetRequestForMenusAuth(){
    console.log("sendGetRequestForMenusAuth");
    return this.http.get(`${this.baseUrl}/menusAuth`);
  }


  

}
