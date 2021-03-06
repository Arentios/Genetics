package com.arentios.gene.sequence;

import java.util.ArrayList;

import com.arentios.gene.domain.Cell;
import com.arentios.gene.domain.Sequence;
import com.arentios.gene.domain.SequenceAlignment;

/**
 * Class to implement Smith-Waterman algorithm for local sequence alignment
 * @author Arentios
 *
 */
public class SmithWaterman extends DynamicProgrammingSequencer {

	/**
	 * Wrapper to pass default values to sequencing function
	 * @param firstSequence
	 * @param secondSequence
	 * @return
	 */
	public static ArrayList<SequenceAlignment> sequence(Sequence firstSequence, Sequence secondSequence){
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
	public static ArrayList<SequenceAlignment> sequence(Sequence firstGeneSequence, Sequence secondGeneSequence, double match, double indel, double mismatch){
		ArrayList<SequenceAlignment> results = new ArrayList<SequenceAlignment>();
		ArrayList<Character> firstSequence = firstGeneSequence.getSequence();
		ArrayList<Character> secondSequence = secondGeneSequence.getSequence();
		Cell[][] scoringMatrix = new Cell[firstSequence.size()+1][secondSequence.size()+1];
		//Set the base and boundary entries
		scoringMatrix[0][0] = new Cell(0.0, 0, 0);
		for(int i=1;i<scoringMatrix.length;i++){
			scoringMatrix[i][0] = new Cell(0.0, i, 0);
		}
		for(int j=1;j<scoringMatrix[0].length;j++){
			scoringMatrix[0][j] = new Cell(0.0, 0, j);
		}
		//Set scores and links
		double currMaxScore = 0.0;
		ArrayList<Cell> maxScores = new ArrayList<Cell>(); //Keep track of all cells with maximum score
		for(int i=1;i<scoringMatrix.length;i++){
			for(int j=1;j<scoringMatrix[0].length;j++){
				//Find the max score and every direction that matches that score and add them to the matrix
				double upScore = scoringMatrix[i][j-1].getScore() + indel;
				double leftScore = scoringMatrix[i-1][j].getScore() + indel;
				double diagonalScore = (firstSequence.get(i-1).equals(secondSequence.get(j-1)) ? scoringMatrix[i-1][j-1].getScore() + match : scoringMatrix[i-1][j-1].getScore() + mismatch);
				double maxScore = Math.max(upScore, leftScore);
				maxScore = Math.max(maxScore, diagonalScore);
				scoringMatrix[i][j] = new Cell(maxScore, i, j);
				//Only add parents if max score is above zero since that's the termination condition
				if(maxScore > 0){
					if(Math.abs(upScore-maxScore)<0.00001){
						scoringMatrix[i][j].addParent(scoringMatrix[i][j-1]);
					}
					if(Math.abs(leftScore-maxScore)<0.00001){
						scoringMatrix[i][j].addParent(scoringMatrix[i-1][j]);
					}
					if(Math.abs(diagonalScore-maxScore)<0.00001){
						scoringMatrix[i][j].addParent(scoringMatrix[i-1][j-1]);
					}
				}
				if(maxScore > currMaxScore){
					currMaxScore = maxScore;
					maxScores.clear();
					maxScores.add(scoringMatrix[i][j]);
				}
				else if(maxScore == currMaxScore){
					maxScores.add(scoringMatrix[i][j]);
				}
			}
		}

		
		for(Cell currCell : maxScores){
			ArrayList<SequenceAlignment> currAlignments = backTrack(currCell, firstSequence, secondSequence);
			for(SequenceAlignment alignment : currAlignments){
				results.add(alignment);
			}
		}
		
		return results;
	}


}
