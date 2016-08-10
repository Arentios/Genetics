package com.arentios.gene.domain;

import java.util.HashMap;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

/**
 * Class to hold the second level of hashmap for the Substitution Matrix
 * Ideally this would be an inner class, or a nested hashmap, but JAXB cannot handle those
 * Fortunately Substitution Matrixes are small so doing it this way isn't a huge burden
 * @author Arentios
 *
 */
public class SubstitutionScore{

	@XmlElement
	private HashMap<Character, Integer> substitutionScore;

	public SubstitutionScore(){
		substitutionScore = new HashMap<Character, Integer>();
	}

	public void addSubstitutionScore(Character key, Integer value){
		substitutionScore.put(key, value);
	}

	public Integer lookupSubstitutionScore(Character key){
		return substitutionScore.get(key);
	}
	
	public Set<Character> getCharacterSet(){
		return substitutionScore.keySet();
	}

}
