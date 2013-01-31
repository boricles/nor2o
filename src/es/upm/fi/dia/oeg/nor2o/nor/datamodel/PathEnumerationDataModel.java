package es.upm.fi.dia.oeg.nor2o.nor.datamodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import es.upm.fi.dia.oeg.nor2o.nor.connector.Connector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.DBConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.FlatfileConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.SpreadsheetConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.XMLConnector;
import es.upm.fi.dia.oeg.nor2o.nor.content.CEntity;
import es.upm.fi.dia.oeg.nor2o.nor.content.CRelation;
import es.upm.fi.dia.oeg.nor2o.nor.content.Content;
import es.upm.fi.dia.oeg.nor2o.nor.schema.Schema;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaEntity;

public class PathEnumerationDataModel extends DataModel {

	String pathField;
	String pathSeparator;
	String mainEntity;
	
	
	
	public PathEnumerationDataModel() {
		super();
	}

	public String getPathField() {
		return pathField;
	}

	public void setPathField(String pathField) {
		this.pathField = pathField;
	}

	public String getPathSeparator() {
		return pathSeparator;
	}

	public void setPathSeparator(String pathSeparator) {
		this.pathSeparator = pathSeparator;
	}
	
	public void setMainEntity(String mainEntity) {
		this.mainEntity = mainEntity;
	}
	
	public String getMainEntity() {
		return this.mainEntity;
	}

	protected void load() {
		
	}
	
	public Set<CEntity> loadContentFromDB(Connector connector, SchemaEntity sEntity) {
		Set<CEntity> topEntities = ((DBConnector)connector).getTopLevelEntitiesPathEnumerationDataModel(sEntity,mainEntity,pathField);
		Set<CEntity> normalEntities;
		Set<CEntity> allEntities = new HashSet<CEntity>();
		
		for (CEntity ent : topEntities) {
			normalEntities = ((DBConnector)connector).getSubEntitiesPathEnumerationDataModel(sEntity,ent,mainEntity,pathField,pathSeparator);
			if (normalEntities != null)
				allEntities.addAll(normalEntities);
		}
	
		allEntities.addAll(topEntities);

		logger.debug("NOR Content for the schema " + sEntity.getName() + " loaded, there are " + allEntities.size() + " entities");
		
		return allEntities;
	}

	public Set<CEntity> loadContentFromSpreadsheet(Connector connector, SchemaEntity sEntity) {
		Set<CEntity> topEntities = ((SpreadsheetConnector)connector).getTopLevelEntitiesPathEnumerationDataModel(sEntity,mainEntity,pathField);
		Set<CEntity> normalEntities;
		Set<CEntity> allEntities = new HashSet<CEntity>();
		
		for (CEntity ent : topEntities) {
			normalEntities = ((SpreadsheetConnector)connector).getSubEntitiesPathEnumerationDataModel(sEntity,ent,mainEntity,pathField,pathSeparator);
			if (normalEntities != null)
				allEntities.addAll(normalEntities);
		}
	
		allEntities.addAll(topEntities);

		logger.debug("NOR Content for the schema " + sEntity.getName() + " loaded, there are " + allEntities.size() + " entities");
		
		return allEntities;
	}

	public Set<CEntity> loadContentFromXML(Connector connector, SchemaEntity sEntity) {
		Set<CEntity> topEntities = ((XMLConnector)connector).getTopLevelEntitiesPathEnumerationDataModel(sEntity,mainEntity,pathField);
		Set<CEntity> normalEntities;
		Set<CEntity> allEntities = new HashSet<CEntity>();
		
		for (CEntity ent : topEntities) {
			normalEntities = ((XMLConnector)connector).getSubEntitiesPathEnumerationDataModel(sEntity,ent,mainEntity,pathField,pathSeparator);
			if (normalEntities != null)
				allEntities.addAll(normalEntities);
		}
	
		allEntities.addAll(topEntities);

		logger.debug("NOR Content for the schema " + sEntity.getName() + " loaded, there are " + allEntities.size() + " entities");
		
		return allEntities;
	}

	
	
	/*
	public Content loadContentFromXML(Connector connector, SchemaEntity sEntity, Content content) {
		//Abrir el xml
		//leer el fichero
		//crear la tabla en un db junk de mysql or hsql
		//recorrer el fichero y generar los inserts into
		//luego crear un db connector y llamar al método de arriba
		
		
		Set<CEntity> topEntities = ((XMLConnector)connector).getTopLevelEntitiesPathEnumerationDataModel(sEntity,mainEntity,pathField);
		Set<CEntity> normalEntities;
		Set<CEntity> allEntities = new HashSet<CEntity>();
		
		for (CEntity ent : topEntities) {
			normalEntities = ((XMLConnector)connector).getSubEntitiesPathEnumerationDataModel(sEntity,ent,mainEntity,pathField,pathSeparator);
			if (normalEntities != null)
				allEntities.addAll(normalEntities);
		}
	
		allEntities.addAll(topEntities);
		
		
		content.setEntities(allEntities);
		return content;
	}
	*/


}
