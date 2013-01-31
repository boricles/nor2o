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

package es.upm.fi.dia.oeg.nor2o.nor.util;

import es.upm.fi.dia.oeg.nor2o.nor.connector.DBConnector;

	
public interface NORDescriptionConstants {

	//Location of the nor.xml configuration file
	public static final String NOR_DESCRIPTION_FILE="nor.xml";
	
	public static final String NOR_TAG = "Nor";
	
	public static final String NOR_TYPE_ATT = "type";
	public static final String NOR_NAME_ATT = "name";	

	public static final String NOR_SCHEMA_NODE = "Schema";
	public static final String NOR_SCHEMA_ENTITIES = "SchemaEntities";
	public static final String NOR_SCHEMA_ENTITY = "SchemaEntity";
	public static final String NOR_SCHEMA_ENTITY_NAME = "name";
	
	public static final String NOR_SCHEMA_IS_LIST = "list";
	public static final String NOR_SCHEMA_IS_LIST_TRUE = "yes";
	public static final String NOR_SCHEMA_IS_LIST_FALSE = "no";
	
	public static final String NOR_SCHEMA_ENTITY_TYPE = "type";
	
	public static final String NOR_SCHEMA_ATTRIBUTE = "Attribute";	
	public static final String NOR_SCHEMA_ATTRIBUTE_NAME = "name";
	public static final String NOR_SCHEMA_ATTRIBUTE_VALUE = "valueFrom";
	public static final String NOR_SCHEMA_ATTRIBUTE_TYPE = "type";	
	
	public static final String NOR_SCHEMA_RELATION = "Relation";
	public static final String NOR_SCHEMA_RELATION_NAME = "name";	
	public static final String NOR_SCHEMA_RELATION_USING = "using";
	
	public static final String NOR_SCHEMA_RELATION_USING_SPREADSHEET_COLUMN = "usingSpreadSheetColumn";
	public static final String NOR_SCHEMA_RELATION_USING_SPREADSHEET_ROW = "usingSpreadSheetRow";
	
	public static final String NOR_SCHEMA_RELATION_CARDINALITY = "cardinality";	
	public static final String NOR_SCHEMA_RELATION_DESTINATION = "destination";
	public static final String NOR_SCHEMA_RELATION_VALUEID = "valueId";
	
	public static final String NOR_SCHEMA_FILTER = "Filter";
	public static final String NOR_SCHEMA_EXPRESSION = "expression";
	
	//public static final String NOR_SCHEMA_RELATION_INVERSE = "inverseOf";	

	/*public static final String NOR_SCHEMA_RELATIONS = "SchemaRelations";
	public static final String NOR_SCHEMA_RELATION = "SchemaRelation";
	public static final String NOR_SCHEMA_RELATION_NAME = "name";	
	public static final String NOR_SCHEMA_RELATION_USING = "using";
	public static final String NOR_SCHEMA_RELATION_CARDINALITY = "cardinality";	

	public static final String NOR_SCHEMA_RELATION_ORIGIN = "Origin";
	public static final String NOR_SCHEMA_RELATION_DESTINATION = "Destination";	*/
	

	
	
	public static final String NOR_DATAMODEL_NODE = "DataModel";
	
	public static final String NOR_PATHENUMERATION_NODE = "PathEnumeration";	
	
	public static final String NOR_PATHENUMERATION_MAINENTITY_NODE = "PathEntity";
	public static final String NOR_PATHENUMERATION_SEPARATOR_NODE = "PathSeparator";	
	public static final String NOR_PATHENUMERATION_PATHFIELD_NODE = "PathField";
	

