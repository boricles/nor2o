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

package es.upm.fi.dia.oeg.nor2o.nor.connector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import es.upm.fi.dia.oeg.nor2o.nor.Constants;
import es.upm.fi.dia.oeg.nor2o.nor.content.CAttribute;
import es.upm.fi.dia.oeg.nor2o.nor.content.CEntity;
import es.upm.fi.dia.oeg.nor2o.nor.schema.Schema;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaAttribute;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaEntity;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaRelation;

/**
 * @author boricles
 *
 */
public class SpreadsheetConnector extends Connector implements SpreadsheetConstants {

	
	protected String fileName;
	protected String type;
	
	protected POIFSFileSystem fs;
	protected HSSFWorkbook wb;
	protected HSSFSheet sheet = null;
	protected HashMap cols;
	/**
	 * 
	 */
	public SpreadsheetConnector() {
		// TODO Auto-generated constructor stub
		super();
	}

	public SpreadsheetConnector(String fileName, String type) {
		this.fileName = fileName;
		this.type = type;
		if (type.equalsIgnoreCase(SPREADSHEET_MS))
        try {
        	//open excel file
			fs = new POIFSFileSystem(new FileInputStream(fileName));
            wb = new HSSFWorkbook(fs);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	protected void loadColumnHeaders(String sheetName) {
		if (sheet== null) {
			//Get the sheet
			sheet = wb.getSheet(sheetName);
			//Get the row
			HSSFRow row = sheet.getRow(0);
			
			int numCells = row.getPhysicalNumberOfCells();
			cols = new HashMap();
			String colName;
			for (int i=0; i < numCells; i++) {
				colName = row.getCell(i).getStringCellValue();
				cols.put(i, colName);
			}
		}
	}
	
	
	protected int getColumnIndex(String columnName) {
       int columnIndex=-1;
		for (Object key : cols.keySet()) {
			if (cols.get(key).equals(columnName))
				columnIndex = Integer.parseInt(key.toString());
		}
		return columnIndex;
	}
	
	protected int getShortestLengthValue(String columnName) {
        int rows = sheet.getPhysicalNumberOfRows();
        HSSFRow row;
        HSSFCell cell;
        int columnIndex = getColumnIndex(columnName);
        String cValue;
        int minLen = 10000;
        int currentLen = -1;
        if (columnIndex != -1) {
	        //1 for skipping the columnheaders
	        for (int i=1; i<rows; i++) {
	        	row = sheet.getRow(i);
	        	if (row != null) {
	        		cValue = getValueAsString(row,columnName);//cell.getStringCellValue();
               		currentLen = cValue.length();
	        		if (currentLen<minLen)
	        			minLen = currentLen;
	        		}
	        	}
	        }
        return minLen;
	}
	
	protected String getValueAsString(HSSFRow row, String columnName) {
		String value = "";
		int columnIndex = getColumnIndex(columnName);
		HSSFCell cell = row.getCell(columnIndex);
		if (cell!=null) {
			if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING )
				value = cell.getStringCellValue();
			if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN )
				value = cell.getBooleanCellValue() + "";
			if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC )
				value = cell.getNumericCellValue() + "";
		}
		return value;
	}

	protected void setCEntityValues(HSSFRow row, CEntity entity, SchemaEntity sEntity) {
		Set<SchemaAttribute> atts = sEntity.getAttributes();
		String cName = "";
		String colName;
		for (SchemaAttribute att : atts) {
			CAttribute cattribute = new CAttribute(att.getName());
			cattribute.setSchemaAttribute(att);
			colName = att.getValues()[0];
			colName = colName.substring(colName.indexOf(SEPARATOR)+1);
			cattribute.addValue(getValueAsString(row,colName));
			entity.addAttribute(cattribute);
			cName += cattribute.getValues().iterator().next() + ENTITY_PREFIX;
		}
		if (cName.endsWith(ENTITY_PREFIX))
			cName = cName.substring(0,cName.length()-1);
		entity.setName(cName);
		entity.setSchema(sEntity);

    }	
	
	  //C. Scheme PathEnumeration data model method
	public Set<CEntity> getTopLevelEntitiesPathEnumerationDataModel(SchemaEntity sEntity, String mainEntity, String pathField) {
		Set<CEntity> entities = new HashSet<CEntity>();
		loadColumnHeaders(mainEntity);
        int rows = sheet.getPhysicalNumberOfRows();
        int minLen = getShortestLengthValue(pathField);
        
        HSSFRow row;
        HSSFCell cell;
        int columnIndex = getColumnIndex(pathField);
        String cValue;
        if (columnIndex != -1) {
	        //1 for skipping the columnheaders
	        for (int i=1; i<rows; i++) {
	        	row = sheet.getRow(i);
	        	if (row != null) {
	        		cell = row.getCell(columnIndex);
	        		if (cell != null) {
		        		cValue = cell.getStringCellValue();
		        		if (cValue.length()==minLen) {
		        			CEntity ent = new CEntity();
		        			setCEntityValues(row,ent,sEntity);
		        			entities.add(ent);
		        		}
	        		}
	        	}
	        }
        }
		return entities;
	}
	
    //C. Scheme PathEnumeration data model method, this is also applicable to the adjacency list
	protected void setCEntityRelations(SchemaEntity sEntity, CEntity parent, CEntity child) {
		Set<SchemaRelation> sRels = sEntity.getRelations();
		/*for (SchemaRelation rel : sRels) {
			parent.setRelation(rel, child);
		}*/
		for (SchemaRelation rel : sRels) {
			if (rel.getName().equals(Constants.GENERIC_SUPERTYPE))
				//{ parent.setRelation(rel, child);  }
				parent.setRelation(rel, child);
			if (rel.getName().equals(Constants.GENERIC_SUBTYPE))
				//{ child.setRelation(rel, parent);  }
				child.setRelation(rel, parent);
		}
	}
	
	public Set<CEntity> getSubEntitiesPathEnumerationDataModel(SchemaEntity sEntity, CEntity entity, String mainEntity, String pathField, String pathSeparator) {
		Set<CEntity> entities = new HashSet<CEntity>();
		
		loadColumnHeaders(mainEntity);
        int rows = sheet.getPhysicalNumberOfRows();
        String cPathValue = "";
        for (SchemaAttribute att : sEntity.getAttributes()) {
    		if (att.getValues()[0].equals(mainEntity + SEPARATOR + pathField))
    			cPathValue = att.getName();
    	}
        String pathValue = entity.getAttributeValuesAsString(cPathValue)[0];
        HSSFRow row;
        HSSFCell cell;
        int columnIndex = getColumnIndex(pathField);
        String cValue;
        if (columnIndex != -1) {
	        //1 for skipping the columnheaders
	        for (int i=1; i<rows; i++) {
	        	row = sheet.getRow(i);
	        	if (row != null) {
	        		cell = row.getCell(columnIndex);
	        		if (cell != null) {
		        		cValue = cell.getStringCellValue();
		        		cValue += pathSeparator;
		        		if (cValue.startsWith(pathValue) && (cValue.length()==(pathValue.length()+1)) ) { //TO DO: improve this, so far esto es un trick que no funciona en todos los casos		        		
		        		//if (cValue.contains(pathValue)) {
		        			CEntity ent = new CEntity();
		        			setCEntityValues(row,ent,sEntity);
							setCEntityRelations(sEntity,entity,ent);		        			
		        			entities.add(ent);
							Set <CEntity> subSetOfEntities = getSubEntitiesPathEnumerationDataModel(sEntity,ent,mainEntity,pathField,pathSeparator);
							if (subSetOfEntities!=null)
								entities.addAll(subSetOfEntities);
		        		}
	        		}
	        	}
	        }
        }
		return entities;
	}


	
	//Adjacency List
	public Set<CEntity> getTopLevelEntitiesAdjacencyListDataModel(SchemaEntity sEntity, String listEntity, String listField, String listID) {
		Set<CEntity> entities = new HashSet<CEntity>();
		loadColumnHeaders(listEntity);
        int rows = sheet.getPhysicalNumberOfRows();

        if (listField.contains(SEPARATOR))
        	listField = listField.substring(listField.indexOf(SEPARATOR)+1);
        
        HSSFRow row;
        HSSFCell cell;
        int columnIndex = getColumnIndex(listField);
        String cValue;
        if (columnIndex != -1) {
	        //1 for skipping the columnheaders
	        for (int i=1; i<rows; i++) {
	        	row = sheet.getRow(i);
	        	if (row != null) {
		        		//cValue = cell.getStringCellValue();
	        			cValue = getValueAsString(row, listField);
		        		if (cValue==null || cValue.equals("")) {	        			
		        			CEntity ent = new CEntity();
		        			setCEntityValues(row,ent,sEntity);
		        			entities.add(ent);
		        		}
	        	}
	        }
        }

		return entities;
	}
	
	  //C. Scheme AdjacencyListDataModel method
	public Set<CEntity> getSubEntitiesAdjacencyListDataModel(SchemaEntity sEntity, CEntity entity, String listEntity, String listField, String listId) {
		
        if (listField.contains(SEPARATOR))
        	listField = listField.substring(listField.indexOf(SEPARATOR)+1);
		
		Set<CEntity> entities = new HashSet<CEntity>();
		loadColumnHeaders(listEntity);
        int rows = sheet.getPhysicalNumberOfRows();
        String cIdValue = "";
        for (SchemaAttribute att : sEntity.getAttributes()) {
    		if (att.getValues()[0].equals(listId))
    			cIdValue = att.getName();
    	}
        String idValue = entity.getAttributeValuesAsString(cIdValue)[0];
        
        HSSFRow row;
        HSSFCell cell;
        int columnIndex = getColumnIndex(listField);
        String cValue;
        if (columnIndex != -1) {
	        //1 for skipping the columnheaders
	        for (int i=1; i<rows; i++) {
	        	row = sheet.getRow(i);
	        	if (row != null) {
	        		cell = row.getCell(columnIndex);
	        		if (cell != null) {
		        		cValue = cell.getStringCellValue();
		        		if (cValue.equals(idValue)) {
		        			CEntity ent = new CEntity();
		        			setCEntityValues(row,ent,sEntity);
							setCEntityRelations(sEntity,entity,ent);		        			
		        			entities.add(ent);
							Set <CEntity> subSetOfEntities = getSubEntitiesAdjacencyListDataModel(sEntity,ent,listEntity,listField,listId);
							if (subSetOfEntities!=null)
								entities.addAll(subSetOfEntities);
		        		}
	        		}
	        	}
	        }
        }
		return entities;
	}

	protected int getColumn(String column) {
		int col =0;
		if (column.length() == 1 ) {
			char c = column.charAt(0);
			col = (int)c;
			col -= ASCII_CODE_A;
		}
		if (column.length() > 1 ) {
			int len = column.length();
			int result = 0;
			for (int i=0;i<len;i++) {
				char c = column.charAt(i);
				col = (int)c;
				col -= ASCII_CODE_A;
				if (i==(len-1))
					result = result + col;
				else
					result = result + (col+1) * ALPHABET_SIZE ;
					
			}
			col = result;
		}
		return col;
	}
	
	protected int getRow(String row) {
		int iRow = Integer.parseInt(row);
		return iRow;
	}
	
	protected boolean isNum(char charac) {
		int asciiCode = (int) charac;
		if (asciiCode>=ASCII_CODE_0 && asciiCode<=ASCII_CODE_9)
			return true;
		return false;
	}

	protected String getColumnRange(String range) {
		String col = "";
		for (int i=0; i<range.length();i++) {
			if (!isNum(range.charAt(i)))
					col += range.charAt(i);
		}
		return col;
	}

	protected String getRowRange(String range) {
		String row = "";
		for (int i=0; i<range.length();i++) {
			if (isNum(range.charAt(i)))
					row += range.charAt(i);
		}
		return row;
	}

	protected String getValueAsString(int rowIndex, int columnIndex) {
		HSSFRow row = sheet.getRow(rowIndex);
	
			
		String value = "";
		HSSFCell cell = row.getCell(columnIndex);
		if (cell!=null) {
			if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING )
				value = cell.getStringCellValue();
			if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN )
				value = cell.getBooleanCellValue() + "";
			if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC )
				value = cell.getNumericCellValue() + "";
		}
		value = value.trim();

		return value;
	}


	protected CEntity createEntity(int col, int row, SchemaEntity sEntity) {
		Set<SchemaAttribute> atts = sEntity.getAttributes();
		String cName = "";
		CEntity entity = new CEntity();
		for (SchemaAttribute att : atts) {
			CAttribute cattribute = new CAttribute(att.getName());
			cattribute.setSchemaAttribute(att);
			cattribute.addValue(getValueAsString(row,col));
			entity.addAttribute(cattribute);
			cName += cattribute.getValues().iterator().next() + ENTITY_PREFIX; 
		}
		if (cName.endsWith(ENTITY_PREFIX))
			cName = cName.substring(0,cName.length()-1);
		entity.setName(cName);
		entity.setSchema(sEntity);
		
		return entity; 
		
	}
	
	protected void obtainRelations(int col, int row, CEntity entity, SchemaEntity sEntity,Set<CEntity> entities) {
		String usingInfo = null;
		int rc;
		SchemaEntity rangeEntity = null;
		CEntity rEntity = null;
		for (SchemaRelation rel : sEntity.getRelations()) {
			rangeEntity = sEntity.getSchema().getSchemaEntity(rel.getRange());
			usingInfo = rel.getUsesSpreadSheetColumn();
			if (usingInfo != null && !usingInfo.equals("")) {
				rc = getColumn(usingInfo);
				rEntity = createEntity(rc,row,rangeEntity);
				if (rEntity != null) {
					entity.setRelation(rel, rEntity);
					entity.setName(entity.getName()+rEntity.getName());
					entities.add(rEntity);
					rEntity = null;
					usingInfo = null;
				}
			}
			else {
				usingInfo = rel.getUsesSpreadSheetRow();
				if (usingInfo != null && !usingInfo.equals("")) {
					rc = getRow(usingInfo)-1;
					rEntity = createEntity(col,rc,rangeEntity);
					if (rEntity != null) {
						entity.setRelation(rel, rEntity);
						entity.setName(entity.getName()+rEntity.getName());
						entities.add(rEntity);
						rEntity = null;
						usingInfo = null;
						
					}
				}
			}
			rEntity = null;
			usingInfo = null;
		}
	}
	
	protected Set<CEntity> obtainEntities(String range,Set<CEntity> entities,SchemaEntity sEntity) {
		String start = range.substring(0,range.lastIndexOf(DOTDOT));
		String end = range.substring(range.lastIndexOf(DOTDOT)+1);
		
		String startCol = getColumnRange(start);
		
		
		String startRow = getRowRange(start);
		String endCol = getColumnRange(end);
		String endRow = getRowRange(end);
		
		int firstCol = getColumn(startCol);
		int eCol = getColumn(endCol);
		int firstRow = getRow(startRow)-1;
		int eRow = getRow(endRow)-1;
		for (int i=firstCol;i<=eCol;i++)
			for (int j=firstRow;j<=eRow;j++) {
				CEntity ent = createEntity(i,j,sEntity);
				if (!ent.getName().equals(ENTITY_PREFIX)) {
					if (sEntity.getType().equals(Schema.NARY_ENTITY)) {
						obtainRelations(i,j,ent,sEntity,entities);
					}
					entities.add(ent);
				}
			}
		return entities;
	}
	
	protected void setValues(Set<CEntity> entities, String value,SchemaEntity sEntity) {		
		String sheetName = value.substring(0,value.indexOf(SEPARATOR+RANGE_STARTS));
		sheet = wb.getSheet(sheetName);
		String range = value.substring(value.indexOf(RANGE_STARTS)+1,value.indexOf(RANGE_ENDS));
		entities = obtainEntities(range,entities,sEntity);
	}

	
	public Set<CEntity> getEntitiesGenericDataModel(SchemaEntity sEntity) {
		Set<CEntity> entities = new HashSet<CEntity>();
		for (SchemaAttribute att : sEntity.getAttributes()) {
				setValues(entities,att.getValues()[0],sEntity);
		}
		/* if (sheet==null)
			sheet = wb.getSheet(sheetName);
		int rows = sheet.getPhysicalNumberOfRows();
        String cIdValue = "";  */		
		return entities;
	}

	public void dipose() {
    }
    
}
