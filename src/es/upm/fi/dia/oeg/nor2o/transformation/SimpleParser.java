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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import es.upm.fi.dia.oeg.nor2o.nor.content.CEntity;
import es.upm.fi.dia.oeg.nor2o.nor.content.CRelation;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaEntity;

/**
 * @author boricles
 *
 */
public class SimpleParser implements ParserConstants {

	protected static String defaultOperator = PLUS_STRING;
	
	/**
	 * 
	 */
	public SimpleParser() {
		// TODO Auto-generated constructor stub
	}
	
	
	protected static String indicatorParse(String res) {
		if (res==null)
			res = "";
		
		res = res.toLowerCase();
		int index = res.indexOf("council");
		if (index!=-1)
			res = res.substring(0,index-1);
		
		if (res.contains("city")) {
			res = res.substring(0,res.indexOf("city")-1);
			res = "city/" + res;
		}
		if (res.contains("county")) {
			res = res.substring(0,res.indexOf("county")-1);
			res = "county/" + res;
		}		
		return res;
	}
	
	protected static String extractValue (String operand, CEntity entity, boolean removeSpecialChars) {
		if (operand.startsWith(START_DELIMITER_ATTRIBUTE))  {
			operand = operand.substring(1,operand.lastIndexOf(END_DELIMITER_ATTRIBUTE));
			String res = "";
			String values[] = null;
			if (entity!=null){
				values = entity.getAttributeValuesAsString(operand);
				if (values!=null) {
					String res1 = values[0];
					//hard coded for the indicators
					//res = indicatorParse(res);
					//
					res = res1.replace(String.valueOf((char) 160), "");
					
					if (removeSpecialChars) 
						res = SimpleParser.removeSpecialChars(res);
					
					
				}
				if (res.equals("")) {
					values = entity.getRelationValuesAsString(operand);
					if (values!=null) {
						res = values[0];
						//hard coded for the indicators
						res = indicatorParse(res);
								//
						
						if (removeSpecialChars) {
							res = SimpleParser.removeSpecialChars(res);
						}	//TODO Check it
					}
				}
			}

			return res;
		}
		return operand;
	}
	
	
	protected static String extractValueAlsoFromObjectProperties(String operand, CEntity entity) {
		if (operand.startsWith(START_DELIMITER_ATTRIBUTE))  {
			operand = operand.substring(1,operand.lastIndexOf(END_DELIMITER_ATTRIBUTE));
			String res = "";
			String values[] = null;
			if (entity!=null){
				values = entity.getAttributeValuesAsString(operand);
				if (values!=null) {
					res = values[0];
					
					//res = SimpleParser.removeSpecialChars(res);
				}
			}
			//this is for the labels in the thesauri
			if (entity!=null && values==null) {
				Set<CRelation> rels = new HashSet<CRelation>();
				rels = entity.getRelations(operand);
				for (CRelation rel : rels) {
					res = rel.getRange().getName();
				}
			}

			return res;
		}
		return operand;
	}

	
	/*protected static String extractValue (String operand, SchemaEntity sEntity) {
		if (operand.startsWith(START_DELIMITER_ATTRIBUTE))  {
			operand = operand.substring(1,operand.lastIndexOf(END_DELIMITER_ATTRIBUTE));
			String res = sEntity.
			return res;
		}
		return operand;
	}*/

	public static String evaluate(String expression, CEntity entity, boolean removeSpecialChars) {
		String result = "";
		String iter = "";
		String[] operands = expression.split("\\"+defaultOperator);
		for (int i=0;i<operands.length;i++) {
			iter = extractValue(operands[i], entity,removeSpecialChars);
			if (i==(operands.length-1)&& removeSpecialChars && operands.length!=1)
				try {
					iter = URLEncoder.encode(iter,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (iter!=null)
				result += iter;

		}
		return result;
	}
	
	public static String evaluateAlsoFromObjectProperties(String expression, CEntity entity) {
		String result = "";
		String iter = "";
		String[] operands = expression.split("\\"+defaultOperator);
		for (int i=0;i<operands.length;i++) {
			iter = extractValueAlsoFromObjectProperties(operands[i], entity);
			if (iter!=null)
				result += iter;
		}
		return result;
	}
	
	
	public static String evaluate(String expression, SchemaEntity sEntity) {
		return expression;
	}
	
	public static String addScape(String value) {
		value = value.replace("'", "\'");
		return value;
	}
	
	public static String removeSpecialChars(String value) {
		if (value == null)
			return "";
		String without = value.replace(",", "_");

		
		//only for the indicators
		without = without.replaceAll(" ", "_");
		//without = without.replaceAll(" ", "-");
		//without = without.replace(".", "_");
		without = without.replace(".", "-");
		
		//without = without.replace("\\ ", "_");
		without = without.replaceAll("\\s", "_");		
		without = without.replace("\"", "_");		
		without = without.replace("\'", "_");
		
		//only for the indicators
		//without = without.replace("-", "_");
		
		//not only for the indicators
		//without = without.replace(":", "_");
		without = without.replace("(", "_");		
		without = without.replace(")", "_");
		//without = without.replace("/", "_");	//@TODO check it later
		without = without.replace("{", "_");
		without = without.replace("}", "_");
		without = without.replace("[", "_");		
		without = without.replace("]", "_");		
		without = without.replace("+", "_");		
		without = without.replace("__", "_");
		without = without.replace("___", "_");		
		without = without.replace("____", "_");		
		without = without.replace("_____", "_");		
		without = without.replace("'", "_");
		without = without.replace("|", "_");
		return without;
	}
	
	public static String getValue (String condition) {
		String value = condition.substring(condition.indexOf(VALUE_DELIMITER)+1,condition.lastIndexOf(VALUE_DELIMITER));
		return value;
	}

	public static String getCondition (String condition) {
		String value = condition.substring(0,condition.indexOf(PLUS_STRING));
		return value;
	}

	public static boolean evaluateIf(String value, String condition) {
		boolean cond = false;
		
		String comparableValue = getValue(condition);
		String iF = getCondition(condition);
		if (iF.equalsIgnoreCase(CONTAINS)){
			if (value==null)
				return cond;
			cond = value.contains(comparableValue);
		}
		
		return cond;
	}

	public static String removeXPathModifiers (String var) {
		String var1 = var.replace("@", "");
		var1 = var1.replace("/", "");
		return var1;
	}

}
