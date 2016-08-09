package com.arentios.gene.genesequence;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.arentios.gene.domain.Sequence;
import com.arentios.gene.domain.SequenceAlignment;
import com.arentios.gene.domain.SubstitutionMatrix;
import com.arentios.gene.sequence.NeedlemanWunsch;
import com.arentios.gene.sequence.SmithWaterman;
import com.arentios.gene.service.AlignmentSystemTools;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


/**
 * Unit test for sequence alignment algorithms.
 */

public class AlignmentTest 
{



	@Test
	public void testNeedlemanWunschBranching()
	{
		Sequence firstSequence = new Sequence("gcggcaggcttaacacatgcaagtcgaggggtatatgtcttcggata");
		Sequence secondSequence = new Sequence("gcggcgtgcttaacacatgcaagtcgaacgatgacccggtgcttgca");	
		
		ArrayList<SequenceAlignment> sequencedGenes = NeedlemanWunsch.sequence(firstSequence, secondSequence, TestConstants.NEEDLEMAN_WUNSCH_MATCH_DEFAULT, TestConstants.NEEDLEMAN_WUNSCH_MISMATCH_DEFAULT, TestConstants.NEEDLEMAN_WUNSCH_GAP_OPEN_DEFAULT, TestConstants.NEEDLEMAN_WUNSCH_GAP_EXTEND_DEFAULT, false, null);
		ArrayList<SequenceAlignment> testAlignment = new ArrayList<SequenceAlignment>();
		ArrayList<Sequence> testSequences = new ArrayList<Sequence>();
		testSequences.add(new Sequence("gcggcaggcttaacacatgcaagtcgaggggt-a-tatgt-cttcggata"));
		testSequences.add(new Sequence("gcggcgtgcttaacacatgcaagtcgaacgatgacccggtgctt--g-ca"));
		testAlignment.add(new SequenceAlignment(testSequences));
		assertTrue(sequencedGenes != null && (sequencedGenes.size() == testAlignment.size()));
		for(int i=0;i<sequencedGenes.size();i++){
			assertTrue( sequencedGenes.get(i).equals(testAlignment.get(i)) );
		}
		
	}
	
	@Test
	public void testNeedlemanWunschSingle()
	{
		Sequence firstSequence = new Sequence("gcggcaggcttaacacatgcaagtcgaggggtatatgtcttcggata");
		Sequence secondSequence = new Sequence("gcggcgtgcttaacacatgcaagtcgaacgatgacccggtgcttgca");	
		
		ArrayList<SequenceAlignment> sequencedGenes = NeedlemanWunsch.sequence(firstSequence, secondSequence, TestConstants.NEEDLEMAN_WUNSCH_MATCH_DEFAULT, TestConstants.NEEDLEMAN_WUNSCH_MISMATCH_DEFAULT, TestConstants.NEEDLEMAN_WUNSCH_GAP_OPEN_DEFAULT, TestConstants.NEEDLEMAN_WUNSCH_GAP_EXTEND_DEFAULT, true, null);
		ArrayList<SequenceAlignment> testAlignment = new ArrayList<SequenceAlignment>();
		ArrayList<Sequence> testSequences = new ArrayList<Sequence>();
		testSequences.add(new Sequence("gcggcaggcttaacacatgcaagtcgaggggt-a-tatgt-cttcggata"));
		testSequences.add(new Sequence("gcggcgtgcttaacacatgcaagtcgaacgatgacccggtgctt--g-ca"));
		testAlignment.add(new SequenceAlignment(testSequences));
		assertTrue(sequencedGenes != null && (sequencedGenes.size() == testAlignment.size()));
		for(int i=0;i<sequencedGenes.size();i++){
			assertTrue( sequencedGenes.get(i).equals(testAlignment.get(i)) );
		}
		
	}
	@Test
	public void testSmithWaterman(){
		Sequence firstSequence = new Sequence("gcggcaggcttaacacatgcaagtcgaggggtatatgtcttcggata");
		Sequence secondSequence = new Sequence("gcggcgtgcttaacacatgcaagtcgaacgatgacccggtgcttgca");
		ArrayList<SequenceAlignment> sequencedGenes = SmithWaterman.sequence(firstSequence, secondSequence, TestConstants.SMITH_WATERMAN_MATCH_DEFAULT, TestConstants.SMITH_WATERMAN_INDEL_DEFAULT, TestConstants.SMITH_WATERMAN_MISMATCH_DEFAULT);
		ArrayList<SequenceAlignment> testAlignment = new ArrayList<SequenceAlignment>();
		ArrayList<Sequence> testSequences = new ArrayList<Sequence>();
		testSequences.add(new Sequence("gcggcag-gcttaacacatgcaagtcgaggggtat-a---tgt-cttcgga"));
		testSequences.add(new Sequence("gcggc-gtgcttaacacatgcaagtcga-acg-atgacccggtgctt-gca"));
		testAlignment.add(new SequenceAlignment(testSequences));
		assertTrue(sequencedGenes != null && (sequencedGenes.size() == testAlignment.size()));
		for(int i=0;i<sequencedGenes.size();i++){
			assertTrue( sequencedGenes.get(i).equals(testAlignment.get(i)) );
		}
	}
	
	@Test
	public void testLoadScoringMatrix(){
		SubstitutionMatrix matrix = AlignmentSystemTools.loadSubstitutionMatrix("pam250.xml");
		assertTrue(matrix.lookupSubstitutionValue('V', 'N') == -2);
	}

}
