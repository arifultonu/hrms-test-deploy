import { Component, OnInit, OnDestroy, ViewChild } from "@angular/core";
import {
  FormBuilder,
  FormGroup,
  FormControl,
  Validators,
  AbstractControlOptions,
} from "@angular/forms";
import { ToastrService } from "ngx-toastr";
import { Subject } from "rxjs";
import { DataTableDirective } from "angular-datatables";
import { AllModulesService } from "src/app/all-modules/all-modules.service";
import { SystemService } from "../../../services/system.service";
import { MustMatch } from "src/app/utils/_helpers";
import { LoginService } from "src/app/login/services/login.service";
import { ActivatedRoute, Router } from "@angular/router";
import { User } from "src/app/login/models/user";

declare const $: any;
@Component({
  selector: 'app-show-users',
  templateUrl: './show-users.component.html',
  styleUrls: ['./show-users.component.css']
})
export class ShowUsersComponent implements OnInit {
  @ViewChild(DataTableDirective, { static: false })
  public dtElement: DataTableDirective;
  public dtOptions: DataTables.Settings = {};
  public dtTrigger: Subject<any> = new Subject();
  public url: any = "leaveType";

  public allLeaveType: any = [];
  public allUsers: any = [];
  public groupUser:User []=[];
  public isGroupUser:boolean = true;


  public addLeaveType: FormGroup;
  public addUserFormGroup:FormGroup;


  public editLeaveType: FormGroup;
  public editId: any;
  public tempId: any;
  constructor(
    private allModuleService: AllModulesService,
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private systemService: SystemService,
    private loginService: LoginService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  ngOnInit() {
    this.initializeForm();
   // this.getLeaveType();
    this.getUsers();
    this.getGroupUser();
    // for data table configuration
    this.dtOptions = {
      // ... skipped ...
      pageLength: 10,
      dom: "lrtip",
    };

    // Add Provident Form Validation And Getting Values

    this.addLeaveType = this.formBuilder.group({
      addLeaveType: ["", [Validators.required]],
      addLeaveDays: ["", [Validators.required]],
    });

    // Edit Provident Form Validation And Getting Values

    this.editLeaveType = this.formBuilder.group({
      editLeave: ["", [Validators.required]],
      editLeaveDays: ["", [Validators.required]],
    });
  }

  initializeForm(){

    const formOptions: AbstractControlOptions = { validators: MustMatch('password', 'confirmPassword') };
    
    this.addUserFormGroup = this.formBuilder.group({
      username: ["", [Validators.required]],
      email: ["", [Validators.required]],
      groupUser:[""],
      userTitle:[""],
      groupUsername:[""],
      password: ["", [Validators.required]],
      confirmPassword: ['', Validators.required],
    }, formOptions);
  }
  getUsers(){
    this.systemService.getAllUsers().subscribe((data) => {
      console.log(data);
      
      this.allUsers=data;
      this.dtTrigger.next();
    });
  }

  // add user 
  addUser(){
    if (this.addUserFormGroup.invalid) {
      this.toastr.warning("invalid form data");
      return;
    }

    if(this.addUserFormGroup.valid){
      let obj: User = Object.assign(this.addUserFormGroup.value);
      this.loginService.register(obj).subscribe((data)=>{
        this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
          dtInstance.destroy();
          this.getUsers();
          $("#add_user").modal("hide");
          this.toastr.success("System User created successfully", "Success");
        });
      },(error)=>{this.toastr.error("error "+error.error.message, "error");});
      $("#add_user").modal("hide");
      this.addUserFormGroup.reset();
    }

  }

  //get group users 
  getGroupUser(){
    this.systemService.getGroupUser().subscribe((data:any) =>{
      this.groupUser = data;
    },(error)=>{this.toastr.error("error")});
  }

  selectIsGroupUser(){

    this.isGroupUser=false;

  }

  // Add Provident Modal Api Call

  addLeave() {
    if (this.addLeaveType.valid) {
      let obj = {
        leaveType: this.addLeaveType.value.addLeaveType,
        leaveDays: this.addLeaveType.value.addLeaveDays,
      };
      this.allModuleService.add(obj, this.url).subscribe((data) => {
        this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
          dtInstance.destroy();
        });
      });
    // this.getLeaveType();
      $("#add_leavetype").modal("hide");
      this.addLeaveType.reset();
      this.toastr.success("Leave type is added", "Success");
    }
  }

  // Edit Provident Modal Api Call

  editLeave() {
    let obj = {
      leaveType: this.editLeaveType.value.editLeave,
      leaveDays: this.editLeaveType.value.editLeaveDays,
      id: this.editId,
    };
    this.allModuleService.update(obj, this.url).subscribe((data1) => {
      this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
        dtInstance.destroy();
      });
    });
  //  this.getLeaveType();
    $("#edit_leavetype").modal("hide");
    this.toastr.success("Leave type is edited", "Success");
  }

  edit(value) {
    this.editId = value;
    const index = this.allLeaveType.findIndex((item) => {
      return item.id === value;
    });
    let toSetValues = this.allLeaveType[index];
    this.editLeaveType.setValue({
      editLeave: toSetValues.leaveType,
      editLeaveDays: toSetValues.leaveDays,
    });
  }

  // Delete Provident Modal Api Call

  deleteLeave() {
    this.allModuleService.delete(this.tempId, this.url).subscribe((data) => {
      this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
        dtInstance.destroy();
      });
   //   this.getLeaveType();
      $("#delete_leavetype").modal("hide");
      this.toastr.success("Leave type is deleted", "Success");
    });
  }
  // for unsubscribe datatable
  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event
    this.dtTrigger.unsubscribe();
  }
}
function first(): import("rxjs").OperatorFunction<Object, unknown> {
  throw new Error("Function not implemented.");
}

