package com.arentios.gene.sequence;

import java.util.ArrayList;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arentios.gene.cache.BackTrackCacheManager;
import com.arentios.gene.domain.BackTrackData;
import com.arentios.gene.domain.Cell;
import com.arentios.gene.domain.Sequence;
import com.arentios.gene.domain.SequenceAlignment;

/**
 * Parent class used by dynamic sequence based alignment protocols
 * @author Arentios
 *
 */
public class DynamicProgrammingSequencer {

	private static Logger LOGGER = LoggerFactory.getLogger(DynamicProgrammingSequencer.class);

	/**
	 * Queue based solution to backtracking for dynamic programming 
	 * Since long sequences lead to exponentially more optimal alignments score wise, we need to pair down to one 'best' sequence
	 * This is unfortunate, but the vast majority of optimal alignments are not different from each other in a meaningful way in the macro sense
	 * The current implementation is to do this based on match score. IE the sequencing with the most matched characters
	 * Another option frequently used is to not branch execution and simply use the first alignment seen
	 * This is considerably faster, but was not chosen to allow for better expansion at a later time
	 * And also, this implementation presents more implementation challenges to address and since this is an exercise... :)
	 * Of course knowing when to use a slightly less optimal but much faster to code and maintain solution is also an important skill!
	 * 
	 * Note that this returns an ArrayList rather than a single SequenceAlignment to keep functionality streamlined for later extension
	 * For example, returning the top N alignments
	 * Used by both Needleman-Wunsch and Smith-Waterman

	 * @param cellToProcess
	 * @param firstSequence
	 * @param secondSequence
	 * @return
	 */
	protected static ArrayList<SequenceAlignment> backTrack(Cell cellToProcess,ArrayList<Character> firstSequence, ArrayList<Character> secondSequence){
		ArrayList<SequenceAlignment> results = new ArrayList<SequenceAlignment>();
		LinkedList<BackTrackData> backTrackQueue = new LinkedList<BackTrackData>();
		BackTrackCacheManager cacheManager = BackTrackCacheManager.getInstance();
		int maxMatches = 0; //Number of matches in current best match
		int possibleSequences = 0; //Number of total sequences explored, for logging and auditing purposes
		try{
			//Build initial cell to kick off the process
			BackTrackData startData = new BackTrackData();
			startData.setCell(cellToProcess);
			startData.setSequenceOne(new ArrayList<Character>());
			startData.setSequenceTwo(new ArrayList<Character>());
			backTrackQueue.add(startData);

			while(backTrackQueue.size() > 0){
				BackTrackData data = backTrackQueue.remove();
				Cell currCell = data.getCell();
				LinkedList<Cell> parents = currCell.getParents();
				if(parents != null){
					//For each parent create a new back track entry in the queue
					for(Cell parent: parents){
						BackTrackData newData = new BackTrackData();
						newData.setCell(parent);
						ArrayList<Character> newSequenceOne = new ArrayList<Character>(data.getSequenceOne());
						ArrayList<Character> newSequenceTwo = new ArrayList<Character>(data.getSequenceTwo());
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
						newData.setSequenceOne(newSequenceOne);
						newData.setSequenceTwo(newSequenceTwo);
						//If the queue is now too big add the current queue to the cache and clear it before adding a new element
						if(backTrackQueue.size() > SequenceConstants.BACKTRACK_MAX_SIZE){
							LOGGER.info("Queue has exceeded max size, moving to cache");
							//Split the queue into two halves so that it doesn't immediately excede max length again the moment it's retrieved			
							cacheManager.putBackTrackData(new LinkedList<BackTrackData>(backTrackQueue.subList(0, backTrackQueue.size()/2)));
							cacheManager.putBackTrackData(new LinkedList<BackTrackData>(backTrackQueue.subList(backTrackQueue.size()/2, backTrackQueue.size())));
							backTrackQueue.clear();
						}
						backTrackQueue.add(newData);
					}
				}
				//If there are no parents we're at the terminus, add the sequences to the results
				else{
					possibleSequences++;
					//See if this is a new best alignment and keep it if so
					int matches = calculateMatches(data.getSequenceOne(), data.getSequenceTwo());
					if(matches > maxMatches){
						results.clear();
						SequenceAlignment sequencedPair = new SequenceAlignment();
						Sequence resultSequence = new Sequence();
						resultSequence.setSequence(data.getSequenceOne());
						sequencedPair.addSequence(resultSequence);
						resultSequence = new Sequence();
						resultSequence.setSequence(data.getSequenceTwo());
						sequencedPair.addSequence(resultSequence);
						results.add(sequencedPair);
						maxMatches = matches;
					}

				}
				//If the queue is now empty check to see if we have anything cached, if so then add a set of cached data to the queue
				if(backTrackQueue.size() == 0){
					LOGGER.info("Queue empty, checking cache");
					Integer cacheKey = cacheManager.getKey();
					if(cacheKey != null){
						LOGGER.info("Found cache data, adding to queue");
						backTrackQueue.addAll(cacheManager.getBackTrackData(cacheKey));
						LOGGER.info("Cached data added, queueSize="+backTrackQueue.size());
					}
				}

			}
		}
		catch(Exception e){
			e.printStackTrace();
		}		
		LOGGER.info("Finished back track, total number of sequences calculated="+possibleSequences+" and maxMatches="+maxMatches);
		return results;
	}

