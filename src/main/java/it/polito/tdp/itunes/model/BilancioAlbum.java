package it.polito.tdp.itunes.model;

public class BilancioAlbum implements Comparable<BilancioAlbum>{
	
	private Album a;
	private int bilancio;
	
	public BilancioAlbum(Album a, int bilancio) {
		super();
		this.a = a;
		this.bilancio = bilancio;
	}

	public Album getA() {
		return a;
	}

	public void setA(Album a) {
		this.a = a;
	}

	public int getBilancio() {
		return bilancio;
	}

	public void setBilancio(int bilancio) {
		this.bilancio = bilancio;
	}

	@Override
	public int compareTo(BilancioAlbum other) {
		return -(this.bilancio-other.bilancio);
	}
	
	
	
	

}
