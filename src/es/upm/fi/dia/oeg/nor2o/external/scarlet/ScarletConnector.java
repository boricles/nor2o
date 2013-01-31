package es.upm.fi.dia.oeg.nor2o.external.scarlet;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Vector;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

import es.upm.fi.dia.oeg.nor2o.external.Resource;
import es.upm.fi.dia.oeg.nor2o.external.wordnet.WordNetConnector;



import uk.ac.open.kmi.scarlet.RelationFinder;
import uk.ac.open.kmi.scarlet.data.Relation;

/**
 * 
 */

/**
 * @author boricles
 *
 */
public class ScarletConnector extends Resource implements Constants {

	/**
	 * @param args
	 */

	protected Vector<Relation> relations;
	RelationFinder rf;
	
	
	Resource resource;
	
	public ScarletConnector() {
		super();
		preProcessor = LUCENE_ANALYZER_PREPROCESSOR;
	}
	
	public void init() {
		relations = new Vector<Relation>();
		
		//Returns the first found relation between "cat" and "dog"
		//Only searches for relations of type disjoint
		rf = new RelationFinder();
		rf.setCheckForInheritedRelations(false);
		rf.setCheckForAllRelationTypes(false);
		//Only searches for relations of type disjoint
		rf.setCheckForDisjointRelations(false);
		
		//Searches for all types of relations
		//takes into account inherited relations to depth = 4
		rf.setStopAtFirstRelation(false);
		rf.setCheckForAllRelationTypes(true);
		rf.setCheckForInheritedRelations(true);
		rf.setDepth(4);
		
		

	}
	
	
	public void setPreProcessor(int preProcessor) {
		this.preProcessor = preProcessor;
		
		if (preProcessor == WORDNET_PREPROCESSOR) {
			resource = new WordNetConnector();
			resource.init();
		}
	}
	
	
	public HashSet<String> getPreProcessedTerms(String term) {
		HashSet<String> terms = new HashSet<String>();
		/*try {
			Analyzer sta = new StandardAnalyzer(Version.LUCENE_CURRENT);
			TokenStream stream = sta.tokenStream("contents", new StringReader(term));
			String word ="";
			while (true) {
				Token token;
				token = stream.next();
				if (token==null)
					break;
				word = token.termText();
				terms.add(word);
			}
		}
		catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}*/
		
		
		
		return terms;
	}

	public String getRelation(String concept1, String concept2) {
		
		String relation = getTrivialSubsumption(concept1, concept2);
		
		if (relation == null || relation.equals("")) {
			
			relation = getSpecificRelation(concept1, concept2);
			
			if (relation == null || relation.equals("")) {
				
				if (preProcessor == LUCENE_ANALYZER_PREPROCESSOR) {
				
					HashSet<String> term1 = getPreProcessedTerms(concept1);
					HashSet<String> term2 = getPreProcessedTerms(concept2);
					for (String t1current : term1) {
						for (String t2current : term2) {
							relation = getSpecificRelation(t1current, t2current);
							if (relation != null && !relation.equals(""))
								return relation;
						}
					}
				}
				else if (preProcessor == WORDNET_PREPROCESSOR) {
					try {
						IndexWord[] concept1iWord = ((WordNetConnector)resource).getIWords(concept1);
						IndexWord[] concept2iWord = ((WordNetConnector)resource).getIWords(concept2);
						for (int i=0; i< concept1iWord.length; i++)
							for (int j=0; j<concept2iWord.length; j++) {
								//if (concept1iWord[i].getPOS()!=POS.ADVERB && concept2iWord[j].getPOS()!=POS.ADVERB)
								if (concept1iWord[i].getPOS()==POS.NOUN && concept2iWord[j].getPOS()==POS.NOUN) {
									relation = getSpecificRelation(concept1iWord[i].getLemma(),concept2iWord[j].getLemma());
									if (relation!=null && !relation.equals(""))
										return relation;
								}
							}
					} catch (JWNLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return relation;
	}

	
	public String getSpecificRelation(String concept1, String concept2) {
		String relationName = "";
		try {
			Relation relation = null;
			relations = rf.findRelationBetweenTerms(concept1, concept2);
			if (relations!=null && !relations.isEmpty()) {
				relation = relations.get(0);
				relationName = relation.getName();
			}
			if (relationName.contains(DEFAULT_SEPARATOR))
				relationName = relationName.substring(relationName.indexOf(DEFAULT_SEPARATOR)+1);
			return relationName;
		}
		catch (java.lang.Exception ex) {
			//ex.printStackTrace();
			return "";
		}
	}

}
