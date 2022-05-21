import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagementFormComponent } from './management-form.component';

describe('ManagementFormComponent', () => {
  let component: ManagementFormComponent;
  let fixture: ComponentFixture<ManagementFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManagementFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManagementFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
