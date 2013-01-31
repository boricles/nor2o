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

package es.upm.fi.dia.oeg.nor2o.transformation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.upm.fi.dia.oeg.nor2o.nor.NOR;
import es.upm.fi.dia.oeg.nor2o.util.XMLReader;

/**
 * @author boricles
 *
 */
public class PRNORReader extends XMLReader implements PRNORConstants {

	protected static PRNOR singleton = null;
	
	public PRNORReader() {
		super();
	}

	protected String folder;
	
	public void setFolder(String folder){
		this.folder = folder;
	}

	
	public void read() {
		try {
			String filePath = folder + PRNOR_DESCRIPTION_FILE;
			//File orFileDesc = new File(filePath); 
			File prnorFileDesc = new File(filePath/*PRNOR_DESCRIPTION_FILE*/); 
			FileInputStream prnorFIS = new FileInputStream(prnorFileDesc);
			if (prnorFIS == null || prnorFIS.available() <= 0)
				throw new FileNotFoundException("Empty " + PRNOR_DESCRIPTION_FILE + "File");
				
			Document doc = null;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			doc = docBuilder.parse(prnorFIS);
			prnorFIS.close();
			
			Element root = doc.getDocumentElement();
			readPRNORDescription(root);
		
			//singleton.load();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception exe) {
			// TODO Auto-generated catch block
			logger.error("Exception " + PRNOR_DESCRIPTION_FILE + " file wrong");
			logger.error("read: " + exe.getMessage());

			exe.printStackTrace();
		}
	}
	
	
	protected void readPRNORDescription(Element root) throws Exception {
		Attr attIdentifier = root.getAttributeNode(PRNOR_IDENTIFIER_ATT);
		Attr attTransformation = root.getAttributeNode(PRNOR_TRANSFORMATION_ATT);	
		Attr attTopClass = root.getAttributeNode(PRNOR_TOPLEVELCLASS_ATT);
		Attr attExternalResource = root.getAttributeNode(PRNOR_EXTERNAL_RESOURCE);
		singleton = new PRNOR();
		singleton.setIdentifier(attIdentifier.getValue());
		singleton.setTransformationApproach(attTransformation.getValue());
		
		if (attTopClass!=null)
			singleton.setTopLevelClass(attTopClass.getValue());
		
		if (attExternalResource!=null)
			singleton.setExternalResource(attExternalResource.getValue());
		
		if (singleton.getTransformationApproach().equals(PRNOR_ABOX)) {
			readClasses(root);
		}
		else if (singleton.getTransformationApproach().equals(PRNOR_TBOX)) {
			readClasses(root);
		}
		else if (singleton.getTransformationApproach().equals(PRNOR_POPULATION)) {
			readClasses(root);
		}
		else
			throw new java.lang.Exception("Incomplete/wrong information on the " + PRNOR_DESCRIPTION_FILE+ " file");
		
	}
	
	protected Set<TIndividual> readIndividuals(Node cClass,TClass tClass) throws Exception {
		Set<TIndividual> props = null;
		NodeList oNodes = cClass.getChildNodes();
		if (oNodes != null) {
			Node cDProperty = null;
			props = new HashSet<TIndividual>();
			for (int i = 0; i<oNodes.getLength(); i++) {
				cDProperty = oNodes.item(i);
				if (cDProperty.getNodeType() == Node.ELEMENT_NODE) {
					if (cDProperty.getNodeName().equals(PRNOR_INDIVIDUAL_NODE)) {
						TIndividual individual = new TIndividual();
						individual.setFrom(getAttribute(cDProperty, PRNOR_INDIVIDUAL_FROM_ATT));
						individual.setIdentifier(getAttribute(cDProperty, PRNOR_INDIVIDUAL_IDENTIFIER_ATT));
						individual.setClazz(tClass);
						props.add(individual);
					}
				}
			}
		}
		return props;
	}

	
	protected Set<TDataTypeProperty> readDataTypeProperties(Node cClass) throws Exception {
		Set<TDataTypeProperty> props = null;
		NodeList oNodes = cClass.getChildNodes();
		if (oNodes != null) {
			Node cDProperty = null;
			props = new HashSet<TDataTypeProperty>();
			for (int i = 0; i<oNodes.getLength(); i++) {
				cDProperty = oNodes.item(i);
				if (cDProperty.getNodeType() == Node.ELEMENT_NODE) {
					if (cDProperty.getNodeName().equals(PRNOR_DATATYPEPROPERTY_NODE)) {
						TDataTypeProperty dProperty = new TDataTypeProperty();
						dProperty.setFrom(getAttribute(cDProperty, PRNOR_DATATYPEPROPERTY_FROM_ATT));
						dProperty.setTo(getAttribute(cDProperty, PRNOR_DATATYPEPROPERTY_TO_ATT));
						dProperty.setType(getAttribute(cDProperty,PRNOR_DATATYPEPROPERTY_TYPE_ATT ));
						dProperty.setLang(getAttribute(cDProperty,PRNOR_DATATYPEPROPERTY_LANG_ATT ));
						dProperty.setIf(getAttribute(cDProperty,PRNOR_DATATYPEPROPERTY_IF_ATT ));
						props.add(dProperty);
					}
				}
			}
		}
		return props;
	}
	
	protected Set<TObjectProperty> readObjectProperties(Node cClass) throws Exception {
		Set<TObjectProperty> props = null;
		NodeList oNodes = cClass.getChildNodes();
		if (oNodes != null) {
			Node cOProperty = null;
			props = new HashSet<TObjectProperty>();
			for (int i = 0; i<oNodes.getLength(); i++) {
				cOProperty = oNodes.item(i);
				if (cOProperty.getNodeType() == Node.ELEMENT_NODE) {
					if (cOProperty.getNodeName().equals(PRNOR_OBJECTPROPERTY_NODE)) {
						TObjectProperty oProperty = new TObjectProperty();
						oProperty.setFrom(getAttribute(cOProperty, PRNOR_OBJECTPROPERTY_FROM_ATT));
						oProperty.setTo(getAttribute(cOProperty, PRNOR_OBJECTPROPERTY_TO_ATT));
						oProperty.setInverse(getAttribute(cOProperty, PRNOR_OBJECTPROPERTY_INVERSE));
						props.add(oProperty);
					}
				}
			}
		}
		return props;
	}
	
	protected void readClasses(Element root) throws Exception {
		NodeList classNodes = root.getElementsByTagName(PRNOR_CLASS_NODE);
		if (classNodes == null)
			throw new Exception("Incorrect information in the " + PRNOR_DESCRIPTION_FILE+ " file");
		Node cClass = null;
		for (int i=0; i<classNodes.getLength();i++) {
			TClass tClass = new TClass();
			cClass = classNodes.item(i);
			tClass.setFrom(getAttribute(cClass, PRNOR_CLASS_FROM_ATT));
			tClass.setIdentifier(getAttribute(cClass, PRNOR_CLASS_IDENTIFIER_ATT));
			tClass.setObjectProperties(readObjectProperties(cClass));
			tClass.setDataTypeProperties(readDataTypeProperties(cClass));
			tClass.setIndividuals(readIndividuals(cClass,tClass));
			singleton.addTElement(tClass);
		}
		
	}
	
	public PRNOR getPRNOR() {
		return singleton;
	}
	
	public void dispose() {
		singleton.dispose();
	}
	
	
}
