package com.arentios.gene.domain;

import java.util.ArrayList;

/**
 * Class to hold a list of sequenced genes
 * Broken out as a seperate class for helper functions and printing
 * @author Arentios
 *
 */
public class SequencedGenes {

	
	ArrayList<GeneSequence> sequencedGenes;
	
	public SequencedGenes(){
		sequencedGenes = new ArrayList<GeneSequence>();
	}
	
	public void addSequence(GeneSequence sequence){
		sequencedGenes.add(sequence);
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		for(GeneSequence sequence : sequencedGenes){
			builder.append(sequence.getSequence());
			builder.append("\n");		
		}
		return builder.toString();
	}
	
}
