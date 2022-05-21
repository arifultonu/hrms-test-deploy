import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { retry } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { SimBillTransaction } from '../models/SimBillTransaction';
import { SimManagement } from '../models/SimManagement';
import { SimRequisition } from '../models/SimRequisition';

@Injectable({
  providedIn: 'root'
})
export class SimService {


  constructor(private http: HttpClient) { }

  // sim requisition
  sendPostRequest(apiURL,simRequisition: SimRequisition): Observable<SimRequisition> {
    return this.http.post<SimRequisition>(apiURL, simRequisition);
  }

  sendGetRequestById(apiURL,requisitionId: string): Observable<SimRequisition> {
    return this.http.get<SimRequisition>(`${apiURL}/${requisitionId}`);
  }

  sendGetRequest(apiURL,queryParams): Observable<SimRequisition[]> {
    return this.http.get<SimRequisition[]>(apiURL,{params: queryParams}).pipe( retry(3));
  }

  sendPutResquest(apiURL,simRequisition: SimRequisition): Observable<SimRequisition> {
    return this.http.put<SimRequisition>(apiURL, simRequisition);
  }

  sendDeleteRequest(apiUrl,id: string): Observable<any> {
    return this.http.delete<any>(`${apiUrl}/${id}`);
  }

  //sim management
  sendPostResquestOfSimManagement(apiURL,simManagement: SimManagement): Observable<SimManagement> {
    return this.http.post<SimManagement>(apiURL, simManagement);
  }

  sendPutResquestOfSimManagement(apiURL,simManagement: SimManagement): Observable<SimManagement> {
    return this.http.put<SimManagement>(apiURL, simManagement);
  }

  sendPutResquestOfStatusChange(apiURL,id:string): Observable<SimManagement> {
    return this.http.put<SimManagement>(`${apiURL}/${id}`,'');
  }

  sendGetRequestOfSimManagement(apiURL,queryParams): Observable<SimManagement[]> {
    return this.http.get<SimManagement[]>(apiURL,{params: queryParams}).pipe( retry(3));
  }

  sendGetRequestByIdOfSimManagement(apiURL,id: string): Observable<any> {
    return this.http.get<any>(`${apiURL}/${id}`);
  }



  sendPutRequestOfSimManagement(apiURL,simManagement: SimManagement): Observable<SimManagement> {
    return this.http.put<SimManagement>(apiURL, simManagement);
  }

  // Sim Bill transaction
  sendPostRequestOfBillUploadFile(apiURL,formData): Observable<any> {
    return this.http.post<any>(apiURL, formData);
  }

  sendGetRequestOfSimBillTransaction(apiURL,queryParams): Observable<SimBillTransaction> {
    return this.http.get<SimBillTransaction>(apiURL,{params: queryParams}).pipe( retry(3));
  }
  sendPutResquestOfSimBillTransaction(apiURL,simBillTransaction: SimBillTransaction): Observable<SimBillTransaction> {
    return this.http.put<SimBillTransaction>(apiURL, simBillTransaction);
  }

  sendGetRequestByIdOfSimBillTransaction(apiURL,id: string): Observable<SimBillTransaction> {
    return this.http.get<SimBillTransaction>(`${apiURL}/${id}`);
  }

  sendDeleteRequestOfSimBillTransaction(apiUrl,id: string): Observable<any> {
    return this.http.delete<any>(`${apiUrl}/${id}`);
  }

}
