import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListTransportComponent } from './list-transport.component';

describe('ListTransportComponent', () => {
  let component: ListTransportComponent;
  let fixture: ComponentFixture<ListTransportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListTransportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListTransportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
