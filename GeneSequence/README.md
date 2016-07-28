# Genetics Sequencing Service

Service to handle sequencing strings of data. These can be DNA, RNA or proteins as the code is agnostic to the underlying data. This does mean that it's up to the user to provide valid and comparable inputs.

Current limitations cause the JVM to fail eventually. At default settings this happens at around 150 characters in length. This is a challenge of doing sequence alignment with multiple optimal matches; switching to an implementation that only returns a single optimal match results in no length constraints as such an implementation runs in place and does not have exponentially increasing memory requirements

Currently looking at changes to the code to increase the threshold of failure, but this is not a high priority.

NYI: Matching more than two sequences at a time. This is a major problem in the bioinformatics industry and primarily revolves around heuristic based work rather than something completely algorithmic like the dynamic programming solutions used for two sequences.

Sample service calls, assuming running locally on port 8080

http://localhost:8080/GeneSequence/alignment/needlemanwunsch/MPAVYGARLTTFED/MPAFYGGKLTTFEDX #Runs Needleman-Wunsch global alignment algorithm on two specified sequences

http://localhost:8080/GeneSequence/alignment/smithwaterman/MPAVYUTR/MPAFYGRTB #Runs Smith-Waterman local alignment algorithm on two specified sequences

http://localhost:8080/GeneSequence/alignment/ #Does an alignment using data specified in message body
Sample Body
{
  "requestType": "Needleman-Wunsch",
  "sequences" : [
    "MPAVYGARLTTFEDSEKESEFGYVRKVSGPVVVADGMAGAAMYELVRVGHDNLIGEIIRLEGDSATIQVYEETAGLMVNDPVLRTHKPLSVELGP","MPAFYGGKLTTFEDDEKESEYGYVRKVSGPVVVADGMAGAAMYELVRVGHDNLIGEIIRLEGDSATIQVYEETAGLTVNDPVLRTHKPLSVELGP" 
    ]
 
}