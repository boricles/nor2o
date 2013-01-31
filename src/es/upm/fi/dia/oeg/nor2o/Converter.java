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

package es.upm.fi.dia.oeg.nor2o;

import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import es.upm.fi.dia.oeg.nor2o.nor.NOR;
import es.upm.fi.dia.oeg.nor2o.nor.content.CEntity;
import es.upm.fi.dia.oeg.nor2o.nor.schema.Schema;
import es.upm.fi.dia.oeg.nor2o.nor.util.NORReader;
import es.upm.fi.dia.oeg.nor2o.or.OR;
import es.upm.fi.dia.oeg.nor2o.or.ORReader;
import es.upm.fi.dia.oeg.nor2o.transformation.PRNOR;
import es.upm.fi.dia.oeg.nor2o.transformation.PRNORReader;

/**
 * @author boricles
 *
 */
public class Converter {

    private static Options options;
	/**
	 * 
	 */
	public Converter() {
		// TODO Auto-generated constructor stub
	}

	
	protected static void process(String configurationFilesFolder) {
		String separator = System.getProperty("file.separator");
		if (!configurationFilesFolder.endsWith(separator))
			configurationFilesFolder = configurationFilesFolder + separator;
		if (System.getProperty("file.separator").equals("\\")) {
			configurationFilesFolder = configurationFilesFolder.replace("\\", "\\\\");
		}

		
		NORReader norReader = new NORReader();
		norReader.setFolder(configurationFilesFolder);
		norReader.read();
		NOR myNOR = norReader.getNOR();
		
		Set<CEntity> entities = myNOR.getContent().getEntities();
		Schema schema = myNOR.getSchema();
		
		
		ORReader orReader = new ORReader();
		orReader.setFolder(configurationFilesFolder);
		orReader.read();
		OR myOR = orReader.getOR();
		
		PRNORReader prnorReader = new PRNORReader();
		prnorReader.setFolder(configurationFilesFolder);
		prnorReader.read();
		
		PRNOR myPRNOR = prnorReader.getPRNOR();
		
		myPRNOR.setNor(myNOR);
		myPRNOR.setOr(myOR); 
		
		myPRNOR.transform();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (!isFolderOptionPresent(args)) {
			process("");
		}
		else {
			createOptions();
			CommandLineParser parser = new PosixParser();
		    CommandLine cmd = null;
	        try {
	            cmd = parser.parse(options, args);
	        } catch (ParseException e) {
	            System.err.println(e.getMessage());
	            System.exit(-1);
	        }
	        String folder = cmd.getOptionValue("f");
	        
	        process(folder);
	        
		}
	}
	
	/**
	 * Checks if the configuration file option is presented
	 * @param args the set of arguments for the application
	 * @return boolean true if the configfileoption is present, false otherwise 
	 */	
	private static boolean isFolderOptionPresent(String[] args) {
		if (args.length == 2) {
			if (args[0].equals("-f") || args[0].equals("--folder"))
			return true;
		}
		return false;
	}	
	
	
	public static void createOptions() {
		options = new Options();
		options.addOption(new Option("f","folder",true,"folder that contains the configuartion files"));
	}

}
