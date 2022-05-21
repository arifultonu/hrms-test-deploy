import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AbstractControlOptions, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { SystemService } from '../../services/system.service';
import { environment } from 'src/environments/environment';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-sys-res-def',
  templateUrl: './sys-res-def.component.html',
  styleUrls: ['./sys-res-def.component.css']
})
export class SysResDefComponent implements OnInit {


  public baseUrl = environment.baseUrl;

  public form: FormGroup;
  id!: string;
  isAddMode!: boolean;
  loading = false;
  submitted = false;

  public paramsConfig:any;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private systemService: SystemService,
    private toastr: ToastrService,

  ) {
    this.paramsConfig = {
      id:'',
      entityName: '',
    };
  }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];
    this.isAddMode = !this.id;
    this.paramsConfig.id=this.id;
    this.initializeForm();

  }

  initializeForm() {
    this.form = this.formBuilder.group({
      entityName: ['',Validators.required],
      entityDescription: [''],
      backendUrl: ['',Validators.required],
      clientReqUrl: ['',Validators.required],
      resourceType: ['entity'],
      openUrl: [''],
      chkAuthorization: ['YES'],
      chkAuthorizationChar: [''],
      adminAccessOnly: [''],
      superAdminAccessOnly: [''],
      sequence: ['0'],
      active: [''],
      resourceCode: [''],
      resourceTitle: [''],
      resourceElement: [''],
    })

    if (!this.isAddMode) {
      let queryParams: any = {};
      queryParams.id = this.paramsConfig.id;
      this.systemService.getSysResDef(queryParams)
          .pipe(first())
          .subscribe((data:any) => {
            this.form.patchValue(data.objectList[0]);
            console.log("@SysResDef "+data.objectList[0].id);

          });
  }
  }

  get f() { return this.form.controls; }

  formSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.form.invalid) {
      return;
    }

    this.loading = true;
    if (this.isAddMode) {
      this.create();
    } else {
      this.update();
    }

  }

  private create() {
    this.systemService.createSysResDef(this.form.value).subscribe(() =>{

      this.router.navigate(["/settings/list-sys-resDef"], {
        relativeTo: this.route,
      });
      this.toastr.success("Successfully created");
    },(error)=>{ this.toastr.error("error in creating");});

  }

  private update() {
    this.systemService.updateSysResDef(this.id,this.form.value).subscribe(() =>{
      this.router.navigate(["/settings/list-sys-resDef"], {
        relativeTo: this.route,
      });
      this.toastr.success("Successfully updated");
    },(error)=>{ this.toastr.error("error in updating");});
  }


}
