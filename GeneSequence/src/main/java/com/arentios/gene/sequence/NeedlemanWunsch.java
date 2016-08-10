package com.arentios.gene.sequence;

import java.util.ArrayList;

import com.arentios.gene.domain.Cell;
import com.arentios.gene.domain.Sequence;
import com.arentios.gene.domain.SequenceAlignment;
import com.arentios.gene.domain.SubstitutionMatrix;

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
	 * @throws Exception 
	 */
	public static ArrayList<SequenceAlignment> sequenceDNA(Sequence firstSequence, Sequence secondSequence, boolean singlePath, SubstitutionMatrix matrix) throws Exception{
		return sequence(firstSequence, secondSequence, SequenceConstants.DNA_NEEDLEMAN_WUNSCH_MATCH_DEFAULT, SequenceConstants.DNA_NEEDLEMAN_WUNSCH_MISMATCH_DEFAULT, SequenceConstants.DNA_NEEDLEMAN_WUNSCH_GAP_OPEN_DEFAULT, SequenceConstants.DNA_NEEDLEMAN_WUNSCH_GAP_EXTEND_DEFAULT, singlePath, matrix);
	}

	public static ArrayList<SequenceAlignment> sequenceProtein(Sequence firstSequence, Sequence secondSequence, boolean singlePath, SubstitutionMatrix matrix) throws Exception{
		return sequence(firstSequence, secondSequence, 0, 0, SequenceConstants.PROTEIN_NEEDLEMAN_WUNSCH_GAP_OPEN_DEFAULT, SequenceConstants.PROTEIN_NEEDLEMAN_WUNSCH_GAP_EXTEND_DEFAULT, singlePath, matrix);
	}





	/**
	 * Perform Needleman-Wunsch algorithm
	 * NYI: Finding multiple best paths
	 * @param firstGeneSequence
	 * @param secondGeneSequence
	 * @param match
	 * @param gapExtend
	 * @param mismatch
	 * @return
	 * @throws Exception 
	 */
	public static ArrayList<SequenceAlignment> sequence(Sequence firstGeneSequence, Sequence secondGeneSequence, double match, double mismatch, double gapOpen, double gapExtend, boolean singlePath, SubstitutionMatrix matrix) throws Exception{
		ArrayList<SequenceAlignment> results = new ArrayList<SequenceAlignment>();
		ArrayList<Character> firstSequence = firstGeneSequence.getSequence();
		ArrayList<Character> secondSequence = secondGeneSequence.getSequence();
		Cell[][] scoringMatrix = new Cell[firstSequence.size()+1][secondSequence.size()+1];
		//Set the base and the pure indel entries
		scoringMatrix[0][0] = new Cell(0.0, 0, 0);
		scoringMatrix[0][0].setGap(false);
		scoringMatrix[1][0] = new Cell(gapOpen, 0, 0);
		scoringMatrix[1][0].setGap(true);
		scoringMatrix[1][0].addParent(scoringMatrix[0][0]);
		scoringMatrix[0][1] = new Cell(gapOpen, 0, 0);
		scoringMatrix[0][1].setGap(true);
		scoringMatrix[0][1].addParent(scoringMatrix[0][0]);
		for(int i=2;i<scoringMatrix.length;i++){
			scoringMatrix[i][0] =  new Cell(scoringMatrix[i-1][0].getScore() + gapExtend, i, 0);
			scoringMatrix[i][0].setGap(true);
			scoringMatrix[i][0].addParent(scoringMatrix[i-1][0]);
		}
		for(int j=2;j<scoringMatrix[0].length;j++){
			scoringMatrix[0][j] = new Cell(scoringMatrix[0][j-1].getScore() + gapExtend, 0, j);
			scoringMatrix[0][j].setGap(true);
			scoringMatrix[0][j].addParent(scoringMatrix[0][j-1]);
		}
		//Now go through the remaining cells and calculate them row wise
		//Due to initializations and ordering we never have to worry about referencing an uninitialized or out of bounds cell
		for(int i=1;i<scoringMatrix.length;i++){
			for(int j=1;j<scoringMatrix[i].length;j++){
				double upScore;
				//Check to see if this is a gap open or gap extension
				if(scoringMatrix[i][j-1].isGap()){
					upScore = scoringMatrix[i][j-1].getScore() + gapExtend;
				}
				else{
					upScore = scoringMatrix[i][j-1].getScore() + gapOpen;
				}				
				double leftScore;
				if(scoringMatrix[i-1][j].isGap()){
					leftScore = scoringMatrix[i-1][j].getScore() + gapExtend;
				}
				else{
					leftScore = scoringMatrix[i-1][j].getScore() + gapOpen;
				}

				double diagonalScore;
				//If no substitution matrix, corresponding characters match give match score, otherwise give mismatch score
				if(matrix==null){
					diagonalScore = (firstSequence.get(i-1).equals(secondSequence.get(j-1)) ? scoringMatrix[i-1][j-1].getScore() + match : scoringMatrix[i-1][j-1].getScore() + mismatch);
				}
				//If there's a substitution matrix use that
				//Matrixes are assumed to have all values for now
				else{
					diagonalScore = scoringMatrix[i-1][j-1].getScore()+ matrix.lookupSubstitutionValue(firstSequence.get(i-1), secondSequence.get(j-1));
				}
				//Find the maximum of the three scores
				double maxScore = Math.max(upScore, leftScore);
				maxScore = Math.max(maxScore, diagonalScore);
				scoringMatrix[i][j] = new Cell(maxScore, i, j);
				//Bias towards matches, only add gap paths if match isn't also best
				//This helps runtime later since paths based on matches are much more useful 
				if(Math.abs(diagonalScore-maxScore)<0.00001){
					scoringMatrix[i][j].addParent(scoringMatrix[i-1][j-1]);
					scoringMatrix[i][j].setGap(false);
				}
				else{
					if(Math.abs(upScore-maxScore)<0.00001){
						scoringMatrix[i][j].addParent(scoringMatrix[i][j-1]);
					}
					if(Math.abs(leftScore-maxScore)<0.00001){
						scoringMatrix[i][j].addParent(scoringMatrix[i-1][j]);
					}
					scoringMatrix[i][j].setGap(true);
				}

			}
		}

		//Now, backtrack to find optimal sequence
		//NW always runs from the bottom right of the scoring matrix
		if(singlePath==true){
			results.add(backTrackNonBranching(scoringMatrix[scoringMatrix.length-1][scoringMatrix[0].length-1], firstSequence, secondSequence));
		}
		else{
			results = backTrack(scoringMatrix[scoringMatrix.length-1][scoringMatrix[0].length-1], firstSequence, secondSequence);
		}
		return results;

	}

}
