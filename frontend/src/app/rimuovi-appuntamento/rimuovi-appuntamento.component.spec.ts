import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RimuoviAppuntamentoComponent } from './rimuovi-appuntamento.component';

describe('RimuoviAppuntamentoComponent', () => {
  let component: RimuoviAppuntamentoComponent;
  let fixture: ComponentFixture<RimuoviAppuntamentoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RimuoviAppuntamentoComponent]
    });
    fixture = TestBed.createComponent(RimuoviAppuntamentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
