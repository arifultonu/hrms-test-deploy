import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApiConfigCreateComponent } from './api-config-create.component';

describe('ApiConfigCreateComponent', () => {
  let component: ApiConfigCreateComponent;
  let fixture: ComponentFixture<ApiConfigCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApiConfigCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApiConfigCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
