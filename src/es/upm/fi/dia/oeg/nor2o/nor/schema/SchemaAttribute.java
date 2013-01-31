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

/**
 * @author boricles
 *
 */
public class SchemaAttribute {

	protected int type; //TO DO: mapear a los xs:datatypes
	protected String name;
	protected String values[];
	
	/**
	 * 
	 */
	public SchemaAttribute() {
		// TODO Auto-generated constructor stub
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}
	
	protected String valuesToString() {
		String vals="";
		for (int i = 0; i<values.length; i++) {
			vals += values[i] + ((values.length==i) ? "" : ",");
		}
		return vals;
	}
	
	/*public String toString() {
		String toStr = "SchemaAttribute Name: " + name + "valueFrom: " + valuesToString() +" type: " + es.upm.fi.dia.oeg.prnor.nor.schema.SchemaConstants.DataTypes[type];
		return toStr;
	}*/
	

}
