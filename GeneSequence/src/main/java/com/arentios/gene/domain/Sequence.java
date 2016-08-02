package com.arentios.gene.domain;

//7/14/2016 EM renamed from GeneSequence to Sequence due to the more generic use case
//Removed mutate/random functionality because they were novel for testing but were extraneous long term
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Sequence {

	private ArrayList<Character> sequence; //Arraylist instead of String for mutability and better index access

	public Sequence(){

	}

	/**
	 * Constructor to take in a string and convert it to an array list 
	 * @param sequenceString
	 */
	public Sequence(String sequenceString){
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
	public Sequence(Sequence firstSequence) {
		this.sequence = new ArrayList<Character>(firstSequence.getSequence());
	}

	public ArrayList<Character> getSequence() {
		return new ArrayList<Character>(sequence); //Return a copy, not a reference
	}

	public void setSequence(ArrayList<Character> sequence) {
		this.sequence = new ArrayList<Character>(sequence); //Make a new copy instead of keeping a reference
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sequence other = (Sequence) obj;
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if(sequence.containsAll(other.sequence) && other.sequence.containsAll(sequence) && (sequence.size()==other.sequence.size())){
			return true;
		}
		return false;
	}
	



}
