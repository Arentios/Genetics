package com.arentios.gene.domain;

import java.util.ArrayList;
import java.util.Arrays;

public class GeneSequence {

	private ArrayList<Character> sequence; //Arraylist instead of String for mutability

	public GeneSequence(){
		
	}
	
	/**
	 * Constructor to take in a string and convert it to an array list 
	 * @param sequenceString
	 */
	public GeneSequence(String sequenceString){
		//Can't convert a String directly to an ArrayList since it converts to char and we need Character
		char[] charArray = sequenceString.toCharArray();	
		this.sequence = new ArrayList<Character>();
		for(int i=0;i<charArray.length;i++){
			this.sequence.add(charArray[i]);
		}
	
	}
	
	public ArrayList<Character> getSequence() {
		return new ArrayList<Character>(sequence); //Return a copy, not a reference
	}

	public void setSequence(ArrayList<Character> sequence) {
		this.sequence = new ArrayList<Character>(sequence); //Make a new copy instead of keeping a reference
	}
	
	
	
}
