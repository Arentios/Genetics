package com.arentios.gene.domain;

import java.util.ArrayList;

/**
 * Data object to hold information for a currently in process back track
 * @author Arentios
 *
 */
public class BackTrackData {
	private Cell cell;
	private ArrayList<Character> sequenceOne;
	private ArrayList<Character> sequenceTwo;
	
	public Cell getCell() {
		return cell;
	}
	public void setCell(Cell cell) {
		this.cell = cell;
	}
	public ArrayList<Character> getSequenceOne() {
		return sequenceOne;
	}
	public void setSequenceOne(ArrayList<Character> sequenceOne) {
		this.sequenceOne = sequenceOne;
	}
	public ArrayList<Character> getSequenceTwo() {
		return sequenceTwo;
	}
	public void setSequenceTwo(ArrayList<Character> sequenceTwo) {
		this.sequenceTwo = sequenceTwo;
	}

}
