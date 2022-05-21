import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllOrgMstComponent } from './all-org-mst.component';

describe('AllOrgMstComponent', () => {
  let component: AllOrgMstComponent;
  let fixture: ComponentFixture<AllOrgMstComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllOrgMstComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AllOrgMstComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
