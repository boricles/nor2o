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

package es.upm.fi.dia.oeg.nor2o.external;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

import es.upm.fi.dia.oeg.nor2o.external.scarlet.ScarletConnector;
import es.upm.fi.dia.oeg.nor2o.external.wordnet.WordNetConnector;

/**
 * @author boricles
 *
 */
public class Resource implements Constants {

	protected String resource;
	
	protected Object resourceConnector;
	protected int preProcessor;
	
	/**
	 * 
	 */
	public Resource() {
		// TODO Auto-generated constructor stub
	}
	
	public String getResource() {
		return resource;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	public void setPreProcessor(int preProcessor) {
		this.preProcessor = preProcessor;
	}
	
	public void init() {
	}
	
	protected String getTrivialSubsumption(String concept1, String concept2) {
		if (concept1.contains(" ") || concept2.contains(" ") || concept1.contains("-") || concept2.contains("-") || concept1.contains("_") || concept2.contains("_")) {
			if (concept1.contains(concept2))
				return SUBCLASSOF_RELATION;
			if (concept2.contains(concept1))
				return SUPERCLASSOF_RELATION;
		}
		return "";
	}
	
	public String getRelation(String concept1, String concept2) {
		return "";
	}

	
	public static void main(String []args) {
		/*Resource res = new ScarletConnector();
		
		res.init();
		
		String relation = res.getRelation("Sales and services elementary occupations", "Sales and services elementary occupations");
		 */
		
		Resource res = new WordNetConnector();
		//Resource res = new ScarletConnector();
		//res.setPreProcessor(ScarletConnector.WORDNET_PREPROCESSOR);
		res.init();

		//String relation = res.getRelation("Sales and services elementary occupations", "Sales and services elementary occupations");
		//String relation = res.getRelation("personal_development","performance");
		//String relation = res.getRelation("professionals", "teaching professionals");  //no devuelve ninguna relación
		//String relation = res.getRelation("CLERKS", "Office clerks");
		//String relation = res.getRelation("occupation", "business worker");	//no devuevle ninguna relación
		//String relation = res.getRelation("occupation", "education worker");	
		//String relation = res.getRelation("occupation", "engineering worker");	
		//String relation = res.getRelation("cultural sector employee", "performing artist"); //no devuelve ninguna relacion
		//String relation = res.getRelation("artist", "cultural sector employee");			//no devuelve ninguna relación
		
		//String relation = res.getRelation("worker", "young worker");
		//String relation = res.getRelation("worker", "occupational status");

		//String relation = res.getRelation("cultural sector employee","artist");
		
		//String relation = res.getRelation("young worker","worker");
		
		//String relation = res.getRelation("sex distribution","woman");		//no devuelve ninguna relación
		
		//String relation = res.getRelation("sex distribution","woman");		//no devuelve ninguna relación
		
		//String relation = res.getRelation("translation","multilingualism");		//no devuelve ninguna relación
		
		//String relation = res.getRelation("industry","textile industry");		//no devuelve ninguna relación
		
		//String relation = res.getRelation("animal","plant");
		
		//String relation = res.getRelation("social behaviour","behaviour");		//no devuelve ninguna relación
		
		//String relation = res.getRelation("small medium enterprise","small enterprise");		

		//String relation = res.getRelation("performance","personal development");		
		
		//String relation = res.getRelation("performance","efficiency");		
		
		//String relation = res.getRelation("performance","success");
		
		//String relation = res.getRelation("research","fundamental research");  
		
		//String relation = res.getRelation("artificial intelligence","expert system");  //no devuelve ninguna relación
		
		//String relation = res.getRelation("engineering","technological sciences");  //no devuelve ninguna relación
		
		//String relation = res.getRelation("Aquatic animals","Aquatic organisms");  
		
		//String relation = res.getRelation("Choline","Alcohols");
		
		//String relation = res.getRelation("Sterols","Cholesterol");
		
		//String relation = res.getRelation("Chemical reactions","Amination");	NNO
		
		//String relation = res.getRelation("Chemical properties","Properties");	//NO	
		
		
		
		//ASFA
		//String relation = res.getRelation("Asthenosphere","Earth structure");	
		//String relation = res.getRelation("Aseismic zones","Earth structure");
		//String relation = res.getRelation("Basement rock","Earth structure");
		//String relation = res.getRelation("Benioff zone","Earth structure");
		//String relation = res.getRelation("Earth core","Earth structure");
		//String relation = res.getRelation("Earth crust","Earth structure");
		//String relation = res.getRelation("Earth mantle","Earth structure");
		//String relation = res.getRelation("Lithosphere","Earth structure");
		//String relation = res.getRelation("Plates","Earth structure");
		//String relation = res.getRelation("Continents","Earth structure");
		//String relation = res.getRelation("Magma","Asthenosphere");
		//String relation = res.getRelation("Magma","Mafic magma");
		//String relation = res.getRelation("Earth mantle","Lower mantle");
		//String relation = res.getRelation("Earth mantle","Upper mantle");
		
		
		
		//ETT
		//String relation = res.getRelation("small medium enterprise","small enterprise");		
		//String relation = res.getRelation("performance","personal development");		
		//String relation = res.getRelation("performance","efficiency");		
		//String relation = res.getRelation("performance","competence");
		//String relation = res.getRelation("performance","productivity");
		//String relation = res.getRelation("creativity","aptitude");
		//String relation = res.getRelation("personal development","intellectual development");
		//String relation = res.getRelation("knowledge","intellectual development");
		//String relation = res.getRelation("knowledge","creativity");
		//String relation = res.getRelation("small medium enterprise","medium enterprise");
		//String relation = res.getRelation("medium enterprise","enterprise");
		//String relation = res.getRelation("administration","enterprise");
		//String relation = res.getRelation("administration","management");
		//String relation = res.getRelation("administration","planning");
		//String relation = res.getRelation("educational planning","planning");
		//String relation = res.getRelation("educational planning","educational reform");
		//String relation = res.getRelation("economic planning","planning");
		//String relation = res.getRelation("intellectual development","personal development");
		//String relation = res.getRelation("personality","personal development");
		//String relation = res.getRelation("know how","aptitude");
		
		//personnel management

		
		
		//small enterprise

		
	}
	/*
	protected static String stemming(String token) {
		SnowballStemmer stemmer = new englishStemmer();
		stemmer.setCurrent(token);
		stemmer.stem();
		return stemmer.getCurrent();
	}
	*/
	
	/* TODO
	 * terminar de insertar el treetagger.
	 */
	/*
	protected static String stemmingTreeTagger(String token) {
		TreeTaggerWrapper tt = new TreeTaggerWrapper<String>();
		 try {
		     tt.setModel("treetagger/models/english.par:iso8859-1");
		     tt.setHandler(new TokenHandler<String>() {
		         public void token(String token, String pos, String lemma) {
		             
		         }
		     });
		     //tt.process(asList(new String[] {"This", "is", "a", "test", "."}));
		     Vector str = new Vector();
		     str.add(token);
		     tt.process(new Vector());
		 }
		 catch (java.lang.Exception ex){
			 ex.printStackTrace();
		 }
		 finally {
		     tt.destroy();
			 return "";		     
		 }		

	}
	
	public static void main(String []args) {
		try {
			Analyzer sta = new StandardAnalyzer(Version.LUCENE_CURRENT);
			TokenStream stream = sta.tokenStream("contents", new StringReader("Sales and services elementary occupations"));
			String word ="";
			while (true) {
				Token token;
				token = stream.next();
				if (token==null)
					break;
				
				
				//word = stemming(token.termText());
				word = token.termText();
				//word = stemmingTreeTagger(token.termText());
				
				}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
	}
	
	*/
	

}
