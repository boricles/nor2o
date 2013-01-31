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
import es.upm.fi.dia.oeg.nor2o.nor.connector.SpreadsheetConnector;
import es.upm.fi.dia.oeg.nor2o.nor.content.CEntity;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaEntity;

/**
 * @author boricles
 *
 */
public class GenericDataModel extends DataModel {

	public GenericDataModel() {
		super();
	}
	
	public Set<CEntity> loadContentFromSpreadSheet(Connector connector, SchemaEntity sEntity) {
		Set<CEntity> allEntities = ((SpreadsheetConnector)connector).getEntitiesGenericDataModel(sEntity);

		logger.debug("NOR Content for the schema " + sEntity.getName() + " loaded, there are " + allEntities.size() + " entities");
		
		return allEntities;
	}
	

}
