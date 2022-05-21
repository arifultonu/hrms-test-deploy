
import { HttpClient } from "@angular/common/http";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { DomSanitizer } from "@angular/platform-browser";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { environment } from "src/environments/environment";
import { HrCrEmp } from "../../../model/HrCrEmp";
import { HrCrEmpEdu } from "../../../model/HrCrEmpEdu";
import { EmployeeService } from "../../../services/employee.service";



declare const $: any;
@Component({
  selector: "app-employee-profile",
  templateUrl: "./employee-profile.component.html",
  styleUrls: ["./employee-profile.component.css"],
})
export class EmployeeProfileComponent implements OnInit {
  public addEmployeeForm: FormGroup;
  id!: number;
  public baseUrl = environment.baseUrl;
  public empProfile: HrCrEmp;
  uploadForm: FormGroup;
  public configPgn: any;
  activeTabCnf: any;
  public myFromGroup: FormGroup;
  public hrCrEmpPreExpInfo:any;
  public hrCrEmpNomineeInfo:any;
  public hrCrEmpTrainingInfo:any;

  imageSrc: any;
  profileImageUrl!: any;

  empBnkAndPrlData: any = [];
  empProfileData: any = [];
  empPrAssignmentData: any = [];
  empPrAssignmentLogData: any = [];

  public hrCrEmpEduInfo: HrCrEmpEdu[] = [];

  public attEmp: any  = [];

  //documents upload
  formData: FormGroup;
  fileToUpload1: File;
  // fileToUpload2: File;

  //document path
  cvDocumentPath: any = "NOT_AVAILABLE";
  passportDocumentPath: any = "NOT_AVAILABLE";
  nidDocumentPath: any = "NOT_AVAILABLE";





  constructor(
    private toastr: ToastrService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private employeeService: EmployeeService,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private httpClient: HttpClient,
    private domSanitizer: DomSanitizer
  ) {

    this.configPgn = {
      // my props
      pageNum: 1,
      pageSize: 10,
      totalItem: 50,
      pageSizes: [3, 5, 10, 25, 50, 100, 200, 500, 1000],
      pgnDiplayLastSeq: 10,
      // ngx plugin props
      itemsPerPage: 10,
      currentPage: 1,
      totalItems: 50
    };


    this.activeTabCnf = {
      education: "",
      assignment: "",
      payroll: "",
      basic: "active",
    };

    // localStorage.setItem('myData', jsonData);

    let activeTabName = localStorage.getItem("activeTabName");
    if (activeTabName == "assignment") {
      this.activeTabCnf.education = "";
      this.activeTabCnf.assignment = "active";
      this.activeTabCnf.payroll = "";
      this.activeTabCnf.basic = "";
    } else if (activeTabName == "payroll") {
      this.activeTabCnf.education = "";
      this.activeTabCnf.basic = "";
      this.activeTabCnf.assignment = "";
      this.activeTabCnf.payroll = "active";
    } else if (activeTabName == "basic") {
      this.activeTabCnf.education = "";
      this.activeTabCnf.assignment = "";
      this.activeTabCnf.payroll = "";
      this.activeTabCnf.basic = "active";
    } else if (activeTabName == "education") {
      this.activeTabCnf.assignment = "";
      this.activeTabCnf.payroll = "";
      this.activeTabCnf.basic = "";
      this.activeTabCnf.education = "active";
    }
  }

  ngOnInit() {
    this.route.params.subscribe((params: any) => {
      this.id = params.id;
      //  this.getEmployeeProfile(this.id);
    });

    this.uploadForm = this.formBuilder.group({
      profile: [""],
    });

    this.addEmployeeForm = this.formBuilder.group({
      client: ["", [Validators.required]],
    });

    this.formData = this.formBuilder.group({
      files: [],
      file_passport: [],
    });

    this._getEmpProfileTabData();
    this.getAttenData();
    this.getPreExpData();
    this.getNomineeData();
    this.getTrainingData();

    this.myFromGroup = new FormGroup({
      pageSize: new FormControl()
    });
    this.myFromGroup.get('pageSize').setValue(this.configPgn.pageSize);
  }

