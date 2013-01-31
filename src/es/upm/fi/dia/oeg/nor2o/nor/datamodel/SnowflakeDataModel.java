package es.upm.fi.dia.oeg.nor2o.nor.datamodel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import es.upm.fi.dia.oeg.nor2o.nor.connector.Connector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.DBConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.XMLConnector;
import es.upm.fi.dia.oeg.nor2o.nor.content.CEntity;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaEntity;

public class SnowflakeDataModel extends DataModel {

	Map<String,String> entities;
	Map<String,String> cols;
	Map<String,String> keys;

	int numberOfEntities;
	
	public SnowflakeDataModel() {
		super();
	}
	
	public void init() {
		entities = new HashMap<String,String>();
		cols = new HashMap<String,String>(); 
		keys = new HashMap<String,String>();
	}
	
	public void setNumberOfEntities(int number) {
		this.numberOfEntities = number;
	}
	
	public void setEntity(String entity, String childEntity) {
		entities.put(entity, childEntity);
	}
	
	public void setChildColumn(String entity, String childColumn) {
		cols.put(entity, childColumn);
	}
	
	public void setKeyColumn(String entity, String keyColumn) {
		keys.put(entity, keyColumn);
	}
	
	//By now I'm assuming I have only one entity
	public Set<CEntity> loadContentFromXML(Connector connector, SchemaEntity sEntity) {
		Iterator it = entities.keySet().iterator();
		Set<CEntity> allEntities = new HashSet<CEntity>();
		while (it.hasNext()) {
			Set<CEntity> ents = ((XMLConnector)connector).getEntitiesSnowflakeDataModel(sEntity,it.next().toString());
			allEntities.addAll(ents);
			
		}
		return allEntities;
	}
	
}
