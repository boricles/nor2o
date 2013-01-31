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
import java.util.Vector;



import es.upm.fi.dia.oeg.nor2o.external.Resource;
import es.upm.fi.dia.oeg.nor2o.external.scarlet.ScarletConnector;
import es.upm.fi.dia.oeg.nor2o.external.wordnet.WordNetConnector;
import es.upm.fi.dia.oeg.nor2o.nor.NOR;
import es.upm.fi.dia.oeg.nor2o.nor.connector.Connector;
import es.upm.fi.dia.oeg.nor2o.nor.content.CEntity;
import es.upm.fi.dia.oeg.nor2o.nor.content.CRelation;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.DataModel;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaAttribute;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaEntity;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaRelation;
import es.upm.fi.dia.oeg.nor2o.nor.util.NORUtils;
import es.upm.fi.dia.oeg.nor2o.or.OR;
import es.upm.fi.dia.oeg.nor2o.or.OROWL;


/**
 * @author boricles
 *
 */
public class PRNOR implements PRNORConstants {

	protected String transformationApproach;
	protected String identifier;
	protected String topLevelClass;
	
	protected String externalResource;
	protected Resource[] external;
	
	protected NOR nor;
	protected OR or;
	
	protected Set<TElement> elements;
	
	
	public void addTElement(TElement element) {
		if (elements == null)
			elements = new HashSet<TElement>();
		elements.add(element);
	}
	
	public String getTransformationApproach() {
		return transformationApproach;
	}

