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
import java.util.Set;

import org.semanticweb.owl.model.OWLAnnotation;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntologyChangeException;

/**
 * @author boricles
 *
 */
public class SKOS extends OR implements ORConstants {

	/* (non-Javadoc)
	 * @see es.upm.fi.dia.oeg.prnor.or.OR#createClass(java.lang.String)
	 */
	@Override
	public void createClass(String className) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.upm.fi.dia.oeg.prnor.or.OR#createClassLabelAxiom(java.lang.String, java.lang.String)
	 */
	@Override
	public void createClassLabelAxiom(String class1, String label) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.upm.fi.dia.oeg.prnor.or.OR#createDataTypePropertyAxiom(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void createDataTypePropertyAxiom(String class1, String dtpName,
			String type) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.upm.fi.dia.oeg.prnor.or.OR#createDataTypePropertyValueAxiom(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void createDataTypePropertyValueAxiom(String individual,
			String dtpName, String value, String type) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.upm.fi.dia.oeg.prnor.or.OR#createIndividualAxiom(java.lang.String, java.lang.String)
	 */
	@Override
	public void createIndividualAxiom(String class1, String individual) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.upm.fi.dia.oeg.prnor.or.OR#createIndividualLabelAxiom(java.lang.String, java.lang.String)
	 */
	@Override
	public void createIndividualLabelAxiom(String individual, String label) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.upm.fi.dia.oeg.prnor.or.OR#createObjectPropertyAxiom(java.lang.String, java.lang.String, int, java.lang.String)
	 */
	@Override
	public void createObjectPropertyAxiom(String class1, String class2,
			int axiom, String adhoc) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.upm.fi.dia.oeg.prnor.or.OR#createObjectPropertyAxiom(java.util.Set, java.lang.String, int, java.lang.String)
	 */
	@Override
	public void createObjectPropertyAxiom(Set<URI> domainClass,
			String rangeClass, int axiom, String adhoc) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.upm.fi.dia.oeg.prnor.or.OR#createObjectPropertyIndividualAxiom(java.lang.String, java.lang.String, int, java.lang.String)
	 */
	@Override
	public void createObjectPropertyIndividualAxiom(String ind1, String ind2,
			int axiom, String adhoc) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.upm.fi.dia.oeg.prnor.or.OR#getTopLevelClasses()
	 */
	@Override
	public Set<URI> getTopLevelClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see es.upm.fi.dia.oeg.prnor.or.OR#getURIOfClass(java.lang.String)
	 */
	@Override
	public URI getURIOfClass(String className) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see es.upm.fi.dia.oeg.prnor.or.OR#init()
	 */
	@Override
	public void init() {
		/*
		try {
			Repository repository = new SailRepository(new MemoryStore());
			repository.initialize();
			RepositoryConnection con = repository.getConnection();
			
			 try {
			   // upload a URL
			   URL url = new URL("http://www.daml.org/2001/01/gedcom/royal92.daml");
			   con.add(url, null, RDFFormat.RDFXML);
			   
			   // create vocabulary
			   ValueFactory vf = con.getValueFactory();
			   String NS = "http://www.daml.org/2001/01/gedcom/gedcom#";
			   URI title = vf.createURI(NS, "title");
			   Literal kingOfEng = vf.createLiteral("King of England");
			   
			   // retrieve statements, null is a wild card
			   RepostoryResult<Statement> sts = con.getStatements(null, title, kingOfEng);
			   try {
			     while (sts.hasNext()) {
			       Statement st = sts.next();
			       // print the subject of every statement returned
			       
			     }
			   } finally {
			     sts.close();
			   }
			 } finally {
			  con.close();
			 }
			
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	/* (non-Javadoc)
	 * @see es.upm.fi.dia.oeg.prnor.or.OR#save()
	 */
	@Override
	public void save() {
		// TODO Auto-generated method stub

	}
	
	public void createOntologyAnnotation(String annotation) {
	}	

	public Set<OWLEntity> getEntities() {
		return null;
	}
	
	public Set<OWLClass> getClasses() {
		return null;
	}
	
	public Set<OWLObjectProperty> getObjectProperties() {
		return null;
	}
	
	public Set<OWLDataProperty> getDataProperties() {
		return null;
	}

	public Set<OWLIndividual> getIndividuals() {
		return null;
	}
	
	public Set getAxioms() {
		return null;
	}
	
	public void createDataPropertyLabelAxiom(String dtpName, String label) {
	}
	
	public void createObjectPropertyLabelAxiom(String adhoc, String label) {
		
	}

	@Override
	public void createIndividualLabelAxiom(String individual, String label,
			String lang) {
		// TODO Auto-generated method stub
		
	}

}
