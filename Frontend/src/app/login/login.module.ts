import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { LoginRoutingModule } from './login-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './components/login/login.component';
import { ForgotComponent } from './components/forgot/forgot.component';
import { OtpComponent } from './components/otp/otp.component';
import { LockscreenComponent } from './components/lockscreen/lockscreen.component';
import { RegisterComponent } from './components/register/register.component';





@NgModule({
  declarations: [LoginComponent, ForgotComponent, RegisterComponent, OtpComponent, LockscreenComponent],
  imports: [
    CommonModule,
    LoginRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    
   
    
  ]
})
export class LoginModule { }
