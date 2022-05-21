import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReportsService {

  baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) { }

  //test reports
  public getTestReports(){
    return this.http.get(`${this.baseUrl}/reports/myAttendanceRpt2`, {  responseType: 'blob' });
  }


  public employeeAttendanceRpt(rptFileName, userId, startDate, endDate){

    const httpOptions = {
      responseType: 'arraybuffer' as 'json'
    };

    let param: any = {'rptFileName': rptFileName, 'userId': userId, 'startDate': startDate, 'endDate':endDate};
    // return this.http.get(`${baseUrl}/reports/myAttendanceRpt`,{params: param, responseType:'blob'});
    // let queryString = "rptFileName=" + rptFileName + "&userId="+userId +"&startDate"+startDate+"&endDate="+endDate+"&outputFileName="+rptFileName;
    return this.http.get(`${this.baseUrl}/reports/myAttendanceRpt?rptFileName=${rptFileName}&userId=${userId}&startDate=${startDate}&endDate=${endDate}&outputFileName=${rptFileName}`,httpOptions);

  }

  public payslipRpt(rptFileName, payslipId){

    const httpOptions = {
      responseType: 'arraybuffer' as 'json'
    };

    let param: any = {'rptFileName': rptFileName, 'payslipId': payslipId};
    return this.http.get(`${this.baseUrl}/reports/payslipReport?rptFileName=${rptFileName}&payslipId=${payslipId}&outputFileName=${rptFileName}`,httpOptions);

  }


  public birthdayRpt(rptParams){

    const httpOptions = {
      responseType: 'arraybuffer' as 'json'
    };

    let param: any = rptParams;
    return this.http.get(`${this.baseUrl}/reports/birthdayReport?rptFileName=${param.rptFileName}&start_date=${param.start_date}&end_date=${param.end_date}&outputFileName=${param.rptFileName}`, httpOptions);

  }

  public irrattnRpt(rptParams){

    const httpOptions = {
      responseType: 'arraybuffer' as 'json'
    };

    let param: any = rptParams;
    return this.http.get(`${this.baseUrl}/reports/empIrregularAttn?rptFileName=${param.rptFileName}&From_Date=${param.From_Date}&To_Date=${param.To_Date}&Emp_Code=${param.Emp_Code}&outputFileName=${param.rptFileName}`, httpOptions);

  }


  public emptopsheet(rptParams){

    const httpOptions = {
      responseType: 'arraybuffer' as 'json'
    };

    let param: any = rptParams;
    return this.http.get(`${this.baseUrl}/reports/empTopSheet?rptFileName=${param.rptFileName}&outputFileName=${param.rptFileName}`, httpOptions);

  }

  public EmpMonthlyattnRpt(rptParams){

    const httpOptions = {
      responseType: 'arraybuffer' as 'json'
    };

    let param: any = rptParams;
    return this.http.get(`${this.baseUrl}/reports/empMonAttn?rptFileName=${param.rptFileName}&From_Date=${param.From_Date}&To_Date=${param.To_Date}&Emp_Code=${param.Emp_Code}&outputFileName=${param.rptFileName}`, httpOptions);

  }

  public EmpSalaryRpt(rptParams){

    const httpOptions = {
      responseType: 'arraybuffer' as 'json'
    };

    let param: any = rptParams;
    return this.http.get(`${this.baseUrl}/reports/empSalRpt?rptFileName=${param.rptFileName}&EMPLOYEE_ID=${param.EMPLOYEE_ID}&outputFileName=${param.rptFileName}`, httpOptions);

  }

  public empleaverpt(rptParams){

    const httpOptions = {
      responseType: 'arraybuffer' as 'json'
    };

    let param: any = rptParams;
    return this.http.get(`${this.baseUrl}/reports/empleaverpt?rptFileName=${param.rptFileName}&outputFileName=${param.rptFileName}`, httpOptions);

  }

  public EmpProfileRpt(rptParams){

    const httpOptions = {
      responseType: 'arraybuffer' as 'json'
    };

    let param: any = rptParams;
    return this.http.get(`${this.baseUrl}/reports/empProRpt?rptFileName=${param.rptFileName}&Emp_Code=${param.Emp_Code}&Photo_Path=${param.Photo_Path}&outputFileName=${param.rptFileName}`, httpOptions);

  }



}
