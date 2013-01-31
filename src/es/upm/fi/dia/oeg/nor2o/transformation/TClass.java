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

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author boricles
 *
 */
public class TClass extends TElement {
	
	protected Set<TObjectProperty> objectProperties;
	protected Set<TDataTypeProperty> dataTypeProperties;
	protected Set<TIndividual> individuals;	
	protected String identifier;
	
	protected String ontologyClassName;
	
	protected URI classURI;
	
	public TClass() {
		super();
		
	}
	
	public Set<TIndividual> getIndividuals() {
		return individuals;
	}

	
	public Set<TObjectProperty> getObjectProperties() {
		return objectProperties;
	}

	public Set<TDataTypeProperty> getDataTypeProperties() {
		return dataTypeProperties;
	}

	public void addObjectProperty(TObjectProperty oProperty) {
		if (objectProperties == null)
			objectProperties = new HashSet<TObjectProperty>();
		objectProperties.add(oProperty);
	}

	public void addDataTypeProperty(TDataTypeProperty dProperty) {
		if (dataTypeProperties == null)
			dataTypeProperties = new HashSet<TDataTypeProperty>();
		dataTypeProperties.add(dProperty);
	}
	
	public void setDataTypeProperties(Set<TDataTypeProperty> properties) {
		dataTypeProperties = properties;
	}
	
	public void setObjectProperties(Set<TObjectProperty> properties) {
		objectProperties = properties;
		
	}
	
	public void setIndividuals(Set<TIndividual> individuals) {
		this.individuals = individuals;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getOntologyClassName() {
		return ontologyClassName;
	}

	public void setOntologyClassName(String ontologyClassName) {
		this.ontologyClassName = ontologyClassName;
	}

	public URI getClassURI() {
		return classURI;
	}

	public void setClassURI(URI classURI) {
		this.classURI = classURI;
	}

}
