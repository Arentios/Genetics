package com.arentios.gene.sequence;

import java.util.ArrayList;

import com.arentios.gene.domain.GeneSequence;
import com.arentios.gene.domain.SequenceAlignment;

/**
 * Class to implement the Needleman-Wunsch algorithm to use dynamic programming to find optimal gene sequences
 * @author Arentios
 *
 */
public class NeedlemanWunsch {



	static{

	}

	public static ArrayList<SequenceAlignment> sequence(GeneSequence firstSequence, GeneSequence secondSequence){
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
	public static ArrayList<SequenceAlignment> sequence(GeneSequence firstGeneSequence, GeneSequence secondGeneSequence, Integer match, Integer indel, Integer mismatch){
		ArrayList<SequenceAlignment> results = new ArrayList<SequenceAlignment>();
		ArrayList<Character> firstSequence = firstGeneSequence.getSequence();
		ArrayList<Character> secondSequence = secondGeneSequence.getSequence();
		Integer[][] scoringMatrix = new Integer[firstSequence.size()+1][secondSequence.size()+1];
		//Set the base and the pure indel entries
		scoringMatrix[0][0] = 0;
		for(int i=1;i<scoringMatrix.length;i++){
			scoringMatrix[i][0] = indel * i;
		}
		for(int j=1;j<scoringMatrix[0].length;j++){
			scoringMatrix[0][j] = indel * j;
		}
		//Now go through the remaining cells and calculate them row wise
		//Due to initializations and ordering we never have to worry about referencing an uninitialized or out of bounds cell
		for(int i=1;i<scoringMatrix.length;i++){
			for(int j=1;j<scoringMatrix[i].length;j++){
				Integer upScore = scoringMatrix[i][j-1] + indel;
				Integer leftScore = scoringMatrix[i-1][j] + indel;
				//If corresponding characters match give match score, otherwise give mismatch score
				Integer diagonalScore = (firstSequence.get(i-1).equals(secondSequence.get(j-1)) ? scoringMatrix[i-1][j-1] + match : scoringMatrix[i-1][j-1] + mismatch);
				//Find the maximum of the three scores
				Integer score = Math.max(upScore, leftScore);
				score = Math.max(score, diagonalScore);
				scoringMatrix[i][j] = score;
			}
		}

		//Now, backtrack to find optimal sequence(s)
		int i = scoringMatrix.length-1;
		int j = scoringMatrix[0].length-1;
		ArrayList<Character> sequenceOne = new ArrayList<Character>();
		ArrayList<Character> sequenceTwo = new ArrayList<Character>();

		results = backTrack(scoringMatrix, i, j, sequenceOne, sequenceTwo, firstSequence, secondSequence, match, indel, mismatch);
		return results;

	}

	/**
	 * Recursive function for backtracking to allow for multiple optimal sequences
	 * NOTE: Since optimal best paths are somewhat uncommon we're using selective recursion only at splits rather than recurring every iteration
	 * On average this should use less memory and perform faster
	 * @param scoringMatrix
	 * @param i
	 * @param j
	 * @param sequenceOne
	 * @param sequenceTwo
	 * @param firstSequence
	 * @param secondSequence
	 * @param match
	 * @param indel
	 * @param mismatch
	 * @return
	 */
	private static ArrayList<SequenceAlignment> backTrack(Integer[][] scoringMatrix, int i, int j, ArrayList<Character> sequenceOne, ArrayList<Character> sequenceTwo, ArrayList<Character> firstSequence, ArrayList<Character> secondSequence, Integer match, Integer indel, Integer mismatch){
		ArrayList<SequenceAlignment> results = new ArrayList<SequenceAlignment>();
		while((i > 0) || (j > 0)){
			//System.out.println(i + " " + j);
			//Can only match if not in the first row or column
			//TODO: Because we're not handling multiple optimal alignments, match/mismatch paths are always prioritized and indels being in the second sequence are more often inserted mid-sequence
			if(i > 0 && j > 0 && (((scoringMatrix[i][j] - match) == scoringMatrix[i-1][j-1]) || ((scoringMatrix[i][j] - mismatch) == scoringMatrix[i-1][j-1]))){
				sequenceOne.add(0,firstSequence.get(i-1));
				sequenceTwo.add(0,secondSequence.get(j-1));
				i = i-1;
				j = j-1;
			}
			//Likewise can only move left if not in the first column
			else if(i > 0 && (scoringMatrix[i][j] - indel) == scoringMatrix[i-1][j]){
				sequenceOne.add(0,firstSequence.get(i-1));
				sequenceTwo.add(0,'-');
				i = i-1;
			}
			//No need for a check here since we know the matrix was generated successfully so SOME move is valid
			else{
				sequenceOne.add(0,'-');
				sequenceTwo.add(0,secondSequence.get(j-1));
				j = j-1;
			}
		}
		SequenceAlignment sequencedPair = new SequenceAlignment();
		GeneSequence resultSequence = new GeneSequence();
		resultSequence.setSequence(sequenceOne);
		sequencedPair.addSequence(resultSequence);
		resultSequence = new GeneSequence();
		resultSequence.setSequence(sequenceTwo);
		sequencedPair.addSequence(resultSequence);
		results.add(sequencedPair);

		return results;
	}

}
