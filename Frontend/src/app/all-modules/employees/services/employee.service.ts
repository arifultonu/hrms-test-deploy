import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { retry } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  public baseUrl = environment.baseUrl;

  constructor(private http:HttpClient) { }

  // Get Employee  Api Call
  public getEmployees(){
    return this.http.get(`${this.baseUrl}/hrCrEmp/empList`);

  }

  // get empList list view
  public getEmpListView(apiURL, queryParams){
    return this.http.get(apiURL, {params: queryParams}).pipe( retry(3));
  }

  // Get Employee via paginations
  public sendGetRequest(apiURL, queryParams){
    console.log("@sendGetRequest");
    return this.http.get(apiURL, {params: queryParams}).pipe( retry(3));

  }
  public sendPostRequest(apiURL, formData){
    console.log(formData);
    return this.http.post(apiURL, formData);
  }
  //send put request
  public sendPutRequest(apiURL, data){
    return this.http.put(apiURL, data);
  }

  // create HrCrEmp  Api Call
  public createEmploy (emp){
    return this.http.post(`${this.baseUrl}/hrCrEmp/create`,emp);
  }

  //update HrCrEmp Api Call
  public updateEmploy (data){
    // return this.http.put(`${baseUrl}/hrCrEmp/update`, data);
    console.log("@updateEmploy" + data);
    return this.http.put(`${this.baseUrl}/hrCrEmp/edit`, data);
  }

  // Get employee By id
  public getEmployeeById(id){
    //return this.http.get(`${this.baseUrl}/hrCrEmp/get/${id}`);
    return this.http.get(`${this.baseUrl}/hrCrEmp/getData/${id}`);
  }

  // Get employee By id
  public findEmployeeById(id){
    return this.http.get(`${this.baseUrl}/hrCrEmp/find/${id}`);
    //return this.http.get(`${baseUrl}/hrCrEmp/get/${id}`);
  }

  //get employee by loginCode
  public getEmployeeByLoginCode(loginCode){
    return this.http.get(`${this.baseUrl}/hrCrEmp/findByLoginCode/${loginCode}`);
  }

   //upload profile image file
   public uploadProfileImage(id,formData){
    return  this.http.post<any>(`${this.baseUrl}/multimedia/profile/${id}`,formData);

  }

//upload Nominee image file
public uploadNomineeImage(id,formData){
  return  this.http.post<any>(`${this.baseUrl}/multimedia/nominee/${id}`,formData);

}

//upload doc file
public uploadDocuments(id,formData){
  return  this.http.post<any>(`${this.baseUrl}/multimedia/certificates/${id}`,formData);

}

  // Get ALKP Search By Keyword Api Call
  public getAlkpSearchByKeyword(keyword){
    return this.http.get(`${this.baseUrl}/alkp/search/${keyword}`);
  }


  //save employee assignemnt data
  public saveEmployeeAssignemntData(data){
    return this.http.post(`${this.baseUrl}/hrCrEmpAssgnmnt/create`,data);
  }

   //save employee assignemnt data
  //  public saveEmployeeAssignemntData2(data){
  //   return this.http.post(`${baseUrl}/hrCrEmpAssgnmnt/save`,data);
  // }

  //update employeeAssignment
  public updateEmployeeAssignment(data){
    return this.http.put(`${this.baseUrl}/hrCrEmpAssgnmnt/edit`,data);
  }

  // get last assignment by HrCrEmpId
  public getLastAssignmentByHrCrEmpId(id){
    return this.http.get(`${this.baseUrl}/hrCrEmpAssgnmnt/getByHrCrEmp/${id}`);
  }

  // get employee assignment By Id
  public getEmployeeAssignmentByHrCrEmpId(id){
    return this.http.get(`${this.baseUrl}/hrCrEmpAssgnmnt/getByHrCrEmpId/${id}`);
  }


  //save or update emp bank and payroll tabs data
  public saveOrUpdateBankAndPayroll(data){
    return this.http.post(`${this.baseUrl}/hrCrEmpAssgnmnt/saveBankAndPayroll`,data);
  }

  // get designation
  public getDesignations(){
    return this.http.get(`${this.baseUrl}/designation/getAll`);
  }

  public getAllDistricts(){
    return this.http.get(`${this.baseUrl}/address/getDistrictsLoader`);
  }


  public getALLDivisions(id){
    return this.http.get(`${this.baseUrl}/address/division/${id}`);
  }

  public fetchAllDivision(){
    return this.http.get(`${this.baseUrl}/address/division/getAll`);
  }

  public getDistrictByDivId(id){
    return this.http.get(`${this.baseUrl}/address/division/${id}`);
  }


  public getAllDistrict(apiURL, queryParams){
    console.log("@getAllDistrict");
    return this.http.get(apiURL, {params: queryParams}).pipe( retry(3));
  }

  public getAllUpazila(apiURL, queryParams){
    console.log("@getAllUpazila");
    return this.http.get(apiURL, {params: queryParams}).pipe( retry(3));
  }

  public getAllUnions(apiURL, queryParams){
    console.log("@sendGetRequest");
    return this.http.get(apiURL, {params: queryParams}).pipe( retry(3));
  }

  // save hrCrEmpEdu informations
  public saveHrCrEmpEdu(data) {
    return this.http.post(`${this.baseUrl}/hrCrEmpEdu/create`,data);
  }

