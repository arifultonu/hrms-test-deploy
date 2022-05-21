import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IRecruitmentComponent } from './i-recruitment.component';

describe('IRecruitmentComponent', () => {
  let component: IRecruitmentComponent;
  let fixture: ComponentFixture<IRecruitmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IRecruitmentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IRecruitmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
