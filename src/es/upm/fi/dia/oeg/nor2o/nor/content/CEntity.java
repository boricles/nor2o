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

package es.upm.fi.dia.oeg.nor2o.nor.content;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaEntity;
import es.upm.fi.dia.oeg.nor2o.nor.schema.SchemaRelation;



/**
 * @author boricles
 *
 */
public class CEntity {

	protected Set<CRelation> relations;
	protected Set<CAttribute> attributes;
	
	protected SchemaEntity schemaEntity;
	
	protected String name;
	//protected String prettyName;
	
	public CEntity() {
		
	}
	
	public CEntity (String name) {
		setName(name);
	}
	
	public void setSchema(SchemaEntity schemaEntity) {
		this.schemaEntity = schemaEntity;
	}
	
	public SchemaEntity getSchema() {
		return schemaEntity;
	}

	public String getName() {
		return name;
	}

	/*
	public String getPrettyName() {
		return prettyName;
	}*/
	
	public void setName(String name) {
		this.name = name;
	/*	prettyName = name.replace(",", "_");
		prettyName = prettyName.replace(".", "_");
		prettyName = prettyName.replace(" ", "_");
		prettyName = prettyName.replace("\"", "_");		
		prettyName = prettyName.replace("\'", "_");*/
	}
	
	public void addAttribute(CAttribute attribute) {
		if (attributes == null)
			attributes = new HashSet<CAttribute>();
		attributes.add(attribute);
	}
	
	public void addRelation(CRelation relation) {
		if (relations == null) 
			relations = new HashSet<CRelation>();
		relations.add(relation);
		
		
	}
	
	public Set<CRelation> getRelations (String relName) {  //Retorno un Set por las relaciones con cardinalidad >1
		Set<CRelation> rels = new HashSet<CRelation>();
		if (relations != null) {
			for (CRelation rel: relations) 
				if (rel.getName().equals(relName)) {
					rels.add(rel);
				}
		}
		return rels;
	}
	
	public Set<CRelation> getRelations() {
		return relations;
	}
	
	public String[] getAttributeValuesAsString(String name) {
		Set<Object> result = null;
		String []vals = null;
		if (attributes != null) {
			for (CAttribute cat : attributes) {
				if (cat.getName().equals(name)) {
					result = cat.getValues();
					int size = result.size();
					int i=0;
					vals = new String[size];
					for (Object obj : result) {
						vals[i++] =	obj.toString();
					}
				}
			}
		}
		return vals;
	}
	
	public String[] getRelationValuesAsString(String name) {
		String result = null;
		String []vals = null;
		if (relations==null) {
			vals = new String[1];
			vals[0] = "";
		}
		else {
			vals = new String[relations.size()];
			if (relations != null) {
				int i=0;
				for (CRelation cRel : relations) {
					if (cRel.getName().equals(name)) {
						if (cRel.getRange()!=null) {
							result = cRel.getRange().getName();
							vals[i++] = result;
						}
					}
				}
			}
		}
		return vals;
	}
	
	public boolean hasRelation(String relName) {
		for (CRelation rel: relations) 
			if (rel.getName().equals(relName))
				return true;
		return false;
	}
	
	public void setRelation(SchemaRelation srelation, CEntity range) {
		
		CRelation relation = new CRelation(srelation.getName());
		relation.setSchemaRelation(srelation);
    	relation.setDomain(this);
    	relation.setRange(range);
    	this.addRelation(relation);
    	
    	/*String inverse = srelation.getInverse();
    	if (inverse!=null || !inverse.equals("")) {
    		Set<SchemaRelation> scheRels = getSchema().getRelations();
    		for (SchemaRelation sRel : scheRels) {
    			if (sRel.getName().equals(inverse)) {
    				CRelation relationInverse = new CRelation(sRel.getName());
    				relationInverse.setSchemaRelation(sRel);
    				relationInverse.setDomain(range);
    				relationInverse.setRange(this);
    		    	range.addRelation(relationInverse);
    		    	break;
    			}
    		}
    	}*/
    	
    	
    	
	}
	
	public String toString() {
		String str = "";
		str += getName() + "\nAttributes\n";
		if (attributes !=null) {
			for (CAttribute att: attributes) {
				str += att.toString() + "\t";
			}
		}
		str += "\nRelations\n";
		if (relations!= null) {
			for (CRelation rel: relations) {
				str +=rel.toString() + "\n";
			}
		}
		return str;
	}
	
	public void dispose () {
		if (attributes !=null) {
			attributes.clear();
			attributes = null;
		}
		if (relations!= null) {
			relations.clear();
			relations = null;
		}
	}
	
}
