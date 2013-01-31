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

package es.upm.fi.dia.oeg.nor2o.nor.connector;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.NodeList;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import nux.xom.pool.XOMUtil;
import nux.xom.xquery.XQuery;
import nux.xom.xquery.XQueryUtil;

import es.upm.fi.dia.oeg.nor2o.nor.Constants;
import es.upm.fi.dia.oeg.nor2o.nor.content.CAttribute;
import es.upm.fi.dia.oeg.nor2o.nor.content.CEntity;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.DMEntity;
import es.upm.fi.dia.oeg.nor2o.nor.schema.Schema;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaAttribute;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaEntity;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaRelation;
import es.upm.fi.dia.oeg.nor2o.nor.util.NORUtils;
import es.upm.fi.dia.oeg.nor2o.transformation.SimpleParser;

/**
 * @author boricles
 *
 */
public class XMLConnector extends Connector {

	
	protected String xmlFilePath;
	protected String xsdFilePath;
	/**
	 * 
	 */
	public XMLConnector() {
		super();
	}

	public XMLConnector( String xsdFilePath, String xmlFilePath){
		setXmlFilePath(xmlFilePath);
		setXsdFilePath(xsdFilePath);
	}

	public String getXmlFilePath() {
		return xmlFilePath;
	}

	public void setXmlFilePath(String xmlFilePath) {
		this.xmlFilePath = xmlFilePath;
	}

	public String getXsdFilePath() {
		return xsdFilePath;
	}

	public void setXsdFilePath(String xsdFilePath) {
		this.xsdFilePath = xsdFilePath;
	}

	
	
	protected CEntity getEntityAttributes(SchemaEntity sEntity, String entityName, Node entityNode) {
		Node fieldNode;
		String from;
		CAttribute attribute;
		Nodes resAtt;
		CEntity entity = new CEntity();

		for (SchemaAttribute att : sEntity.getAttributes()) {
			attribute = new CAttribute();
			attribute.setName(att.getName());
			attribute.setSchemaAttribute(att);
			
			from = att.getValues()[0];
			from = NORUtils.replaceSeparatorXML(from);
			
			resAtt = XQueryUtil.xquery(entityNode,from);
			
			if (resAtt.size()!=0) {
				for (int j=0; j<resAtt.size(); j++) {
					fieldNode = resAtt.get(j);
					attribute.addValue(fieldNode.getValue());
					entity.addAttribute(attribute);
				}
										
			}
			
			if (att.getName().equals(Constants.IDENTIFIER_ATTRIBUTE_NAME)) {
				if (attribute.getValues()==null) {
					entity.dispose();
					entity = null;
					return entity;
				}
				else
					entity.setName((String)attribute.getValues().iterator().next());
			}
		}
		return entity;
	}

	protected Nodes getEntityNodes(Document doc, SchemaEntity sEntity,String entityName, String rangeName) {
		String identifier = sEntity.getAttribute(Constants.IDENTIFIER_ATTRIBUTE_NAME).getValues()[0];
		
		String parseRangeName = SimpleParser.addScape(rangeName);
		String xPathExpression = XML_DEFAULT_PATH + entityName +XPATH_OE + identifier + XPATH_EQUAL + XPATH_STRSTART  + parseRangeName + XPATH_STREND + XPATH_CE;
		
		
		
		Nodes resNodes = XQueryUtil.xquery(doc,xPathExpression );
		
		if (resNodes != null && resNodes.size()>0)
			return resNodes;
		return null;
	}
	
	
	protected Node getEntityNode(Document doc, SchemaEntity sEntity,String entityName, String rangeName) {
		String identifier = sEntity.getAttribute(Constants.IDENTIFIER_ATTRIBUTE_NAME).getValues()[0];
		
		String parseRangeName = SimpleParser.addScape(rangeName);
		String xPathExpression = XML_DEFAULT_PATH + entityName +XPATH_OE + identifier + XPATH_EQUAL + XPATH_STRSTART  + parseRangeName + XPATH_STREND + XPATH_CE;
		
		
		
		Nodes resNodes = XQueryUtil.xquery(doc,xPathExpression );
		
		if (resNodes != null && resNodes.size()>0)
			return resNodes.get(0);
		return null;
	}
	
