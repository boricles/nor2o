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
package es.upm.fi.dia.oeg.nor2o.nor.connector;

public interface Constants {

	
	public static final String []IMPLEMENTATIONS = {"XML",
		 "Database",
		 "Flat file",
		 "Spreadsheet"};
	
	//NOR IMPLEMENTATIONS
	public static final int XML = 0;
	public static final int DATABASE = 1;
	public static final int FLAT_FILE = 2;
	public static final int SPREADSHEET = 3;

	
	public static final String DB_SEPARATOR = ".";
	
	public static final String XML_SEPARATOR = "/";	
	
	public static final String XML_DEFAULT_PATH = "//";
	
	public static final String XPATH_OE = "[";
	
	public static final String XPATH_CE = "]";	
	
	public static final String XPATH_EQUAL = "=";
	
	public static final String XPATH_STREND = "\"";
	public static final String XPATH_STRSTART = "\"";
	
	public static final String SPREADSHEET_MS = "ms";
	
	public static final String ENTITY_PREFIX = "_";
	
	
	public static String PATH_ENUMERATION_TOP_LEVEL_QUERY = "xquery version \"1.0\";\n" +
															"declare namespace functx = \"http://www.functx.com\"; \n"+ 
															"declare namespace fn = \"http://www.w3.org/2005/xpath-functions\"; \n"+
															"declare function functx:min-string \n" + 
															"( $strings as xs:anyAtomicType* )  as xs:string? { \n" +
															" min(for $string in $strings return string($string)) \n" +
															"} ; \n"+
															" let $minLen := fn:string-length(functx:min-string( //.NODE./.ID_NODE.)) \n" +
															" for $cell in //.NODE.  \n " +
															" where $cell[fn:string-length(.ID_NODE.)=$minLen] ";
															//" return <.NODE. id=\"{$cell/@id}\" label=\"{$cell/@label}\"/> \n ";
	public static String PATH_ENUMERATION_TOP_LEVEL_QUERY_RETURN = "return <";
	public static String PATH_ENUMERATION_TOP_LEVEL_QUERY_RETURN_START_VAR =  "\"{$cell/";
	public static String PATH_ENUMERATION_TOP_LEVEL_QUERY_RETURN_END_VAR = "}\"";
	public static String PATH_ENUMERATION_TOP_LEVEL_QUERY_RETURN_END ="/>";
	public static String NODE_PATH_ENUMERATION = ".NODE.";
	public static String NODE_PATH_ENUMERATION_ID = ".ID_NODE.";
	
	public static String NODE_KEY_VALUE = "@@KEY_VALUE@@";
	
	
	public static String PATH_ENUMERATION_SUB_LEVEL_QUERY = "xquery version \"1.0\";\n" +
															"declare namespace functx = \"http://www.functx.com\";\n"+ 
															"declare namespace fn = \"http://www.w3.org/2005/xpath-functions\";\n"+
															"declare function functx:min-string\n" + 
															"( $strings as xs:anyAtomicType* )  as xs:string? { \n"+
															"min(for $string in $strings return string($string))\n" +
	 														"} ;\n"+
	 														"let $minLen := fn:string-length(functx:min-string(@@KEY_VALUE@@))+1\n"+
	 														"for $cell in //.NODE. \n"+
	 														"where $cell[starts-with(.ID_NODE.,@@KEY_VALUE@@)] and $cell[.ID_NODE.!=@@KEY_VALUE@@] and $cell[fn:string-length(.ID_NODE.)=$minLen] ";
}
	
