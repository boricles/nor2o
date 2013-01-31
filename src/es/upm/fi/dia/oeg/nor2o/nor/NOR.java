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

package es.upm.fi.dia.oeg.nor2o.nor;

import es.upm.fi.dia.oeg.nor2o.nor.connector.Connector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.DBConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.FlatfileConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.SpreadsheetConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.XMLConnector;
import es.upm.fi.dia.oeg.nor2o.nor.content.Content;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.DataModel;
import es.upm.fi.dia.oeg.nor2o.nor.schema.Schema;

public class NOR implements Constants {

	private String name;
	private int type;
	
	private int model;
	private int implementation;
	
	protected DataModel dataModel;
	protected Connector connector;
	
	protected Schema schema;
	protected Content content;
	
	
	public NOR() {
		
	}

	public NOR(String name) {
		setName(name);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}	
	
	public int getModel() {
		return model;
	}

	protected void setModel(int model) {
		this.model = model;
	}	
	
	public int getImplementation() {
		return implementation;
	}

	public String toString() {
		String toString = "NOR { Name: " + getName() + "\nType: " + NORS[getType()] + "\nData model: "+DataModel.DATAMODELS[getModel()] + "\nImplementation: "+ Connector.IMPLEMENTATIONS [getImplementation()] + "}";
		return toString;
	}

	public Connector getConnector() {
		return connector;
	}
	
	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
		dataModel.setNor(this);
	}

	public void setConnector(Connector connector) {
		this.connector = connector;
		
		if (connector instanceof DBConnector)
			this.implementation = Connector.DATABASE;

		if (connector instanceof XMLConnector)
			this.implementation = Connector.XML;

		if (connector instanceof SpreadsheetConnector)
			this.implementation = Connector.SPREADSHEET;

		if (connector instanceof FlatfileConnector)
			this.implementation = Connector.FLAT_FILE;
	}
	
	public void dispose() {
		if (connector != null) {
			if (implementation == Connector.DATABASE) {
				DBConnector dbconnector = (DBConnector) connector;
				dbconnector.dipose();
				dbconnector = null;
			}
			
			if (implementation == Connector.FLAT_FILE) {
			}
			
			if (implementation == Connector.SPREADSHEET) {
			}

			if (implementation == Connector.XML) {
			}
			
			connector = null;
		}
	}
	
	public void load() {
	}
	
	public void defineSchema() {
		
	}

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
		schema.setNOR(this);
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

}
