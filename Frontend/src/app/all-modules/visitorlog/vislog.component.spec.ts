import { ComponentFixture, TestBed } from '@angular/core/testing';

import { vislog } from './vislog.component';

describe('vislog', () => {
  let component: vislog;
  let fixture: ComponentFixture<vislog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ vislog ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(vislog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
