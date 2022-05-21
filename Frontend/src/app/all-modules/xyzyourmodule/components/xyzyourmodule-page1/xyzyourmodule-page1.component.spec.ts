import { ComponentFixture, TestBed } from '@angular/core/testing';

import { XyzyourmodulePage1Component } from './xyzyourmodule-page1.component';

describe('XyzyourmodulePage1Component', () => {
  let component: XyzyourmodulePage1Component;
  let fixture: ComponentFixture<XyzyourmodulePage1Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ XyzyourmodulePage1Component ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(XyzyourmodulePage1Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
