import { Component, OnInit, OnDestroy} from "@angular/core";
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { first } from "rxjs/operators";
import { Role } from "src/app/login/models/role";
import { User } from "src/app/login/models/user";
import { environment } from "src/environments/environment";
import { SysResourceAuth } from "../../models/SysResourceAuth";
import { SysResourceDefinition } from "../../models/SysResourceDefinition";
import { SystemService } from "../../services/system.service";

declare const $: any;
@Component({
  selector: "app-list-sys-res-def",
  templateUrl: "./list-sys-res-def.component.html",
  styleUrls: ["./list-sys-res-def.component.css"],
})
export class ListSysResDefComponent implements OnInit, OnDestroy {

  public baseUrl = environment.baseUrl;
  public listData: SysResourceDefinition[] = [];
  public listAuthData: SysResourceAuth[] = [];
  public listUserData: User[] = [];
  public listRolesData: Role[] = [];
  public configPgn: any;
  public myGroup: FormGroup;
  public tempId: any;
  public authTempId:any;
  public editId: any;
  public editUsername:any;
  public form: FormGroup;
  public resAuthId: any;
  public tempClose: any;
  //using for form submit
  loading = false;
  submitted = false;

  public configDDL: any;

   // search fields
   private entityName: string;
   private backendUrl: string;
   private resourceType: string;


  constructor(
    private spinnerService: NgxSpinnerService,
    private systemService: SystemService,
    private toastr: ToastrService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.configPgn = {
      pageNum: 1,
      pageSize: 25,
      pageSizes: [3, 5, 10, 25, 50, 100, 200, 500, 1000],
      totalItem: 50,
      pngDiplayLastSeq: 10,
      entityName: "",
    };

    this.initConfigDDL();
    this.customInitLoadData();
  }

  ngOnInit(): void {

    this.myGroup = new FormGroup({
      pageSize: new FormControl(),
    });
    this.myGroup.get("pageSize").setValue(this.configPgn.pageSize);

    // bind event & action
    this._bindFromFloatingLabel();

    // get List Data
    this.getListData();
    this.initializeForm();
    $('body').addClass('mini-sidebar');

  }

  initializeForm() {
    this.form = this.formBuilder.group({
      id:[""],
      systemResource: [""],
      systemResourceName: [""],
      createAuth: [""],
      readAuth: [""],
      updateAuth: [""],
      deleteAuth: [""],
      queryAuth: [""],
      submitAuth: [""],
      crudqsString: ["",],
      othersString: [""],
      fullPrivilegeString: ["",[Validators.required]],
      visibleToAll: [""],
      username: {},
      role: [""],
    });

  }

  get f() {
    return this.form.controls;
  }

  formSubmit() {

    this.submitted = true;

    // stop here if form is invalid
    if (this.form.invalid) {
      return;
    }
    this.loading = true;
    let editId =this.form.get("id").value?this.form.get("id").value:null;
    if (editId ==null) {
      this.createSysResAuth();
    } else {
      this.updateSysResAuth();
    }

  }

  createSysResAuth(){

    let sysResourceAuth: SysResourceAuth = Object.assign(this.form.value, {
      systemResource: this.resAuthId ? { id: this.resAuthId } : null,
      role: this.getRole.value ? { id: this.getRole.value } : null,
    });
    this.systemService.createSysResAuth(sysResourceAuth).subscribe(
      (data: any) => {
        this.loading=false;
        this.getAuthData(this.resAuthId);
        this.toastr.success("Successfully created");
      },
      (error) => {
        this.toastr.info(error.error.message);
      }
    );
  }

  updateSysResAuth(){

    let sysResourceAuth: SysResourceAuth = Object.assign(this.form.value, {
      systemResource: this.resAuthId ? { id: this.resAuthId } : null,
      role: this.getRole.value ? { id: this.getRole.value } : null,
    });

    this.systemService.updateSysResAuth(sysResourceAuth).subscribe(
      (data: any) => {
        this.editId = null;
        this.loading=false;
        this.resetTheForm();
        this.getAuthData(this.resAuthId);
        this.toastr.success("Successfully updated");
      },
      (error) => {
        this.toastr.info(error.error.message);
      }
    );

  }

