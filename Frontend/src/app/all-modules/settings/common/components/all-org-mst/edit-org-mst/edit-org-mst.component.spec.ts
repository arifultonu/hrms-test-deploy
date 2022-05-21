import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditOrgMstComponent } from './edit-org-mst.component';

describe('EditOrgMstComponent', () => {
  let component: EditOrgMstComponent;
  let fixture: ComponentFixture<EditOrgMstComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditOrgMstComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditOrgMstComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
