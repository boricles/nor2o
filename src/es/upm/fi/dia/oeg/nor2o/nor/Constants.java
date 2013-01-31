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

public interface Constants {
	
	
	public static final String []NORS = {"Classification Scheme",
										 "Thesaurus",
										 "Lexicon",
										 "GenericSpreadSheet"};

	//NOR TYPES
	public static final int CLASSIFICATION_SCHEME = 0;
	public static final int THESAURUS = 1;
	public static final int LEXICON = 2;
	
	
	public static final int GENERIC_NOR = 3;
	
	//THESAURUS TYPES
	public static final int CONCEPT_BASED_THESAURUS = 20;
	public static final int TERM_BASED_THESAURUS = 21;	

	//SCHEMA COMPONENTS
	public static final String CS_ITEM = "Classification Scheme Item";
	public static final String CS_ITEM_RELATIONSHIP = "Classification Scheme Item Relationship";
	
	//Schema relations
	public static final String GENERIC_SUBTYPE = "subType";
	public static final String GENERIC_SUPERTYPE = "superType";
	
	
	//attribute identifier
	public static final String IDENTIFIER_ATTRIBUTE_NAME = "Identifier";

}
