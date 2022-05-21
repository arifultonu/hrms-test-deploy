import { ComponentFixture, TestBed } from '@angular/core/testing';

import { XyzyourmoduleComponent } from './xyzyourmodule.component';

describe('XyzyourmoduleComponent', () => {
  let component: XyzyourmoduleComponent;
  let fixture: ComponentFixture<XyzyourmoduleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ XyzyourmoduleComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(XyzyourmoduleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
