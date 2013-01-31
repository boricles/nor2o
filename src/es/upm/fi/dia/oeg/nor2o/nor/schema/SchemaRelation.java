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
public class SchemaRelation {

	protected String domain;
	protected String range;
	protected String name;
	protected String uses;
	protected String cardinality;	
	protected String valueId;
	
	//SpreadSheet
	protected String usesSpreadSheetColumn;
	protected String usesSpreadSheetRow;
	
	/**
	 * 
	 */
	public SchemaRelation() {
		// TODO Auto-generated constructor stub
	}
	
	public SchemaRelation(String name) {
		setName(name);

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getUses() {
		return uses;
	}

	public void setUses(String uses) {
		this.uses = uses;
	}

	public String getCardinality() {
		return cardinality;
	}

	public void setCardinality(String cardinality) {
		this.cardinality = cardinality;
	}

	public String toString() {
		String toStr = "SchemaRelation Name:" + name + " uses: " + uses + " cardinality: " + cardinality + " domain: " + domain + " range: " + range;
		return toStr;
	}

	public String getValueId() {
		return valueId;
	}

	public void setValueId(String valueId) {
		this.valueId = valueId;
	}

	public String getUsesSpreadSheetColumn() {
		return usesSpreadSheetColumn;
	}

	public void setUsesSpreadSheetColumn(String usesSpreadSheetColumn) {
		this.usesSpreadSheetColumn = usesSpreadSheetColumn;
	}

	public String getUsesSpreadSheetRow() {
		return usesSpreadSheetRow;
	}

	public void setUsesSpreadSheetRow(String usesSpreadSheetRow) {
		this.usesSpreadSheetRow = usesSpreadSheetRow;
	}

}
