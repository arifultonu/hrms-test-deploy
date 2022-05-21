import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IvbmbcreateComponent } from './ivbmbcreate.component';

describe('IvbmbcreateComponent', () => {
  let component: IvbmbcreateComponent;
  let fixture: ComponentFixture<IvbmbcreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IvbmbcreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IvbmbcreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
