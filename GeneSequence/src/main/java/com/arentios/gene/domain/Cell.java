package com.arentios.gene.domain;

/**
 * Cell class used in dynamic programming algorithms
 * @author Arentios
 *
 */
public class Cell {

	private Cell parent;
	private Integer score;
	
	public Cell(){
		parent = null;
		score = 0;
	}
	
	public Cell(Integer score){
		parent = null;
		this.score = score;
	}
	
	public Cell(Integer score, Cell parent){
		this.parent = parent;
		this.score = score;
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
	
	
	
}
