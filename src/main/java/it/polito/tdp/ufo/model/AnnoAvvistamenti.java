package it.polito.tdp.ufo.model;

public class AnnoAvvistamenti {
	
	int anno;
	int numeroAvvistamenti;
	public int getAnno() {
		return anno;
	}
	public void setAnno(int anno) {
		this.anno = anno;
	}
	public int getNumeroAvvistamenti() {
		return numeroAvvistamenti;
	}
	public void setNumeroAvvistamenti(int numeroAvvistamenti) {
		this.numeroAvvistamenti = numeroAvvistamenti;
	}
	public AnnoAvvistamenti(int anno, int numeroAvvistamenti) {
		super();
		this.anno = anno;
		this.numeroAvvistamenti = numeroAvvistamenti;
	}
	@Override
	public String toString() {
		return "Anno " + anno + ": (numeroAvvistamenti= " + numeroAvvistamenti+")";
	}
	
}