	/**
	 * Helper function to calculate the number of matches between two sequences
	 * @param sequenceOne
	 * @param sequenceTwo
	 * @return
	 */
	private static int calculateMatches(ArrayList<Character> sequenceOne, ArrayList<Character> sequenceTwo){
		int match = 0;
		for(int i=0;i<sequenceOne.size();i++){
			//We can use one index since both sequences must be the same length by definition
			//Matching gaps DO NOT count as matches
			if((sequenceOne.get(i) != '-') && sequenceOne.get(i).equals(sequenceTwo.get(i))){
				match++;
			}
		}
		return match;
	}


	/**
	 * Single path based solution to backtracking
	 * This will only ever consider a single path through backtracking, making it much faster than the queue based approach but less valuable in terms of results
	 * @param cellToProcess
	 * @param firstSequence
	 * @param secondSequence
	 * @return
	 */
	protected static SequenceAlignment backTrackNonBranching(Cell cellToProcess,ArrayList<Character> firstSequence, ArrayList<Character> secondSequence){

		try{
			ArrayList<Character> sequenceOne = new ArrayList<Character>();
			ArrayList<Character> sequenceTwo = new ArrayList<Character>();
			LinkedList<Cell> parents = cellToProcess.getParents();
			while(parents != null){
				//Since this back track is blind, just take the first parent from the list
				Cell parent = parents.get(0);
				//Need to figure out which character to add to each sequence
				if(parent.getI() != cellToProcess.getI()){
					//Diagonal movement
					if(parent.getJ() != cellToProcess.getJ()){
						sequenceOne.add(0,firstSequence.get(parent.getI()));
						sequenceTwo.add(0,secondSequence.get(parent.getJ()));
					}
					//Leftwards movement
					else{
						sequenceOne.add(0,firstSequence.get(parent.getI()));
						sequenceTwo.add(0,'-');
					}
				}
				//Upwards movement by default
				else{
					sequenceOne.add(0,'-');
					sequenceTwo.add(0,secondSequence.get(parent.getJ()));
				}
				cellToProcess = parent;
				parents = cellToProcess.getParents();
			}
			//If there are no parents we're at the terminus, return the result
			SequenceAlignment sequencedPair = new SequenceAlignment();
			Sequence resultSequence = new Sequence();
			resultSequence.setSequence(sequenceOne);
			sequencedPair.addSequence(resultSequence);
			resultSequence = new Sequence();
			resultSequence.setSequence(sequenceTwo);
			sequencedPair.addSequence(resultSequence);
			return sequencedPair;
		}
		catch(Exception e){
			e.printStackTrace();
		}		
		return null;
	}

}
