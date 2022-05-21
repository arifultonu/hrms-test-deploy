import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { retry } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CommonService {

  public baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) { }

  // Get ALKP Api Call
  public getAlkp() {
    return this.http.get(`${this.baseUrl}/alkp/getAll`);
  }

  // Save ALKP Api Call
  public saveAlkp(data) {
    return this.http.post(`${this.baseUrl}/alkp/save`, data);
  }

  // Get Parent Alkp Api Call
  public getParentAlkp() {
    return this.http.get(`${this.baseUrl}/alkp/parent`);
  }

  //Get Alkp by keyword
  public getAlkpByKeyword(keyWord) {
    return this.http.get(`${this.baseUrl}/alkp/search/${keyWord}`)
  }


  // Get All Org Mst Api Call
  public getAllOrgMst() {
    return this.http.get(`${this.baseUrl}/allOrgMst/getAll`);
  }
 // Get All Org Mst Api Call
 public getAllOrgMstID(id) {
  return this.http.get(`${this.baseUrl}/allOrgMst/get/${id}`);
}

  //Get parent ALKP Api Call
  public getParentAllOrgMstByOrgType(orgType) {
    return this.http.get(`${this.baseUrl}/allOrgMst/orgType/${orgType}`);
  }

  // Get AllOrgMst By OrgType
  public getParentAlkpByOrgTypeSearch(orgType) {
    return this.http.get(`${this.baseUrl}/allOrgMst/search/${orgType}`);
  }

  //save All org mst
  public saveOrgMst(data) {
    return this.http.post(`${this.baseUrl}/allOrgMst/create`, data);
  }

  public sendGetRequest(apiURL, queryParams) {
    console.log("@sendGetRequest");
    return this.http.get(apiURL, { params: queryParams }).pipe(retry(3));

  }

  // save address
  public saveAddress(data) {
    return this.http.post(`${this.baseUrl}/address/createAddress`, data);
  }

  //update address
  public updateAddress(data) {
    return this.http.post(`${this.baseUrl}/address/update`, data);
  }

  // find address by allOrgMstId
  public findAddressByAllOrgMstId(allOrgMstId) {
    return this.http.get(`${this.baseUrl}/address/getByAllOrgMst/${allOrgMstId}`);
  }

  //  All Common Entity Fetch data
  public getAllEmpListData(apiURL, queryParams){
    return this.http.get(apiURL, { params: queryParams }).pipe(retry(3));
  }

}
