package com.arentios.gene.domain;

/**
 * Cell class used in dynamic programming algorithms
 * @author Arentios
 *
 */
public class Cell {

	private Cell parent;
	private Integer score;
	private String type;
	
	public Cell(){
		this.parent = null;
		this.score = 0;
		this.setType(null);
	}
	
	public Cell(Integer score){
		this.parent = null;
		this.score = score;
		this.setType(null);
	}
	
	public Cell(Integer score, Cell parent, String type){
		this.parent = parent;
		this.score = score;
		this.setType(type);
	}
	
	public Cell getParent() {
		return parent;
	}
	public void setParent(Cell parent) {
		this.parent = parent;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
