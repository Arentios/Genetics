package com.arentios.gene.domain;


import java.io.Serializable;
import java.util.LinkedList;

/**
 * Cell class used in dynamic programming algorithms
 * @author Arentios
 *
 */
public class Cell implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 95331134073804026L;
	private LinkedList<Cell> parents = null;
	private Double score;
	private int i;
	private int j;
	private boolean gap;
	
	public Cell(){
		this.score = 0.0;
		this.gap = false;
	}
	
	public Cell(Double score, int i, int j){
		this.score = score;
		this.i = i;
		this.j = j;
		this.gap = false;
	}
	

	
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}

	public void addParent(Cell parent){
		if(parents==null){
			parents = new LinkedList<Cell>();
		}
		parents.add(parent);
	}

	public LinkedList<Cell> getParents() {
		return parents;
	}

	public void setParents(LinkedList<Cell> parents) {
		this.parents = new LinkedList<Cell>(parents);
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public boolean isGap() {
		return gap;
	}

	public void setGap(boolean gap) {
		this.gap = gap;
	}
	
	
}
