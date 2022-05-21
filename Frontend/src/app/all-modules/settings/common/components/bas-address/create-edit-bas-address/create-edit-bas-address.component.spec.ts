import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateEditBasAddressComponent } from './create-edit-bas-address.component';

describe('CreateEditBasAddressComponent', () => {
  let component: CreateEditBasAddressComponent;
  let fixture: ComponentFixture<CreateEditBasAddressComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateEditBasAddressComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateEditBasAddressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
