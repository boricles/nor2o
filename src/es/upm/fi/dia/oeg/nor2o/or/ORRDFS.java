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
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Set;

import org.semanticweb.owl.model.OWLClass;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class ORRDFS extends OR {

	OntModel model;	
	
	public ORRDFS() {
		super();
	}
	
	public void init() {
		try {
			//model = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM);
			model = ModelFactory.createOntologyModel(OntModelSpec.OWL_LITE_MEM_RDFS_INF);
			if (alreadyExists.equals(OR.NO)){
				//File file = new File(pURI);
				ontologyURI = URI.create(oURI);
				//FileInputStream fs = new FileInputStream(file);
				//model.read(fs, RDF_XML);
				//model.createOntology(name);
				
			}
			else {
				File file = new File(pURI);
				ontologyURI = URI.create(oURI);
				FileInputStream fs = new FileInputStream(file);
				model.read(fs, RDF_XML);
				//model.createOntology(name);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void createClass(String className) {
		try {
			String clazzName = URLEncoder.encode(className,DEFAULT_ENCODING);
			OntClass oClass = model.createClass(ontologyURI + separator + clazzName);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void createObjectPropertyAxiom(String clazz1, String clazz2, int axiom, String adhocz) {
		try {
			OntClass clsA,clsB;
			String class1 = URLEncoder.encode(clazz1,DEFAULT_ENCODING);
			String class2 = URLEncoder.encode(clazz2,DEFAULT_ENCODING);
			
			if (!class1.contains(CLASS[getImplementationLanguage()]))
				clsA = model.createClass(ontologyURI + separator + class1);
			else
				clsA = model.createClass(class1);
			if (!class2.contains(CLASS[getImplementationLanguage()]))
				clsB = model.createClass(ontologyURI + separator + class2);
			else
				clsB = model.createClass(class2);
			
			if (axiom==EQUIVALENT_CLASS_AXIOM) {
				clsA.addEquivalentClass(clsB);
				
			} else if (axiom==SUBCLASSOF_AXIOM) {
				clsB.addSubClass(clsA);
				
			} else if (axiom == ADHOC_RELATION_CLASS_AXIOM) {
				String adhoc = URLEncoder.encode(adhocz,DEFAULT_ENCODING);
				ObjectProperty obj = model.createObjectProperty(ontologyURI + separator + adhoc);
				obj.addDomain(clsA);
				obj.addRange(clsB);
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public void createObjectPropertyAxiom(Set<URI>domainClass, String rangeClass, int axiom, String adhoc) {
		
	}

	public void createDataTypePropertyAxiom(String class1, String dtpName, String type) {
		
	}

	public void createIndividualLabelAxiom(String individual, String label) {
		
	}
	
	public void createIndividualAxiom(String class1, String individual) {
		
	}
	
	public void createObjectPropertyIndividualAxiom (String ind1, String ind2, int axiom, String adhoc) {
		
	}
	
	public void createDataTypePropertyValueAxiom(String individual, String dtpName, String value,String type) {
		
	}
	
	public void createClassLabelAxiom(String class1, String label) {
		
	}
	
	public void save() {
		try {

			File file = new File(pURI);
			FileOutputStream fs = new FileOutputStream(file);
			model.write(fs);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Set<URI> getTopLevelClasses() {
		return null;
	}
	
	public URI getURIOfClass (String className) {
		return null;
	}

	@Override
	public void createDataPropertyLabelAxiom(String adhoc, String label) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createIndividualLabelAxiom(String individual, String label,
			String lang) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createObjectPropertyLabelAxiom(String adhoc, String label) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createOntologyAnnotation(String annotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set getAxioms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getDataProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getIndividuals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getObjectProperties() {
		// TODO Auto-generated method stub
		return null;
	}


	public static void main(String []args) {
		OR ontology = new ORRDFS();
		ontology.setAlreadyExists(OR.NO);
		ontology.setImplementationLanguage(OR.RDFS_LANGUAGE);
		ontology.setName("unemployment ontology");
		ontology.setOntologyURI("http://geo.linkeddata.es/ontology/");
		ontology.setSeparator("");
		ontology.setPhysicalURI("unemployment.rdfs");
		
		ontology.init();
		
		ontology.createClass("Televisión");
		ontology.createClass("Persona");
		ontology.createObjectPropertyAxiom("Televisión", "Persona", OR.ADHOC_RELATION_CLASS_AXIOM, "compradaPor");

		
		ontology.createClass("Persona");
		ontology.createClass("Hombre");
		ontology.createObjectPropertyAxiom("Hombre", "Persona", OR.SUBCLASSOF_AXIOM, "");		
		ontology.save();
		
		
	}
}
