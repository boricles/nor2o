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

package es.upm.fi.dia.oeg.nor2o.nor.datamodel;

import java.util.Set;

import es.upm.fi.dia.oeg.nor2o.nor.connector.Connector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.DBConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.XMLConnector;
import es.upm.fi.dia.oeg.nor2o.nor.content.CEntity;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaEntity;

/**
 * @author boricles
 *
 */
public class RecordBasedDataModel extends DataModel {

	
	protected String entity;
	
	
	/**
	 * 
	 */
	
	public RecordBasedDataModel() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public String getEntity() {
		return entity;
	}
	
	
	
	public void setEntity(String entity) {
		this.entity = entity;
	}
	
	

	public Set<CEntity> loadContentFromXMLSEntityList(Connector connector, SchemaEntity sEntity) {
		logger.debug("Cargando entidades para " + sEntity.getName());
		
		Set<CEntity> entities = ((XMLConnector)connector).getEntitiesOfListRecordBasedDataModel(sEntity,entity);
		
		logger.debug("NOR Content loaded, there are " + entities.size() + " entities");
		
		return entities;
	}
	
	
	public Set<CEntity> loadContentFromXML(Connector connector, SchemaEntity sEntity) {
		logger.debug("Cargando entidades para " + sEntity.getName());
		
		Set<CEntity> entities = ((XMLConnector)connector).getEntitiesRecordBasedDataModel(sEntity,entity);
		
		logger.debug("NOR Content loaded, there are " + entities.size() + " entities");
		
		return entities;
	}

	public Set<CEntity> loadContentFromDB(Connector connector, SchemaEntity sEntity) {
		Set<CEntity> entities = ((DBConnector)connector).getEntitiesRecordBasedDataModel(sEntity,entity);
		
		logger.debug("NOR Content loaded, there are " + entities.size() + " entities");

		return entities;
	
	}
}
