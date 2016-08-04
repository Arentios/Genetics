package com.arentios.gene.service.domain;

/**
 * Class for JSON deserialization to contain a request for sequence alignment
 * @author Arentios
 *
 */
public class AlignmentRequest {

	private String requestType;
	private String[] sequences;
	private RequestOption[] options;
	
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String[] getSequences() {
		return sequences;
	}
	public void setSequences(String[] sequences) {
		this.sequences = sequences;
	}
	public RequestOption[] getOptions() {
		return options;
	}
	public void setOptions(RequestOption[] options) {
		this.options = options;
	}
	
	
}
