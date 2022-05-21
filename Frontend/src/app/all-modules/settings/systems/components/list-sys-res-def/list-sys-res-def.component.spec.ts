import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListSysResDefComponent } from './list-sys-res-def.component';

describe('ListSysResDefComponent', () => {
  let component: ListSysResDefComponent;
  let fixture: ComponentFixture<ListSysResDefComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListSysResDefComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListSysResDefComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
