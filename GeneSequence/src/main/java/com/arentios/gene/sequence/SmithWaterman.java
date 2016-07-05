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
		scoringMatrix[0][0] = new Cell(0, 0, 0);
		for(int i=1;i<scoringMatrix.length;i++){
			scoringMatrix[i][0] = new Cell(0, i, 0);
		}
		for(int j=1;j<scoringMatrix[0].length;j++){
			scoringMatrix[0][j] = new Cell(0, 0, j);
		}
		//Set scores and links
		int currMaxScore = 0;
		ArrayList<Cell> maxScores = new ArrayList<Cell>(); //Keep track of all cells with maximum score
		for(int i=1;i<scoringMatrix.length;i++){
			for(int j=1;j<scoringMatrix[0].length;j++){
				//Find the max score and every direction that matches that score and add them to the matrix
				int upScore = scoringMatrix[i][j-1].getScore() + indel;
				int leftScore = scoringMatrix[i-1][j].getScore() + indel;
				int diagonalScore = (firstSequence.get(i-1).equals(secondSequence.get(j-1)) ? scoringMatrix[i-1][j-1].getScore() + match : scoringMatrix[i-1][j-1].getScore() + mismatch);
				int maxScore = Math.max(upScore, leftScore);
				maxScore = Math.max(maxScore, diagonalScore);
				scoringMatrix[i][j] = new Cell(maxScore, i, j);
				if(upScore == maxScore){
					scoringMatrix[i][j].addParent(scoringMatrix[i][j-1], "Up");
				}
				if(leftScore == maxScore){
					scoringMatrix[i][j].addParent(scoringMatrix[i-1][j], "Left");
				}
				if(diagonalScore == maxScore){
					scoringMatrix[i][j].addParent(scoringMatrix[i-1][j-1], "Diagonal");
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
		
//		for(int i=0;i<scoringMatrix.length;i++){
//			for(int j=0;j<scoringMatrix[i].length;j++){
//				System.out.print(scoringMatrix[i][j].getScore() + " ");
//			}
//			System.out.println();
//		}
		for(Cell currCell : maxScores){
			ArrayList<SequenceAlignment> currAlignments = backTrack(scoringMatrix, currCell.getI(), currCell.getJ(), new ArrayList<Character>(), new ArrayList<Character>(), firstSequence, secondSequence, match, indel, mismatch);
			for(SequenceAlignment alignment : currAlignments){
				results.add(alignment);
			}
		}
		
		return results;
	}
	
	private static ArrayList<SequenceAlignment> backTrack(Cell[][] scoringMatrix, int i, int j, ArrayList<Character> sequenceOne, ArrayList<Character> sequenceTwo, ArrayList<Character> firstSequence, ArrayList<Character> secondSequence, Integer match, Integer indel, Integer mismatch){
		ArrayList<SequenceAlignment> results = new ArrayList<SequenceAlignment>();
		
		while(scoringMatrix[i][j].getScore() != 0){
			//System.out.println(i + " " + j);
			//Can only match if not in the first row or column
			//TODO: Because we're not handling multiple optimal alignments, match/mismatch paths are always prioritized and indels being in the second sequence are more often inserted mid-sequence
			if(i > 0 && j > 0 && (((scoringMatrix[i][j].getScore() - match) == scoringMatrix[i-1][j-1].getScore()) || ((scoringMatrix[i][j].getScore() - mismatch) == scoringMatrix[i-1][j-1].getScore()))){
				sequenceOne.add(0,firstSequence.get(i-1));
				sequenceTwo.add(0,secondSequence.get(j-1));
				i = i-1;
				j = j-1;
			}
			//Likewise can only move left if not in the first column
			else if(i > 0 && (scoringMatrix[i][j].getScore() - indel) == scoringMatrix[i-1][j].getScore()){
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
