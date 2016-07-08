package com.arentios.gene.sequence;

import java.util.ArrayList;
import java.util.LinkedList;

import com.arentios.gene.domain.Cell;
import com.arentios.gene.domain.GeneSequence;
import com.arentios.gene.domain.SequenceAlignment;

/**
 * Parent class used by dynamic sequence based alignment protocols
 * @author Arentios
 *
 */
public class DynamicProgrammingSequencer {
	
	/**
	 * Backtrack from a given cell accumulating sequences based on it's parents
	 * Used by both Needleman-Wunsch and Smith-Waterman
	 * TBD: Current cannot handle large sequences due to recursion
	 * 		Need to implement code to loop by default and only recur if there's a branch
	 * 		Along with general performance improvements to decrease memory footprint of each recursion
	 * @param currCell
	 * @param sequenceOne
	 * @param sequenceTwo
	 * @param firstSequence
	 * @param secondSequence
	 * @param match
	 * @param indel
	 * @param mismatch
	 * @return
	 */
	protected static ArrayList<SequenceAlignment> backTrack(Cell currCell, ArrayList<Character> sequenceOne, ArrayList<Character> sequenceTwo, ArrayList<Character> firstSequence, ArrayList<Character> secondSequence, Integer match, Integer indel, Integer mismatch){
		ArrayList<SequenceAlignment> results = new ArrayList<SequenceAlignment>();	
		LinkedList<Cell> parents = currCell.getParents();
		if(parents != null){
			for(Cell parent: parents){
				ArrayList<Character> newSequenceOne = new ArrayList<Character>(sequenceOne);
				ArrayList<Character> newSequenceTwo = new ArrayList<Character>(sequenceTwo);
				//Need to figure out which character to add to each sequence
				if(parent.getI() != currCell.getI()){
					//Diagonal movement
					if(parent.getJ() != currCell.getJ()){
						newSequenceOne.add(0,firstSequence.get(parent.getI()));
						newSequenceTwo.add(0,secondSequence.get(parent.getJ()));
					}
					//Leftwards movement
					else{
						newSequenceOne.add(0,firstSequence.get(parent.getI()));
						newSequenceTwo.add(0,'-');
					}
				}
				//Upwards movement by default
				else{
					newSequenceOne.add(0,'-');
					newSequenceTwo.add(0,secondSequence.get(parent.getJ()));
				}
				ArrayList<SequenceAlignment> subResults = backTrack(parent, newSequenceOne, newSequenceTwo, firstSequence, secondSequence, match, indel, mismatch);
				for(SequenceAlignment alignment : subResults){
					results.add(alignment);
				}
			}
		}
		//If there are no parents we're at the terminus
		else{

			SequenceAlignment sequencedPair = new SequenceAlignment();
			GeneSequence resultSequence = new GeneSequence();
			resultSequence.setSequence(sequenceOne);
			sequencedPair.addSequence(resultSequence);
			resultSequence = new GeneSequence();
			resultSequence.setSequence(sequenceTwo);
			sequencedPair.addSequence(resultSequence);
			results.add(sequencedPair);
		}
		return results;
	}
}
