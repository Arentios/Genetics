package com.arentios.gene.sequence;

import java.util.ArrayList;
import java.util.LinkedList;

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
					scoringMatrix[i][j].addParent(scoringMatrix[i][j-1]);
				}
				if(leftScore == maxScore){
					scoringMatrix[i][j].addParent(scoringMatrix[i-1][j]);
				}
				if(diagonalScore == maxScore){
					scoringMatrix[i][j].addParent(scoringMatrix[i-1][j-1]);
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
			ArrayList<SequenceAlignment> currAlignments = backTrack(currCell, new ArrayList<Character>(), new ArrayList<Character>(), firstSequence, secondSequence, match, indel, mismatch);
			for(SequenceAlignment alignment : currAlignments){
				results.add(alignment);
			}
		}

		return results;
	}

	private static ArrayList<SequenceAlignment> backTrack(Cell currCell, ArrayList<Character> sequenceOne, ArrayList<Character> sequenceTwo, ArrayList<Character> firstSequence, ArrayList<Character> secondSequence, Integer match, Integer indel, Integer mismatch){
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
