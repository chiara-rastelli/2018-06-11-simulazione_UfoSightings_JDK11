package it.polito.tdp.ufo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.ufo.db.SightingsDAO;

public class Model {
	
	SightingsDAO db;
	SimpleDirectedGraph<String, DefaultEdge> graph;
	List<String> camminoPiuLungo;
	
	public Model() {
		this.db = new SightingsDAO();
	}
	
	public List<AnnoAvvistamenti> getAllAvvistamentiAnno(){
		return this.db.getAnnoAvvistamenti();
	}
	
	public void creaGrafo(int anno) {
		this.graph = new SimpleDirectedGraph<>(DefaultEdge.class);
	//	System.out.println(this.db.getStatiAvvistamentiAnno(anno).size());
		for (String s : this.db.getStatiAvvistamentiAnno(anno))
			this.graph.addVertex(s);
	//	System.out.println("Grafo creato con "+this.graph.vertexSet().size()+" vertici!\n");
		for (Adiacenza a : this.db.getAdiacenze(anno))
			this.graph.addEdge(a.s1, a.s2);
	//	System.out.println("Al grafo sono stati aggiunti "+this.graph.edgeSet().size()+" archi!\n");
	}
	
	public List<String> getVertici(){
		List<String> daRitornare = new ArrayList<String>();
		for (String s : this.graph.vertexSet())
			daRitornare.add(s);
		Collections.sort(daRitornare);
		return daRitornare;
	}

	public List<String> getPrecedenti(String value) {
		return Graphs.predecessorListOf(this.graph, value);
	}
	
	public List<String> getSuccessivi(String value) {
		return Graphs.successorListOf(this.graph, value);
	}
	
	public List<String> getStatiRaggiungibili(String stato){
		List<String> daRitornare = new ArrayList<String>();
		BreadthFirstIterator<String, DefaultEdge> bfv = new BreadthFirstIterator<>(this.graph, stato);
		bfv.next();
		while (bfv.hasNext())
			daRitornare.add(bfv.next());
		return daRitornare;
	}
	
	public List<String> getCamminoPiuLungo(String partenza){
		this.camminoPiuLungo = new ArrayList<String>();
		List<String> parziale = new ArrayList<String>();
		parziale.add(partenza);
		this.ricorri(parziale);
		return this.camminoPiuLungo;
	}

	private void ricorri(List<String> parziale) {
		
		// CASI TERMINALI: HO FINITO I PROSSIMI STATI RAGGIUNGIBILI, PARZIALE CONTIENE GIA' TUTTI I VERTICI DEL GRAFO
		if (parziale.size() == this.graph.vertexSet().size()) {
			if (parziale.size() > this.camminoPiuLungo.size()) 
				this.camminoPiuLungo = new ArrayList<>(parziale);
			return;
		}
		Set<String> disponibili = new HashSet<>();
		for (String s : Graphs.successorListOf(this.graph, parziale.get(parziale.size()-1)))
				disponibili.add(s);
		for (String s : parziale) {
			if (disponibili.contains(s))
				disponibili.remove(s);
		}
		if (disponibili.size() == 0) {
			if (parziale.size() > this.camminoPiuLungo.size())
				this.camminoPiuLungo = new ArrayList<>(parziale);
			return;
		}
		
		// SE NO, PROVO AGGIUNGENDONE UNO DEI DISPONIBILI
		for (String s : disponibili) {
			parziale.add(s);
			this.ricorri(parziale);
			parziale.remove(parziale.size()-1);
		}
	}
}
