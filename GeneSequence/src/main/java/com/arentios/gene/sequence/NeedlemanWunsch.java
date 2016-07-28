package com.arentios.gene.sequence;

import java.util.ArrayList;
import java.util.LinkedList;

import com.arentios.gene.domain.Cell;
import com.arentios.gene.domain.Sequence;
import com.arentios.gene.domain.SequenceAlignment;

/**
 * Class to implement the Needleman-Wunsch algorithm to use dynamic programming to find optimal gene sequences
 * @author Arentios
 *
 */
public class NeedlemanWunsch extends DynamicProgrammingSequencer {
	
	/**
	 * Wrapper to pass default values to sequencing function
	 * @param firstSequence
	 * @param secondSequence
	 * @return
	 */
	public static ArrayList<SequenceAlignment> sequence(Sequence firstSequence, Sequence secondSequence){
		return sequence(firstSequence, secondSequence, SequenceConstants.NEEDLEMAN_WUNSCH_MATCH_DEFAULT, SequenceConstants.NEEDLEMAN_WUNSCH_INDEL_DEFAULT, SequenceConstants.NEEDLEMAN_WUNSCH_MISMATCH_DEFAULT);
	}




	/**
	 * Perform Needleman-Wunsch algorithm
	 * NYI: Finding multiple best paths
	 * @param firstGeneSequence
	 * @param secondGeneSequence
	 * @param match
	 * @param indel
	 * @param mismatch
	 * @return
	 */
	public static ArrayList<SequenceAlignment> sequence(Sequence firstGeneSequence, Sequence secondGeneSequence, Integer match, Integer indel, Integer mismatch){
		ArrayList<SequenceAlignment> results = new ArrayList<SequenceAlignment>();
		ArrayList<Character> firstSequence = firstGeneSequence.getSequence();
		ArrayList<Character> secondSequence = secondGeneSequence.getSequence();
		Cell[][] scoringMatrix = new Cell[firstSequence.size()+1][secondSequence.size()+1];
		//Set the base and the pure indel entries
		scoringMatrix[0][0] = new Cell(0, 0, 0);
		for(int i=1;i<scoringMatrix.length;i++){
			scoringMatrix[i][0] =  new Cell(indel * i, i, 0);
			scoringMatrix[i][0].addParent(scoringMatrix[i-1][0]);
		}
		for(int j=1;j<scoringMatrix[0].length;j++){
			scoringMatrix[0][j] = new Cell(indel * j, 0, j);
			scoringMatrix[0][j].addParent(scoringMatrix[0][j-1]);
		}
		//Now go through the remaining cells and calculate them row wise
		//Due to initializations and ordering we never have to worry about referencing an uninitialized or out of bounds cell
		for(int i=1;i<scoringMatrix.length;i++){
			for(int j=1;j<scoringMatrix[i].length;j++){
				int upScore = scoringMatrix[i][j-1].getScore() + indel;
				int leftScore = scoringMatrix[i-1][j].getScore() + indel;
				//If corresponding characters match give match score, otherwise give mismatch score
				int diagonalScore = (firstSequence.get(i-1).equals(secondSequence.get(j-1)) ? scoringMatrix[i-1][j-1].getScore() + match : scoringMatrix[i-1][j-1].getScore() + mismatch);
				//Find the maximum of the three scores
				int maxScore = Math.max(upScore, leftScore);
				maxScore = Math.max(maxScore, diagonalScore);
				scoringMatrix[i][j] = new Cell(maxScore, i, j);
				if(upScore == maxScore){
					scoringMatrix[i][j].addParent(scoringMatrix[i][j-1]);
				}
				if(leftScore == maxScore){
					scoringMatrix[i][j].addParent(scoringMatrix[i-1][j]);
				}
				if(diagonalScore == maxScore){
					scoringMatrix[i][j].addParent(scoringMatrix[i-1][j-1]);
				}
			}
		}
		//for(int i=0;i<scoringMatrix.length;i++){
		//			for(int j=0;j<scoringMatrix[i].length;j++){
		//				System.out.print(scoringMatrix[i][j].getScore() + " ");
		//			}
		//			System.out.println();
		//		}
		//Now, backtrack to find optimal sequence(s)
		//NW always runs from the bottom right of the scoring matrix
		LinkedList<Cell> startCell = new LinkedList<Cell>();
		startCell.add(scoringMatrix[scoringMatrix.length-1][scoringMatrix[0].length-1]);
		results = backTrack(startCell, firstSequence, secondSequence, match, indel, mismatch);
		return results;

	}

}
