package com.arentios.gene.domain;

import java.util.HashMap;

/**
 * Cell class used in dynamic programming algorithms
 * @author Arentios
 *
 */
public class Cell {

	//Parents are a hash of direction to cell, there will never be multiple pointers in the same direction so a hash is okay
	private HashMap<String, Cell> parents;
	private Integer score;
	private int i;
	private int j;
	
	public Cell(){
		this.score = 0;
	}
	
	public Cell(Integer score, int i, int j){
		this.score = score;
		this.i = i;
		this.j = j;
	}
	

	
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}

	public void addParent(Cell parent, String direction){
		if(parents==null){
			parents = new HashMap<String, Cell>();
		}
		parents.put(direction,parent);
	}

	public HashMap<String, Cell> getParents() {
		return parents;
	}

	public void setParents(HashMap<String, Cell> parents) {
		this.parents = parents;
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
	
	
}