	public void setTransformationApproach(String transformationApproach) {
		this.transformationApproach = transformationApproach;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * 
	 */
	public PRNOR() {
		super();
	}

	public void dispose() {
		
	}

	public NOR getNor() {
		return nor;
	}

	public void setNor(NOR nor) {
		this.nor = nor;
	}

	public OR getOr() {
		return or;
	}

	public void setOr(OR or) {
		this.or = or;
	}
	
	
	protected void addProvenanceInformation() {
		String norInformation = "\nOntology Generated from " + nor.getName() + ", which is a " +  NOR.NORS[nor.getType()] + " \nmodelled using a "  + DataModel.DATAMODELS[nor.getModel()] + "\nimplemented in " + Connector.IMPLEMENTATIONS[nor.getImplementation()] + "\n";
		or.createOntologyAnnotation(norInformation);
		
	}
	
	protected void checkExternalResource() {
		//Check if there is an external resource
		//Resource external = null;
		if (externalResource!=null && !externalResource.equals("")) { 
			
			if (externalResource.equals(Resource.SCARLET)) {
				external = new Resource[1];
				external[0] = new ScarletConnector();
				//((ScarletConnector)external).setResource(externalResource);
				external[0].init();
			}
			
			if (externalResource.equals(Resource.WORDNET)) {
				external = new Resource[1];
				external[0] = new WordNetConnector();
				//((WordNetConnector)external).setResource(externalResource);
				external[0].init();
			}
			if (externalResource.equals(Resource.WORDNET_SCARLET)) {
				external = new Resource[2];
				external[0] = new WordNetConnector();
				external[0].init();
				external[1] = new ScarletConnector();
				external[1].init();
			}
			if (externalResource.equals(Resource.SCARLET_WORDNET)) {
				external = new Resource[2];
				external[0] = new ScarletConnector();
				external[0].init();
				external[1] = new WordNetConnector();
				external[1].init();
			}
		}
	}
	
	protected String getSemanticRelation(String concept1, String concept2) {
		String rel = "";
		for (int i=0;i<external.length; i++) {
			rel = external[i].getRelation(concept1, concept2); 
			if (!rel.equals(""))
				return rel; 
		}
		return rel;
	}
	
	public void transform() {
		if (transformationApproach.equals(PRNOR_ABOX)) {
			convertToABox();
		}
		else if (transformationApproach.equals(PRNOR_TBOX)) {
			convertToTBox();
			
		}
		else if (transformationApproach.equals(PRNOR_POPULATION)) {
			populate();
		}
		
		addProvenanceInformation();
	}
	
	
	protected int getObjectPropertyCode(String relation) {
		if (or.EQUIVALENT_CLASS[or.getImplementationLanguage()].contains(relation)) {
			return or.EQUIVALENT_CLASS_AXIOM;
		}
		
		if (or.SUBCLASSOF[or.getImplementationLanguage()].contains(relation)) {
			return or.SUBCLASSOF_AXIOM;
		}
		return or.ADHOC_RELATION_CLASS_AXIOM;
	}
	
	protected void convertEntity2Individual (CEntity entity, TIndividual individual) {
		//Create the individual from the entity
		String identifierAtt = null, identifierValue=null;		
		identifierAtt = individual.getIdentifier();
		identifierValue = SimpleParser.evaluate(identifierAtt, entity,true);
		identifierValue = SimpleParser.removeSpecialChars(identifierValue);
		TClass indClass = individual.getClazz();


		

		or.createIndividualAxiom(indClass.getOntologyClassName(), identifierValue);
		//Inspect its objectproperties 
		Set<TObjectProperty> obProps = indClass.getObjectProperties();
		String from=null,to=null, inverse=null;
		Set<CRelation> rels = null;
		CEntity range = null;
		int i=0;
		for (TObjectProperty obProp : obProps) {
			from = obProp.getFrom();
			to = obProp.getTo();
			inverse = obProp.getInverse();

			if (from.contains("PATTERN")) {
				int idx = from.lastIndexOf("_");
				String obProppattern = from.substring(idx+1);
				String instan ="";
				if (to.contains("_")) {
					int idxx = to.lastIndexOf("_");
					instan = to.substring(0, idxx);
					to = to.substring(idxx+1);

				}
				else {
					instan = identifierValue + "_Pattern_" + i++; 

				}
				or.createIndividualAxiom(to, instan);				
				or.createObjectPropertyIndividualAxiom(identifierValue,instan , or.ADHOC_RELATION_CLASS_AXIOM,obProppattern);

			}
				
			else {	
			
				
				rels = entity.getRelations(from);
				if (rels!=null) {
					
					for (CRelation rel : rels) {
	
						int relation = 0;
						if (rel != null) {
							range = rel.getRange();
							//create the range class of the objectproperty
							for (TElement elem : elements) {
								if (elem instanceof TClass) {
									TClass rClass = (TClass)elem;
									Set<TIndividual> inds = rClass.getIndividuals();
									
									for (TIndividual rIndividual : inds) {
										if (range.getSchema().getName().equals(rIndividual.getFrom())) { //<Individual from="CSItem == scehma de la entity
											
											
											
											String ridentifierAtt = null, ridentifierValue=null;
											ridentifierAtt = rIndividual.getIdentifier();
											ridentifierValue = SimpleParser.evaluate(ridentifierAtt, range,true);
											ridentifierValue = SimpleParser.removeSpecialChars(ridentifierValue);
											TClass indRClass = rIndividual.getClazz();
										
											if (indRClass.getOntologyClassName()!=null) 
												or.createIndividualAxiom(indRClass.getOntologyClassName(), ridentifierValue);
											else
												or.createIndividualAxiom(rIndividual.getClazz().getIdentifier(), ridentifierValue);
												//or.createIndividualAxiom(rIndividual., ridentifierValue);
											//or.createIndividualAxiom(indRClass.getOntologyClassName(), ridentifierValue);							
											//Since
											//1. we dealing with clases
											//2. we can have a parametrized version of the relation
											//->we parse the relation if it is an adhoc one.
											relation = getObjectPropertyCode(to);
											if (relation == or.ADHOC_RELATION_CLASS_AXIOM) {
												to = SimpleParser.evaluate(to, range,true);
											}
											//}
											
											
											
											
											or.createObjectPropertyIndividualAxiom(identifierValue,ridentifierValue,relation,to);
											
											//for the inverse
											if (inverse != null && !inverse.isEmpty()) {
												relation = getObjectPropertyCode(inverse);
												if (relation == or.ADHOC_RELATION_CLASS_AXIOM) {
													inverse = SimpleParser.evaluate(inverse, range,true);
												}
												or.createObjectPropertyIndividualAxiom(ridentifierValue,identifierValue,relation,inverse);
												
											}
											
											
											
											
										}
										
										else {
											
											if (to.equals(PRNOR_RDFS_LABEL)) {
												/*String ridentifierAtt = null, ridentifierValue=null;
												ridentifierAtt = rClass.getIdentifier();
												ridentifierValue = SimpleParser.evaluate(ridentifierAtt, range);
												ridentifierValue = SimpleParser.removeSpecialChars(ridentifierValue);
												
												or.createClass(ridentifierValue);*/
												or.createIndividualLabelAxiom(identifierValue, range.getName());
											}
											
											
										}
										
										
									}
								}
							}
						}
					}
				}
			}
		}
		
		//Inspect its datatypeproperties
		Set<TDataTypeProperty> dtProps = indClass.getDataTypeProperties();
		String type=null;
		from = to = null;
		String att = null;
		String lang = null;
		String iF = null;
		boolean ifCondition = true;
		for (TDataTypeProperty dtProp : dtProps) {
			ifCondition = true;
			from = dtProp.getFrom();
			to = dtProp.getTo();
			type = dtProp.getType();
			lang = dtProp.getLang();
			iF = dtProp.getIf();
			if (to.equals(PRNOR_SKOS_PREFLABEL) || to.equals(PRNOR_SKOS_ALTLABEL) || to.equals(PRNOR_RDFS_LABEL)) {
				att = SimpleParser.evaluateAlsoFromObjectProperties(from, entity);
				if (iF != null)
					ifCondition = SimpleParser.evaluateIf(att,iF);
				if (ifCondition) {
					if (att!=null && !att.equals("")) {
						//or.createIndividualLabelAxiom(identifierValue, att);
						if (lang==null)
							or.createDataTypePropertyValueAxiom(identifierValue, to, att ,type);
						else
							or.createIndividualLabelAxiom(identifierValue,att,lang);
					}
				}
			}
			else {
				att = SimpleParser.evaluate(from, entity,false);
				if (iF != null)
					ifCondition = SimpleParser.evaluateIf(att,iF);
				if (ifCondition) {
					if (att!=null) {
						if (type.contains("decimal")) {
							String a = "decimal";
						}
						to = SimpleParser.evaluate(to, entity,true); //hard-coded for the indicators
						or.createDataTypePropertyValueAxiom(identifierValue, to, att ,type);
					}
				}
			}
			att = null;
		}
	}
	
	protected void convertSchemaEntity2Class (SchemaEntity sEntity, TClass eClass) {
		//Create the class from the entity
		String identifierAtt = null, identifierValue=null;
		identifierAtt = eClass.getIdentifier();
		identifierValue = SimpleParser.evaluate(identifierAtt, sEntity);
		identifierValue = SimpleParser.removeSpecialChars(identifierValue);
		eClass.setOntologyClassName(identifierValue);
		or.createClass(identifierValue);
		
		//Inspect its objectproperties 
		Set<TObjectProperty> obProps = eClass.getObjectProperties();
		String from=null,to=null;
		SchemaRelation rel = null;
		SchemaEntity range = null;
		for (TObjectProperty obProp : obProps) {
			from = obProp.getFrom();
			to = obProp.getTo();
			rel = sEntity.getRelation(from);
			int relation = 0;
			if (rel != null) {
				range = NORUtils.findSchemaEntity(rel.getRange(),nor);
						
				//create the range class of the objectproperty
				for (TElement elem : elements) {
					if (elem instanceof TClass) {
						TClass rClass = (TClass)elem;
						if (range.getName().equals(rClass.getFrom())) { //<Class from="CSItem == scehma de la entity
							String ridentifierAtt = null, ridentifierValue=null;
							ridentifierAtt = rClass.getIdentifier();
							ridentifierValue = SimpleParser.evaluate(ridentifierAtt, range);
							ridentifierValue = SimpleParser.removeSpecialChars(ridentifierValue);
							or.createClass(ridentifierValue);
							
							//Since
							//1. we dealing with clases
							//2. we can have a parametrized version of the relation
							//->we parse the relation if it is an adhoc one.
							relation = getObjectPropertyCode(to);
							if (relation == or.ADHOC_RELATION_CLASS_AXIOM) {
								to = SimpleParser.evaluate(to, range);
							}
							
							or.createObjectPropertyAxiom(identifierValue,ridentifierValue,relation,to);
						}
					}
				}
			}
		}
		
		//Inspect its datatypeproperties
		Set<TDataTypeProperty> dtProps = eClass.getDataTypeProperties();
		String type=null;
		from = to = null;
		SchemaAttribute att = null;
		for (TDataTypeProperty dtProp : dtProps) {
			from = dtProp.getFrom();
			to = dtProp.getTo();
			type = dtProp.getType();
			att = sEntity.getAttribute(from);
			if (att!=null) {
				or.createDataTypePropertyAxiom(identifierValue, to, type);
			}
		}
	}
	
	protected void convertEntity2Class(CEntity entity,TClass eClass) {
		//Create the class from the entity
		String identifierAtt = null, identifierValue=null;
		String concept1="", concept2="";
		identifierAtt = eClass.getIdentifier();
		identifierValue = SimpleParser.evaluate(identifierAtt, entity,true);

		concept1 = SimpleParser.evaluate(identifierAtt, entity,false);
		
		identifierValue = SimpleParser.removeSpecialChars(identifierValue);
		or.createClass(identifierValue);
		or.createClassLabelAxiom(identifierValue,concept1);
		
		//Inspect its objectproperties 
		Set<TObjectProperty> obProps = eClass.getObjectProperties();
		String from=null,to=null;
		Set<CRelation> rels = null;
		CEntity range = null;
		for (TObjectProperty obProp : obProps) {
			from = obProp.getFrom();
			to = obProp.getTo();
			rels = entity.getRelations(from);
			if (rels!=null) {
				
				for (CRelation rel : rels) {

					int relation = 0;
					if (rel != null) {
						range = rel.getRange();
						//create the range class of the objectproperty
						for (TElement elem : elements) {
							if (elem instanceof TClass) {
								TClass rClass = (TClass)elem;
								if (range.getSchema().getName().equals(rClass.getFrom())) { //<Class from="CSItem == scehma de la entity
									String ridentifierAtt = null, ridentifierValue=null;
									ridentifierAtt = rClass.getIdentifier();
									ridentifierValue = SimpleParser.evaluate(ridentifierAtt, range,true);
									concept2 = SimpleParser.evaluate(ridentifierAtt, range,false);
									ridentifierValue = SimpleParser.removeSpecialChars(ridentifierValue);
									or.createClass(ridentifierValue);
									
									//Since
									//1. we dealing with clases
									//2. we can have a parametrized version of the relation
									//->we parse the relation if it is an adhoc one.
									
									if (external!=null ) {
										
										String externalRelation = getSemanticRelation(concept1, concept2); 
										if (!externalRelation.equals(""))
											to = externalRelation;
									}
									
									
									relation = getObjectPropertyCode(to);
									if (relation == or.ADHOC_RELATION_CLASS_AXIOM) {
										to = SimpleParser.evaluate(to, range,true);
										to += SimpleParser.RELATION_SEPARATOR + SimpleParser.removeSpecialChars(range.getName());
									}
									
									or.createObjectPropertyAxiom(identifierValue,ridentifierValue,relation,to);
									
									to = obProp.getTo();
									
									
								}
								else {
									
									if (to.equals(PRNOR_RDFS_LABEL)) {
										/*String ridentifierAtt = null, ridentifierValue=null;
										ridentifierAtt = rClass.getIdentifier();
										ridentifierValue = SimpleParser.evaluate(ridentifierAtt, range);
										ridentifierValue = SimpleParser.removeSpecialChars(ridentifierValue);
										
										or.createClass(ridentifierValue);*/
										or.createClassLabelAxiom(identifierValue, range.getName());
									}
								}
							}
						}
					}
				}
			}
		}

		//Inspect its datatypeproperties
		Set<TDataTypeProperty> dtProps = eClass.getDataTypeProperties();
		String type=null;
		from = to = null;
		SchemaAttribute att = null;
		for (TDataTypeProperty dtProp : dtProps) {
			from = dtProp.getFrom();
			to = dtProp.getTo();
			type = dtProp.getType();
			att = entity.getSchema().getAttribute(from);
			if (att!=null) {
				or.createDataTypePropertyAxiom(identifierValue, to, type);
			}
		}
	}

	protected void checkTopLevelClass() {
		//if there is more than one toplevel class, create one single toplevel classes
		
		if (topLevelClass == null || topLevelClass.equals(""))
			return;
		
		Set<URI> topClasses = or.getTopLevelClasses();
		
		if (topClasses == null || topClasses.isEmpty()) {
			return;
		}
		
		or.createObjectPropertyAxiom(topClasses, topLevelClass, OR.SUBCLASSOF_AXIOM , "");
		
	}
	
	
	public void populate() {
		
	
		for (TElement elem : elements) {
			
			if (elem instanceof TClass) {
				TClass eClass = (TClass)elem;
				String identifier = eClass.getIdentifier();
				eClass.setOntologyClassName(identifier);
				//eClass.setClassURI(or.getURIOfClass(identifier)); //This function validate if the class exists in the target ontology
				
				Set<TIndividual> individuals = eClass.getIndividuals();
				for (TIndividual individual : individuals) {
					Set<CEntity> entities = nor.getContent().getEntities();
					for (CEntity entity : entities) {
						
						if (entity.getSchema().getName().equals(individual.getFrom())) { //<Class from="CSItem == schema de la entity
							convertEntity2Individual(entity,individual);
						}
					}
				}
				addProvenanceInformation();
				or.save();
			}
		}
	}
	
	
	public void convertToTBox() {
		checkExternalResource();
		
		for (TElement elem : elements) {
			if (elem instanceof TClass) {
				TClass eClass = (TClass)elem;
				
				//Convert the Entities
				Set<CEntity> entities = nor.getContent().getEntities();
				for (CEntity entity : entities) {
					if (entity.getSchema().getName().equals(eClass.getFrom())) { //<Class from="CSItem == scehma de la entity
						convertEntity2Class(entity,eClass);
					}
				}
				
				checkTopLevelClass();
		
				//Convert the SchemEntity
				/*
				Set<SchemaEntity> sEntities = nor.getSchema().getSchemaEntities();
				for (SchemaEntity sEntity : sEntities) {
					if (sEntity.getName().equals(eClass.getFrom())) {
						or.createClass(sEntity.getName());
						or.createObjectPropertyAxiom(sEntity.getName(),OR.CLASS[or.getImplementationLanguage()],getObjectPropertyCode(OR.EQUIVALENT_CLASS[or.getImplementationLanguage()]),"");
					}
				}
				*/
				
				addProvenanceInformation();
				or.save();
			}
				
		}
	}
	
	public void convertToABox() {
		for (TElement elem : elements) {
			if (elem instanceof TClass) {
				TClass eClass = (TClass)elem;
				//Convert the SchemEntity to OWL TBox
				Set<SchemaEntity> sEntities = nor.getSchema().getSchemaEntities();
				for (SchemaEntity sEntity : sEntities) {
					if (sEntity.getName().equals(eClass.getFrom())) {
						convertSchemaEntity2Class(sEntity,eClass);
					}
				}
				
				Set<TIndividual> individuals = eClass.getIndividuals();
				for (TIndividual individual : individuals) {
					Set<CEntity> entities = nor.getContent().getEntities();
					for (CEntity entity : entities) {
						if (entity.getSchema().getName().equals(individual.getFrom())) { //<Class from="CSItem == scehma de la entity
							convertEntity2Individual(entity,individual);
						}
					}
				}
				addProvenanceInformation();
				or.save();
			}
		}
		
	}

	public String getTopLevelClass() {
		return topLevelClass;
	}

	public void setTopLevelClass(String topLevelClass) {
		this.topLevelClass = topLevelClass;
	}

	public String getExternalResource() {
		return externalResource;
	}

	public void setExternalResource(String externalResource) {
		this.externalResource = externalResource;
	}
	
	
}
