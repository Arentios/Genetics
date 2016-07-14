package com.arentios.gene.service;


import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arentios.gene.domain.Sequence;
import com.arentios.gene.domain.SequenceAlignment;
import com.arentios.gene.sequence.NeedlemanWunsch;
import com.arentios.gene.sequence.SmithWaterman;

/**
 * Controller for RESTful services accessing person data
 * @author Arentios
 *
 */

@RestController
@RequestMapping("/alignment")
public class AlignmentController {

	@Autowired
	private static Logger LOGGER = LoggerFactory.getLogger(AlignmentController.class);
	
	@RequestMapping(value = "/needlemanwunsch/{sequenceOne}/{sequenceTwo}", method = RequestMethod.GET)
	public ResponseEntity<String> needlemanWunsch(@PathVariable String sequenceOne, @PathVariable String sequenceTwo){
		LOGGER.info("Attempting to run Needleman-Wunsch with default scoring on "+sequenceOne+" and " + sequenceTwo);
		try{
			ArrayList<SequenceAlignment> alignments = NeedlemanWunsch.sequence(new Sequence(sequenceOne), new Sequence(sequenceTwo));
			StringBuffer results = new StringBuffer();
			for(SequenceAlignment alignment : alignments){
				results.append(alignment);
			}
			return ResponseEntity.status(HttpStatus.OK).body(results.toString());
		}
		catch(Exception e){
			LOGGER.error("Failed to RESTfully run Needleman-Wunsch");
			LOGGER.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Failed to run Needleman-Wunsch");
		}
		
	}
	
	@RequestMapping(value = "/smithwaterman/{sequenceOne}/{sequenceTwo}", method = RequestMethod.GET)
	public ResponseEntity<String> smithWaterman(@PathVariable String sequenceOne, @PathVariable String sequenceTwo){
		LOGGER.info("Attempting to run Smith-Waterman with default scoring on "+sequenceOne+" and " + sequenceTwo);
		try{
			ArrayList<SequenceAlignment> alignments = SmithWaterman.sequence(new Sequence(sequenceOne), new Sequence(sequenceTwo));
			StringBuffer results = new StringBuffer();
			for(SequenceAlignment alignment : alignments){
				results.append(alignment);
			}
			return ResponseEntity.status(HttpStatus.OK).body(results.toString());
		}
		catch(Exception e){
			LOGGER.error("Failed to RESTfully run Smith-Waterman");
			LOGGER.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Failed to run Smith-Waterman");
		}
		
	}
	
	
}