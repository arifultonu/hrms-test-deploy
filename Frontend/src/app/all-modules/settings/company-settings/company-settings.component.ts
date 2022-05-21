import { Component, OnInit } from "@angular/core";
import {
  FormBuilder,
  FormGroup,
  FormControl,
  Validators,
} from "@angular/forms";
import { ToastrService } from "ngx-toastr";

@Component({
  selector: "app-company-settings",
  templateUrl: "./company-settings.component.html",
  styleUrls: ["./company-settings.component.css"],
})
export class CompanySettingsComponent implements OnInit {
  public companySettings: FormGroup;
  constructor(
    private formBuilder: FormBuilder,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.companySettings = this.formBuilder.group({
      companyName: ["One Direction Company Limited", [Validators.required]],
      contactPerson: ["S.M. Rezaul Alam", [Validators.required]],
      address: ["Basundhara,Dhaka", [Validators.required]],
      country: ["Bangladesh", [Validators.required]],
      city: ["Dhaka", [Validators.required]],
      state: ["Dhaka", [Validators.required]],
      postalCode: ["1212", [Validators.required]],
      email: ["hrms@odcl.com", [Validators.required]],
      phoneNumber: ["071-654124", [Validators.required]],
      mobileNumber: ["8547522541", [Validators.required]],
      fax: ["012-456213", [Validators.required]],
      website: ["www.odcl.com.bd", [Validators.required]],
    });
  }

  submitCompany() {
    if (this.companySettings.valid) {
      this.toastr.success("Company Settings is added", "Success");
    }
  }
}
