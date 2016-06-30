package com.arentios.gene.domain;

import java.util.ArrayList;

public class GeneSequence {

	private ArrayList<Character> sequence; //Arraylist instead of String for mutability

	/**
	 * Constructor to take in a string and convert it to an array list 
	 * @param sequenceString
	 */
	public GeneSequence(String sequenceString){
		//this.sequence = new ArrayList<Character>(Arrays.asList(sequenceString.split(regex)))
	}
	
	public ArrayList<Character> getSequence() {
		return new ArrayList<Character>(sequence); //Return a copy, not a reference
	}

	public void setSequence(ArrayList<Character> sequence) {
		this.sequence = new ArrayList<Character>(sequence); //Make a new copy instead of keeping a reference
	}
	
	
	
}