  editSysResAuthClickEvent(id) {

    this.systemService
      .getSysResAuthByIds(id)
      .pipe(first())
      .subscribe((data: any) => {

        this.listUserData = data.refFields.username;
        this.form.patchValue(data);

      });

  }

  private getListData() {

    let queryParams: any = {};
    const params = this._getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
    queryParams = params;

    console.log(queryParams);

    this.spinnerService.show();
    this.systemService.getSysResDef(queryParams).subscribe(
      (response: any) => {
        this.listData = response.objectList;
        this.configPgn.totalItem = response.totalItems;
        this.setDisplayLastSequence();
        this.spinnerService.hide();
      },
      (error) => {
        console.log(error);
      }
    );
  }


  actionTableLine(event:any, thisResObj){

    var targetEl = event.target;
    console.log(targetEl);
    $( '#genListTable tr' ).removeClass('selected');
    $(targetEl).closest('tr').addClass('selected');

    $('#authResourcesTxt').text( thisResObj.entityName + ", Backend URL: " + thisResObj.backendUrl );
    if( $('#authResourcesTxt').length == 0){
      setTimeout(() => {
        $('#authResourcesTxt').text( thisResObj.entityName + ", Backend URL: " + thisResObj.backendUrl );
      }, 500);
    }

    $('.formTitleCt').css({
      'font-size': 'medium',
      'display': 'inline-block',
    });

  }

  //provide permission function
  getAuthData(id) {

    this.resAuthId = id;
    this.tempClose = 1;

    this.systemService.getSysResAuthById(id).subscribe(
      (data: any) => {
        this.getRoles();
        this.listAuthData = data;
      },
      (error) => {
        this.toastr.error("error in fetching SysResAuth data");
      }
    );
  }



  getRoles() {
    this.systemService.getRoles().subscribe((data: any) => {
      this.listRolesData = data;
    });
  }




  setDisplayLastSequence() {
    this.configPgn.pngDiplayLastSeq =
      (this.configPgn.pageNum - 1) * this.configPgn.pageSize +
      this.configPgn.pageSize;
    if (this.listData.length < this.configPgn.pageSize) {
      this.configPgn.pngDiplayLastSeq =
        (this.configPgn.pageNum - 1) * this.configPgn.pageSize +
        this.configPgn.pageSize;
    }
    if (this.configPgn.totalItem < this.configPgn.pngDiplayLastSeq) {
      this.configPgn.pngDiplayLastSeq = this.configPgn.totalItem;
    }
  }

  handlePageChange(event: number) {
    this.configPgn.pageNum = event;
    this.getListData();
  }
  handlePageSizeChange(event: any): void {
    this.configPgn.pageSize = event.target.value;
    this.configPgn.pageNum = 1;
    this.getListData();
  }
  close() {
    this.tempClose = null;
    this.listAuthData = null;
  }

  //search
  searchEntity(paramName, entityType) {
    //  alert(paramName);
    //  alert (entityType);

    if(entityType=='entityName'){
    this.entityName = paramName;
    }

    if(entityType=='backendUrl'){
      this.backendUrl = paramName;
    }

    if(entityType=='resourceType'){
      this.resourceType = paramName;
    }

  }

  private _getUserQueryParams(page: number, pageSize: number): any {

    let params: any = {};

    if (page) {
      params[`pageNum`] = page - 0;
    }
    if (pageSize) {
      params[`pageSize`] = pageSize;
    }

    // push other attributes
    if(this.entityName){
      params[`entityName`] = this.entityName;
    }
    if(this.backendUrl){
      params[`backendUrl`] = this.backendUrl;
    }
    if(this.resourceType){
      params[`resourceType`] = this.resourceType;
    }


    return params;

  }

  public _getSearchData() {
    this.getListData();
  }

