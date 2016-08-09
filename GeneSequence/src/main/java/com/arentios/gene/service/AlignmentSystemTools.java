package com.arentios.gene.service;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.arentios.gene.domain.SubstitutionMatrix;

public class AlignmentSystemTools {

	private static Logger LOGGER = LoggerFactory.getLogger(AlignmentSystemTools.class);
	
	/**
	 * Take in the specified filename and return the substitution matrix contained therein
	 * TBD: Better logging
	 * @param filename
	 * @return
	 */
	public static SubstitutionMatrix loadSubstitutionMatrix(String filename){		
		try {
			ClassPathResource resource = new ClassPathResource(filename);
			File file = new File(resource.getURI());
			JAXBContext context;
			context = JAXBContext.newInstance(SubstitutionMatrix.class);
			Unmarshaller  unMarshaller = context.createUnmarshaller();
			SubstitutionMatrix matrix = (SubstitutionMatrix) unMarshaller.unmarshal(file);
			return matrix;
		} catch (JAXBException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		return null;


	}
}
