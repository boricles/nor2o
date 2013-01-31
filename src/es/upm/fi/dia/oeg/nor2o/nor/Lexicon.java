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

import java.util.HashSet;
import java.util.Set;

import es.upm.fi.dia.oeg.nor2o.nor.connector.Connector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.DBConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.FlatfileConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.SpreadsheetConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.XMLConnector;
import es.upm.fi.dia.oeg.nor2o.nor.content.CEntity;
import es.upm.fi.dia.oeg.nor2o.nor.content.Content;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.DataModel;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.RecordBasedDataModel;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.RelationBasedDataModel;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaEntity;

/**
 * @author boricles
 *
 */
public class Lexicon extends NOR {
	/**
	 * 
	 */
	public Lexicon() {
		// TODO Auto-generated constructor stub
		setType(Constants.THESAURUS);
	}

	/**
	 * @param name
	 */
	public Lexicon(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		setType(Constants.LEXICON);
	}

	public void setDataModel(DataModel dataModel) {
		super.setDataModel(dataModel);
		
		if (dataModel instanceof RelationBasedDataModel)
			setModel(DataModel.RELATION_BASED);

		if (dataModel instanceof RecordBasedDataModel)
			setModel(DataModel.RECORD_BASED);
		
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
			if (dataModel instanceof RelationBasedDataModel) {
				//aqui hay que hacer un foreach de los cschemaentities y para cada uno hacer el gettoplevel
				//o suponer que ya que estamos en un cscheme hacer una sola llamada para ese metodo.
				//la segunda opción es l amejor
				
				Set<CEntity> entities;
				for (SchemaEntity sEntity : getSchema().getSchemaEntities()) {
					entities = ((RelationBasedDataModel)dataModel).loadContentFromDB(connector,sEntity);
					allEntities.addAll(entities);
				}
				
				
			}
			if (dataModel instanceof RecordBasedDataModel) {
				Set<CEntity> entities;
				for (SchemaEntity sEntity : getSchema().getSchemaEntities()) {
					entities = ((RecordBasedDataModel)dataModel).loadContentFromDB(connector,sEntity);
					allEntities.addAll(entities);
				}			}
			
		}
		if (connector instanceof XMLConnector) {
			if (dataModel instanceof RelationBasedDataModel) {
				
				//aqui hay que hacer un foreach de los cschemaentities y para cada uno hacer el gettoplevel
				//o suponer que ya que estamos en un cscheme hacer una sola llamada para ese metodo.
				//la segunda opción es l amejor
				/*
				SchemaEntity sEntity = getSchema().getSchemaEntities().get(0);
				content = ((PathEnumerationDataModel)dataModel).loadContentFromXML(connector,sEntity,content);
				*/ 
			}
			if (dataModel instanceof RecordBasedDataModel) {
				Set<CEntity> entities;
				for (SchemaEntity sEntity : getSchema().getSchemaEntities()) {
					entities = ((RecordBasedDataModel)dataModel).loadContentFromXML(connector,sEntity);
					allEntities.addAll(entities);
				}
				
				/*//IDEM AL PATHENUMERATION
				SchemaEntity sEntity = getSchema().getSchemaEntities().get(0);
				content = ((AdjacencyListDataModel)dataModel).loadContentFromXML(connector,sEntity,content);*/ 
			}
			
		}
		if (connector instanceof FlatfileConnector) {
			
		}
		if (connector instanceof SpreadsheetConnector) {
			
		}
		
		//content = checkInverseRelations(content); TO DO: we have to do this when we are transforming to the ontology
		
		content.setEntities(allEntities);
		setContent(content);
		
	}

}
