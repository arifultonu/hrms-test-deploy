import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

// Bootstrap DataTable
import { DataTablesModule } from 'angular-datatables';
import { ToastrModule } from 'ngx-toastr';
import { HttpClientModule } from '@angular/common/http';
import { authInterceptorProviders } from './guard/auth.interceptor';
import { DatePipe } from '@angular/common';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    DataTablesModule,
    HttpClientModule,


    ToastrModule.forRoot(
      {
        timeOut: 3000,
        positionClass: 'toast-bottom-right',
        preventDuplicates: true,
        closeButton: true
      }
    ),
  ],
  providers: [
    authInterceptorProviders,
    DatePipe
  ],

  bootstrap: [AppComponent]
})
export class AppModule { }
