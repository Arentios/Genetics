package com.arentios.gene.sequence;

import java.util.ArrayList;

import com.arentios.gene.domain.Cell;
import com.arentios.gene.domain.GeneSequence;
import com.arentios.gene.domain.SequenceAlignment;

public class SmithWaterman {

	static{
		
	}
	
	public static ArrayList<SequenceAlignment> sequence(GeneSequence firstSequence, GeneSequence secondSequence){
		return sequence(firstSequence, secondSequence, SequenceConstants.SMITH_WATERMAN_MATCH_DEFAULT, SequenceConstants.SMITH_WATERMAN_INDEL_DEFAULT, SequenceConstants.SMITH_WATERMAN_MISMATCH_DEFAULT);
	}
	
	/**
	 * Sequence assignment using Smith-Waterman, which focuses on local alignment
	 * @param firstGeneSequence
	 * @param secondGeneSequence
	 * @param match
	 * @param indel
	 * @param mismatch
	 * @return
	 */
	public static ArrayList<SequenceAlignment> sequence(GeneSequence firstGeneSequence, GeneSequence secondGeneSequence, Integer match, Integer indel, Integer mismatch){
		ArrayList<SequenceAlignment> results = new ArrayList<SequenceAlignment>();
		ArrayList<Character> firstSequence = firstGeneSequence.getSequence();
		ArrayList<Character> secondSequence = secondGeneSequence.getSequence();
		Cell[][] scoringMatrix = new Cell[firstSequence.size()+1][secondSequence.size()+1];
		//Set the base and boundary entries
		scoringMatrix[0][0] = new Cell(0);
		for(int i=1;i<scoringMatrix.length;i++){
			scoringMatrix[i][0] = new Cell(0);
		}
		for(int j=1;j<scoringMatrix[0].length;j++){
			scoringMatrix[0][j] = new Cell(0);
		}
		
		return results;
	}
	
}
