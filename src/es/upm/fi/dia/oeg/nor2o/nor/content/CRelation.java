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

package es.upm.fi.dia.oeg.nor2o.nor.content;

import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaRelation;

/**
 * @author boricles
 *
 */
public class CRelation {

	
	protected CEntity domain;
	protected CEntity range;
	protected String name;
	
	protected String inverseRelation;
	
	protected SchemaRelation schemaRelation;
	
	public CRelation() {
		
	}
	
	public CRelation(String name) {
		setName(name);
	}

	public void setSchemaRelation(SchemaRelation schemaRelation) {
		this.schemaRelation = schemaRelation;
	}
	
	public SchemaRelation getSchemaRelation() {
		return schemaRelation;
	}
	
	
	public CEntity getDomain() {
		return domain;
	}

	public void setDomain(CEntity domain) {
		this.domain = domain;
	}

	public CEntity getRange() {
		return range;
	}

	public void setRange(CEntity range) {
		this.range = range;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		String str = "";
		str += getName() + " domain " + ((domain!=null) ? domain.getName() : "") + " range " + ((range!=null) ? range.getName() : "");
		return str;
	}

	public String getInverseRelation() {
		return inverseRelation;
	}

	public void setInverseRelation(String inverseRelation) {
		this.inverseRelation = inverseRelation;
	}
	
}
