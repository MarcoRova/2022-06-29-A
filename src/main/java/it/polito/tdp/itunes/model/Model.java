package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private ItunesDAO dao;
	private Graph<Album, DefaultWeightedEdge> grafo;
	private List<Album> percorso;
	private int bestScore;
	
	public Model() {
		this.dao = new ItunesDAO();
	}
	
	
	public void creaGrafo(int N) {
		
		this.grafo = new SimpleDirectedWeightedGraph<Album, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(N));
		aggiungiArchi();
		
		
		
		
	}
	
	public void aggiungiArchi() {
		
		for(Album a1: this.grafo.vertexSet()) {
			for(Album a2 : this.grafo.vertexSet()) {
				if(!a1.equals(a2) && !this.grafo.containsEdge(a1, a2) && !this.grafo.containsEdge(a2, a1)) {
					int delta = 0;
					int p1 = this.dao.getNTracks(a1);
					int p2 = this.dao.getNTracks(a2);
					
					if(p1-p2> 0) {
						delta = Math.abs(p1-p2);
						Graphs.addEdgeWithVertices(this.grafo, a2, a1, delta);
					}
					else if(p1-p2 < 0) {
						delta = Math.abs(p1-p2);
						Graphs.addEdgeWithVertices(this.grafo, a1, a2, delta);
					}
				}
			}
		}
	}
	
	public List<Album> getVertici(int N){
		return this.dao.getVertici(N);
	}
	
	public String infoGrafo() {
		return "Grafo creato!\n#Vertici: "+this.grafo.vertexSet().size()+"\n#Archi: "+this.grafo.edgeSet().size();
	}


	public Graph<Album, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	
	public List<BilancioAlbum> getAdiacenti(Album a){
		
		List<Album> successori = Graphs.successorListOf(this.grafo, a);
		
		List<BilancioAlbum> bilancioSucc = new LinkedList<>();
		
		for(Album al : successori) {
			bilancioSucc.add(new BilancioAlbum(al, getBilancio(al)));
		}
		
		Collections.sort(bilancioSucc);
		
		return bilancioSucc;
		
	}


	public int getBilancio(Album al) {
		
		int bilancio = 0;
		
		List<DefaultWeightedEdge> entranti = new ArrayList<>(this.grafo.incomingEdgesOf(al));
		
		List<DefaultWeightedEdge> uscenti = new ArrayList<>(this.grafo.outgoingEdgesOf(al));
		
		
		for(DefaultWeightedEdge e1 : entranti) {
			bilancio += this.grafo.getEdgeWeight(e1);
		}
		
		for(DefaultWeightedEdge e2 : uscenti) {
			bilancio -= this.grafo.getEdgeWeight(e2);
		}
		return bilancio;
	}
	
	
	public List<Album> getPercorso(Album partenza, Album arrivo, int soglia){
		
		List<Album> parziale = new ArrayList<>();
		this.percorso = new ArrayList<>();
		this.bestScore = 0;
		
		parziale.add(partenza);
		
		ricorsione(parziale, arrivo, soglia);
		
		return percorso;
		
	}


	public void ricorsione(List<Album> parziale, Album arrivo, int soglia) {
		
		Album current = parziale.get(parziale.size()-1);
		
		// uscita
		
		if(current.equals(arrivo)) {
			
			if(getScore(parziale)>bestScore) {
				bestScore = getScore(parziale);
				this.percorso = new ArrayList<>(parziale);
			}
			return;
		}
		
		List<Album> successori = Graphs.successorListOf(this.grafo, current);
		
		for(Album a : successori) {
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(current, a)) >= soglia && !parziale.contains(a)) {
				
				parziale.add(a);
				
				ricorsione(parziale, arrivo, soglia);
				
				parziale.remove(a);
				
			}
		}
		
	}


	public int getScore(List<Album> parziale) {
		int score = 0;
		
		Album source = parziale.get(0);
		
		for(Album a : parziale.subList(1, parziale.size()-1)) {
			
			if(getBilancio(a) > getBilancio(source)) {
				score +=1;
			}
			
		}
		
		return score;
	}
	
	
}
