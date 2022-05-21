import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { retry } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SystemService {

  public sysRes = new Subject<boolean>();

  
  public baseUrl = environment.baseUrl;

  constructor(private http:HttpClient) { }

 

  // get All System User 
  public getAllUsers(){
    return this.http.get(`${this.baseUrl}/user/getAll`);
  }

  //send get request by id
  public sendGetRequestById(apiURL, id){
    console.log("@sendGetRequestById");
    return this.http.get(`${apiURL}/${id}`);
  }


  // get All paginated System getusers
  public getAllPaginatedUsers(queryParams){
      return this.http.get(`${this.baseUrl}/api/common/getUser`,{params: queryParams}).pipe( retry(3));
  }
  

  // Get All System User whose has no employee created
  public getNotEmpUsers(){
    return this.http.get(`${this.baseUrl}/user/notEmp`);
  }

  // get group user api 
  public getGroupUser(){
    return this.http.get(`${this.baseUrl}/user/getGroupUser`);

  }

  // create sysResDef
  public createSysResDef(reqBody){
    return this.http.post(`${this.baseUrl}/sysDef/create`,reqBody);
  }
  //update sysResDef
  public updateSysResDef(id,reqBody){
    return this.http.put(`${this.baseUrl}/sysDef/update/${id}`,reqBody);
  }

  // get SysResDef 
  public getSysResDef(queryParams){
    return this.http.get(`${this.baseUrl}/sysDef/get`,{params: queryParams}).pipe( retry(3));
  }

  //delete SysResDef
  public deleteSysResDef(id){
    return this.http.delete(`${this.baseUrl}/sysDef/delete/${id}`);
  }

  //create SysResAuth 

  public createSysResAuth(reqBody){
    return this.http.post(`${this.baseUrl}/sysAuth/create`,reqBody);
  }

  // get SysResAuth
  public getSysResAuth(queryParams){
    return this.http.get(`${this.baseUrl}/sysAuth/get`,{params: queryParams}).pipe( retry(3));
  }

  public getSysResAuthByIds(id){
    return this.http.get(`${this.baseUrl}/sysAuth/find/${id}`);
  }

  //get SysAuth By id
  public getSysResAuthById(id){
    return this.http.get(`${this.baseUrl}/sysAuth/get/${id}`);
  }

   //update sysResDef
   public updateSysResAuth(reqBody){
    return this.http.put(`${this.baseUrl}/sysAuth/update`,reqBody);
  }

   //delete SysResDef
   public deleteSysResAuth(id){
    return this.http.delete(`${this.baseUrl}/sysAuth/delete/${id}`);
  }

  //get roles
  public getRoles(){
    return this.http.get(`${this.baseUrl}/roles`);
  }



  public sendGetRequest(apiURL, queryParams){

    console.log("@sendGetRequest");
    return this.http.get(apiURL, {params: queryParams}).pipe( retry(3));

  }

  public sendPostRequest(apiURL, formData){

    console.log("@sendPostRequest");
    return this.http.post(apiURL, formData);

  }

  sendDeleteRequest(apiUrl,id: string): Observable<any> {
    return this.http.delete<any>(`${apiUrl}/${id}`);
  }

  
  
}
