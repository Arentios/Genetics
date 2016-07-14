package com.arentios.gene.genesequence;

import org.junit.Test;

import com.arentios.gene.domain.Sequence;
import com.arentios.gene.domain.SequenceAlignment;
import com.arentios.gene.sequence.NeedlemanWunsch;
import com.arentios.gene.sequence.SmithWaterman;

import static org.junit.Assert.*;

import java.util.ArrayList;


/**
 * Unit test for sequence alignment algorithms.
 */

public class AlignmentTest 
{



	@Test
	public void testNeedlemanWunsch()
	{
		Sequence firstSequence = new Sequence("MPAVYG");
		Sequence secondSequence = new Sequence("MPAFYG");	
		
		ArrayList<SequenceAlignment> sequencedGenes = NeedlemanWunsch.sequence(firstSequence, secondSequence, TestConstants.NEEDLEMAN_WUNSCH_MATCH_DEFAULT, TestConstants.NEEDLEMAN_WUNSCH_INDEL_DEFAULT, TestConstants.NEEDLEMAN_WUNSCH_MISMATCH_DEFAULT);
		ArrayList<SequenceAlignment> testAlignment = new ArrayList<SequenceAlignment>();
		ArrayList<Sequence> testSequences = new ArrayList<Sequence>();
		testSequences.add(new Sequence("MPAV-YG"));
		testSequences.add(new Sequence("MPA-FYG"));
		testAlignment.add(new SequenceAlignment(testSequences));
		testSequences = new ArrayList<Sequence>();
		testSequences.add(new Sequence("MPA-VYG"));
		testSequences.add(new Sequence("MPAF-YG"));
		testAlignment.add(new SequenceAlignment(testSequences));
		testSequences = new ArrayList<Sequence>();
		testSequences.add(new Sequence("MPAVYG"));
		testSequences.add(new Sequence("MPAFYG"));
		testAlignment.add(new SequenceAlignment(testSequences));
		assertTrue(sequencedGenes != null && (sequencedGenes.size() == testAlignment.size()));
		for(int i=0;i<sequencedGenes.size();i++){
			assertTrue( sequencedGenes.get(i).equals(testAlignment.get(i)) );
		}
		
	}
	@Test
	public void testSmithWaterman(){
		Sequence firstSequence = new Sequence("MPAVYGARL");
		Sequence secondSequence = new Sequence("MPAFYGG");
		ArrayList<SequenceAlignment> sequencedGenes = SmithWaterman.sequence(firstSequence, secondSequence, TestConstants.SMITH_WATERMAN_MATCH_DEFAULT, TestConstants.SMITH_WATERMAN_INDEL_DEFAULT, TestConstants.SMITH_WATERMAN_MISMATCH_DEFAULT);
		ArrayList<SequenceAlignment> testAlignment = new ArrayList<SequenceAlignment>();
		ArrayList<Sequence> testSequences = new ArrayList<Sequence>();
		testSequences.add(new Sequence("MPAVYG"));
		testSequences.add(new Sequence("MPAFYG"));
		testAlignment.add(new SequenceAlignment(testSequences));
		assertTrue(sequencedGenes != null && (sequencedGenes.size() == testAlignment.size()));
		for(int i=0;i<sequencedGenes.size();i++){
			assertTrue( sequencedGenes.get(i).equals(testAlignment.get(i)) );
		}
	}

}