// // save hrCrEmpExp informations
// public saveHrCrEmpExp() {
//   return this.http.post(`${this.baseUrl}/hrCrEmpPexp/create`);
// }

  //find by empId
  public findhrCrEmpEduByEmpId(empId){
    return this.http.get(`${this.baseUrl}/hrCrEmpEdu/find/${empId}`);
  }

//find previous experience by empId
public findhrCrEmpPreExpByEmpId(empId){
  return this.http.get(`${this.baseUrl}/hrCrEmpPexp/find/${empId}`);
}

//find previous experience by empId
public findhrCrEmpNomineeEmpId(empId){
  return this.http.get(`${this.baseUrl}/hrCrEmpNominee/find/${empId}`);
}

//find previous experience by empId
public findhrCrEmpTrainingEmpId(empId){
  return this.http.get(`${this.baseUrl}/hrCrEmpTraining/find/${empId}`);
}

  //find byId
  public findhrCrEmpEduById(id){
    return this.http.get(`${this.baseUrl}/hrCrEmpEdu/get/${id}`);
  }

//find byId
  public findhrCrEmpNomineeById(id){
    return this.http.get(`${this.baseUrl}/hrCrEmpNominee/get/${id}`);
  }

  //edit employee education
  public edithrCrEmpEducation(data){
    return this.http.put(`${this.baseUrl}/hrCrEmpEdu/edit`, data);
  }

//edit employee education
public edithrCrEmpNominee(data){
  return this.http.put(`${this.baseUrl}/hrCrEmpNominee/edit`, data);
}

  //delete emp education
  public deleteHrCrEmpEducation(id){
    return this.http.delete(`${this.baseUrl}/hrCrEmpEdu/delete/${id}`);
  }

//delete emp experience
public deleteHrCrEmpExp(id){
  return this.http.delete(`${this.baseUrl}/hrCrEmpPexp/delete/${id}`);
}

//delete emp nominee
public deleteHrCrEmpNominee(id){
  return this.http.delete(`${this.baseUrl}/hrCrEmpNominee/delete/${id}`);
}

//delete emp tranining
public deleteHrCrEmpTraining(id){
  return this.http.delete(`${this.baseUrl}/hrCrEmpTraining/delete/${id}`);
}


    //Get Raw Attendance data from Device

    public getAllRawAttendanceData()
    {
      return this.http.get(`${this.baseUrl}/attn/findAllBySrcType`);
    }

    public getAllRawAttendanceData2(apiURL, queryParams)
    {
      return this.http.get(apiURL, {params: queryParams}).pipe( retry(3));
    }


    //Post viaHrAttn
    public createAttnViaHr (data){
      return this.http.post(`${this.baseUrl}/AttnViaHr/save`, data);
    }
    //Get viaHrAttn Attendance data from

    public getAllViaHrAttnData()
    {
      return this.http.get(`${this.baseUrl}/AttnViaHr/findAllBySrcType`);
    }

    public getAllViaHrAttnData2(apiURL, queryParams)
    {
      return this.http.get(apiURL, {params: queryParams}).pipe( retry(3));
    }

    public getSearchAttn(apiURL, queryParams)
    {
      return this.http.get(apiURL, {params: queryParams}).pipe( retry(3));
    }


    //Leave related api

    //crearte leave tenc
    public createLeave (data){
      return this.http.post(`${this.baseUrl}/leaveTrnse/save`, data);
    }

 //find by Attndance empId
 public findAttenDataByEmpId(hrCrEmpId){
  return this.http.get(`${this.baseUrl}/attnReport/attnDataTest/${hrCrEmpId}`);
}


}
