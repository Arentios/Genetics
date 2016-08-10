package com.arentios.gene.service;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.arentios.gene.domain.SubstitutionMatrix;

/**
 * Various tools used to interact with system resources for sequence alignment
 * @author Arentios
 *
 */
public class AlignmentSystemTools {

	private static Logger LOGGER = LoggerFactory.getLogger(AlignmentSystemTools.class);
	
	/**
	 * Take in the specified filename and return the substitution matrix contained therein
	 * TBD: Better logging
	 * @param fileName
	 * @return
	 */
	public static SubstitutionMatrix loadSubstitutionMatrix(String fileName){		
		try {
			LOGGER.info("Loading matrix with fileName="+fileName);
			ClassPathResource resource = new ClassPathResource(fileName);
			File file = new File(resource.getURI());
			JAXBContext context;
			context = JAXBContext.newInstance(SubstitutionMatrix.class);
			Unmarshaller  unMarshaller = context.createUnmarshaller();
			SubstitutionMatrix matrix = (SubstitutionMatrix) unMarshaller.unmarshal(file);
			if(matrix==null){
				LOGGER.error("Matrix load did not throw exception but loaded as null");
			}
			return matrix;
		} catch (JAXBException e) {
			LOGGER.error("Failed to properly load matrix with fileName="+fileName);
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		return null;


	}
	
	/**
	 * Take the passed in matrix and build an XML file out of it
	 * @param matrixName
	 * @param matrix
	 */
	public static void serializeSubstitutionMatrix(String matrixName,SubstitutionMatrix matrix){
		File file = new File(matrixName+".xml");
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(SubstitutionMatrix.class);
			Marshaller jaxbMarshaller = context.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(matrix, file);
			jaxbMarshaller.marshal(matrix, System.out);
		} catch (JAXBException e) {
			LOGGER.error("Failed to properly serialize matrix with matrixName="+matrixName);
			LOGGER.error(e.getMessage());
		}
	}
}
