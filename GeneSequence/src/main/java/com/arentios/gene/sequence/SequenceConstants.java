package com.arentios.gene.sequence;

public class SequenceConstants {

	public static final double DNA_NEEDLEMAN_WUNSCH_MISMATCH_DEFAULT = -3;
	public static final double DNA_NEEDLEMAN_WUNSCH_MATCH_DEFAULT = 2;
	public static final double DNA_NEEDLEMAN_WUNSCH_GAP_OPEN_DEFAULT = -5;
	public static final double DNA_NEEDLEMAN_WUNSCH_GAP_EXTEND_DEFAULT = -2;
	public static final double PROTEIN_NEEDLEMAN_WUNSCH_GAP_OPEN_DEFAULT = -10;
	public static final double PROTEIN_NEEDLEMAN_WUNSCH_GAP_EXTEND_DEFAULT = -0.5;
	
	public static final double SMITH_WATERMAN_MISMATCH_DEFAULT = -1;
	public static final double SMITH_WATERMAN_MATCH_DEFAULT = 2;
	public static final double SMITH_WATERMAN_INDEL_DEFAULT = -1;
	
	public static final int BACKTRACK_MAX_SIZE = 10000;
	
	public static final char WILD_CARD_CHARACTER = '*';
	
	
	
	
}
