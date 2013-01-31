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

import java.util.HashSet;
import java.util.Set;

import es.upm.fi.dia.oeg.nor2o.nor.connector.Connector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.DBConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.SpreadsheetConnector;
import es.upm.fi.dia.oeg.nor2o.nor.content.CEntity;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaEntity;

/**
 * @author boricles
 *
 */
public class MasterDetailDataModel extends DataModel {

	String masterEntity;
	String detailEntity;
	String field;
	
	
	
	public MasterDetailDataModel() {
		super();
	}

	
	protected void load() {
		
	}
	
	public Set<CEntity> loadContentFromDB(Connector connector, SchemaEntity sMasterEntity) {
		Set<CEntity> masterEntities = ((DBConnector)connector).getMasterEntitiesMasterDetailDataModel(sMasterEntity);
		
		logger.debug("NOR Content for the schema " + sMasterEntity.getName() + " loaded, there are " + masterEntities.size() + " entities");
		
		return masterEntities;
	}
	
	public Set<CEntity> loadContentFromDB(Connector connector, SchemaEntity sMasterEntity, SchemaEntity sDetailEntity) {
		Set<CEntity> masterEntities = ((DBConnector)connector).getMasterEntitiesMasterDetailDataModel(sMasterEntity,masterEntity);
		Set<CEntity> detailEntities;
		Set<CEntity> allEntities = new HashSet<CEntity>();
		
		for (CEntity ent : masterEntities) {
			detailEntities = ((DBConnector)connector).getDetailEntitiesMasterDetailDataModel(sMasterEntity,sDetailEntity,ent,masterEntity,detailEntity,field);
			if (detailEntities != null)
				allEntities.addAll(detailEntities);
		}
	
		allEntities.addAll(masterEntities);


		
		logger.debug("NOR Content for the schema " + sMasterEntity.getName() + " loaded, there are " + allEntities.size() + " entities");
		
		return allEntities;
	}

/*	public Set<CEntity> loadContentFromSpreadsheet(Connector connector, SchemaEntity sEntity) {
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
*/

	public String getMasterEntity() {
		return masterEntity;
	}


	public void setMasterEntity(String masterEntity) {
		this.masterEntity = masterEntity;
	}


	public String getDetailEntity() {
		return detailEntity;
	}


	public void setDetailEntity(String detailEntity) {
		this.detailEntity = detailEntity;
	}


	public String getField() {
		return field;
	}


	public void setField(String field) {
		this.field = field;
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
