import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SysResDefComponent } from './sys-res-def.component';

describe('SysResDefComponent', () => {
  let component: SysResDefComponent;
  let fixture: ComponentFixture<SysResDefComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SysResDefComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SysResDefComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
