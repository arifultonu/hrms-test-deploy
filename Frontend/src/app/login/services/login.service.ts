import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { ToastrService } from "ngx-toastr";
import { Observable, Subject } from "rxjs";
import { environment } from "src/environments/environment";
import { User } from "../models/user";

@Injectable({
  providedIn: "root",
})
export class LoginService {
  baseUrl = environment.baseUrl;


  public loginStatusSubject = new Subject<boolean>();
  clearTimeout: any;

  constructor(private http: HttpClient, private toastr: ToastrService) {}



  //current user : which is loogedin
  public getCurrentUser() {
    return this.http.get(`${this.baseUrl}/currentUser`);
  }

  //generate Token
  public generateToken(loginData: any) {
    return this.http.post(`${this.baseUrl}/generateToken`, loginData);
  }

  //login user : set token in local storage
  public loginUser(token) {
    localStorage.setItem("token", token);
    // this.loginStatusSubject.next(true);
    return true;
  }

  //isLoogedIn: user is loggedin or not
  public isLoggedIn() {
    let tokenStr = localStorage.getItem("token");
    if (tokenStr == undefined || tokenStr == "" || tokenStr == null) {
      return false;
    } else {
      return true;
    }
  }

  //set organization
  public setOrg(orgName) {
    localStorage.setItem("org", orgName);
  }

  public getOrg(){
    return localStorage.getItem("org");
  }





  //logout:: remove token from localstorage
  public logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    localStorage.removeItem("activeTabName");
    if (this.clearTimeout) {
      clearTimeout(this.clearTimeout);
    }
    return true;
  }

  //getToken
  public getToken() {
    return localStorage.getItem("token");
  }

  //set userDetail
  public setUser(user) {
    console.log("user=>" + JSON.stringify(user));
    localStorage.setItem("user", JSON.stringify(user));
  }

  //getUser
  public getUser() {
    let userStr = localStorage.getItem("user");
    if (userStr != null) {
      return JSON.parse(userStr);
    } else {
      this.logout();
      return null;
    }
  }


  //get user role
  public getLoginUserRole() {
    let userAuthorities = "";

    let loginUser = this.getUser();
    if (loginUser) {
      let authorities = loginUser.authorities;
      authorities.forEach((element) => {
        userAuthorities = userAuthorities + element.authority + ",";
      });
    }

    console.log("userAuthorities" + userAuthorities);
    return userAuthorities;
  }



  // Register User
  public register(user) {
    return this.http.post(`${this.baseUrl}/user/register`, user);
  }
  // Get By Id
  public getById(id) {
    return this.http.get(`${this.baseUrl}/user/get/${id}`);
  }

  sendPostRequest(apiURL, user: User): Observable<User> {
    return this.http.post<User>(apiURL, user);
  }

  sendPutRequest(apiURL, user: User): Observable<User> {
    return this.http.put<User>(apiURL, user);
  }

  public sendGetRequest(apiURL, queryParams){
    console.log("@sendGetRequest");
    return this.http.get<any>(apiURL, {params: queryParams}).pipe( retry(3));
  }



  public getOrgIdn() {
    return this.http.get(`${this.baseUrl}/api/OrgIdn/getActiveOrg`);
  }



}
function retry(arg0: number): import("rxjs").OperatorFunction<any, unknown> {
  throw new Error("Function not implemented.");
}

