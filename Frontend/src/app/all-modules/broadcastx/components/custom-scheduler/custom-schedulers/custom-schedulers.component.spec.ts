import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomSchedulersComponent } from './custom-schedulers.component';

describe('CustomSchedulersComponent', () => {
  let component: CustomSchedulersComponent;
  let fixture: ComponentFixture<CustomSchedulersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CustomSchedulersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomSchedulersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
