package org.rochlitz.kontoNotifier.rest;

public class Overplus {

	int maxTageMonat;
	double einnahmen;
	double ausgaben;
	double moeglicheAusgabenProTag; 
	int vergangeneTageMonat;
	double bisherigeAusgabenProTag; 
	
	public Overplus(int maxTageMonat, double einnahmen, double ausgaben, int vergangeneTageMonat ) {
		super();
		this.maxTageMonat = maxTageMonat;
		this.einnahmen = einnahmen;
		this.ausgaben = ausgaben;
		calculateDaylyValues();
		this.vergangeneTageMonat = vergangeneTageMonat;
		System.out.println(" §§§§§§§  moeglicheAusgabenProTag: " + moeglicheAusgabenProTag);
		System.out.println(" §§§§§§§  tatsächlicheAusgabeProTag: " + bisherigeAusgabenProTag);
	}

	private void calculateDaylyValues() {
		this.moeglicheAusgabenProTag = einnahmen /  maxTageMonat;  
		this.bisherigeAusgabenProTag =  ausgaben / vergangeneTageMonat	  ;
	}

	public Overplus() {
	}

	public int getMaxTageMonat() {
		return maxTageMonat;
	}

	public void setMaxTageMonat(int maxTageMonat) {
		this.maxTageMonat = maxTageMonat;
	}

	public double getEinnahmen() {
		return einnahmen;
	}

	public void setEinnahmen(double einnahmen) {
		this.einnahmen = einnahmen;
	}

	public double getMoeglicheAusgabenProTag() {
		return moeglicheAusgabenProTag;
	}

	public void setMoeglicheAusgabenProTag(double moeglicheAusgabenProTag) {
		this.moeglicheAusgabenProTag = moeglicheAusgabenProTag;
	}

	public int getVergangeneTageMonat() {
		return vergangeneTageMonat;
	}

	public void setVergangeneTageMonat(int vergangeneTageMonat) {
		this.vergangeneTageMonat = vergangeneTageMonat;
	}

	public double getbisherigeAusgabenProTag() {
		return bisherigeAusgabenProTag;
	}

	public void setbisherigeAusgabenProTag(double bisherigeAusgabenProTag) {
		this.bisherigeAusgabenProTag = bisherigeAusgabenProTag;
	}

	public double getAusgaben() {
		return ausgaben;
	}

	public void setAusgaben(double ausgaben) {
		this.ausgaben = ausgaben;
	}

	public void merge(Overplus o) {
		this.setMaxTageMonat(o.getMaxTageMonat());
		this.setVergangeneTageMonat(o.getVergangeneTageMonat());
		this.setEinnahmen(this.getEinnahmen() + o.getEinnahmen() );
		this.setAusgaben( this.getAusgaben() + o.getAusgaben() );
		this.setMoeglicheAusgabenProTag( this.getMoeglicheAusgabenProTag() + o.getMoeglicheAusgabenProTag() );
		calculateDaylyValues();
		
	}
	
	

}
