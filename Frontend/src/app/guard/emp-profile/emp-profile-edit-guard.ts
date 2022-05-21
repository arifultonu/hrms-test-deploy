import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { EmployeeService } from 'src/app/all-modules/employees/services/employee.service';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})

export class EmpProfileEditGuard implements CanActivate{
  response:boolean = false;
  constructor(private employeeService: EmployeeService,private router:Router){

  }


  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {



    const resCode = "EMP_REF";
    const apiURL = environment.baseUrl + "/getFormSectionsAuth" + "/" + resCode;



    this.employeeService.sendGetRequest(apiURL, {}).subscribe((resData) => {

      if(resData['EMP_REF_ACTION_EDIT']==true){
        console.log("ON API CALL "+"true");
        this.response=true;
      }
    });


    if(this.response==true){
      console.log("Inside IF :: "+this.response)
      return true;
    }

    this.router.navigate(['error/error403']);

    return false;
  }

}
