import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrenotaAppuntamentoComponent } from './prenota-appuntamento.component';

describe('PrenotaAppuntamentoComponent', () => {
  let component: PrenotaAppuntamentoComponent;
  let fixture: ComponentFixture<PrenotaAppuntamentoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrenotaAppuntamentoComponent]
    });
    fixture = TestBed.createComponent(PrenotaAppuntamentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
