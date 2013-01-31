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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.upm.fi.dia.oeg.nor2o.nor.CScheme;
import es.upm.fi.dia.oeg.nor2o.nor.GenericNOR;
import es.upm.fi.dia.oeg.nor2o.nor.Lexicon;
import es.upm.fi.dia.oeg.nor2o.nor.NOR;
import es.upm.fi.dia.oeg.nor2o.nor.Thesaurus;
import es.upm.fi.dia.oeg.nor2o.nor.connector.Connector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.DBConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.DBConstants;
import es.upm.fi.dia.oeg.nor2o.nor.connector.SpreadsheetConnector;
import es.upm.fi.dia.oeg.nor2o.nor.connector.XMLConnector;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.AdjacencyListDataModel;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.DataModel;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.GenericDataModel;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.MasterDetailDataModel;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.PathEnumerationDataModel;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.RecordBasedDataModel;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.RelationBasedDataModel;
import es.upm.fi.dia.oeg.nor2o.nor.datamodel.SnowflakeDataModel;
import es.upm.fi.dia.oeg.nor2o.nor.schema.Schema;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaAttribute;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaEntity;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaRelation;
import es.upm.fi.dia.oeg.nor2o.util.XMLReader;


/**
 * @author boricles
 *
 */
public class NORReader extends XMLReader implements NORDescriptionConstants {
	
	protected static NOR singleton = null;
	
	protected String folder;
	
	public void setFolder(String folder){
		this.folder = folder;
	}
	
