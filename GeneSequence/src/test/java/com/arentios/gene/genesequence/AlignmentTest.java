package com.arentios.gene.genesequence;

import org.junit.Test;

import com.arentios.gene.domain.Sequence;
import com.arentios.gene.domain.SequenceAlignment;
import com.arentios.gene.domain.SubstitutionMatrix;
import com.arentios.gene.sequence.NeedlemanWunsch;
import com.arentios.gene.sequence.SmithWaterman;
import com.arentios.gene.service.AlignmentSystemTools;

import static org.junit.Assert.*;


import java.util.ArrayList;




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
		try{
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
		}catch(Exception e){
			assertTrue(false);
		}

	}

	@Test
	public void testNeedlemanWunschSingle()
	{
		Sequence firstSequence = new Sequence("gcggcaggcttaacacatgcaagtcgaggggtatatgtcttcggata");
		Sequence secondSequence = new Sequence("gcggcgtgcttaacacatgcaagtcgaacgatgacccggtgcttgca");	
		try{
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
		}catch(Exception e){
			assertTrue(false);
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
		
		try{
			assertTrue(matrix.lookupSubstitutionValue('V', 'N') == -2);
		}catch(Exception e){
			assertTrue(false);
		}
	}

	@Test
	public void testSaveScoringMatrix(){
		SubstitutionMatrix matrix = new SubstitutionMatrix();
		matrix.addSubstitutionMatrixValue('A','A',4);
		matrix.addSubstitutionMatrixValue('R','A',-1);
		matrix.addSubstitutionMatrixValue('N','A',-2);
		matrix.addSubstitutionMatrixValue('D','A',-2);
		matrix.addSubstitutionMatrixValue('C','A',0);
		matrix.addSubstitutionMatrixValue('Q','A',-1);
		matrix.addSubstitutionMatrixValue('E','A',-1);
		matrix.addSubstitutionMatrixValue('G','A',0);
		matrix.addSubstitutionMatrixValue('H','A',-2);
		matrix.addSubstitutionMatrixValue('I','A',-1);
		matrix.addSubstitutionMatrixValue('L','A',-1);
		matrix.addSubstitutionMatrixValue('K','A',-1);
		matrix.addSubstitutionMatrixValue('M','A',-1);
		try{
			AlignmentSystemTools.serializeSubstitutionMatrix("test", matrix);
		}
		catch(Exception e){
			assertTrue(false);
		}
	}


}
