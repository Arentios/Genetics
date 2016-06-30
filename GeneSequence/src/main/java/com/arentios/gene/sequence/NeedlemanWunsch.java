package com.arentios.gene.sequence;

import java.util.ArrayList;

import com.arentios.gene.domain.Cell;
import com.arentios.gene.domain.GeneSequence;
import com.arentios.gene.domain.SequencedGenes;

/**
 * Class to implement the Needleman-Wunsch algorithm to use dynamic programming to find optimal gene sequences
 * @author Arentios
 *
 */
public class NeedlemanWunsch {

	
	
	static{
		
	}
	
	public static SequencedGenes sequence(GeneSequence firstSequence, GeneSequence secondSequence){
		return sequence(firstSequence, secondSequence, SequenceConstants.NEEDLEMAN_WUNSCH_MATCH_DEFAULT, SequenceConstants.NEEDLEMAN_WUNSCH_INDEL_DEFAULT, SequenceConstants.NEEDLEMAN_WUNSCH_MISMATCH_DEFAULT);
	}

	

	
	public static SequencedGenes sequence(GeneSequence firstGeneSequence, GeneSequence secondGeneSequence, Integer match, Integer indel, Integer mismatch){
		SequencedGenes results = new SequencedGenes();
		ArrayList<Character> firstSequence = firstGeneSequence.getSequence();
		ArrayList<Character> secondSequence = secondGeneSequence.getSequence();
		Cell[][] scoringMatrix = new Cell[firstSequence.size()+1][secondSequence.size()+1];
		//Set the base and the pure indel entries
		scoringMatrix[0][0] = new Cell();
		for(int i=1;i<scoringMatrix.length;i++){
			scoringMatrix[i][0] = new Cell(Integer.valueOf(indel * i));
		}
		for(int j=1;j<scoringMatrix[0].length;j++){
			scoringMatrix[0][j] = new Cell(Integer.valueOf(indel * j));
		}
		//Now go through the remaining cells and calculate them row wise
		//Due to initializations and ordering we never have to worry about referencing an uninitialized or out of bounds cell
		for(int i=1;i<scoringMatrix.length;i++){
			for(int j=1;j<scoringMatrix[i].length;j++){
				Integer aboveScore = scoringMatrix[i][j-1].getScore() + indel;
				Integer leftScore = scoringMatrix[i-1][j].getScore() + indel;
				//If corresponding characters match give match score, otherwise give mismatch score
				Integer diagonalScore = (firstSequence.get(i-1).equals(secondSequence.get(j-1))? match : mismatch);
				if(aboveScore > leftScore){
					if(aboveScore > diagonalScore){
						scoringMatrix[i][j] = new Cell(aboveScore, scoringMatrix[i][j-1]);
					}
					else{
						scoringMatrix[i][j] = new Cell(diagonalScore, scoringMatrix[i-1][j-1]);
					}
				}
				else if(leftScore > diagonalScore){
					scoringMatrix[i][j] = new Cell(leftScore, scoringMatrix[i-1][j]);
				}
				else{
					scoringMatrix[i][j] = new Cell(diagonalScore, scoringMatrix[i-1][j-1]);
				}
			}
		}
		
		//Now, backtrack to find optimal sequence(s)
		//Start by finding maximum value in a cell and every cell containing that value
		ArrayList<Cell> maxValueCells = new ArrayList<Cell>(); 
		Integer maxScore = -1000;
		for(int i = 0;i<scoringMatrix.length;i++){
			for(int j=0;j<scoringMatrix[i].length;j++){
				if(scoringMatrix[i][j].getScore() > maxScore){
					maxValueCells.clear(); //Clear old list of max value cells
					maxValueCells.add(scoringMatrix[i][j]);
					maxScore = scoringMatrix[i][j].getScore();
				}
				else if(scoringMatrix[i][j].getScore() == maxScore){
					maxValueCells.add(scoringMatrix[i][j]);
				}
			}
		}
		
		return results;
		
	}
	
}
