/*
 * Copyright (C) 2009 Ontology Engineering Group, Departamento de Inteligencia Artificial
 * 					  Facultad de Informática, Universidad Politécnica de Madrid, Spain
 * 					  Boris Villazón-Terrazas
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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;


import org.apache.log4j.Logger;

import es.upm.fi.dia.oeg.nor2o.nor.Constants;
import es.upm.fi.dia.oeg.nor2o.nor.connector.Connector;
import es.upm.fi.dia.oeg.nor2o.nor.content.CAttribute;
import es.upm.fi.dia.oeg.nor2o.nor.content.CEntity;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.DataModel;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaAttribute;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaEntity;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaRelation;
import es.upm.fi.dia.oeg.nor2o.nor.util.NORUtils;



public class DBConnector extends Connector implements DBConstants {
	
	protected String username;
    protected String password;
    protected String driver;
    protected String url;
    protected String dbms;
    protected String host;
    protected String port;
    protected String dbname;
    
    protected int type;
    
    protected Connection connection;
    
    protected DatabaseMetaData md;
    
    protected Statement stmt;
    
    public DBConnector() {
    	super();
    }
    
    public DBConnector(int type, String dbname, String username, String password, String host, String port) {
    	super();
    	connection = getConnection(type,dbname, username,password,host,port);
    }
    
    protected Connection getConnection(int type,String dbname, String username, String password, String host, String port) {
    	Connection con = null;
    	this.username = username;
    	this.password = password;
    	this.dbname = dbname;
    	this.host = host;
    	this.port = port;
    	this.dbms = DBMS[type];
    	this.driver = DRIVERS[type];
    	this.url = getDatabaseURL(type);
    	this.type = type;
    	try {
    		Class.forName(driver);
    		con = DriverManager.getConnection(url, username, password);
    	}
    	catch (Exception ex) {
    		//throw new SQLException ();
    		ex.printStackTrace();
    		logger.error("Connection exception: " + ex.getMessage());
    	}
    	return con;
    }

    protected String getDatabaseURL(int type) {
    	String url="";
    	if (type == MSACCESS) {
    		url += BASEURL[type] + dbname + ";" + "UID=" + username + ";" + "PWD=" +  password;
    	}

    	if (type == MYSQL) {
    		url += BASEURL[type] + "//" + host + ":" + port + "/" + dbname; 
    	}
    	
    	if (type == ORACLE) {
    		url += BASEURL[type] + "@" + host + ":" + port + ":" + dbname;
    	}

    	return url;
    } 
   
    /*
    public Set<DMEntity> retrieveEntities() {
    	Set<DMEntity> entities = new HashSet<DMEntity>();
    	try {
        	this.md = connection.getMetaData();
        	Set<String> userEntitiesSet = this.getUserEntities();
        	for (String entityName : userEntitiesSet) {
        		DMEntity entity = new DMEntity();
        		entity.setName(entityName);
        		Set<DMField> fields = getFields(entityName);
        		entity.setFields(fields);
        		entities.add(entity);
        	}
    	}
    	catch (SQLException ex) {
    		ex.printStackTrace();
    	}
    	
    	return entities;
    }

    protected Set<DMField> getFields(String tableName) {
    	Set<DMField> fields = new HashSet<DMField>();
    	String userName = null;
    	try {
        	if (type != MSACCESS)
        		userName = md.getUserName();
    		ResultSet columns = md.getColumns(null, userName, tableName, "%");
        	while (columns.next()) {
        		String columnName = columns.getString("COLUMN_NAME");
        		DMField f = new DMField();
        		f.setName(columnName);
        		f.setDataType(columns.getType()); //TODO Aqui hay que hacer una transformación de tipos
        		fields.add(f);
        	}
    	}
    	catch (SQLException ex) {
    		ex.printStackTrace();
    	}
    	return fields;
    }
    */
    
    protected Set<String> getUserEntities() throws SQLException {
    	
    	if (type != MSACCESS)
    		return getStandardJDBCUserEntities(md.getUserName());
    	return getStandardJDBCUserEntities(null);
    }
    
    protected Set<String> getStandardJDBCUserEntities(String user) throws SQLException {
    	String[] types = {"TABLE"};
    	ResultSet rs = md.getTables(null, user, "%", types);
    	HashSet<String> userEntitiesSet = new HashSet<String>();
    	while (rs.next()) {
    		userEntitiesSet.add(rs.getString("TABLE_NAME"));
    	}
    	return userEntitiesSet;

    }
    
    protected ResultSet executeQuery(String query) {
    	ResultSet rs = null;
    	try {
    		stmt = connection.createStatement();
    
    		//logger.debug("QUERY: \n" + query);


			rs = stmt.executeQuery(query);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    		logger.error("Execute query exception: " + e.getMessage());
		}
		return rs;
    	
    }
    
    protected String getTable(String fromItem) {
    	return fromItem.substring(0,fromItem.indexOf(SEPARATOR));
    }

    //C. Scheme PathEnumeration data model method
    protected String removeDuplicates (String cad,String separator) {
    	String[] split = cad.split("\\,");
    	String fromClause ="";
    	for (int i=0; i<split.length; i++) {
    		if (!fromClause.contains(split[i]))
    			fromClause += split[i] + ",";
    	}
    	
    	fromClause = fromClause.substring(0,fromClause.lastIndexOf(","));
    	return fromClause;
    }
    
    //C. Scheme PathEnumeration data model method
    protected String buildPathEnumerationTopLevelQuery(Set<SchemaAttribute> attributes, String mainEntity, String pathField, String filterExpression) {
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
    	
    	if (filterExpression!=null && !filterExpression.equals(""))
    		query += " AND " + filterExpression;
    	return query;
    	
    }
    
    
    //C. Scheme PathEnumeration data model method
    protected String buildPathEnumerationSubEntitiesQuery (Set<SchemaAttribute> attributes, CEntity entity, String mainEntity, String pathField, String pathSeparator, String filterExpression) {
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

    	if (filterExpression!=null && !filterExpression.equals(""))
    		whereClause += " AND " + filterExpression;

    	
    	String query = "";
    	query = "SELECT " + selectClause + " FROM " + fromClause + " WHERE " + whereClause;
    	return query;
    }
    
    protected void setCEntityValues(ResultSet rs, CEntity ent, SchemaEntity mainEntity) throws SQLException {
		Set<SchemaAttribute> atts = mainEntity.getAttributes();
		String cName = "";
		for (SchemaAttribute att : atts) {
			CAttribute cattribute = new CAttribute(att.getName());
			cattribute.setSchemaAttribute(att);
			cattribute.addValue(rs.getObject(att.getName()));
			ent.addAttribute(cattribute);
			cName += "_" + cattribute.getValues().iterator().next();
			ent.setName(cName);
			ent.setSchema(mainEntity);
		}
    }
    
    
    //C. Scheme PathEnumeration data model method, this is also applicable to the adjacency list
	protected void setCEntityRelations(SchemaEntity sEntity, CEntity parent, CEntity child) {
		Set<SchemaRelation> sRels = sEntity.getRelations();
		/*for (SchemaRelation rel : sRels) {
			parent.setRelation(rel, child);
		}*/
		if (sRels != null) {
			for (SchemaRelation rel : sRels) {
				if (rel.getName().equals(Constants.GENERIC_SUPERTYPE))
					//{ parent.setRelation(rel, child);  }
					parent.setRelation(rel, child);
				if (rel.getName().equals(Constants.GENERIC_SUBTYPE))
					//{ child.setRelation(rel, parent);  }
					child.setRelation(rel, parent);
			}
		}
	}
	
	  //C. Scheme PathEnumeration data model method
	public Set<CEntity> getTopLevelEntitiesPathEnumerationDataModel(SchemaEntity sEntity, String mainEntity, String pathField) {
		
		Set<CEntity> entities = new HashSet<CEntity>();
		try {
			String query = buildPathEnumerationTopLevelQuery(sEntity.getAttributes(), mainEntity,pathField,sEntity.getFilterExpression());
			ResultSet rs = executeQuery(query);
			
			while (rs.next()) {
				CEntity ent = new CEntity();
				setCEntityValues(rs,ent,sEntity);
				entities.add(ent);
			}

			
			rs.close();
			stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    		logger.error("getTopLevelEntitiesPathEnumerationDataModel exception: " + e.getMessage());
		}
		
		return entities;

	}

	
	//C. Scheme PathEnumeration data model method
	public Set<CEntity> getSubEntitiesPathEnumerationDataModel(SchemaEntity sEntity, CEntity entity, String mainEntity, String pathField, String pathSeparator) {
		Set<CEntity> entities = new HashSet<CEntity>();
		try {
			String query = buildPathEnumerationSubEntitiesQuery(sEntity.getAttributes(),entity, mainEntity,pathField, pathSeparator,sEntity.getFilterExpression());
			ResultSet rs = executeQuery(query);
			
			if (rs.next()) {
				do {
					CEntity ent = new CEntity();
					
					setCEntityValues(rs,ent,sEntity);
					setCEntityRelations(sEntity,entity,ent);
					entities.add(ent);

					
					Set <CEntity> subSetOfEntities = getSubEntitiesPathEnumerationDataModel(sEntity,ent,mainEntity,pathField,pathSeparator);
					if (subSetOfEntities!=null)
						entities.addAll(subSetOfEntities);
				} while(rs.next());
				
			}
			else {
				entities.clear();
				entities = null;
			}
		
			
			rs.close();
			stmt.close();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getSubEntitiesPathEnumerationDataModel: " + e.getMessage());
		}
		
		return entities;
	}
    
	
	//AdjacencyListDataModel
	
    //C. Scheme AdjacencyListDataModel method
    protected String buildAdjacencyListTopLevelQuery(Set<SchemaAttribute> attributes, String listEntity, String listField, String filterExpression) {
    	String selectClause = "";
    	String fromClause = "";
    	for (SchemaAttribute att : attributes) {
    		selectClause += att.getValues()[0] + " as " + att.getName() + ",";
    		fromClause += getTable(att.getValues()[0]) + ",";
    	}
    	selectClause += listField;
    	fromClause += listEntity;
    	fromClause = removeDuplicates(fromClause,",");
    	
    	String query = "SELECT " + selectClause + " FROM " + fromClause + " WHERE " + listField + " is NULL OR " + listField + " = \'\'";

    	if (filterExpression!=null && !filterExpression.equals(""))
    		query += " AND " + filterExpression;

		logger.debug("buildAdjacencyListTopLevelQuery: " + query);
    	return query;
    	
    	
    }
    
  //C. Scheme AdjacencyListDataModel method
    protected String buildAdjacencyListSubEntitiesQuery (Set<SchemaAttribute> attributes, CEntity entity, String listEntity, String listField, String listId, String filterExpression) {
    	String selectClause = "";
    	String fromClause = "";
    	String whereClause = "";
    	String cAListValue = "";
    	String delimiter = "";
    	for (SchemaAttribute att : attributes) {
    		selectClause += att.getValues()[0] + " as " + att.getName() + ",";
    		fromClause += getTable(att.getValues()[0]) + ",";
    		if (att.getValues()[0].equals(listId)) {
    			if (att.getType() == es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaConstants.STRING_DATATYPE)
    				delimiter = "\'";
    			cAListValue = att.getName();
    		}
    	}
    	selectClause += listField;
    	fromClause += listEntity;
    	fromClause = removeDuplicates(fromClause,",");
    	
    	String []alistValues = entity.getAttributeValuesAsString(cAListValue);
    	
    	whereClause += listField + " = " + delimiter + alistValues[0] +  delimiter;
    	
    	if (filterExpression!=null && !filterExpression.equals(""))
    		whereClause += " AND " + filterExpression;

   	
    	String query = "";
    	query = "SELECT " + selectClause + " FROM " + fromClause + " WHERE " + whereClause;
    	logger.debug("buildAdjacencyListSubEntitiesQuery: " + query);
    	return query;
    }
    
	
    //C. Scheme AdjacencyListDataModel method	
	public Set<CEntity> getTopLevelEntitiesAdjacencyListDataModel(SchemaEntity sEntity, String listEntity, String listField, String listID) {
		
		Set<CEntity> entities = new HashSet<CEntity>();
		try {
			String query = buildAdjacencyListTopLevelQuery(sEntity.getAttributes(), listEntity,listField,sEntity.getFilterExpression());
			ResultSet rs = executeQuery(query);
			
			while (rs.next()) {
				CEntity ent = new CEntity();
				setCEntityValues(rs,ent,sEntity);
				entities.add(ent);
			}

			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getTopLevelEntitiesAdjacencyListDataModel: " + e.getMessage());
		}
		
		return entities;

	}
	
	  //C. Scheme AdjacencyListDataModel method
	public Set<CEntity> getSubEntitiesAdjacencyListDataModel(SchemaEntity sEntity, CEntity entity, String listEntity, String listField, String listId) {
		Set <CEntity> entities = new HashSet<CEntity>();
		try {
			String query = buildAdjacencyListSubEntitiesQuery(sEntity.getAttributes(),entity, listEntity,listField, listId, sEntity.getFilterExpression());
			ResultSet rs = executeQuery(query);
			
			if (rs.next()) {
				do {
					CEntity ent = new CEntity();
					setCEntityValues(rs,ent,sEntity);
					setCEntityRelations(sEntity,entity,ent);
					entities.add(ent);
					
					Set <CEntity> subSetOfEntities = getSubEntitiesAdjacencyListDataModel(sEntity,ent,listEntity,listField,listId);
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
			logger.error("getSubEntitiesAdjacencyListDataModel: " + e.getMessage());
		}
		return entities;
	}

	//Master - detail Data Model Classification scheme method
	private String buildMasterDetailMasterQuery(Set<SchemaAttribute> attributes, String masterEntity, String filterExpression) {
    	String selectClause = "";
    	String fromClause = "";
    	for (SchemaAttribute att : attributes) {
    		selectClause += att.getValues()[0] + " as " + att.getName() + " ,";
    		fromClause += getTable(att.getValues()[0]) + ",";
    	}
    	selectClause = selectClause.substring(0,selectClause.lastIndexOf(","));
    	fromClause += masterEntity;
    	fromClause = removeDuplicates(fromClause,",");
    	
    	String query = "SELECT " + selectClause + " FROM " + fromClause;

    	if (filterExpression!=null && !filterExpression.equals(""))
    		query += " WHERE " + filterExpression;

		logger.debug("buildMasterDetailMasterQuery: " + query);
    	return query;
	}
	
	//Master - detail Data Model Classification scheme method

	private String buildMasterDetailDetailQuery(Set<SchemaAttribute> attributes, CEntity entity, String masterEntity, String detailEntity, String field, String filterExpression) {
    	String selectClause = "";
    	String fromClause = "";
    	String whereClause = "";
    	String cAListValue = "";
    	String delimiter = "";
    	for (SchemaAttribute att : attributes) {
    		selectClause += att.getValues()[0] + " as " + att.getName() + ",";
    		fromClause += getTable(att.getValues()[0]) + ",";
    		if (att.getValues()[0].equals(detailEntity + SEPARATOR + field)) {
    			if (att.getType() == es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaConstants.STRING_DATATYPE)
    				delimiter = "\'";
    			cAListValue = att.getName();
    		}

    		/*if (att.getValues()[0].equals(field)) {
    			if (att.getType() == es.upm.fi.dia.oeg.prnor.nor.schema.SchemaConstants.STRING_DATATYPE)
    				delimiter = "\'";
    			cAListValue = att.getName();
    		}*/
    	}
    	selectClause = selectClause.substring(0,selectClause.lastIndexOf(","));
    	fromClause += masterEntity;
    	fromClause = removeDuplicates(fromClause,",");
    	
    	String []alistValues = entity.getAttributeValuesAsString(cAListValue);
    	//TO DO: UPPER esto es una chapuza sólo para el caso de LUIS
    	whereClause += "UPPER (" +detailEntity + SEPARATOR + field  + ") = " + delimiter + alistValues[0] +  delimiter + " and " + "UPPER ("+detailEntity + SEPARATOR + field + ") = UPPER(" + masterEntity + SEPARATOR + field +")";
    	
    	if (filterExpression!=null && !filterExpression.equals(""))
    		whereClause += " AND " + filterExpression;

   	
    	String query = "";
    	query = "SELECT " + selectClause + " FROM " + fromClause + " WHERE " + whereClause;
    	logger.debug("buildMasterDetailDetailQuery: " + query);
    	return query;
		
	}
	
	private String buildMasterDetailMasterQuery(Set<SchemaAttribute> attributes, String filterExpression) {
    	String selectClause = "";
    	String fromClause = "";
    	String whereClause = "";
    	for (SchemaAttribute att : attributes) {
    		selectClause += att.getValues()[0] + " as " + att.getName() + ",";
    		fromClause += getTable(att.getValues()[0]) + ",";
    		/*if (att.getValues()[0].equals(field)) {
    			if (att.getType() == es.upm.fi.dia.oeg.prnor.nor.schema.SchemaConstants.STRING_DATATYPE)
    				delimiter = "\'";
    			cAListValue = att.getName();
    		}*/
    	}
    	selectClause = selectClause.substring(0,selectClause.lastIndexOf(","));
    	fromClause = removeDuplicates(fromClause,",");
    	
    	
    	if (filterExpression!=null && !filterExpression.equals("")) {
    		if (!whereClause.equals(""))
    			whereClause += " AND " + filterExpression;
    		else
    			whereClause += filterExpression;
    	}
    	

   	
    	String query = "";
    	query = "SELECT " + selectClause + " FROM " + fromClause + " WHERE " + whereClause;
    	logger.debug("buildMasterDetailDetailQuery: " + query);
    	return query;
	
	}
	
	//Master - detail Data Model Classification scheme method
	public Set<CEntity> getDetailEntitiesMasterDetailDataModel(SchemaEntity sMasterEntity,SchemaEntity sDetailEntity, CEntity masterEntity, String master, String detailEntity, String field) {
		Set <CEntity> entities = new HashSet<CEntity>();
		try {
			String query = buildMasterDetailDetailQuery(sDetailEntity.getAttributes(),masterEntity, master, detailEntity,field, sDetailEntity.getFilterExpression());
			ResultSet rs = executeQuery(query);
			
			if (rs.next()) {
				do {
					CEntity ent = new CEntity();
					setCEntityValues(rs,ent,sDetailEntity);
					setCEntityRelations(sMasterEntity,masterEntity,ent);
					entities.add(ent);
					
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
			logger.error("getDetailEntitiesMasterDetailDataModel: " + e.getMessage());
		}
		return entities;

	}
	
	//Master - detail Data Model Classification scheme method
	public Set<CEntity> getMasterEntitiesMasterDetailDataModel(SchemaEntity sEntity, String masterEntity) {

		Set<CEntity> entities = new HashSet<CEntity>();
		try {
			String query = buildMasterDetailMasterQuery(sEntity.getAttributes(), masterEntity,sEntity.getFilterExpression());
			ResultSet rs = executeQuery(query);
			
			while (rs.next()) {
				CEntity ent = new CEntity();
				setCEntityValues(rs,ent,sEntity);
				entities.add(ent);
			}

			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getMasterEntitiesMasterDetailDataModel: " + e.getMessage());
		}
		return entities;
	}
	
	//Master - detail Data Model Classification scheme method
	public Set<CEntity> getMasterEntitiesMasterDetailDataModel(SchemaEntity masterEntity) {

		Set<CEntity> entities = new HashSet<CEntity>();
		try {
			String query = buildMasterDetailMasterQuery(masterEntity.getAttributes(), masterEntity.getFilterExpression());
			ResultSet rs = executeQuery(query);
			
			while (rs.next()) {
				CEntity ent = new CEntity();
				setCEntityValues(rs,ent,masterEntity);
				entities.add(ent);
			}

			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getMasterEntitiesMasterDetailDataModel: " + e.getMessage());
		}
		return entities;
	}
	
	
	protected String buildRelationBasedQuery(String relId, Set<SchemaAttribute> attributes, String termEntity, String termCode, String linkEntity, String linkTerm1, String linkTerm2, String linkType, String filterExpression ) {
    	String selectClause = "";
    	String fromClause = "";
    	String whereClause = "";
    	String query = "";
    	String termEntityAlias1 = termEntity.substring(0,2) + DataModel.RELATION_BASED_ENTITY_END_1;
    	String termEntityAlias2 = termEntity.substring(0,2) + DataModel.RELATION_BASED_ENTITY_END_2;    	
    	String linkAlias = linkEntity.substring(0,2);
    	//For term1
    	for (SchemaAttribute att : attributes) {
    		if (!termEntity.equals(getTable(att.getValues()[0]))) {
    			selectClause += att.getValues()[0] + " as " + att.getName() + ",";

    		}
    		else {
    			selectClause += termEntityAlias1 + "." + NORUtils.removeTable(att.getValues()[0]) + " as " + att.getName() + DataModel.RELATION_BASED_ENTITY_END_1 + ",";
    			//fromClause += termEntity + " as " + termEntityAlias1 + ",";    		
    		}
    	}
		fromClause += termEntity + " as " + termEntityAlias1 + ",";
    	
    	//For term2
    	for (SchemaAttribute att : attributes) {
    		if (!termEntity.equals(getTable(att.getValues()[0]))) {
    			selectClause += att.getValues()[0] + " as " + att.getName() + ",";
    			fromClause += getTable(att.getValues()[0]) + ",";
    		}
    		else {
    			selectClause += termEntityAlias2 + "." + NORUtils.removeTable(att.getValues()[0]) + " as " + att.getName() + DataModel.RELATION_BASED_ENTITY_END_2 + ",";

    		}
    	}
		fromClause += termEntity + " as " + termEntityAlias2 + ",";    	
		
		selectClause = selectClause.substring(0,selectClause.lastIndexOf(","));
    	
    	//selectClause += linkAlias + "." + linkTerm1 + "," + linkAlias + "." + linkTerm2;
		fromClause += linkEntity + " as " + linkAlias;
  		
    	whereClause += linkAlias + "." + linkType + " = " + relId + " AND " + linkAlias + "." + linkTerm1 + " = " + termEntityAlias1 + "." + termCode + " AND " + linkAlias + "." + linkTerm2 + " = " + termEntityAlias2 + "." + termCode;
		
		
		query = "SELECT " + selectClause + " \nFROM "+ fromClause + " \nWHERE "+ whereClause + " " + filterExpression;
		
		logger.debug("buildRelationBasedQuery: " + query);
		
		return query;
	}

	//Thesaurus Term-based Relation-based model  method
	protected void setCEntityValues(ResultSet rs, CEntity entity1, CEntity entity2, SchemaEntity sEntity) throws SQLException {
		Set<SchemaAttribute> atts = sEntity.getAttributes();
		String cName = "";
		for (SchemaAttribute att : atts) {
			CAttribute cattribute = new CAttribute(att.getName());
			cattribute.setSchemaAttribute(att);
			cattribute.addValue(rs.getObject(att.getName()+DataModel.RELATION_BASED_ENTITY_END_1));
			entity1.addAttribute(cattribute);
			cName += "_" + cattribute.getValues().iterator().next(); //el nombre de la entidad es la concatenacion de valores del atributo
			entity1.setName(cName);
			entity1.setSchema(sEntity);
		}

		cName = "";
		for (SchemaAttribute att : atts) {
			CAttribute cattribute = new CAttribute(att.getName());
			cattribute.setSchemaAttribute(att);
			cattribute.addValue(rs.getObject(att.getName()+DataModel.RELATION_BASED_ENTITY_END_2));
			entity2.addAttribute(cattribute);
			cName += "_" + cattribute.getValues().iterator().next(); //el nombre de la entidad es la concatenacion de valores del atributo
			entity2.setName(cName);
			entity2.setSchema(sEntity);
		}
		
	}
	
	
	
	
		//Thesaurus Term-based Relation-based model  method
	public Set<CEntity> getEntitiesRelationBasedDataModel(SchemaEntity sEntity, String termEntity, String termCode, String linkEntity, String linkTerm1, String linkTerm2, String linkType) {
		Set<CEntity> allEntities = new HashSet<CEntity>();
		
		try {
			
			Set<SchemaRelation> sRels = sEntity.getRelations();
			
			for (SchemaRelation sRel : sRels) {
				
				String query = buildRelationBasedQuery(sRel.getValueId(),sEntity.getAttributes(), termEntity, termCode, linkEntity, linkTerm1, linkTerm2, linkType,sEntity.getFilterExpression());
				ResultSet rs = executeQuery(query);
				
				if (rs.next()) {
					do {
						CEntity entity1 = new CEntity();
						CEntity entity2 = new CEntity();
						setCEntityValues(rs,entity1,entity2,sEntity);
						entity1.setRelation(sRel, entity2);
						allEntities.add(entity1);
						allEntities.add(entity2);					
						
					} while(rs.next());
				}
				rs.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getEntitiesRelationBasedDataModel: " + e.getMessage());
		}
		return allEntities;
	}

	protected String buildRecordBasedQuery(String relId, Set<SchemaAttribute> attributes, String entity){
    	String selectClause = "",fromClause = "", query = "", whereClause = "";
    	
    	for (SchemaAttribute att : attributes) {
    			selectClause += att.getValues()[0] + " as " + att.getName() + ",";
    	}
		
		query = "SELECT " + selectClause + " \nFROM "+ fromClause + " \nWHERE "+ whereClause;
		
		
		
		return query;
	}
	
	//Lexicon record-based model  method
	public Set<CEntity> getEntitiesRecordBasedDataModel (SchemaEntity sEntity, String entityName) {
		Set <CEntity> entities = new HashSet<CEntity>();
		try {
			/*
			Set<SchemaRelation> sRels = sEntity.getRelations();
			
			for (SchemaRelation sRel : sRels) {
				
				String query = buildRelationBasedQuery(sRel.getValueId(),sEntity.getAttributes(), entityName);
				ResultSet rs = executeQuery(query);
				
				if (rs.next()) {
					do {
						CEntity entity1 = new CEntity();
						CEntity entity2 = new CEntity();
						setCEntityValues(rs,entity1,entity2,sEntity);
						entity1.setRelation(sRel, entity2);
						allEntities.add(entity1);
						allEntities.add(entity2);					
						
					} while(rs.next());
				}
				rs.close();
			}

			*/
			
			
			
			/*
			String query = buildRecordBasedQuery(sEntity.getRelations(),sEntity.getAttributes(), entityName);
			
		
			System.exit(0);
			
			ResultSet rs = executeQuery(query);
			
			if (rs.next()) {
				do {
					CEntity entity1 = new CEntity();
					CEntity entity2 = new CEntity();
					setCEntityValues(rs,entity1,entity2,sEntity);
			//		entity1.setRelation(sRel, entity2);
				//	allEntities.add(entity1);
					//allEntities.add(entity2);					
					
				} while(rs.next());
			}
			rs.close();*/
		}
		catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}
		
		return entities;
	}
	
    public void dipose() {
    	try {
        	connection.close();
        	connection = null;
    	}
    	catch (SQLException ex) {
    		
    		ex.printStackTrace();
    	}
    }
    
    
    
}