	protected void createRelationWithListEntity(Set <CEntity> entities, SchemaEntity sEntity, CEntity entity, String entityName, SchemaRelation rel, String range, Nodes resNodes) {
		Node relationNode;
		String rangeName;
		Node rangeEntityNode;
		CEntity rangeEntity = null;
		Nodes result;
		String from;
		Nodes resAtt;
		Node fieldNode;
		
		if (resNodes.size()!=0) {
			for (int j=0; j<resNodes.size(); j++) {
				relationNode = resNodes.get(j);

				
				
				Schema schema = sEntity.getSchema();
				SchemaEntity sREntity = null;
				for (SchemaEntity sEnt : schema.getSchemaEntities())
					if (sEnt.getName().equals(range)) {
						sREntity = sEnt;
						break;
					}
				//rangeEntityNode = resNodes.get(j);
				rangeEntityNode = relationNode;
				rangeName = relationNode.getValue();
				
				for (SchemaAttribute att : sEntity.getAttributes()) {
					if (att.getName().equals(Constants.IDENTIFIER_ATTRIBUTE_NAME)) {
						//from = att.getValues()[0];
						//from = NORUtils.replaceSeparatorXML(from);
						String identifier = rangeName;
						rangeEntity = createEntity(sREntity,entityName,identifier);
						
						/*
						resAtt = XQueryUtil.xquery(rangeEntityNode ,from);
						if (resAtt.size()!=0) {
							for (int k=0; k<resAtt.size(); j++) {
								fieldNode = resAtt.get(k);
								String identifier = fieldNode.getValue();
								rangeEntity = createEntity(sEntity,entityName,identifier);
							}
						}*/
					}
				}
				if (rangeEntity != null) {
					rangeEntity.setSchema(sREntity);
					entity.setRelation(rel, rangeEntity);
					entities.add(rangeEntity);
				
				}
				rangeEntity=null;
				
			}
				
				

/*				if (sREntity!=null) {
					result = relationNode;//getEntityNodes(relationNode.getDocument(),sREntity,entityName,rangeName);

					if (result != null) {
						for (int i=0; i<result.size(); i++) {
							rangeEntityNode = result.get(i);
							for (SchemaAttribute att : sEntity.getAttributes()) {
								if (att.getName().equals(Constants.IDENTIFIER_ATTRIBUTE_NAME)) {
									from = att.getValues()[0];
									from = NORUtils.replaceSeparatorXML(from);
									
									resAtt = XQueryUtil.xquery(rangeEntityNode ,from);
									if (resAtt.size()!=0) {
										for (int k=0; k<resAtt.size(); j++) {
											fieldNode = resAtt.get(k);
											String identifier = fieldNode.getValue();
											rangeEntity = createEntity(sEntity,entityName,identifier);
										}
									}
								}
							}
							if (rangeEntity != null) {
								entity.setRelation(rel, rangeEntity);
								
								rangeEntity.setSchema(sREntity);
								entities.add(rangeEntity);
							
							}
							rangeEntity=null;
						}
					}
				}
				
				
			}*/
		}
	}
	
