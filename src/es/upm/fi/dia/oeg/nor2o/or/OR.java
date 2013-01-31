/*
 * Copyright (C) 2009 Ontology Engineering Group, Departamento de Inteligencia Artificial
 * 					  Facultad de Informática, Universidad Politécnica de Madrid, Spain
 * 					  boricles
 *	
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package es.upm.fi.dia.oeg.nor2o.or;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owl.model.OWLClassAxiom;

/**
 * @author boricles
 *
 */
public abstract class OR implements ORConstants {

	protected URI ontologyURI;
	protected URI physicalURI;
	protected String oURI;
	protected String pURI;
	
	protected String separator = "#";
	
	protected String alreadyExists;
	
	protected int implementationLanguage;
	
	protected String name;

	protected Map prefixes;
	
	public String getName() {
		return name;
	}

	public void addPrefix(String name, String uri) {
		prefixes.put(name, uri);
	}

	protected String buildElementURI(String element) {
		String elementURI = "";
		if (element.contains(":")) {
			String[]name = element.split(":");
			String baseURI = getPrefixURI(name[0]);
			
				
			elementURI = baseURI + name[1];
		}
		else {
			element = encode(element);
			elementURI = ontologyURI + element;
		}
		
		
		return elementURI;
		
	}

	public String getPrefixURI(String prefix){
		String uri = (String)prefixes.get(prefix);
		if (uri==null)
			return "";
		return uri;
	}

	protected String encode(String localName) {
		String encodedString;
		int pos = localName.lastIndexOf(LAST_SEPARATOR);  //TODO check it 
		String lastPart = localName;
		String firstPart = "";
		if (pos != -1) {
			lastPart = localName.substring(pos+1);
			/*if (lastPart.charAt(0)=='_')		//TODO check it
				lastPart = lastPart.substring(1);*/
			firstPart = localName.substring(0,pos+1);
		}
		try {
			lastPart = URLEncoder.encode(lastPart,DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		encodedString = firstPart + lastPart;
		return encodedString;
		
	}
	

	public void setName(String name) {
		this.name = name;
	}


	public String getOURI() {
		return oURI;
	}


/*	public void setOURI(String ouri) {
		oURI = ouri;
	}
*/

	public String getPURI() {
		return pURI;
	}


/*	public void setPURI(String puri) {
		pURI = puri;
	}
*/

	/**
	 * 
	 */
	public OR() {
		super();
		prefixes = new HashMap();
	}

	
	public URI getOntologyURI() {
		return ontologyURI;
	}

	public void setOntologyURI(String ontologyURI) {
		this.oURI = ontologyURI;
	}

	public URI getPhysicalURI() {
		return physicalURI;
	}

	public void setPhysicalURI(String physicalURI) {
		this.pURI = physicalURI;
	}
	
	abstract public void createClass(String className); 
	
	abstract public void createObjectPropertyAxiom(String class1, String class2, int axiom, String adhoc);
	
	abstract public void createClassLabelAxiom(String class1, String label);
	
	abstract public void createIndividualLabelAxiom(String individual, String label);
		
	abstract public void createIndividualLabelAxiom(String individual, String label, String lang);

	abstract public void createObjectPropertyAxiom(Set<URI>domainClass, String rangeClass, int axiom, String adhoc);

	abstract public void createDataTypePropertyAxiom(String class1, String dtpName, String type);

	abstract public void createIndividualAxiom(String class1, String individual);
	
	abstract public void createObjectPropertyIndividualAxiom (String ind1, String ind2, int axiom, String adhoc);
	
	abstract public void createDataTypePropertyValueAxiom(String individual, String dtpName, String value,String type);
	
	abstract public void init();
	
	abstract public void save();
	
	abstract public URI getURIOfClass (String className);

	abstract public void createOntologyAnnotation(String annotation);
	
	public int getImplementationLanguage() {
		return implementationLanguage;
	}


	public void setImplementationLanguage(int implementationLanguage) {
		this.implementationLanguage = implementationLanguage;
	}
	
	abstract public Set<URI> getTopLevelClasses();


	public String getAlreadyExists() {
		return alreadyExists;
	}


	public void setAlreadyExists(String alreadyExists) {
		this.alreadyExists = alreadyExists;
	}
	
	abstract public Set getAxioms();
	
	abstract public Set getEntities();
	
	abstract public Set getClasses();
	abstract public Set getObjectProperties();
	abstract public Set getDataProperties();
	abstract public Set getIndividuals();	

	abstract public void createObjectPropertyLabelAxiom(String adhoc, String label);
	abstract public void createDataPropertyLabelAxiom(String adhoc, String label);


	public String getSeparator() {
		return separator;
	}


	public void setSeparator(String separator) {
		this.separator = separator;
	}

}
