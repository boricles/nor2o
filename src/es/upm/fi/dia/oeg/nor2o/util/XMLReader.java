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

package es.upm.fi.dia.oeg.nor2o.util;

import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import es.upm.fi.dia.oeg.nor2o.nor.connector.Connector;

/**
 * @author boricles
 *
 */
public class XMLReader {
	
	
	static protected Logger logger = Logger.getLogger(XMLReader.class);
	
	public XMLReader() {
		
	}
	
	protected String getAttribute(Node node, String attributeName) {
		NamedNodeMap map = node.getAttributes();
		if (map != null) {
			Node n = map.getNamedItem(attributeName);
			if (n != null) {
				return n.getNodeValue();
			}
		}
		return null;		
	}
	

}
