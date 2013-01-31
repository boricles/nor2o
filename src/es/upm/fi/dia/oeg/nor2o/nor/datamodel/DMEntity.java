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
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class DMEntity {
	
	String name;
	Set<DMField> fields;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setFields(Set<DMField> fields) {
		this.fields = fields;
	}
	
	public int getNumFields() {
		if (fields!=null)
			return fields.size();
		return 0;
	}
	
	/*
	public String getFieldName(int index) {
		if (fields!=null)
			return fields.get(index).getName();
		return "";
	}
	*/
	public Set<DMField> getFields() {
		return this.fields;
	}

}
