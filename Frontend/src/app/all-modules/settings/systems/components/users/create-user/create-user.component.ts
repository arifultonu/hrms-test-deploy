import { Component, OnInit } from '@angular/core';
import { AbstractControlOptions, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AllModulesService } from 'src/app/all-modules/all-modules.service';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { SimService } from 'src/app/all-modules/sim/services/sim.service';
import { User } from 'src/app/login/models/user';
import { LoginService } from 'src/app/login/services/login.service';
import { MustMatch } from 'src/app/utils/_helpers';
import { environment } from 'src/environments/environment';
import { SystemService } from '../../../services/system.service';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent implements OnInit {

  baseUrl = environment.baseUrl;
  form: FormGroup;
  isSubmitted = false;
  readMode = false;
  editmode = false;
  formMode = "create";
  currentId: string;
  endsubs$: Subject<any> = new Subject();
  myFormData: any = {};
  public groupUserListData: User[] = [];
  public isGroupUser: boolean = true;



  constructor(
    private allModuleService: AllModulesService,
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private systemService: SystemService,
    private loginService: LoginService,
    private router: Router,
    private route: ActivatedRoute,
  ) { }

  ngOnInit(): void {

    this._getGroupUsers();
    this._getRoles();
    this._checkEditMode();
    this._initForm();
    this._getFormMode();
  }

  _initForm() {

    const formOptions: AbstractControlOptions = { validators: MustMatch('password', 'confirmPassword') };
    this.form = this.formBuilder.group({
      id: [""],
      username: ["", [Validators.required]],
      email: ["", [Validators.required]],
      groupUser: [""],
      userTitle: [""],
      groupUsername: [""],
      enabled: [""],
      accountExpired: [""],
      accountLocked: [""],
      passwordExpired: [""],
      password: ["", [Validators.required]],
      confirmPassword: ['', Validators.required],
      roles:[],
    }, formOptions);
  }

  onSubmit(){

    this.isSubmitted = true;
    if (this.form.invalid) {
      return;
    }

    const obj: User = Object.assign(this.form.value);

    if(this.editmode){
     this._updateUser(obj);
    }else{
      this._createUser(obj);
    }

  }

  _createUser(obj: User) {
    const apiURL = this.baseUrl + "/user/register";
    this.loginService
      .sendPostRequest(apiURL, obj)
      .pipe(takeUntil(this.endsubs$))
      .subscribe(
        (user: User) => {
          this.toastr.success("Created successfully");
          this.router.navigate(["/settings/system-user/lists"]);
        },
        () => {
          this.toastr.error("Error");
        }
      );
  }
  _updateUser(obj: User) {
    const apiURL = this.baseUrl + "/user/update";
    this.loginService
      .sendPutRequest(apiURL, obj)
      .pipe(takeUntil(this.endsubs$))
      .subscribe(
        (user: User) => {
          this.toastr.success("Updated successfully");
          this.router.navigate(["/settings/system-user/lists"]);
        },
        () => {
          this.toastr.error("Error");
        }
      );

  }


  _getGroupUsers() {
    this.systemService.getGroupUser().subscribe((data: any) => {
      this.groupUserListData = data;
    }, (error) => {
      this.toastr.error("error");
    });
  }

  _getRoles(){
    this.systemService.getRoles().subscribe((data: any) => {
      this.myFormData.roles = data;
      console.log(this.myFormData.roles);
    }, (error) => {
      this.toastr.error("error");
    });
  }

  _getFormMode() {
    let currURL = this.router.url;

    this.formMode = "create";
    if (currURL.includes("/edit/")) {
      this.formMode = "edit";
    } else if (currURL.includes("/show/")) {
      this.formMode = "read";
    }
    console.log(currURL);
    console.log(this.formMode);
    return this.formMode;
  }

  _checkEditMode() {
    const apiURL = environment.baseUrl + "/user/get";
    this.route.params.pipe(takeUntil(this.endsubs$)).subscribe((params) => {
      if (params.id) {
        this.editmode = true;
        this.currentId = params.id;
        this.systemService
          .sendGetRequestById(apiURL, params.id)
          .pipe(takeUntil(this.endsubs$))
          .subscribe((response: any) => {
            this.myFormData = response;
            this.formControls.id.setValue(response.id);
            this.formControls.username.setValue(response.username);
            this.formControls.email.setValue(response.email);
            this.formControls.groupUser.setValue(response.groupUser);
            this.formControls.userTitle.setValue(response.userTitle);
            this.formControls.groupUsername.setValue(response.groupUsername);
            this.formControls.password.setValue(response.password);
            this.formControls.confirmPassword.setValue(response.password);
            this.formControls.enabled.setValue(response.enabled);
            this.formControls.accountExpired.setValue(response.accountExpired);
            this.formControls.accountLocked.setValue(response.accountLocked);
            this.formControls.passwordExpired.setValue(response.passwordExpired);
            this.formControls.roles.setValue(response.roles);



            // disable form fields in readonly mode

            if (this._getFormMode() == "read") {

              $("#formERP").find("input").attr("readonly", 1);
              $("#formERP").find("select").attr("readonly", 1);
              $("#formERP").find("select").attr("disabled", "disabled");
              $("#formERP").find("textarea").attr("readonly", 1);
              $("#groupUserId").attr("disabled", 1);
              $("#enabledId").attr("disabled", 1);
              $("#roleId").attr("disabled", 1);
              $("#formERP")
                .find("div.ng-select-container")
                .attr("disabled", "disabled");

              $("#formERP").find("div.ng-select-container").css({
                "pointer-events": "none",
                "cursor": "none",
                "background-color": "#e9ecef",
              });
              $("#formERP").find("button").attr("hidden", 1);

              // input field border none
              $("#formERP").find("input").css({
                "border": "0",

              });
              $("#formERP").find("select").css({
                "border": "0",

              });
              $("#formERP").find("textarea").css({
                "border": "0",

              });
              $("#formERP").find("div.ng-select-container").css({
                "border": "0",

              });


            }
          });
      }

    });
  }

  check(event, value) {
    // DOM Query Selector JQUERY
    if(value == "groupUser"){
      let isChecked =  $('#groupUserId').prop('checked');
      if(isChecked){
        this.isGroupUser = false;
      }else{
        this.isGroupUser = true;
      }
    }
  }


  get formControls() {
    return this.form.controls;
  }

  resetFormValues(){
    this.form.reset();
  }


}