	protected void setEntityRelations(Set <CEntity> entities, SchemaEntity sEntity, CEntity entity, String entityName, Node entityNode) {
		String valueId,name,range,rangeName;
		
		Node relationNode;
		Nodes resNodes;
		
		if (sEntity.getRelations() != null) {
		
			for (SchemaRelation rel : sEntity.getRelations()) {
				valueId = rel.getValueId();
				name = rel.getName();
				range = rel.getRange();
				
				resNodes = XQueryUtil.xquery(entityNode,valueId);
				
				if (sEntity.getSchema().getSchemaEntity(range).isList()) {
					createRelationWithListEntity(entities,sEntity, entity, entityName, rel, range,resNodes);
				}
				else {
					
					Node rangeEntityNode;
					CEntity rangeEntity;
					if (resNodes.size()!=0) {
						for (int j=0; j<resNodes.size(); j++) {
							if (range.equals(sEntity.getName())) {	//if the range of the relation is an entity of the same schemaEntity
								relationNode = resNodes.get(j);
								rangeName = relationNode.getValue();
								rangeEntityNode = getEntityNode(relationNode.getDocument(),sEntity,entityName,rangeName);
								if (rangeEntityNode != null) {
									rangeEntity = getEntityAttributes(sEntity,entityName,rangeEntityNode);
									if (rangeEntity != null) {
										entity.setRelation(rel, rangeEntity);
										
										rangeEntity.setSchema(sEntity);
										entities.add(rangeEntity);
										//setEntityRelations(entities,sEntity,entity,entityName,entityNode);
	
									}
								}
							}
							else { //if the range of the relation is an entity of the other schemaEntity
								relationNode = resNodes.get(j);
								rangeName = relationNode.getValue();
								
								Schema schema = sEntity.getSchema();
								SchemaEntity sREntity = null;
								for (SchemaEntity sEnt : schema.getSchemaEntities())
									if (sEnt.getName().equals(range)) {
										sREntity = sEnt;
										break;
									}
								if (sREntity!=null) {
									rangeEntityNode = getEntityNode(relationNode.getDocument(),sREntity,entityName,rangeName);
		
									if (rangeEntityNode != null) {
										rangeEntity = getEntityAttributes(sREntity,entityName,rangeEntityNode);
										if (rangeEntity != null) {
											entity.setRelation(rel, rangeEntity);
											
											rangeEntity.setSchema(sREntity);
											entities.add(rangeEntity);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		entity.setSchema(sEntity);
		entities.add(entity);
	}

	protected CEntity createEntity(SchemaEntity sEntity, String entityName, String identifierValue) {
		CEntity entity = null;
		CAttribute attribute;
		for (SchemaAttribute att : sEntity.getAttributes()) {
			if (att.getName().equals(Constants.IDENTIFIER_ATTRIBUTE_NAME)) {
				entity = new CEntity();
				attribute = new CAttribute();
				attribute.setName(att.getName());
				attribute.setSchemaAttribute(att);
				attribute.addValue(identifierValue);
				entity.addAttribute(attribute);
				entity.setName((String)attribute.getValues().iterator().next());
			}
		}
		entity.setSchema(sEntity);
		return entity;
	}
	
	public Set<CEntity> getEntitiesOfListRecordBasedDataModel (SchemaEntity sEntity, String entityName) {
		String from;
		Nodes resAtt;
		Node fieldNode;
		
		Set <CEntity> entities = new HashSet<CEntity>();
		try {
			Document doc;
			doc = new Builder().build(new File(xmlFilePath));
			Nodes result = XQueryUtil.xquery(doc, XML_DEFAULT_PATH + entityName);
			
			Node entityNode;
			
			for (int i=0; i<result.size(); i++) {
				entityNode = result.get(i);
				for (SchemaAttribute att : sEntity.getAttributes()) {
					if (att.getName().equals(Constants.IDENTIFIER_ATTRIBUTE_NAME)) {
						from = att.getValues()[0];
						from = NORUtils.replaceSeparatorXML(from);
						
						resAtt = XQueryUtil.xquery(entityNode,from);
						if (resAtt.size()!=0) {
							for (int j=0; j<resAtt.size(); j++) {
								fieldNode = resAtt.get(j);
								String identifier = fieldNode.getValue();
								CEntity centity = createEntity(sEntity,entityName+j,identifier);
								if (centity!=null)
									entities.add(centity);
							}
						}
					}
				}
			}
			
		} catch (ValidityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getEntitiesRecordBasedDataModel: " + e.getMessage());
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getEntitiesRecordBasedDataModel: " + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getEntitiesRecordBasedDataModel: " + e.getMessage());
		}

	
		return entities;
	}

	
	//esto tiene que estar de acuerdo al xsd information
	public Set<CEntity> getEntitiesRecordBasedDataModel (SchemaEntity sEntity, String entityName) {
		Set <CEntity> entities = new HashSet<CEntity>();
		try {
			Document doc;
			doc = new Builder().build(new File(xmlFilePath));
			Nodes result = XQueryUtil.xquery(doc, XML_DEFAULT_PATH + entityName);
			
			CEntity entity;
			Node entityNode;
			for (int i=0; i<result.size(); i++) {
				entityNode = result.get(i);
				entity = getEntityAttributes(sEntity,entityName,entityNode);
				
				if (entity != null) {
					
					setEntityRelations(entities,sEntity,entity,entityName,entityNode);
					
				}
			}
			
		} catch (ValidityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getEntitiesRecordBasedDataModel: " + e.getMessage());
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getEntitiesRecordBasedDataModel: " + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getEntitiesRecordBasedDataModel: " + e.getMessage());
		}

	
		return entities;
	}
	
	//esto tiene que estar de acuerdo al xsd information
	public Set<CEntity> getEntitiesSnowflakeDataModel (SchemaEntity sEntity, String entityName) {
		Set <CEntity> entities = new HashSet<CEntity>();
		try {
			Document doc;
			doc = new Builder().build(new File(xmlFilePath));
			Nodes result = XQueryUtil.xquery(doc, XML_DEFAULT_PATH + entityName);
			
			CEntity entity;
			Node entityNode;
			for (int i=0; i<result.size(); i++) {
				entityNode = result.get(i);
				entity = getEntityAttributes(sEntity,entityName,entityNode);
				if (entity != null) {
					setEntityRelations(entities,sEntity,entity,entityName,entityNode);
				}
			}
			
		} catch (ValidityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getEntitiesSnowflakeDataModel: " + e.getMessage());
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getEntitiesSnowflakeDataModel: " + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getEntitiesSnowflakeDataModel: " + e.getMessage());
		}
		return entities;
	}

	
    //C. Scheme PathEnumeration data model method
    protected String buildPathEnumerationSubEntitiesQuery (Set<SchemaAttribute> attributes, CEntity entity, String mainEntity, String pathField, String pathSeparator, String filterExpression) {
    	String query = PATH_ENUMERATION_SUB_LEVEL_QUERY;
    	query = query.replace(NODE_PATH_ENUMERATION, mainEntity);
    	query = query.replace(NODE_PATH_ENUMERATION_ID, pathField);
    	String cPathValue = "";    	    	
    	String returnClause = PATH_ENUMERATION_TOP_LEVEL_QUERY_RETURN + mainEntity+" ";
    	for (SchemaAttribute att : attributes) {
    		returnClause +=  SimpleParser.removeXPathModifiers(att.getValues()[0]) + " = " + PATH_ENUMERATION_TOP_LEVEL_QUERY_RETURN_START_VAR + att.getValues()[0] + PATH_ENUMERATION_TOP_LEVEL_QUERY_RETURN_END_VAR + " ";
    		if (att.getValues()[0].equals(pathField))
    			cPathValue = att.getName();
    	}
    	returnClause += PATH_ENUMERATION_TOP_LEVEL_QUERY_RETURN_END;
    	String []pathValues = entity.getAttributeValuesAsString(cPathValue);
    	if (filterExpression!=null && !filterExpression.isEmpty())
    		query = query + " and " + filterExpression;
    	String value = pathValues[0];
    	if (!value.endsWith(pathSeparator))
    		value = value + pathSeparator;
    	query = query.replace(NODE_KEY_VALUE, "\'" + value + "\'");
    	query = query + " \n" + returnClause;
    	
    	return query;
    }
    
	//C. Scheme PathEnumeration data model method
	public Set<CEntity> getSubEntitiesPathEnumerationDataModel(SchemaEntity sEntity, CEntity entity, String mainEntity, String pathField, String pathSeparator) {
		Set<CEntity> entities = new HashSet<CEntity>();
		try {
			String query = buildPathEnumerationSubEntitiesQuery(sEntity.getAttributes(),entity, mainEntity,pathField, pathSeparator,sEntity.getFilterExpression());
			Document doc;
			doc = new Builder().build(new File(xmlFilePath));
			Nodes result = XQueryUtil.xquery(doc, query);
			CEntity nEntity;
			Node entityNode;
			for (int i=0; i<result.size(); i++) {
				entityNode = result.get(i);
				nEntity = getEntityAttributes(sEntity,mainEntity,entityNode);
				nEntity.setSchema(sEntity);
				entities.add(nEntity);
				setCEntityRelations(sEntity,entity,nEntity);
				Set <CEntity> subSetOfEntities = getSubEntitiesPathEnumerationDataModel(sEntity,nEntity,mainEntity,pathField,pathSeparator);
				if (subSetOfEntities!=null)
					entities.addAll(subSetOfEntities);
			}
			
			} catch (ValidityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("getEntitiesRecordBasedDataModel: " + e.getMessage());
			} catch (ParsingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("getEntitiesRecordBasedDataModel: " + e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("getEntitiesRecordBasedDataModel: " + e.getMessage());
			}
		
		return entities;
	}
	
    //C. Scheme PathEnumeration data model method
    protected String buildPathEnumerationTopLevelQuery(Set<SchemaAttribute> attributes, String mainEntity, String pathField, String filterExpression) {
    	String query = PATH_ENUMERATION_TOP_LEVEL_QUERY;
    	query = query.replace(NODE_PATH_ENUMERATION, mainEntity);
    	query = query.replace(NODE_PATH_ENUMERATION_ID, pathField);
    	if (filterExpression!=null && !filterExpression.isEmpty())
    		query = query + " and " + filterExpression;
    	String returnClause = PATH_ENUMERATION_TOP_LEVEL_QUERY_RETURN + mainEntity+" ";
    	for (SchemaAttribute att : attributes) {
    		returnClause +=  SimpleParser.removeXPathModifiers(att.getValues()[0]) + " = " + PATH_ENUMERATION_TOP_LEVEL_QUERY_RETURN_START_VAR + att.getValues()[0] + PATH_ENUMERATION_TOP_LEVEL_QUERY_RETURN_END_VAR + " ";
    	}
    	returnClause += PATH_ENUMERATION_TOP_LEVEL_QUERY_RETURN_END;
    	query = query + " \n" + returnClause;
    	return query;
    	
    }
	
	//C. Scheme PathEnumeration
	public Set<CEntity> getTopLevelEntitiesPathEnumerationDataModel(SchemaEntity sEntity, String mainEntity, String pathField) {
		Set<CEntity> entities = new HashSet<CEntity>();
		try {
			Document doc;
			doc = new Builder().build(new File(xmlFilePath));
			String query = buildPathEnumerationTopLevelQuery(sEntity.getAttributes(), mainEntity,pathField,sEntity.getFilterExpression());
			Nodes resNodes = XQueryUtil.xquery(doc,query );
			CEntity currentEntity;
			Node currentNode;
			if (resNodes != null && resNodes.size()>0) {
				for (int i=0;i<resNodes.size();i++) {
					currentNode = resNodes.get(i);
					currentEntity = getEntityAttributes(sEntity,mainEntity,currentNode);
					currentEntity.setSchema(sEntity);
					entities.add(currentEntity);
				}
			}
				
			
		} catch (ValidityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getEntitiesSnowflakeDataModel: " + e.getMessage());
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getEntitiesSnowflakeDataModel: " + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getEntitiesSnowflakeDataModel: " + e.getMessage());
		}
		
		return entities;

		
	}
	
	
	
    //C. Scheme PathEnumeration data model method
	protected void setCEntityRelations(SchemaEntity sEntity, CEntity parent, CEntity child) {
		Set<SchemaRelation> sRels = sEntity.getRelations();
		/*for (SchemaRelation rel : sRels) {
			parent.setRelation(rel, child);
		}*/
		for (SchemaRelation rel : sRels) {
			if (rel.getName().equals(Constants.GENERIC_SUPERTYPE))
				parent.setRelation(rel, child);
			if (rel.getName().equals(Constants.GENERIC_SUBTYPE))
				child.setRelation(rel, parent);
		}
	}

/*	
    //C. Scheme PathEnumeration data model method
    protected String buildPathEnumerationSubEntitiesQuery (ArrayList<SchemaAttribute> attributes, CEntity entity, String mainEntity, String pathField, String pathSeparator) {
    	String selectClause = "";
    	String fromClause = "";
    	String whereClause = "";
    	String cPathValue = "";
    	for (SchemaAttribute att : attributes) {
    		selectClause += att.getValues()[0] + " as " + att.getName() + ",";
    		fromClause += getTable(att.getValues()[0]) + ",";
    		if (att.getValues()[0].equals(mainEntity + SEPARATOR + pathField))
    			cPathValue = att.getName();
    	}
    	selectClause += mainEntity + "." + pathField;
    	fromClause += mainEntity;
    	fromClause = removeDuplicates(fromClause,",");
    	
    	String []pathValues = entity.getAttributeValuesAsString(cPathValue);

    	if (pathSeparator.equals(""))
    		whereClause += mainEntity + SEPARATOR + pathField + " LIKE " + "\'" + pathValues[0] + pathSeparator + "_" + "\'";
    	else
    		whereClause += mainEntity + SEPARATOR + pathField + " LIKE " + "\'" + pathValues[0] + (pathValues[0].endsWith(pathSeparator) ? "" : pathSeparator )  + "_" + "\'";
    	
    	String query = "";
    	query = "SELECT " + selectClause + " FROM " + fromClause + " WHERE " + whereClause;
    	return query;
    }
*/
	
	//C. Scheme PathEnumeration data model method
	/*
	public Set<CEntity> getSubEntitiesPathEnumerationDataModel(SchemaEntity sEntity, CEntity entity, String mainEntity, String pathField, String pathSeparator) {
		ArrayList <CEntity> entities = new HashSet<CEntity>();
		try {
			String query = buildPathEnumerationSubEntitiesQuery(sEntity.getAttributes(),entity, mainEntity,pathField, pathSeparator);
			ResultSet rs = executeQuery(query);
			
			if (rs.next()) {
				do {
					CEntity ent = new CEntity();
					
					setCEntityValues(rs,ent,sEntity);
					setCEntityRelations(sEntity,entity,ent);
					entities.add(ent);

					
					ArrayList <CEntity> subSetOfEntities = getSubEntitiesPathEnumerationDataModel(sEntity,ent,mainEntity,pathField,pathSeparator);
					if (subSetOfEntities!=null)
						entities.addAll(subSetOfEntities);
				} while(rs.next());
				
			}
			else {
				entities.clear();
				entities = null;
			}
		
			rs.close();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return entities;
	}
	*/

	/*
	//C. Scheme PathEnumeration data model method
    protected String buildPathEnumerationTopLevelQuery(ArrayList<SchemaAttribute> attributes, String mainEntity, String pathField) {
    	
    	
    	
    	String selectClause = "";
    	String fromClause = "";
    	for (SchemaAttribute att : attributes) {
    		selectClause += att.getValues()[0] + " as " + att.getName() + ",";
    		fromClause += getTable(att.getValues()[0]) + ",";
    	}
    	selectClause += mainEntity + "." + pathField;
    	fromClause += mainEntity;
    	fromClause = removeDuplicates(fromClause,",");
    	
    	String query = "SELECT " + selectClause + " FROM " + fromClause + " WHERE " + LENPRIMITIVE[type] + "(" + pathField + 
    	") = (SELECT MIN( " + LENPRIMITIVE[type] + "("+ pathField +")) FROM " + mainEntity + ")" ;
    	return query;
    	
    }

    //C. Scheme PathEnumeration data model method    
    protected void setCEntityValues(ResultSet rs, CEntity ent, SchemaEntity mainEntity) throws SQLException {
		Set<SchemaAttribute> atts = mainEntity.getAttributes();
		String cName = "";
		for (SchemaAttribute att : atts) {
			CAttribute cattribute = new CAttribute(att.getName());
			cattribute.setSchemaAttribute(att);
			cattribute.addValue(rs.getObject(att.getName()));
			ent.addAttribute(cattribute);
			cName += "_" + cattribute.getValues().get(0);
			ent.setName(cName);
			ent.setSchema(mainEntity);
		}
    }
	
    //C. Scheme PathEnumeration data model method	
	public Set<CEntity> getTopLevelEntitiesPathEnumerationDataModel(SchemaEntity sEntity, String mainEntity, String pathField) {
		Set<CEntity> entities = new HashSet<CEntity>();
		String query = buildPathEnumerationTopLevelQuery(sEntity.getAttributes(), mainEntity,pathField);
		ResultSet rs = executeQuery(query);
		
		while (rs.next()) {
			CEntity ent = new CEntity();
			setCEntityValues(rs,ent,sEntity);
			entities.add(ent);
		}
		rs.close();
		return entities;
	}
	
	protected XResultSet executeQuery(String query) {
		
	}
	
	*/
	/*
    protected XResultSet executeQuery(String query) {
    	ResultSet rs = null;
    	try {
			Statement stmt = connection.createStatement();
			
			
			
			rs = stmt.executeQuery(query);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
    	
    }

	
    protected String buildPathEnumerationTopLevelQuery(String mainEntity, String pathField) {
    	String query = "";
    	//SELECT * FROM " + mainEntity + " WHERE " + LENPRIMITIVE[type] + " (" + pathField + 
    	//") = (SELECT MIN( " + LENPRIMITIVE[type] + "("+ pathField +")) FROM " + mainEntity + ")" ;
    	return query;
    	
    }

	
	public Set<CEntity> getTopLevelEntitiesPathEnumerationDataModel(DMEntity mainEntity, String pathField) {
		Set<CEntity> entities = new HashSet<CEntity>();
		try {
			String query = buildPathEnumerationTopLevelQuery(mainEntity.getName(), pathField);
			ResultSet rs = executeQuery(query);
			
			while (rs.next()) {
				CEntity ent = new CEntity();
				setCEntityValues(rs,ent,mainEntity);
				entities.add(ent);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return entities;

	}
	*/
	
	public static void main(String []args) {
		Document doc;
		try {
			doc = new Builder().build(new File("nors/cschemes/pathEnumeration/xml/isco.xml"));
		
			
		
			// find the atom named 'Zinc' in the periodic table: 
			//Nodes result = XQueryUtil.xquery(doc, "/dataroot/isco/id[starts-with(.,'2')]/..");
			Nodes result = XQueryUtil.xquery(doc, "min(/dataroot/isco/string-length(id))");			
			if (result!=null) {
				int size = result.size();
				for (int i=0; i<size;i++) {


				}
				
				
				
			}
			
			
			
			
		} catch (ValidityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
