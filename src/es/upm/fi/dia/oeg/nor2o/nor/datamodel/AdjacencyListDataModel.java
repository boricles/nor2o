package es.upm.fi.dia.oeg.nor2o.nor.datamodel;

import java.util.HashSet;
import java.util.Set;

import es.upm.fi.dia.oeg.nor2o.nor.connector.Connector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.DBConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.SpreadsheetConnector;
import es.upm.fi.dia.oeg.nor2o.nor.content.CEntity;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaEntity;

public class AdjacencyListDataModel extends DataModel {
	
	String listField;
	String listId;
	String mainEntity;
	
	public AdjacencyListDataModel() {
		super();
	}

	public String getListField() {
		return listField;
	}

	public void setListField(String listField) {
		this.listField = listField;
	}

	public String getListId() {
		return listId;
	}

	public void setListId(String listId) {
		this.listId = listId;
	}

	public String getMainEntity() {
		return mainEntity;
	}

	public void setMainEntity(String mainEntity) {
		this.mainEntity = mainEntity;
	}

	public Set<CEntity> loadContentFromDB(Connector connector, SchemaEntity sEntity) {
		
		Set<CEntity> topEntities = ((DBConnector)connector).getTopLevelEntitiesAdjacencyListDataModel(sEntity,mainEntity,listField,listId);
		Set<CEntity> normalEntities;
		Set<CEntity> allEntities = new HashSet<CEntity>();
		
		for (CEntity ent : topEntities) {
			normalEntities = ((DBConnector)connector).getSubEntitiesAdjacencyListDataModel(sEntity,ent,mainEntity,listField,listId);
			if (normalEntities != null)
				allEntities.addAll(normalEntities);
		}
	
		allEntities.addAll(topEntities);
		
		logger.debug("NOR Content loaded, there are " + allEntities.size() + " entities");
		
		return allEntities;
	}
	
	public Set<CEntity> loadContentFromSpreadsheet(Connector connector, SchemaEntity sEntity) {
		
		Set<CEntity> topEntities = ((SpreadsheetConnector)connector).getTopLevelEntitiesAdjacencyListDataModel(sEntity,mainEntity,listField,listId);
		Set<CEntity> normalEntities;
		Set<CEntity> allEntities = new HashSet<CEntity>();
		
		for (CEntity ent : topEntities) {
			normalEntities = ((SpreadsheetConnector)connector).getSubEntitiesAdjacencyListDataModel(sEntity,ent,mainEntity,listField,listId);
			if (normalEntities != null)
				allEntities.addAll(normalEntities);
		}
	
		allEntities.addAll(topEntities);
		
		logger.debug("NOR Content loaded, there are " + allEntities.size() + " entities");
		
		return allEntities;
	}	
	
	
}
