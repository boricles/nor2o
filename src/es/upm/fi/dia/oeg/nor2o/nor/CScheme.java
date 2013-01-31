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

package es.upm.fi.dia.oeg.nor2o.nor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import es.upm.fi.dia.oeg.nor2o.nor.connector.Connector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.DBConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.FlatfileConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.SpreadsheetConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.XMLConnector;
import es.upm.fi.dia.oeg.nor2o.nor.content.CEntity;
import es.upm.fi.dia.oeg.nor2o.nor.content.Content;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.AdjacencyListDataModel;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.DMEntity;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.DataModel;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.FlattenedDataModel;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.MasterDetailDataModel;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.PathEnumerationDataModel;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.RecordBasedDataModel;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.SnowflakeDataModel;
import es.upm.fi.dia.oeg.nor2o.nor.schema.Schema;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaEntity;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaRelation;
import es.upm.fi.dia.oeg.nor2o.nor.util.NORDescriptionConstants;

public class CScheme extends NOR {

	public CScheme() {
		setType(Constants.CLASSIFICATION_SCHEME);
		//defineSchema();
		
	}
	
	public CScheme(String name) {
		super(name);
		setType(Constants.CLASSIFICATION_SCHEME);
		//defineSchema();
	}
	
	public void setDataModel(DataModel dataModel) {
		super.setDataModel(dataModel);
		
		if (dataModel instanceof PathEnumerationDataModel)
			setModel(DataModel.PATH_ENUMERATION);

		if (dataModel instanceof AdjacencyListDataModel)
			setModel(DataModel.ADJACENCY_LIST);
		
		if (dataModel instanceof SnowflakeDataModel)
			setModel(DataModel.SNOWFLACKE);
		
		if (dataModel instanceof FlattenedDataModel)
			setModel(DataModel.FLATTENED);
	}
	
	
	public void load() {
		loadContent();
		//dataModel.loadModel();
		//dataModel.loadContent();
			
	}
	

	protected void loadContent() {
		Connector connector = getConnector();
		Content content = getContent();
		if (content == null)
			content = new Content(getName());
		Set<CEntity> allEntities = new HashSet<CEntity>(); 

		if (connector instanceof DBConnector) {
			if (dataModel instanceof PathEnumerationDataModel) {
				Set<CEntity> entities;
				for (SchemaEntity sEntity : getSchema().getSchemaEntities()) {
					entities = ((PathEnumerationDataModel)dataModel).loadContentFromDB(connector,sEntity);
					allEntities.addAll(entities);
				}
				((DBConnector)connector).dipose();
			}
			if (dataModel instanceof AdjacencyListDataModel) {
				Set<CEntity> entities;
				for (SchemaEntity sEntity : getSchema().getSchemaEntities()) {
					entities = ((AdjacencyListDataModel)dataModel).loadContentFromDB(connector,sEntity);
					allEntities.addAll(entities);
				}
				((DBConnector)connector).dipose();
			}
			if (dataModel instanceof MasterDetailDataModel) {
				Set<CEntity> entities;
				for (SchemaEntity sEntity : getSchema().getSchemaEntities()) {
					String masterEntityName = sEntity.getName();
					if (sEntity.getRelations() != null) {
						for (SchemaRelation sRelation : sEntity.getRelations()) {
							if (sRelation.getUses().equals(NORDescriptionConstants.NOR_MASTERDETAIL_NODE)) {
								for (SchemaEntity detailEntity : getSchema().getSchemaEntities())
									if (!detailEntity.getName().equals(masterEntityName)) {
										entities = ((MasterDetailDataModel)dataModel).loadContentFromDB(connector,sEntity,detailEntity);
										allEntities.addAll(entities);
									}
							}
						}
					}
					else {
						entities = ((MasterDetailDataModel)dataModel).loadContentFromDB(connector,sEntity);
						allEntities.addAll(entities);
					}
						
				}
				((DBConnector)connector).dipose();
			}
			
			
		}
		if (connector instanceof XMLConnector) {
			if (dataModel instanceof PathEnumerationDataModel) {
				Set<CEntity> entities;
				for (SchemaEntity sEntity : getSchema().getSchemaEntities()) {
					entities = ((PathEnumerationDataModel)dataModel).loadContentFromXML(connector,sEntity);
					allEntities.addAll(entities);
				}
				
			}
			if (dataModel instanceof AdjacencyListDataModel) {
				/*//IDEM AL PATHENUMERATION
				SchemaEntity sEntity = getSchema().getSchemaEntities().get(0);
				content = ((AdjacencyListDataModel)dataModel).loadContentFromXML(connector,sEntity,content);*/ 
			}
			
			if (dataModel instanceof SnowflakeDataModel) {
				Set<CEntity> entities;
				for (SchemaEntity sEntity : getSchema().getSchemaEntities()) {
					entities = ((SnowflakeDataModel)dataModel).loadContentFromXML(connector,sEntity);
					allEntities.addAll(entities);
				}				
			}
			
		}
		if (connector instanceof SpreadsheetConnector) {
			if (dataModel instanceof PathEnumerationDataModel) {
				Set<CEntity> entities;
				for (SchemaEntity sEntity : getSchema().getSchemaEntities()) {
					entities = ((PathEnumerationDataModel)dataModel).loadContentFromSpreadsheet(connector,sEntity);
					allEntities.addAll(entities);
				}
				((SpreadsheetConnector)connector).dipose();
			}
			if (dataModel instanceof AdjacencyListDataModel) {
				Set<CEntity> entities;
				for (SchemaEntity sEntity : getSchema().getSchemaEntities()) {
					entities = ((AdjacencyListDataModel)dataModel).loadContentFromSpreadsheet(connector,sEntity);
					allEntities.addAll(entities);
				}
				((SpreadsheetConnector)connector).dipose();
			}
		}

		if (connector instanceof FlatfileConnector) {
			
		}

		
		content.setEntities(allEntities);
		setContent(content);
		
	}
}
