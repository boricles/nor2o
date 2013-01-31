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

package es.upm.fi.dia.oeg.nor2o.external.wordnet;

/**
 * @author boricles
 *
 */
public interface WordNetConstants {
	
    public static String CONFIG_PATH = "./";
    public static String FILE_CONFIG_NAME = "file_properties.xml";
    
    public static String STOP_WORD_TAG = "CC";
    public static String STOP_WORD1_TAG = "WDT";
    public static String STOP_WORD2_TAG = "DT";
    public static String STOP_WORD3_TAG = "IN";
    
    public static int RELATIONSHIP_DEEP = 100;
    
    /*
    key !	name antonym
    key @	name hypernym
    key ~	name hyponym
    key =	name attribute
    key ^	name also see
    key *	name entailment
    key ?	name entailed by
    key >	name cause
    key $	name verb group
    key %m	name member meronym
    key %s	name substance meronym
    key %p	name part meronym
    key #m	name member holonym
    key #s	name substance holonym
    key #p	name part holonym
    key &	name similar
    key <	name participle of
    key \	name derived
    key +	name nominalization
    key ;c	name category domain
    key ;r	name region domain
    key ;u	name usage domain
    key -c	name member of category domain
    key -r	name member of region domain
    key -u	name member of usage domain
    key @i	name INSTANCE_HYPERNYM
    key ~i	name INSTANCES_HYPONYM
    */
}
