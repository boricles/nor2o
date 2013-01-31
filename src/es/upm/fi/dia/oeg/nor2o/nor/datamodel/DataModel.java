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

package es.upm.fi.dia.oeg.nor2o.nor.datamodel;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.upm.fi.dia.oeg.nor2o.nor.NOR;
import es.upm.fi.dia.oeg.nor2o.nor.connector.Connector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.DBConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.FlatfileConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.SpreadsheetConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.XMLConnector;

public class DataModel implements Constants {
	
	protected Set<DMEntity> entities;
	protected int numEntities;
	protected NOR nor;
	
	static protected Logger logger = Logger.getLogger(DataModel.class.getName());
	
	public DataModel() {
		entities = new HashSet<DMEntity>();
	}
	
	public void setNor(NOR nor) {
		this.nor = nor;
	}
	
	public void addEntity(DMEntity entity) {
		if (entities!=null) {
			entities.add(entity);
		}
	}

	/*public DMEntity[] getEntities() {
		DMEntity []result = null;
		if (entities!=null) {
			result = new DMEntity[entities.size()];
			for (int i=0; i<entities.size();i++) {
				result[i] = entities.get(i);
			}
		}
		return result;
	}*/
	
	/*public DMEntity getEntity(String name) {
		for (int i=0; i<entities.size(); i++) {
			if ( ((DMEntity)entities.get(i)).getName().equals(name))
				return ((DMEntity)entities.get(i));
		}
		return null;
	}*/
	
	public void loadModel() {
		//loadEntities();
	}
	
	protected void loadEntities() {
		/*Connector connector = nor.getConnector();
		if (connector instanceof DBConnector) {
			entities = ((DBConnector)connector).retrieveEntities();
		}
		if (connector instanceof XMLConnector) {
			
		}
		if (connector instanceof FlatfileConnector) {
			
		}
		if (connector instanceof SpreadsheetConnector) {
			
		}*/
	}
	
	public void loadContent() {
		
	}

	public NOR getNor() {
		return nor;
	}
	
}
