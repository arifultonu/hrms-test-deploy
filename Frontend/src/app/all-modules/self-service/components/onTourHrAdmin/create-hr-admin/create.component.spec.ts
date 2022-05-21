import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateHrAdminComponent } from './create.component';

describe('CreateHrAdminComponent', () => {
  let component: CreateHrAdminComponent;
  let fixture: ComponentFixture<CreateHrAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateHrAdminComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateHrAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
