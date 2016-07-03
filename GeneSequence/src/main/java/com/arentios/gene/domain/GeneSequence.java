package com.arentios.gene.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GeneSequence {

	private ArrayList<Character> sequence; //Arraylist instead of String for mutability

	private static final Character[] nucleotides = new Character[]{ 'U', 'T', 'C', 'A'};
	
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
	
	/**
	 * Copy constructor
	 * @param firstSequence
	 */
	public GeneSequence(GeneSequence firstSequence) {
		this.sequence = new ArrayList<Character>(firstSequence.getSequence());
	}

	public ArrayList<Character> getSequence() {
		return new ArrayList<Character>(sequence); //Return a copy, not a reference
	}

	public void setSequence(ArrayList<Character> sequence) {
		this.sequence = new ArrayList<Character>(sequence); //Make a new copy instead of keeping a reference
	}
	
	/**
	 * Generate a random gene sequence of a given length
	 * @param length
	 */
	public void randomize(int length){
		sequence = new ArrayList<Character>(length);
		for(int i=0;i<length;i++){
			sequence.add(nucleotides[new Random().nextInt(nucleotides.length)]);
		}
	}
	
	/**
	 * Alter a gene sequence by randomly altering each nucleotide with a given chance
	 * @param chance
	 */
	public void mutate(int chance){
		for(int i=0;i<sequence.size();i++){
			if(new Random().nextInt(100) < chance){
				sequence.set(i, nucleotides[new Random().nextInt(nucleotides.length)]);
			}	
		}
	}
	
	
}
