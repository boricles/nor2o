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

package es.upm.fi.dia.oeg.nor2o.transformation;

public interface PRNORConstants {

	//Location of the prnor.xml configuration file
	public static final String PRNOR_DESCRIPTION_FILE="prnor.xml";
	
	public static final String PRNOR_TAG = "Prnor";
	
	public static final String PRNOR_IDENTIFIER_ATT = "identifier";
	public static final String PRNOR_TRANSFORMATION_ATT = "transformationApproach";	
	public static final String PRNOR_TOPLEVELCLASS_ATT = "topLevelClass";	
	public static final String PRNOR_EXTERNAL_RESOURCE = "externalResource";
	
	
	public static final String PRNOR_CLASS_NODE = "Class";	
	public static final String PRNOR_CLASS_FROM_ATT = "from";	
	public static final String PRNOR_CLASS_IDENTIFIER_ATT = "identifier";	
	
	public static final String PRNOR_OBJECTPROPERTY_NODE = "ObjectProperty";	
	public static final String PRNOR_OBJECTPROPERTY_FROM_ATT = "from";	
	public static final String PRNOR_OBJECTPROPERTY_TO_ATT = "to";
	
	public static final String PRNOR_OBJECTPROPERTY_INVERSE = "inverse";
	
	

	public static final String PRNOR_INDIVIDUAL_NODE = "Individual";	
	public static final String PRNOR_INDIVIDUAL_FROM_ATT = "from";	
	public static final String PRNOR_INDIVIDUAL_IDENTIFIER_ATT = "identifier";	
	
	public static final String PRNOR_DATATYPEPROPERTY_NODE = "DataTypeProperty";	
	public static final String PRNOR_DATATYPEPROPERTY_FROM_ATT = "from";	
	public static final String PRNOR_DATATYPEPROPERTY_TO_ATT = "to";	
	public static final String PRNOR_DATATYPEPROPERTY_TYPE_ATT = "type";
	public static final String PRNOR_DATATYPEPROPERTY_LANG_ATT = "lang";
	public static final String PRNOR_DATATYPEPROPERTY_IF_ATT = "if";
	
	public static final String PRNOR_ABOX = "ABox";
	public static final String PRNOR_TBOX = "TBox";
	public static final String PRNOR_POPULATION = "Population";	
	
	public static final String PRNOR_RDFS_LABEL = "rdfs:label";
	
	public static final String PRNOR_SKOS_PREFLABEL = "skos:prefLabel";
	public static final String PRNOR_SKOS_ALTLABEL = "skos:altLabel";	
}
