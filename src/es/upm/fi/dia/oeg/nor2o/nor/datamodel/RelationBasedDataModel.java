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

import java.util.ArrayList;
import java.util.Set;

import es.upm.fi.dia.oeg.nor2o.nor.connector.Connector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.DBConnector;
import es.upm.fi.dia.oeg.nor2o.nor.content.CEntity;
import es.upm.fi.dia.oeg.nor2o.nor.content.Content;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaEntity;

/**
 * @author boricles
 *
 */
public class RelationBasedDataModel extends DataModel {

	
	protected String termEntity; 
	protected String termCode; 
	protected String linkEntity; 
	protected String linkTerm1; 
	protected String linkTerm2; 
	protected String linkType; 
	protected String linkTypeEntity; 
	protected String linkTypeId; 
	protected String linkDesc; 
	protected String linkAbbr; 
	
	
	/**
	 * 
	 */
	public RelationBasedDataModel() {
		// TODO Auto-generated constructor stub
	}

	public void setTermEntityInformation (String termEntity, String termCode) {
		this.termCode = termCode;
		this.termEntity = termEntity;
	}
	
	public void setTermLinkInformation (String linkEntity, String linkTerm1, String linkTerm2, String linkType) {
		this.linkEntity = linkEntity; 
		this.linkTerm1 = linkTerm1; 
		this.linkTerm2 = linkTerm2;
		this.linkType = linkType;
	}
	
	public void setLinkTypeInformation (String linkTypeEntity, String linkTypeId, String linkDesc, String linkAbbr) {
		this.linkTypeEntity = linkTypeEntity;
		this.linkTypeId = linkTypeId;
		this.linkDesc = linkDesc;
		this.linkAbbr = linkAbbr;
		
	}
	
	public Set<CEntity> loadContentFromDB(Connector connector, SchemaEntity sEntity) {
		Set<CEntity> entities = ((DBConnector)connector).getEntitiesRelationBasedDataModel(sEntity,termEntity,termCode,linkEntity,linkTerm1,linkTerm2,linkType);
		
		logger.debug("NOR Content loaded, there are " + entities.size() + " entities");
		
		/*
		Set<CEntity> topEntities = ((DBConnector)connector).getTopLevelEntitiesPathEnumerationDataModel(sEntity,mainEntity,pathField);
		Set<CEntity> normalEntities;
		Set<CEntity> allEntities = new HashSet<CEntity>();
		for (CEntity ent : topEntities) {
			normalEntities = ((DBConnector)connector).getSubEntitiesPathEnumerationDataModel(sEntity,ent,mainEntity,pathField,pathSeparator);
			if (normalEntities != null)
				allEntities.addAll(normalEntities);
		}
		allEntities.addAll(topEntities);*/
	
		return entities;
	
	}

}
