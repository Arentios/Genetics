package com.arentios.gene.domain;

import com.arentios.gene.sequence.NeedlemanWunsch;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        GeneSequence firstSequence = new GeneSequence("GGGGGT");
        GeneSequence secondSequence = new GeneSequence("GAGUGT");
        SequencedGenes sequenced = NeedlemanWunsch.sequence(firstSequence, secondSequence);
        System.out.println(sequenced);
    }
}
