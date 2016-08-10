package com.arentios.gene.domain;

import java.util.HashMap;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.arentios.gene.sequence.SequenceConstants;


/**
 * Class to represent a substitution matrix, used to represent the likelihood of one symbol becoming another
 * @author Arentios
 *
 */
@XmlRootElement
public class SubstitutionMatrix {

	
	
	
	@XmlElement
	private HashMap<Character, SubstitutionScore> substitutionMatrix;
	
	public SubstitutionMatrix(){
		substitutionMatrix = new HashMap<Character, SubstitutionScore>();
	}

	

	/**
	 * Add a new entry to the substitution matrix
	 * All entries are mirrored and the mirroring is done here instead of at lookup to save runtime
	 * @param key1
	 * @param key2
	 * @param value
	 */
	public void addSubstitutionMatrixValue(Character key1, Character key2, Integer value){
		SubstitutionScore subMatrix = substitutionMatrix.get(key1);
		if(subMatrix==null){
			subMatrix = new SubstitutionScore();
		}
		subMatrix.addSubstitutionScore(key2, value);
		substitutionMatrix.put(key1, subMatrix);
		subMatrix = substitutionMatrix.get(key2);
		if(subMatrix==null){
			subMatrix = new SubstitutionScore();
		}
		subMatrix.addSubstitutionScore(key1, value);
		substitutionMatrix.put(key2, subMatrix);
	}

	/**
	 * Return the value of a given pair of keys
	 * @param key1
	 * @param key2
	 * @return
	 * @throws Exception 
	 */
	public Integer lookupSubstitutionValue(Character key1, Character key2) throws Exception{
		SubstitutionScore subMatrix = substitutionMatrix.get(key1);
		if(subMatrix==null){
			subMatrix = substitutionMatrix.get(SequenceConstants.WILD_CARD_CHARACTER);
			if(subMatrix == null){
				throw new Exception("Failed to find value in matrix for key1="+key1+", key2="+key2);
			}
		}
		Integer value = subMatrix.lookupSubstitutionScore(key2);
		return value;
	}



	/**
	 * Test function to print out a matrix's contents to system output
	 */
	public void printMatrix() {
		for(Character c : substitutionMatrix.keySet()){
			for(Character e : substitutionMatrix.get(c).getCharacterSet()){
				System.out.println(c + " " + e + " " + substitutionMatrix.get(c).lookupSubstitutionScore(e));
			}
		}
		
	}
	
	

}