  ngAfterViewInit() {
    setTimeout(() => {
      this._chkFormSectionAuth();
    }, 300);
  }

  ngAfterViewChecked() {
    // this._chkFormSectionAuth();
  }

  onFileSelect(event) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      const reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]);
      reader.onload = (e) => (this.imageSrc = reader.result);
      this.uploadForm.get("profile").setValue(file);
    }
  }

  Submit() {
    const formData = new FormData();
    formData.append("file", this.uploadForm.get("profile").value);
    formData.append("type", "profile");
    this.employeeService.uploadProfileImage(this.id, formData).subscribe(
      (data) => {
        $("#profile_Image").modal("hide");
        this.router.navigate(["/employees/employeepage"], {
          relativeTo: this.route,
        });
        this.toastr.success("Successfully uploaded image");
      },
      (error) => {
        this.toastr.error("Error" + error.message);
      }
    );
  }

  //Get Employee profile
  getEmployeeProfile(id) {
    this.employeeService.findEmployeeById(id).subscribe((data: any) => {
      this.empProfile = data;
      console.log(
        "Data " + this.empProfile.primaryAssgnmnt?.hrEmpDesignations?.title
      );
    });
  }

  getEducationData() {
    if (this.id) {
      this.employeeService.findhrCrEmpEduByEmpId(this.id).subscribe(
        (data: any) => {
          this.hrCrEmpEduInfo = data;
        },
        (error) => {
          this.toastr.error(error.error.message);
        }
      );
    }
  }

  getAttenData() {

    if (this.id){
    let apiURL = this.baseUrl + "/attnReport/attnDataTest/"+ this.id;
    let queryParams: any = {};
    const params = this.getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
    queryParams = params;

    this.spinnerService.show();
    this.employeeService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.attEmp = response.objectList;


        this.configPgn.totalItem = response.totalItems;
        this.configPgn.totalItems = response.totalItems;
        this.setDisplayLastSequence();
        this.spinnerService.hide();
      },
      (error) => {
        console.log(error)
      }
    );
  }
  }

  getPreExpData(){

    if(this.id){
      this.employeeService.findhrCrEmpPreExpByEmpId(this.id).subscribe((data: any)=>{
        this.hrCrEmpPreExpInfo=data;
      },(error)=>{
        this.toastr.error("Error fetching data");
      });
    }


  }

  getNomineeData(){

    if(this.id){
      this.employeeService.findhrCrEmpNomineeEmpId(this.id).subscribe((data: any)=>{
        this.hrCrEmpNomineeInfo=data;
      },(error)=>{
        this.toastr.error("Error fetching data");
      });
    }


  }

  getTrainingData(){

    if(this.id){
      this.employeeService.findhrCrEmpTrainingEmpId(this.id).subscribe((data: any)=>{
        this.hrCrEmpTrainingInfo=data;
      },(error)=>{
        this.toastr.error("Error fetching data");
      });
    }
  }


  private getUserQueryParams(page: number, pageSize: number): any {

    let params: any = {};

    if (page) {
      params[`pageNum`] = page - 0;
    }
    if (pageSize) {
      params[`pageSize`] = pageSize;
    }
    return params;

  }



  onSubmit() {
    this.toastr.success("Bank & statutory added", "Success");
  }

  // tab click event handling

  _chkFormSectionAuth() {
    const resCode = "EMP_REF";
    const apiURL = environment.baseUrl + "/getFormSectionsAuth" + "/" + resCode;

    this.spinnerService.show();
    this.employeeService.sendGetRequest(apiURL, {}).subscribe((resData) => {
      this.spinnerService.hide();

      for (const item in resData) {
        const auth = resData[item];

        console.log(item);

        if (auth) {
          $("#formContainerId")
            .find("#" + item)
            .removeClass("d-none");
          $("#formContainerId")
            .find("#" + item)
            .css({
              display: "block",
            });

          $("#EDIT_ACTION_ID")
            .find("#" + item)
            .removeClass("d-none");

          $("#EDIT_ACTION_ID")
            .find("#" + item)
            .css({
              display: "block",
            });
        }
      }
    });
  }

  handleBankAndPayrollSectionData() {
    const apiURL =
      environment.baseUrl + "/employeeMasterView/bankAndPrl" + "/" + this.id;
    this.spinnerService.show();
    this.employeeService.sendGetRequest(apiURL, {}).subscribe(
      (resData) => {
        this.spinnerService.hide();
        this.empBnkAndPrlData = resData;
        // console.log(this.empBnkAndPrlData);
      },
      (error) => {
        this.toastr.error(error.error.message);
      }
    );
  }

  handlePrimaryAssignmentSectionData() {
    const apiURL =
      environment.baseUrl + "/employeeMasterView/prAssignment" + "/" + this.id;
    this.spinnerService.show();
    this.employeeService.sendGetRequest(apiURL, {}).subscribe(
      (resData) => {
        this._getPrimaryAssignmentLogData();
        this.spinnerService.hide();
        this.empPrAssignmentData = resData;
        console.log(this.empPrAssignmentData);
      },
      (error) => {
        this.toastr.error(error.error.message);
      }
    );
  }

  _getEmpProfileTabData() {
    const apiURL =
      environment.baseUrl + "/employeeMasterView/profile" + "/" + this.id;
    this.spinnerService.show();
    this.employeeService.sendGetRequest(apiURL, {}).subscribe(
      (resData) => {
        this.spinnerService.hide();
        this.empProfileData = resData;
        console.log(this.empProfileData);
      },
      (error) => {
        this.toastr.error(error.error.message);
      }
    );
  }

  _getPrimaryAssignmentLogData() {
    const apiURL =
      environment.baseUrl +
      "/employeeMasterView/prAssignmentLog" +
      "/" +
      this.id;
    this.spinnerService.show();
    this.employeeService.sendGetRequest(apiURL, {}).subscribe(
      (resData) => {
        this.spinnerService.hide();
        this.empPrAssignmentLogData = resData;
        console.log(this.empPrAssignmentLogData);
      },
      (error) => {
        this.toastr.error(error.error.message);
      }
    );
  }

  sendMessage() {
    this.toastr.info("This feature is not available in this version");
  }

  //documents upload
  // handleFileInput1(event) {
  //   this.fileToUpload1 = <File>event.target.files[0];
  // }

  // handleFileInput2(event) {
  //   this.fileToUpload2 = <File>event.target.files[0];
  // }

  documentsUpload_callback(type) {
    if (type == 'cv') {
      $('#cv_icon_id').removeClass('border-warning');
      $('#cv_icon_id').addClass('border-success');
    }
    if (type == 'passport') {
      $('#passport_icon_id').removeClass('border-warning');
      $('#passport_icon_id').addClass('border-success');
    }

  }

  documentsUpload(formData) {
    let url = this.baseUrl + "/multimedia/documents/" + this.id;
    this.httpClient.post(url, formData, { observe: "response" }).subscribe(
      (resp) => {
        console.log(resp.body);
        this.toastr.success("Successfully uploaded documents");
        this._getDocumentsData();
        //refreshing the data
        this.reset();
        this.documentsUpload_callback(formData.type);

      },
      (err) => {
        console.log(err);
      }
    );
  }

  uploadFiles(docType) {
    if (docType == "cv") {
      const formData: FormData = new FormData();
      const file = <File>$("#file_cv").get(0).files[0];
      formData.append("files", file, file.name);
      formData.append("type", "cv");
      console.log(file);
      this.documentsUpload(formData);
    }
    if (docType == "passport") {
      const formData: FormData = new FormData();
      const file = <File>$("#file_passport").get(0).files[0];
      formData.append("files", file, file.name);
      formData.append("type", "passport");
      console.log(file);
      this.documentsUpload(formData);
    }
    if (docType == "nid") {
      const formData: FormData = new FormData();
      const file = <File>$("#file_nid").get(0).files[0];
      formData.append("files", file, file.name);
      formData.append("type", "nid");
      console.log(file);
      this.documentsUpload(formData);
    }
  }

  _getDocumentsData() {
    let url = this.baseUrl + "/multimedia/getDocuments/" + this.id;
    let length = 0;
    this.employeeService.sendGetRequest(url, {}).subscribe(
      (resData) => {
        console.log("RES DATA : " + resData);
        this.spinnerService.hide();
        while (resData[length] != null) {
          console.log("Log : " + resData[length].documentType);

          if (resData[length].documentType == "cv") {
            this.cvDocumentPath =
              this.baseUrl + "/" + resData[length].documentPath;
            this.documentsUpload_callback('cv')
          }

          if (resData[length].documentType == "passport") {
            this.passportDocumentPath =
              this.baseUrl + "/" + resData[length].documentPath;
            this.documentsUpload_callback('passport');
          }
          if (resData[length].documentType == "nid") {
            this.nidDocumentPath = resData[length].documentPath;
          }

          length++;
        }

        console.log(
          "Log Data  :: " +
          " CV " + this.cvDocumentPath +
          " PASS " +
          this.passportDocumentPath +
          " " +
          this.nidDocumentPath
        );

        if (this.passportDocumentPath == "") {
          alert("Passport document is not available");
        }
      },
      (error) => {
        this.toastr.error(error.error.message);
      }
    );
  }

  resetFormValues() {
    this.formData.reset();
  }

  @ViewChild("documentsForm") documentsForm;
  reset() {
    this.documentsForm.nativeElement.reset();
  }

  getUrlForCV() {
    if (this.cvDocumentPath != "NOT_AVAILABLE") {
      return "url('/assets/img/docx/CV_ICON_UPLOADED_2.png')";
    } else {
      return "url('/assets/img/docx/CV_ICON_PENDING_2.png')";
    }

  }

  getUrlForPassport() {
    if (this.passportDocumentPath != "NOT_AVAILABLE") {
      return "url('/assets/img/docx/PASSPORT_ICON_UPLOADED.png')";
    } else {
      return "url('/assets/img/docx/PASSPORT_ICON_PENDING.png')";
    }
  }

  removeDocs(docType) {

    let url = this.baseUrl + "/multimedia/updateDocumentStatus/" + this.id + "/" + docType;
    this.employeeService.sendPutRequest(url, null).subscribe(
      (resData) => {

        if(docType == "cv"){
          this.cvDocumentPath = "NOT_AVAILABLE";
        }
        if(docType == "passport"){
          this.passportDocumentPath = "NOT_AVAILABLE";
        }
        $('#passport_icon_id').removeClass('border-success');
        $('#passport_icon_id').addClass('border-warning');
        this._getDocumentsData();
        this.toastr.success("Successfully removed  document");

      }, (error) => {
        this.toastr.error("Error while removing document : " + error);

      });


  }


// pagination handling methods start -----------------------------------------------------------------------
setDisplayLastSequence(){
  this.configPgn.pngDiplayLastSeq = (((this.configPgn.pageNum - 1 ) * this.configPgn.pageSize) + this.configPgn.pageSize);
  if(this.attEmp.length < this.configPgn.pageSize){
    this.configPgn.pngDiplayLastSeq = (((this.configPgn.pageNum - 1 ) * this.configPgn.pageSize) + this.configPgn.pageSize);
  }
  if(this.configPgn.totalItem < this.configPgn.pngDiplayLastSeq){
    this.configPgn.pngDiplayLastSeq = this.configPgn.totalItem;
  }
}
handlePageChange(event: number){
  this.configPgn.pageNum = event;
  // set for ngx
  this.configPgn.currentPage = this.configPgn.pageNum;
  this.getAttenData();
}
handlePageSizeChange(event: any): void {
  this.configPgn.pageSize = event.target.value;
  this.configPgn.pageNum = 1;
  // set for ngx
  this.configPgn.itemsPerPage = this.configPgn.pageSize;
  this.getAttenData();
}
// pagination handling methods end -------------------------------------------------------------------------





}