	public static final String NOR_MASTERDETAIL_NODE = "MasterDetail";
	public static final String NOR_MASTERDETAIL_MASTER_NODE = "MasterEntity";
	public static final String NOR_MASTERDETAIL_DETAIL_NODE = "DetailEntity";	
	public static final String NOR_MASTERDETAIL_FIELD_NODE = "Field";

	
	public static final String NOR_ADJACENCYLIST_NODE = "AdjacencyList";
	public static final String NOR_ADJACENCYLIST_MAINENTITY_NODE = "ListEntity";
	public static final String NOR_ADJACENCYLIST_ID_NODE = "ListID";	
	public static final String NOR_ADJACENCYLIST_FIELD_NODE = "ListField";

	
	public static final String NOR_SNOWFLAKE_NODE = "Snowflake";
	public static final String NOR_SNOWFLAKE_NODE_NUMBER_ENTITIES = "numberOfEntities";
	public static final String NOR_SNOWFLAKE_ENTITY_NODE = "SnowflakeEntity";
	public static final String NOR_SNOWFLAKE_ENTITY_NODE_NAME = "name";
	public static final String NOR_SNOWFLAKE_ENTITY_NODE_CHILD = "child";	
	public static final String NOR_SNOWFLAKE_ENTITY_NODE_CHILD_COL = "childCol";	
	public static final String NOR_SNOWFLAKE_ENTITY_NODE_KEY = "key";	
	
	
	public static final String NOR_RECORDBASED_NODE = "RecordBased";
	public static final String NOR_RECORDBASED_TERMENTITY_NODE = "Entity";

	public static final String NOR_GENERIC_DATA_MODEL_NODE = "GenericDataModel";
	
	
	public static final String NOR_RELATIONBASED_NODE = "RelationBased";

	public static final String NOR_RELATIONBASED_TERMENTITY_NODE = "TermEntity";
	public static final String NOR_RELATIONBASED_TERMENTITY_NAME_ATT = "name";
	public static final String NOR_RELATIONBASED_TERMENTITY_TERMID_NODE = "TermId";
	
	public static final String NOR_RELATIONBASED_LINKENTITY_NODE = "LinkEntity";
	public static final String NOR_RELATIONBASED_LINKENTITY_NAME_ATT = "name";
	public static final String NOR_RELATIONBASED_LINKENTITY_TERM1_NODE = "LinkTerm1";
	public static final String NOR_RELATIONBASED_LINKENTITY_TERM2_NODE = "LinkTerm2";
	public static final String NOR_RELATIONBASED_LINKENTITY_TYPE_NODE = "LinkType";
	
	public static final String NOR_RELATIONBASED_LINKTYPEENTITY_NODE = "LinkTypeEntity";
	public static final String NOR_RELATIONBASED_LINKTYPEENTITY_NAME_ATT = "name";
	public static final String NOR_RELATIONBASED_LINKTYPEENTITY_TYPE_NODE = "LinkType";
	public static final String NOR_RELATIONBASED_LINKTYPEENTITY_DESCRIPTION_NODE = "LinkDescription";
	public static final String NOR_RELATIONBASED_LINKTYPEENTITY_ABBR_NODE = "LinkAbbr";
	
	public static final String NOR_RELATIONBASED_ID_NODE = "ListID";	
	public static final String NOR_RELATIONBASED_FIELD_NODE = "ListField";
	
	public static final String NOR_IMPLEMENTATION_NODE = "Implementation";	
	
	public static final int NOR_DBIMPLEMENTATIONS_SUPPORTED = DBConnector.DBMS.length;
	
	public static final String NOR_DB_NODE = "Database";
	public static final String NOR_DBMS_NODE = "Dbms";	
	public static final String NOR_DBNAME_NODE = "Name";
	public static final String NOR_DBUSERNAME_NODE = "Username";	
	public static final String NOR_DBPASSWORD_NODE = "Password";
	public static final String NOR_DBHOST_NODE = "Host";
	public static final String NOR_DBPORT_NODE = "Port";	
	
	//Esto podria irse a xml constants
	
	public static final String NOR_XML_NODE = "Xml";
	public static final String NOR_XSD_ATT = "xsdFile";
	public static final String NOR_XML_ATT = "xmlFile";
	
	// Spreadsheet constants
	public static final String NOR_SPREADSHEET_NODE = "Spreadsheet";
	public static final String NOR_SPREADSHEET_TYPE = "type";
	public static final String NOR_SPREADSHEET_FILE = "file";
	
		
}
