package com.arentios.gene.service;


import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arentios.gene.domain.Sequence;
import com.arentios.gene.domain.SequenceAlignment;
import com.arentios.gene.domain.SubstitutionMatrix;
import com.arentios.gene.sequence.NeedlemanWunsch;
import com.arentios.gene.sequence.SmithWaterman;
import com.arentios.gene.service.domain.AlignmentRequest;
import com.arentios.gene.service.domain.RequestOption;

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
			ArrayList<SequenceAlignment> alignments = NeedlemanWunsch.sequenceDNA(new Sequence(sequenceOne), new Sequence(sequenceTwo), ServiceConstants.SINGLE_TRACK_DEFAULT, null);
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


	@RequestMapping(value = "/dna", method = RequestMethod.POST)
	public ResponseEntity<String> RequestDNAAlignment(@RequestBody AlignmentRequest request){
		LOGGER.info("Attempting to DNA process alignment request");
		try{
			if(request.getRequestType().equalsIgnoreCase("Needleman-Wunsch")){
				if(request.getSequences().length != 2){
					return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Need exactly two sequences to run Needleman-Wunsch");
				}
				LOGGER.info("Attempting to process Needleman-Wunsch post request with sequences="+request.getSequences()[0]+","+request.getSequences()[1]);
				boolean singleTrack = ServiceConstants.SINGLE_TRACK_DEFAULT;
				for(RequestOption option : request.getOptions()){
					LOGGER.info("Got option="+option.getOption()+ " with value="+option.getValue());
					if(option.getOption().equalsIgnoreCase(("SingleTrack"))){
						singleTrack = Boolean.parseBoolean(option.getValue());
					}	

				}
				ArrayList<SequenceAlignment> alignments = NeedlemanWunsch.sequenceDNA(new Sequence(request.getSequences()[0]), new Sequence(request.getSequences()[1]), singleTrack, null);
				StringBuffer results = new StringBuffer();
				for(SequenceAlignment alignment : alignments){
					results.append(alignment);
				}
				return ResponseEntity.status(HttpStatus.OK).body(results.toString());
			}
			else if(request.getRequestType().equalsIgnoreCase("Smith-Waterman")){
				if(request.getSequences().length != 2){
					return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Need exactly two sequences to run Smith-Waterman");
				}
				ArrayList<SequenceAlignment> alignments = SmithWaterman.sequence(new Sequence(request.getSequences()[0]), new Sequence(request.getSequences()[1]));
				StringBuffer results = new StringBuffer();
				for(SequenceAlignment alignment : alignments){
					results.append(alignment);
				}
				return ResponseEntity.status(HttpStatus.OK).body(results.toString());
			}
			else return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Unknown alignment request type");			
		}
		catch(Exception e){
			LOGGER.error("Failed to RESTfully run request alignment");
			LOGGER.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Failed to run request alignment");
		}

	}

	@RequestMapping(value = "/protein", method = RequestMethod.POST)
	public ResponseEntity<String> RequestProteinAlignment(@RequestBody AlignmentRequest request){
		LOGGER.info("Attempting to process protein alignment request");
		try{
			if(request.getRequestType().equalsIgnoreCase("Needleman-Wunsch")){
				if(request.getSequences().length != 2){
					return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Need exactly two sequences to run Needleman-Wunsch");
				}
				LOGGER.info("Attempting to process Needleman-Wunsch post request with sequences="+request.getSequences()[0]+","+request.getSequences()[1]);
				boolean singleTrack = ServiceConstants.SINGLE_TRACK_DEFAULT;
				String  substitutionMatrix = null;
				for(RequestOption option : request.getOptions()){
					LOGGER.info("Got option="+option.getOption()+ " with value="+option.getValue());
					if(option.getOption().equalsIgnoreCase(("SingleTrack"))){
						singleTrack = Boolean.parseBoolean(option.getValue());
					}	
					if(option.getOption().equalsIgnoreCase(("SubstitutionMatrix"))){
						substitutionMatrix = option.getValue();
					}	
				}
				if(substitutionMatrix == null){
					LOGGER.info("No substitution matrix defined in request, using default");
					substitutionMatrix = ServiceConstants.DEFAULT_SUBSTITUTION_MATRIX;
				}
				try{
					SubstitutionMatrix matrix = AlignmentSystemTools.loadSubstitutionMatrix(ServiceConstants.MATRIX_FILE_PATH+substitutionMatrix+".xml");
					ArrayList<SequenceAlignment> alignments = NeedlemanWunsch.sequenceProtein(new Sequence(request.getSequences()[0]), new Sequence(request.getSequences()[1]), singleTrack, matrix);
					StringBuffer results = new StringBuffer();
					for(SequenceAlignment alignment : alignments){
						results.append(alignment);
					}
					return ResponseEntity.status(HttpStatus.OK).body(results.toString());
				} catch(Exception e){
					e.printStackTrace();
				}
				return null;
			}
			else if(request.getRequestType().equalsIgnoreCase("Smith-Waterman")){
				if(request.getSequences().length != 2){
					return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Need exactly two sequences to run Smith-Waterman");
				}
				ArrayList<SequenceAlignment> alignments = SmithWaterman.sequence(new Sequence(request.getSequences()[0]), new Sequence(request.getSequences()[1]));
				StringBuffer results = new StringBuffer();
				for(SequenceAlignment alignment : alignments){
					results.append(alignment);
				}
				return ResponseEntity.status(HttpStatus.OK).body(results.toString());
			}
			else return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Unknown alignment request type");			
		}
		catch(Exception e){
			LOGGER.error("Failed to RESTfully run request alignment");
			LOGGER.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Failed to run request alignment");
		}

	}


}