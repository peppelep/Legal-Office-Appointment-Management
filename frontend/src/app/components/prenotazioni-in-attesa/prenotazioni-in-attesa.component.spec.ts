import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrenotazioniInAttesaComponent } from './prenotazioni-in-attesa.component';

describe('PrenotazioniInAttesaComponent', () => {
  let component: PrenotazioniInAttesaComponent;
  let fixture: ComponentFixture<PrenotazioniInAttesaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrenotazioniInAttesaComponent]
    });
    fixture = TestBed.createComponent(PrenotazioniInAttesaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
