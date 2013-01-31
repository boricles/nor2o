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
import java.util.Set;

import es.upm.fi.dia.oeg.nor2o.nor.NOR;

/**
 * @author boricles
 *
 */
public class Schema implements SchemaConstants {

	NOR nor;
	Set<SchemaEntity> entities;
	//ArrayList<SchemaRelation> relations;
	//ArrayList<SchemaAttribute> attributes;
	
	String name;
	
	/**
	 * 
	 */
	public Schema() {
		// TODO Auto-generated constructor stub
	}
	
	public Schema(String name) {
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setNOR(NOR nor) {
		this.nor = nor;
	}
	
	public void addSchemaEntity(SchemaEntity schemaEntity) {
		if (entities == null)
			entities = new HashSet<SchemaEntity>();
		entities.add(schemaEntity);
	}

	/*
	public void addSchemaRelation(SchemaRelation schemaRelation) {
		if (relations == null)
			relations = new HashSet<SchemaRelation>();
		relations.add(schemaRelation);
	}
	*/
	public SchemaEntity getSchemaEntity(String name) {
		for (SchemaEntity entity: entities) {
			if (entity.getName().equals(name))
				return entity; 
		}
		return null;
	}
	
	public Set<SchemaEntity> getSchemaEntities() {
		return entities;
	}

	/*
	public SchemaRelation getSchemaRelation(String name) {
		for (SchemaRelation relation: relations) {
			if (relation.getName().equals(name))
				return relation; 
		}
		return null;
	}
	*/

	
	public String toString() {
		String toStr = "Entities\n";
		for (SchemaEntity entity: entities) {
			toStr += entity.toString() + "\n";
		}
		/*toStr += "Relations\n";
		for (SchemaRelation relation: relations) {
			toStr += relation.toString() + "\n";
		}*/
		return toStr;
	}

	
}
