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

import java.net.URI;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author boricles
 *
 */
public class ORRDF /*extends OR*/ {

	/**
	 * 
	 */
	public ORRDF() {
		super();
	}
	
	public void createClass(String className) {
		
	}
	
	public void createObjectPropertyAxiom(String class1, String class2, int axiom, String adhoc) {
		
	}

	public void createObjectPropertyAxiom(Set<URI>domainClass, String rangeClass, int axiom, String adhoc) {
		
	}

	public void createDataTypePropertyAxiom(String class1, String dtpName, String type) {
		
	}

	public void createIndividualAxiom(String class1, String individual) {
		
	}
	
	public void createObjectPropertyIndividualAxiom (String ind1, String ind2, int axiom, String adhoc) {
		
	}
	
	public void createDataTypePropertyValueAxiom(String individual, String dtpName, String value,String type) {
		
	}

	public void createClassLabelAxiom(String class1, String label) {
	
	}
	public void createIndividualLabelAxiom(String individual, String label) {
		
	}
	public void init() {
		
	}
	
	public void save() {
		
	}
	
	public Set<URI> getTopLevelClasses() {
		return null;
	}
	
	public URI getURIOfClass (String className) {
		return null;
	}


}
