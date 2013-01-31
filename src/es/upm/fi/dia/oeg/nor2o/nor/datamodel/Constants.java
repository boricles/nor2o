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

public interface Constants {

	//NOR DATAMODELS
	public static final String []DATAMODELS = { "Path Enumeration",
												"Adjacency List",
												"Snowflake",
												"Flattened",
												"Master Detail",
												"Record-based",
												"Relation-based",
												"GenericDataModel"
	}; 
	
	// CLASSIFICATION SCHEME
	public static final int PATH_ENUMERATION = 0;
	public static final int ADJACENCY_LIST = 1;
	public static final int SNOWFLACKE = 2;
	public static final int FLATTENED = 3;	
	
	public static final int MASTER_DETAIL = 4;
	
	// THESAURUS 
	public static final int RECORD_BASED = 5;
	public static final int RELATION_BASED = 6;	
	
	
	public static final String RELATION_BASED_ENTITY_END_1 = "1";
	public static final String RELATION_BASED_ENTITY_END_2 = "2";
	// LEXICON
	
	
	public static final int GENERIC_DATA_MODEL = 7;
	
}
