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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaEntity;
import es.upm.fi.dia.oeg.nor2o.transformation.PRNOR;
import es.upm.fi.dia.oeg.nor2o.util.XMLReader;

import org.w3c.dom.Node;

/**
 * @author boricles
 *
 */
public class ORReader extends XMLReader implements ORConstants {

	protected static OR singleton = null;	
	
	/**
	 * 
	 */
	public ORReader() {
		// TODO Auto-generated constructor stub
	}
	
	protected String folder;
	
	public void setFolder(String folder){
		this.folder = folder;
	}

	
	protected void readPrefixes(NodeList orElements) {
		int len = orElements.getLength();
		Node cNode;
		String name,uri;
		for (int i=0; i<len; i++) {
			cNode = orElements.item(i);
			if (cNode.getNodeType() == Node.ELEMENT_NODE) {
				if (cNode.getNodeName().equalsIgnoreCase("prefix")) {
					name = getAttribute(cNode,"name");
					uri = getAttribute(cNode,"uri");
					singleton.addPrefix(name, uri);
				}
			}
		}
		
	}
	
	protected void readORDescription(Element root) throws Exception {
		Attr nameAtt = root.getAttributeNode(OR_NAME_ATT);
		Attr oURIAtt = root.getAttributeNode(OR_LOGICAL_URI_ATT);		
		Attr pURIAtt = root.getAttributeNode(OR_PHYSICAL_URI_ATT);
		Attr implAtt = root.getAttributeNode(OR_IMPL_ATT);
		Attr existAtt = root.getAttributeNode(OR_EXIST_ATT);
		//Attr separatorAtt = root.getAttributeNode(OR_SEPARATOR_ATT);
		String lURI = oURIAtt.getValue();
		String pURI = pURIAtt.getValue();
		String impl = implAtt.getValue();
		String exist = existAtt.getValue();
		//String sep = separatorAtt.getValue();
		

		
		if (impl.equals(OWL)) {
			singleton = new OROWL();
		}

		if (impl.equals(RDFS)) {
			//singleton = new ORRDFS();
		}
		
		readPrefixes(root.getChildNodes());
		
		singleton.setOntologyURI(lURI);
		String ontoLoc = folder + pURI;
		singleton.setPhysicalURI( ontoLoc);
		singleton.setName(nameAtt.getValue());
		singleton.setAlreadyExists(exist);
		//singleton.setSeparator(sep);
		singleton.init();
	}

	public void read() {
		try {
			String filePath = folder + OR_DESCRIPTION_FILE;
			File orFileDesc = new File(filePath); 
			FileInputStream orFIS = new FileInputStream(orFileDesc);
			if (orFIS == null || orFIS.available() <= 0)
				throw new FileNotFoundException("Empty " + OR_DESCRIPTION_FILE + "File");
				
			Document doc = null;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			doc = docBuilder.parse(orFIS);
			orFIS.close();
			
			Element root = doc.getDocumentElement();
			readORDescription(root);
		
			
			
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
			
			exe.printStackTrace();
		}
	}

	public OR getOR(){
		return singleton;
	}
	

}
