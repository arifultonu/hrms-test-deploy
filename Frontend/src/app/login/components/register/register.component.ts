import { Component, OnInit } from '@angular/core';
import { AbstractControlOptions, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { first } from 'rxjs/operators';
import { MustMatch } from 'src/app/utils/_helpers';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  form!: FormGroup;
  id!: string;
  isAddMode!: boolean;
  loading = false;
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private loginService: LoginService,
    private route: ActivatedRoute,
    private router: Router,
    private toastr:ToastrService,
  ) { }

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    this.isAddMode = !this.id;
    this.initializeForm();
  }

  initializeForm() {
    const formOptions: AbstractControlOptions = { validators: MustMatch('password', 'confirmPassword') };
    this.form = this.formBuilder.group({
      username: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      phone: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
     // role: ['', Validators.required],
      password: ['', [Validators.minLength(4), Validators.required]],
      confirmPassword: ['', Validators.required]
    }, formOptions);

    if (!this.isAddMode) {
      this.loginService.getById(this.id)
        .pipe(first())
        .subscribe(x => this.form.patchValue(x));
    }
  }

  // convenience getter for easy access to form fields
  get f() { return this.form.controls; }

  onSubmit() {

   
    this.submitted = true;
    console.log("OK"+this.isAddMode);
    // stop here if form is invalid
    if (this.form.invalid) {
      return;
    }

  
     
    this.loading = true;
    if (this.isAddMode) {
      this.createUser();
    }
  }

  private createUser() {
    this.loginService.register(this.form.value)
      .pipe(first())
      .subscribe(() => {
        this.toastr.success("Successfully Registered");
        this.router.navigate(['../'], { relativeTo: this.route });
      },(error)=>{
        this.toastr.error(error.error.message);
      })
      .add(() => this.loading = false);
  }

}
