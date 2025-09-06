import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InserisciAppuntamentoComponent } from './inserisci-appuntamento.component';

describe('InserisciAppuntamentoComponent', () => {
  let component: InserisciAppuntamentoComponent;
  let fixture: ComponentFixture<InserisciAppuntamentoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InserisciAppuntamentoComponent]
    });
    fixture = TestBed.createComponent(InserisciAppuntamentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