	public NORReader() {
		super();
	}

	
	//Thesauri datamodels
	private void readRecordBasedDataModel(Node datamodel) throws Exception {
		NodeList nodes = datamodel.getChildNodes();
		int len = nodes.getLength(); 
		
		Node node;
		
		String entity = null;
		for (int i=0; i<len; i++) {
			node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equalsIgnoreCase(NOR_RECORDBASED_TERMENTITY_NODE)) {
					entity = node.getTextContent();
				}
			}
		}
		
		if (entity==null)
			throw new Exception("Incorrect information in the " + NOR_DESCRIPTION_FILE+ " file");
		
		DataModel norDataModel = new RecordBasedDataModel();
		((RecordBasedDataModel)norDataModel).setEntity(entity);
		
		singleton.setDataModel(norDataModel);
		
	}
	
	private void readRelationBasedDataModel(Node datamodel) throws Exception {
		NodeList nodes = datamodel.getChildNodes();
		int len = nodes.getLength(); 
		
		Node node;
		Node subNode;
		NodeList subNodes;
		
		String termEntity = null, termCode = null, linkEntity = null, linkTerm1 = null,linkTerm2 = null,linkType=null,linkTypeEntity=null,linkTypeId=null,linkDesc=null,linkAbbr=null;
		for (int i=0; i<len; i++) {
			node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equalsIgnoreCase(NOR_RELATIONBASED_TERMENTITY_NODE)) {
					termEntity = getAttribute(node, NOR_RELATIONBASED_TERMENTITY_NAME_ATT);
					subNodes = node.getChildNodes();
					for (int j=0; j<subNodes.getLength(); j++) {
						subNode = subNodes.item(j);
						if (subNode.getNodeType() == Node.ELEMENT_NODE) {
							if (subNode.getNodeName().equalsIgnoreCase(NOR_RELATIONBASED_TERMENTITY_TERMID_NODE)) {
								termCode = subNode.getTextContent();
							}
						}
					}
					
				
				}
				if (node.getNodeName().equalsIgnoreCase(NOR_RELATIONBASED_LINKENTITY_NODE)) {
					linkEntity = getAttribute(node, NOR_RELATIONBASED_LINKENTITY_NAME_ATT);
					subNodes = node.getChildNodes();
					for (int j=0; j<subNodes.getLength(); j++) {
						subNode = subNodes.item(j);
						if (subNode.getNodeType() == Node.ELEMENT_NODE) {
							if (subNode.getNodeName().equalsIgnoreCase(NOR_RELATIONBASED_LINKENTITY_TERM1_NODE))
								linkTerm1 = subNode.getTextContent();
							if (subNode.getNodeName().equalsIgnoreCase(NOR_RELATIONBASED_LINKENTITY_TERM2_NODE))
								linkTerm2 = subNode.getTextContent();
							if (subNode.getNodeName().equalsIgnoreCase(NOR_RELATIONBASED_LINKENTITY_TYPE_NODE))
								linkType = subNode.getTextContent();
						}
					}
				}
					
				if (node.getNodeName().equalsIgnoreCase(NOR_RELATIONBASED_LINKTYPEENTITY_NODE)) {
					linkTypeEntity = getAttribute(node, NOR_RELATIONBASED_LINKTYPEENTITY_NAME_ATT);
					subNodes = node.getChildNodes();
					for (int j=0; j<subNodes.getLength(); j++) {
						subNode = subNodes.item(j);
						if (subNode.getNodeType() == Node.ELEMENT_NODE) {
							if (subNode.getNodeName().equalsIgnoreCase(NOR_RELATIONBASED_LINKTYPEENTITY_TYPE_NODE))
								linkTypeId = subNode.getTextContent();
							if (subNode.getNodeName().equalsIgnoreCase(NOR_RELATIONBASED_LINKTYPEENTITY_DESCRIPTION_NODE))
								linkDesc = subNode.getTextContent();
							if (subNode.getNodeName().equalsIgnoreCase(NOR_RELATIONBASED_LINKTYPEENTITY_ABBR_NODE))
								linkAbbr = subNode.getTextContent();
						}
					}
					
				}
			}
		}
		
		
		if (termEntity == null || termCode == null || linkEntity == null || linkTerm1 == null ||linkTerm2 == null ||linkType==null ||linkTypeEntity==null ||linkTypeId==null ||linkDesc==null ||linkAbbr==null )
			throw new Exception("Incorrect information in the " + NOR_DESCRIPTION_FILE+ " file");


		DataModel norDataModel = new RelationBasedDataModel();
		((RelationBasedDataModel)norDataModel).setTermEntityInformation(termEntity, termCode);
		((RelationBasedDataModel)norDataModel).setTermLinkInformation(linkEntity, linkTerm1, linkTerm2, linkType);
		((RelationBasedDataModel)norDataModel).setLinkTypeInformation(linkTypeEntity, linkTypeId, linkDesc, linkAbbr);
		
		singleton.setDataModel(norDataModel);
		
		
	}
	
	
	
	
	//Classification scheme data models
	private void readPathEnumerationDataModel (Node datamodel) throws Exception {
		NodeList nodes = datamodel.getChildNodes();
		int len = nodes.getLength(); 
		
		Node node;
		String mainEntity = null, pathSeparator=null, pathField=null;
		for (int i=0; i<len; i++) {
			node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equalsIgnoreCase(NOR_PATHENUMERATION_MAINENTITY_NODE))
					mainEntity = node.getTextContent(); 
				if (node.getNodeName().equalsIgnoreCase(NOR_PATHENUMERATION_SEPARATOR_NODE))
					pathSeparator = node.getTextContent();
				if (node.getNodeName().equalsIgnoreCase(NOR_PATHENUMERATION_PATHFIELD_NODE))
					pathField = node.getTextContent();
			}
		}
		
		if (mainEntity==null || pathSeparator ==null || pathField==null )
			throw new Exception("Incorrect information in the " + NOR_DESCRIPTION_FILE+ " file");
		
		DataModel norDataModel = new PathEnumerationDataModel();
		((PathEnumerationDataModel)norDataModel).setMainEntity(mainEntity);
		((PathEnumerationDataModel)norDataModel).setPathField(pathField);
		((PathEnumerationDataModel)norDataModel).setPathSeparator(pathSeparator);
		
		singleton.setDataModel(norDataModel);
	
	}

	private void readAdjacencyListDataModel (Node datamodel) throws Exception {
		NodeList nodes = datamodel.getChildNodes();
		int len = nodes.getLength(); 
		
		Node node;
		String mainEntity = null, idList=null, fieldList=null;
		for (int i=0; i<len; i++) {
			node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equalsIgnoreCase(NOR_ADJACENCYLIST_MAINENTITY_NODE))
					mainEntity = node.getTextContent(); 
				if (node.getNodeName().equalsIgnoreCase(NOR_ADJACENCYLIST_ID_NODE))
					idList = node.getTextContent();
				if (node.getNodeName().equalsIgnoreCase(NOR_ADJACENCYLIST_FIELD_NODE))
					fieldList = node.getTextContent();
			}
		}
		
		if (mainEntity==null || idList ==null || fieldList==null )
			throw new Exception("Incorrect information in the " + NOR_DESCRIPTION_FILE+ " file");
		
		DataModel norDataModel = new AdjacencyListDataModel();
		((AdjacencyListDataModel)norDataModel).setMainEntity(mainEntity);
		((AdjacencyListDataModel)norDataModel).setListField(fieldList);
		((AdjacencyListDataModel)norDataModel).setListId(idList);
		
		singleton.setDataModel(norDataModel);
	
	}
	
	
	private void readSnowflakeDataModel(Node datamodel) throws Exception {
		NodeList nodes = datamodel.getChildNodes();
		int len = nodes.getLength(); 
		String numEntities,name,child,childCol,key;
		Node node;
		DataModel norDataModel = new SnowflakeDataModel();
		((SnowflakeDataModel)norDataModel).init();
		for (int i=0; i<len; i++) {
			node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equalsIgnoreCase(NOR_SNOWFLAKE_NODE)) {
					numEntities = getAttribute(node, NOR_SNOWFLAKE_NODE_NUMBER_ENTITIES);
					((SnowflakeDataModel)norDataModel).setNumberOfEntities(Integer.parseInt(numEntities));
				}
				if (node.getNodeName().equalsIgnoreCase(NOR_SNOWFLAKE_ENTITY_NODE)) {
					name = getAttribute(node, NOR_SNOWFLAKE_ENTITY_NODE_NAME);
					child = getAttribute(node,NOR_SNOWFLAKE_ENTITY_NODE_CHILD);
					childCol = getAttribute(node,NOR_SNOWFLAKE_ENTITY_NODE_CHILD_COL);
					key = getAttribute(node,NOR_SNOWFLAKE_ENTITY_NODE_KEY);
					((SnowflakeDataModel)norDataModel).setChildColumn(name, childCol);
					((SnowflakeDataModel)norDataModel).setEntity(name, child);
					((SnowflakeDataModel)norDataModel).setKeyColumn(name, key);
				}
			}
		}
		singleton.setDataModel(norDataModel);
	}


	private void readMasterDetailModel (Node datamodel) throws Exception {
		NodeList nodes = datamodel.getChildNodes();
		int len = nodes.getLength(); 
		
		Node node;
		String masterEntity = null, detailEntity=null, field=null;
		for (int i=0; i<len; i++) {
			node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equalsIgnoreCase(NOR_MASTERDETAIL_MASTER_NODE))
					masterEntity = node.getTextContent(); 
				if (node.getNodeName().equalsIgnoreCase(NOR_MASTERDETAIL_DETAIL_NODE))
					detailEntity = node.getTextContent();
				if (node.getNodeName().equalsIgnoreCase(NOR_MASTERDETAIL_FIELD_NODE))
					field = node.getTextContent();
			}
		}
		
		if (masterEntity==null || detailEntity ==null || field==null )
			throw new Exception("Incorrect information in the " + NOR_DESCRIPTION_FILE+ " file");
		
		DataModel norDataModel = new MasterDetailDataModel();
		((MasterDetailDataModel)norDataModel).setMasterEntity(masterEntity);
		((MasterDetailDataModel)norDataModel).setDetailEntity(detailEntity);
		((MasterDetailDataModel)norDataModel).setField(field);
		
		singleton.setDataModel(norDataModel);
	
	}

	
	
	private int getDataType (String dataType) {
		
		for (int i=0; i<es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaConstants.DataTypes.length; i++) {
			if (es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaConstants.DataTypes[i].equalsIgnoreCase(dataType)) 
				return i;
		}
		return 0;
		
	}

	
	private SchemaEntity readSEntity(Node sEntity) throws Exception {
		SchemaEntity ent = new SchemaEntity();
		String name = getAttribute(sEntity,NOR_SCHEMA_ENTITY_NAME);
		ent.setName(name);
		
		String isList = getAttribute(sEntity,NOR_SCHEMA_IS_LIST);
		if (isList != null)
			ent.setIsList(isList.equalsIgnoreCase(NOR_SCHEMA_IS_LIST_TRUE));
		else
			ent.setIsList(false);
		
		String sEntityType = getAttribute(sEntity,NOR_SCHEMA_ENTITY_TYPE);
		if (sEntityType==null)
			sEntityType = "";
		ent.setType(sEntityType);
		
		NodeList attributes = sEntity.getChildNodes();
		
		if (attributes != null) {
			int len = attributes.getLength();
			Node attNode;
			String attName=null, valueFrom=null, type = null;
			String relName=null, using = null, cardinality = null, destination = null, valueId = null;
			String usingSpreadSheetColumn = null, usingSpreadSheetRow = null;
			for (int i=0;i<len;i++) {
				SchemaAttribute scAtt = null;
				SchemaRelation scRel = null;
				attName = valueFrom = type = null;
				relName = using = cardinality = destination = valueId = null;
				attNode = attributes.item(i);
				if (attNode.getNodeType() == Node.ELEMENT_NODE) {
					if (!attNode.getNodeName().equalsIgnoreCase(NOR_SCHEMA_ATTRIBUTE) && !attNode.getNodeName().equalsIgnoreCase(NOR_SCHEMA_RELATION) && !attNode.getNodeName().equalsIgnoreCase(NOR_SCHEMA_FILTER) )
						throw new Exception("Incorrect information in the " + NOR_DESCRIPTION_FILE+ " file");
					
					if (attNode.getNodeName().equalsIgnoreCase(NOR_SCHEMA_ATTRIBUTE)) {	//ATTRIBUTES
						scAtt = new SchemaAttribute();					
						attName = getAttribute(attNode,NOR_SCHEMA_ATTRIBUTE_NAME);
						valueFrom = getAttribute(attNode,NOR_SCHEMA_ATTRIBUTE_VALUE);
						type = getAttribute(attNode,NOR_SCHEMA_ATTRIBUTE_TYPE);
						scAtt.setName(attName);
						scAtt.setValues(new String[]{valueFrom});
						scAtt.setType(getDataType(type));
						if (scAtt != null)
							ent.addAttribute(scAtt);
						
					}
					if (attNode.getNodeName().equalsIgnoreCase(NOR_SCHEMA_RELATION)) { //RELATIONS
						SchemaRelation sRel = new SchemaRelation();
						relName = getAttribute(attNode,NOR_SCHEMA_RELATION_NAME);
						using = getAttribute(attNode,NOR_SCHEMA_RELATION_USING);
						//for genericSpreadSheet
						if (using==null || using.equals("")) {
							usingSpreadSheetColumn = getAttribute(attNode,NOR_SCHEMA_RELATION_USING_SPREADSHEET_COLUMN);
							if (usingSpreadSheetColumn==null || usingSpreadSheetColumn.equals("")) {
								usingSpreadSheetRow = getAttribute(attNode,NOR_SCHEMA_RELATION_USING_SPREADSHEET_ROW);
							}
						}
						
						cardinality = getAttribute(attNode,NOR_SCHEMA_RELATION_CARDINALITY);
						destination = getAttribute(attNode,NOR_SCHEMA_RELATION_DESTINATION);
						//inverse = getAttribute(attNode,NOR_SCHEMA_RELATION_INVERSE);
						valueId = getAttribute(attNode,NOR_SCHEMA_RELATION_VALUEID);
						sRel.setName(relName);
						if (using!=null) 
							sRel.setUses(using);
						if (usingSpreadSheetColumn!=null) 
							sRel.setUsesSpreadSheetColumn(usingSpreadSheetColumn);
						if (usingSpreadSheetRow!=null) 
							sRel.setUsesSpreadSheetRow(usingSpreadSheetRow);
						
						sRel.setCardinality(cardinality);
						sRel.setRange(destination);
						sRel.setDomain(name);
						sRel.setValueId(valueId);
						if (sRel != null)
							ent.addRelation(sRel);
					}
					
					if (attNode.getNodeName().equalsIgnoreCase(NOR_SCHEMA_FILTER)) { //FILTER
						String exp = getAttribute(attNode,NOR_SCHEMA_EXPRESSION);
						ent.setFilterExpression(exp);
					}
				}
			}
		}
		
		return ent;
	}
	
	
	private void readSchema(NodeList schemaNodes) throws Exception {
		Schema norSchema = new Schema();
		int len = schemaNodes.getLength();
		Node cNode;
		for (int i=0; i<len; i++) {
			cNode = schemaNodes.item(i);
			if (cNode.getNodeType() == Node.ELEMENT_NODE) {
				if (cNode.getNodeName().equalsIgnoreCase(NOR_SCHEMA_ENTITIES)) {
					NodeList sEntities = cNode.getChildNodes();
					int sLen = sEntities.getLength();
					Node sEntity;
					SchemaEntity scEntity;
					for (int j=0;j<sLen;j++) {
						sEntity = sEntities.item(j);
						if (sEntity.getNodeType() == Node.ELEMENT_NODE) {
							scEntity = readSEntity(sEntity);
							norSchema.addSchemaEntity(scEntity);
							scEntity.setSchema(norSchema);
						}
					}
				}
			}
		}
		singleton.setSchema(norSchema);
	}

	
	
	private void readClassificationSchemeDescription(Element root) throws Exception {
		String name = root.getAttributeNode(NOR_NAME_ATT).getValue();
		
		singleton = new CScheme(name);
		
		NodeList schemaNodes = root.getElementsByTagName(NOR_SCHEMA_NODE);
		if (schemaNodes == null)
			throw new Exception("Incorrect information in the " + NOR_DESCRIPTION_FILE+ " file");		
		Node sNode = schemaNodes.item(0);
		schemaNodes = sNode.getChildNodes();
		
		readSchema(schemaNodes);
		
		NodeList dataModelNodes = root.getElementsByTagName(NOR_DATAMODEL_NODE);
		if (dataModelNodes == null)
			throw new Exception("Incorrect information in the " + NOR_DESCRIPTION_FILE+ " file");
		
		Node node = dataModelNodes.item(0);
		dataModelNodes = node.getChildNodes();
		
		for (int i=0; i<dataModelNodes.getLength();i++) {
			Node cNode = dataModelNodes.item(i);
			if (cNode.getNodeType() == Node.ELEMENT_NODE) {
				String dataModel = cNode.getNodeName();
				if (dataModel.equalsIgnoreCase(NOR_PATHENUMERATION_NODE))
					readPathEnumerationDataModel(cNode);
				if (dataModel.equalsIgnoreCase(NOR_ADJACENCYLIST_NODE))
					readAdjacencyListDataModel(cNode);
				if (dataModel.equalsIgnoreCase(NOR_MASTERDETAIL_NODE))
					readMasterDetailModel(cNode);
				if (dataModel.equalsIgnoreCase(NOR_SNOWFLAKE_NODE))
					readSnowflakeDataModel(cNode);
				//TO DO: the same for the other datamodels
			}
		}
	
	}


	private void readDBImplementation (Node implementation) throws Exception {
		NodeList nodes = implementation.getChildNodes();
		int len = nodes.getLength(); 
		
		Node node;
		
		String dbms = null, name=null, username=null, password =null, host=null, port=null;
		for (int i=0; i<len; i++) {
			node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {			
				if (node.getNodeName().equalsIgnoreCase(NOR_DBMS_NODE))
					dbms = node.getTextContent(); 
				if (node.getNodeName().equalsIgnoreCase(NOR_DBNAME_NODE))
					name = node.getTextContent();
				if (node.getNodeName().equalsIgnoreCase(NOR_DBUSERNAME_NODE))
					username = node.getTextContent();
				if (node.getNodeName().equalsIgnoreCase(NOR_DBPASSWORD_NODE))
					password = node.getTextContent(); 
				if (node.getNodeName().equalsIgnoreCase(NOR_DBHOST_NODE))
					host = node.getTextContent();
				if (node.getNodeName().equalsIgnoreCase(NOR_DBPORT_NODE))
					port = node.getTextContent();
			}
			
		}
		
		if (dbms==null || name ==null || username==null || password==null || host==null || port==null )
			throw new Exception("Incorrect information in the " + NOR_DESCRIPTION_FILE+ " file");
		
		int dbmsType = 0;
		for (String cDBMS : DBConnector.DBMS ) {
			if (cDBMS.equalsIgnoreCase(dbms)) {
				break;
			}
			dbmsType++;
		}
		
		if (dbmsType >= NOR_DBIMPLEMENTATIONS_SUPPORTED)
			throw new Exception("Incorrect information in the " + NOR_DESCRIPTION_FILE+ " file");		
		
		
		Connector norConnector = new DBConnector(dbmsType,name,username,password,host,port);		
		singleton.setConnector(norConnector);

		
	}
	
	
	private void readXMLImplementation (Node implementation) throws Exception {
		
		Node node = implementation;
		
		String xsdFile = getAttribute(node, NOR_XSD_ATT);
		String xmlFile = getAttribute(node, NOR_XML_ATT);
		
		String xmlFilePath = folder + xmlFile;
		String xsdFilePath = folder + xsdFile;
		
		Connector norConnector = new XMLConnector(xsdFilePath,xmlFilePath);
		
		singleton.setConnector(norConnector);
	}
	
	private void readSpreadsheetImplementation(Node implementation) throws Exception {
		
		Node node = implementation;
		
		String spreadsheetFile = getAttribute(node, NOR_SPREADSHEET_TYPE);
		String spreadsheetType = getAttribute(node, NOR_SPREADSHEET_FILE);
		
		Connector norConnector = new SpreadsheetConnector(folder+spreadsheetType,spreadsheetFile);
		singleton.setConnector(norConnector);
	}
	
	
	private void readNORImplementation (NodeList implementationNodes) throws Exception {
		
		Node node = implementationNodes.item(0);
		implementationNodes = node.getChildNodes();

		Node implementationNode;
		for (int i=0; i<implementationNodes.getLength();i++) {
			implementationNode = implementationNodes.item(i);
			if (implementationNode.getNodeType() ==  Node.ELEMENT_NODE) {
				String implementation = implementationNode.getNodeName();
				if (implementation.equalsIgnoreCase(NOR_DB_NODE))
					readDBImplementation(implementationNode);
				if (implementation.equalsIgnoreCase(NOR_XML_NODE))
					readXMLImplementation(implementationNode);
				if (implementation.equalsIgnoreCase(NOR_SPREADSHEET_NODE))
					readSpreadsheetImplementation(implementationNode);
		
					//TO DO: implementation de flat file
				
			}
		}
	}
	
	private void readThesaurusDescription(Element root) throws Exception {
		String name = root.getAttributeNode(NOR_NAME_ATT).getValue();
		
		singleton = new Thesaurus(name);
		
		NodeList schemaNodes = root.getElementsByTagName(NOR_SCHEMA_NODE);
		if (schemaNodes == null)
			throw new Exception("Incorrect information in the " + NOR_DESCRIPTION_FILE+ " file");		
		Node sNode = schemaNodes.item(0);
		schemaNodes = sNode.getChildNodes();
		
		readSchema(schemaNodes);
		
		NodeList dataModelNodes = root.getElementsByTagName(NOR_DATAMODEL_NODE);
		if (dataModelNodes == null)
			throw new Exception("Incorrect information in the " + NOR_DESCRIPTION_FILE+ " file");
		
		Node node = dataModelNodes.item(0);
		dataModelNodes = node.getChildNodes();
		
		for (int i=0; i<dataModelNodes.getLength();i++) {
			Node cNode = dataModelNodes.item(i);
			if (cNode.getNodeType() == Node.ELEMENT_NODE) {
				String dataModel = cNode.getNodeName();
				if (dataModel.equalsIgnoreCase(NOR_RELATIONBASED_NODE))
					readRelationBasedDataModel(cNode);
				if (dataModel.equalsIgnoreCase(NOR_RECORDBASED_NODE))
					readRecordBasedDataModel(cNode);
			}
		}
	}
	
	private void readLexiconDescription(Element root) throws Exception {
		String name = root.getAttributeNode(NOR_NAME_ATT).getValue();
		
		singleton = new Lexicon(name);
		
		NodeList schemaNodes = root.getElementsByTagName(NOR_SCHEMA_NODE);
		if (schemaNodes == null)
			throw new Exception("Incorrect information in the " + NOR_DESCRIPTION_FILE+ " file");		
		Node sNode = schemaNodes.item(0);
		schemaNodes = sNode.getChildNodes();
		
		readSchema(schemaNodes);
		
		NodeList dataModelNodes = root.getElementsByTagName(NOR_DATAMODEL_NODE);
		if (dataModelNodes == null)
			throw new Exception("Incorrect information in the " + NOR_DESCRIPTION_FILE+ " file");
		
		Node node = dataModelNodes.item(0);
		dataModelNodes = node.getChildNodes();
		
		for (int i=0; i<dataModelNodes.getLength();i++) {
			Node cNode = dataModelNodes.item(i);
			if (cNode.getNodeType() == Node.ELEMENT_NODE) {
				String dataModel = cNode.getNodeName();
				if (dataModel.equalsIgnoreCase(NOR_RELATIONBASED_NODE))
					readRelationBasedDataModel(cNode);
				if (dataModel.equalsIgnoreCase(NOR_RECORDBASED_NODE))
					readRecordBasedDataModel(cNode);
			}
		}
	}
	
	private void readGenericSpreadSheetDescription(Element root) throws Exception {
		String name = root.getAttributeNode(NOR_NAME_ATT).getValue();
		
		singleton = new GenericNOR(name);
		
		NodeList schemaNodes = root.getElementsByTagName(NOR_SCHEMA_NODE);
		if (schemaNodes == null)
			throw new Exception("Incorrect information in the " + NOR_DESCRIPTION_FILE+ " file");		
		Node sNode = schemaNodes.item(0);
		schemaNodes = sNode.getChildNodes();
		
		readSchema(schemaNodes);
		
		NodeList dataModelNodes = root.getElementsByTagName(NOR_DATAMODEL_NODE);
		if (dataModelNodes == null)
			throw new Exception("Incorrect information in the " + NOR_DESCRIPTION_FILE+ " file");
		
		Node node = dataModelNodes.item(0);
		dataModelNodes = node.getChildNodes();
		
		for (int i=0; i<dataModelNodes.getLength();i++) {
			Node cNode = dataModelNodes.item(i);
			if (cNode.getNodeType() == Node.ELEMENT_NODE) {
				String dataModel = cNode.getNodeName();
				if (!dataModel.equalsIgnoreCase(NOR_GENERIC_DATA_MODEL_NODE)) 
					throw new Exception("Incorrect information in the " + NOR_DESCRIPTION_FILE+ " file");
				DataModel norDataModel = new GenericDataModel();
				singleton.setDataModel(norDataModel);

					
			}
		}
		
	}
	
	private void readNORDescription(Element root) throws Exception {
		//root.getElementsByTagName(NOR_TAG);
		
		Attr attType = root.getAttributeNode(NOR_TYPE_ATT);
		
		if (attType!=null) {
			String type = attType.getValue();
			if (type.equalsIgnoreCase("Classification Scheme"))
				readClassificationSchemeDescription(root);
			if (type.equalsIgnoreCase("Thesaurus"))
				readThesaurusDescription(root);
			if (type.equalsIgnoreCase("Lexicon"))
				readLexiconDescription(root);
			
			if (type.equalsIgnoreCase("GenericSpreadSheet"))
				readGenericSpreadSheetDescription(root);
 			
			readNORImplementation(root.getElementsByTagName(NOR_IMPLEMENTATION_NODE));			
			
		}
		else
			throw new java.lang.Exception("Incomplete information on the " + NOR_DESCRIPTION_FILE+ " file");
	}
	
	public void read() {
		try {
			String filePath = folder + NOR_DESCRIPTION_FILE;
			File norFileDesc = new File(filePath); 
			FileInputStream norFIS = new FileInputStream(norFileDesc);
			if (norFIS == null || norFIS.available() <= 0)
				throw new FileNotFoundException("Empty " + NOR_DESCRIPTION_FILE + "File");
				
			Document doc = null;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			doc = docBuilder.parse(norFIS);
			norFIS.close();
			
			Element root = doc.getDocumentElement();
			readNORDescription(root);
		
			singleton.load();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception exe) {
			// TODO Auto-generated catch block
			logger.error("Exception " + NOR_DESCRIPTION_FILE + " file wrong");
			logger.error("read: " + exe.getMessage());
			exe.printStackTrace();
		}
		
	
		 
	}
	
	
	public NOR getNOR() {
		return singleton;
	}
	
	public void dispose() {
		singleton.dispose();
	}
	
	
	
}
