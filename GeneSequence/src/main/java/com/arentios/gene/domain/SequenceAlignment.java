package com.arentios.gene.domain;

import java.util.ArrayList;

/**
 * Class to hold a list of sequenced genes
 * Broken out as a seperate class for helper functions and printing
 * @author Arentios
 *
 */
public class SequenceAlignment {


	ArrayList<Sequence> sequencedGenes;

	public SequenceAlignment(){
		sequencedGenes = new ArrayList<Sequence>();
	}
	
	public SequenceAlignment(ArrayList<Sequence> sequences){
		sequencedGenes = new ArrayList<Sequence>(sequences);
	}

	public void addSequence(Sequence sequence){
		sequencedGenes.add(sequence);
	}

	public String toString(){
		StringBuilder builder = new StringBuilder();
		for(Sequence sequence : sequencedGenes){
			builder.append(sequence.getSequence());
			builder.append("\n");		
		}
		return builder.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SequenceAlignment other = (SequenceAlignment) obj;
		if (sequencedGenes == null) {
			if (other.sequencedGenes != null)
				return false;
		} else{
			if(sequencedGenes.size() != other.sequencedGenes.size()){
				return false;
			}
			else{
				//TODO: This currently requires all sequences to be in the same order, which may not match future changes to generation, fix this
				for(int i=0;i<sequencedGenes.size();i++){
					if(!sequencedGenes.get(i).equals(other.sequencedGenes.get(i))){
						return false;
					}
				}
			}
		}
		return true;
	}

}
