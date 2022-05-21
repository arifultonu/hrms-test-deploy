import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewTransportComponent } from './view-transport.component';

describe('ViewTransportComponent', () => {
  let component: ViewTransportComponent;
  let fixture: ComponentFixture<ViewTransportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewTransportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewTransportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
