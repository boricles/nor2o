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

package es.upm.fi.dia.oeg.nor2o.nor.schema;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.upm.fi.dia.oeg.nor2o.nor.content.CRelation;

/**
 * @author boricles
 *
 */
public class SchemaEntity {

	protected Set<SchemaRelation> relations;
	protected Set<SchemaAttribute> attributes;
	
	protected String filterExpression;
	
	protected Schema schema;
	
	protected String name;
	
	protected String type;
	
	protected boolean isList = false;
	
	/**
	 * 
	 */
	public SchemaEntity() {
		// TODO Auto-generated constructor stub
		filterExpression = "";
	}

	public SchemaEntity(String name) {
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void addAttribute(SchemaAttribute attr) {
		if (attributes == null)
			attributes = new HashSet<SchemaAttribute>();
		attributes.add(attr);
	}
	
	public void addRelation(SchemaRelation rel) {
		if (relations == null)
			relations = new HashSet<SchemaRelation>();
		relations.add(rel);
	}
	
	public SchemaRelation getRelation (String relName) {
		for (SchemaRelation rel: relations) 
			if (rel.getName().equals(relName))
				return rel;
		return null;
	}

	public SchemaAttribute getAttribute (String attName) {
		for (SchemaAttribute att: attributes) 
			if (att.getName().equals(attName))
				return att;
		return null;
	}
	
	public Set<SchemaAttribute> getAttributes() {
		return attributes;
	}
	
	public Set<SchemaRelation> getRelations() {
		return relations;
	}
	/*
	public String toString() {
		String toStr = "SchemaEntity Name: " + name + "\n Attributes\n";
		for (SchemaAttribute att : attributes) {
			toStr += att.toString() + "\n";
		}
		for (SchemaRelation rel : relations) {
			toStr += rel.toString() + "\n";
		}
		
		return toStr;
	}*/

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	public String getFilterExpression() {
		return filterExpression;
	}

	public void setFilterExpression(String filterExpression) {
		this.filterExpression = filterExpression;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void setIsList(boolean isList) {
		this.isList = isList;
	}
	
	public boolean isList() {
		return this.isList;
	}
	
}
