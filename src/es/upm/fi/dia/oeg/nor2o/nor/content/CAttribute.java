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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;



import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaAttribute;

/**
 * @author boricles
 *
 */
public class CAttribute implements Constants {

	protected String name;
	protected Set<Object> values;
	
	protected SchemaAttribute schemaAttribute;

	public CAttribute() {
		
	}
	
	public CAttribute(String name) {
		setName(name);
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Object> getValues() {
		return values;
	}

	public void setValues(Set<Object> values) {
		this.values = values;
	}
	
	public void addValue(Object value) {
		if (this.values == null) 
			values = new HashSet<Object>();
		values.add(value);
		/*switch (type) {
			case STRING : values.add((String)value);
				break;
			case INTEGER : values.add((Integer)value);
				break;				
			case REAL : values.add((Float)value);
				break;
			case DATE : values.add((java.util.Date)value);
				break;			
		}*/
	}

	public SchemaAttribute getSchemaAttribute() {
		return schemaAttribute;
	}

	public void setSchemaAttribute(SchemaAttribute schemaAttribute) {
		this.schemaAttribute = schemaAttribute;
	}
	
	public String toString() {
		String str = "";
		str += getName() + " ";
		for (Object val: values) {
			str += val.toString(); 
		}
		return str;
	}
}