  private _bindFromFloatingLabel(){

    var self = this;

    // for floating label
    if ($(".floating").length > 0) {
      $(".floating")
        .on("focus blur", function (e) {
          $(this)
            .parents(".form-focus")
            .toggleClass("focused", e.type === "focus" || this.value.length > 0);
        })
        .trigger("blur");
    }

    $('.filter-row').find('input, select, textarea').keyup(function(e){

      console.log(e.keyCode)
      if(e.keyCode == 13){
        self._getSearchData();
      }

    });

  }




  deleteSysResDef() {
    this.systemService.deleteSysResDef(this.tempId).subscribe(
      () => {
        $("#delete_sysResDef").modal("hide");
        this.toastr.success("Successfully deleted");
        this.listData = this.listData.filter((d) => d.id != this.tempId);
      },
      (error) => {
        this.toastr.error("error in deleting data");
      }
    );
  }

  deleteSysResAuth(){
    this.systemService.deleteSysResAuth(this.authTempId).subscribe(
      () => {
        $("#delete_sysResAuth").modal("hide");
        this.toastr.success("Successfully deleted");
        this.listData = this.listData.filter((d) => d.id != this.tempId);
      },
      (error) => {
        this.toastr.error("error in deleting data");
      }
    );
  }

  //catching form data change
  get getRole() {
    return this.form.get("role");
  }

  resetTheForm(): void {
    this.form.reset();
  }




  // --------------------------- DDL (Dinamic Dropdown List) Methods Start -----------------------------------
  searchDDL(event: any) {

    let q = event.term;
    this.configDDL.q = q;
    this.configDDL.pageNum = 1;
    this.configDDL.append = false;
    this.getListDataDDL();

  }

  scrollToEndDDL() {

    this.configDDL.pageNum++;
    this.configDDL.append = true;
    this.getListDataDDL();

  }

  clearDDL(){
    this.configDDL.q = "";
  }

  private getListDataDDL() {

    let apiURL = this.baseUrl + this.configDDL.dataGetApiPath;

    let queryParams: any = {};
    queryParams.pageNum = this.configDDL.pageNum;
    queryParams.pageSize = this.configDDL.pageSize;
    if(this.configDDL.q && this.configDDL.q != null){
      queryParams[this.configDDL.apiQueryFieldName] = this.configDDL.q;
    }

    this.systemService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {

        if(this.configDDL.append){
          this.configDDL.listData = this.configDDL.listData.concat(response.objectList);
        } else {
          this.configDDL.listData = response.objectList;
        }
        this.configDDL.totalItem = response.totalItems;

      },
      (error) => {
        console.log(error)
      }
    );

  }


  setDefaultParamsDDL(){
    this.initConfigDDL();
  }

  customInitLoadData(){
    this.configDDL.activeFieldName = "ddlUsername";
    this.configDDL.dataGetApiPath = "/api/common/getUser";
    this.configDDL.apiQueryFieldName = "username";
    this.getListDataDDL();
  }

  initConfigDDL(){

    this.configDDL = {
      pageNum: 1,
      pageSize: 10,
      totalItem: 50,
      listData: [],
      append: false,
      q: "",
      activeFieldName: "xxxFieldName",
      dataGetApiPath: '',
      apiQueryFieldName: 'xxxFieldName'
    };

  }

  initSysParamsDDL(event, activeFieldNameDDL, dataGetApiPathDDL, apiQueryFieldNameDDL){
    console.log("...");
    console.log("ddlActiveFieldName:" + activeFieldNameDDL);
    console.log("dataGetApiPathDDL:" + dataGetApiPathDDL);
    console.log(event.target);

    if(this.configDDL.activeFieldName && this.configDDL.activeFieldName != activeFieldNameDDL){
      this.setDefaultParamsDDL();
    }

    this.configDDL.activeFieldName = activeFieldNameDDL;
    this.configDDL.dataGetApiPath = dataGetApiPathDDL;
    this.configDDL.apiQueryFieldName = apiQueryFieldNameDDL;
    this.getListDataDDL();
  }
  // --------------------------- DDL (Dinamic Dropdown List) Methods End -------------------------------------


  ngOnDestroy(): void {
    $('body').removeClass('mini-sidebar');
  }

}
