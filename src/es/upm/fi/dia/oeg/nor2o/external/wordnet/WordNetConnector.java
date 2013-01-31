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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.upm.fi.dia.oeg.nor2o.external.Resource;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.IndexWordSet;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.data.list.PointerTargetNode;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import net.didion.jwnl.data.list.PointerTargetTree;
import net.didion.jwnl.data.relationship.AsymmetricRelationship;
import net.didion.jwnl.data.relationship.Relationship;
import net.didion.jwnl.data.relationship.RelationshipFinder;
import net.didion.jwnl.data.relationship.RelationshipList;
import net.didion.jwnl.dictionary.Dictionary;

/**
 * @author boricles
 *
 */
public class WordNetConnector extends Resource implements WordNetConstants {

    Dictionary wordnet;
    RelationshipFinder relationFinder;
	public WordNetConnector() {
		
	}
	
	public void init(){
		FileInputStream propertiesStream;
		try {
			propertiesStream = new FileInputStream(CONFIG_PATH + FILE_CONFIG_NAME);
			JWNL.initialize(propertiesStream);		
			//tagger = new StandfordTagger();
			//tagger.init();
			
			wordnet = Dictionary.getInstance();
			relationFinder = RelationshipFinder.getInstance();
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException ef) {
			// TODO Auto-generated catch block
			ef.printStackTrace();
		}
	}
	
    public IndexWord[] getIWords(String s) throws JWNLException {
        // Look up all IndexWords (an IndexWord can only be one POS)
        IndexWordSet set = wordnet.lookupAllIndexWords(s);
        // Turn it into an array of IndexWords
        IndexWord[] words = set.getIndexWordArray();
        return words;
    }

    
    
    
    protected String getRelationName(PointerType pt) {
    	
    	String relation = "";
    	if (pt == PointerType.HYPONYM || pt == PointerType.INSTANCES_HYPONYM)
    		relation = SUBCLASSOF_RELATION;
    		
    	if (pt == PointerType.HYPERNYM || pt == PointerType.INSTANCE_HYPERNYM)
    		relation = SUPERCLASSOF_RELATION;
    	
    	if (pt == PointerType.MEMBER_MERONYM || pt == PointerType.PART_MERONYM || pt == PointerType.SUBSTANCE_MERONYM || pt == PointerType.REGION_MEMBER)
    		relation = PARTOF_RELATION;

    	if (pt == PointerType.MEMBER_HOLONYM || pt == PointerType.PART_HOLONYM || pt == PointerType.SUBSTANCE_HOLONYM || pt == PointerType.REGION)
    		relation = HASPART_RELATION;

    	return relation;
    }
    

    protected String getRelevantRelation(Set<Relationship> relsSet, int depth) {
    	String relation = "";
    	
    	for (Relationship rel : relsSet) {
    		if (rel.getDepth()==depth)
    			return getRelationName(rel.getType());
    	}
		
    	return relation;
	}
    
    protected boolean isSynsetOfPOS(Synset synset,POS []posES) {
    	for (int i=0;i<posES.length;i++)
    		if (synset.getPOS() == posES[i])
    			return true;
    	return false;
    }
    
    protected boolean isTermContainedInWords(Synset synset, String term) {
    	for (int i=0;i<synset.getWords().length;i++) {
    		if (synset.getWord(i).getLemma().equalsIgnoreCase(term) /*|| synset.getWord(i).getLemma().contains(term)*/)
    			return true;
    	}
    	return false;
    }
    
    protected String getSpecificRelation(IndexWord word1, String term1, IndexWord word2, String term2, POS[] posES) {
		String relation = "";
		int depth = 1110;
		//int depth = 1;
    	try {
	        List<PointerType> typesconcept1 = PointerType.getAllPointerTypesForPOS(word1.getPOS());
	        List<PointerType> typesconcept2 = PointerType.getAllPointerTypesForPOS(word2.getPOS());
	        
	        Set<PointerType> pointersSet = new HashSet<PointerType>();
	        pointersSet.addAll(typesconcept1);
	        pointersSet.addAll(typesconcept2);
	        
	        Synset[] concept1Synsets = word1.getSenses();
	        //Synset concept1Synset = concept1Synsets[0];
	        
	        Synset[] concept2Synsets = word2.getSenses();
	        //Synset concept2Synset = concept2Synsets[0];

	        List<PointerType> pts = PointerType.getAllPointerTypes();
	        
	        
	        if (concept1Synsets != null && concept2Synsets != null) {
	        	Set<Relationship> relsSet = new HashSet<Relationship>();
	        	
	        	for (int i=0; i<concept1Synsets.length; i++) {
	        		Synset concept1Synset,concept2Synset;
	        		
	        		if (isSynsetOfPOS(concept1Synsets[i],posES) && isTermContainedInWords(concept1Synsets[i],term1)) {
	        			concept1Synset = concept1Synsets[i];
	        		
		        		for (int j=0;j<concept2Synsets.length; j++)
		        			
		        			if (isSynsetOfPOS(concept2Synsets[j],posES) && isTermContainedInWords(concept2Synsets[i],term2)) {
		        				concept2Synset = concept2Synsets[j];
		        	
					        	for (PointerType pt : pts) {
					        		//RelationshipList list = relationFinder.findRelationships(concept1Synset, concept2Synset, pt);
					        		RelationshipList list = relationFinder.findRelationships(concept1Synset, concept2Synset, pt,RELATIONSHIP_DEEP);
				
					        		if (list!=null && !list.isEmpty()) {
					        				Relationship rel = list.getShallowest();
					        				if (rel.getDepth() < depth)
					        					depth = rel.getDepth();
					        				relsSet.add(rel);
					    	        		list.clear();
					    	        		list=null;
					        		}
					        	}
					        	
		        			}
	        		}
	        	}
	        	relation = getRelevantRelation(relsSet,depth);
	        }
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return relation;
    }
	
    protected boolean isHyponym(String term1, String term2, POS pos) {
    	HashSet<String> res = new HashSet<String>();
    	IndexWord iw;
		try {
			iw = wordnet.lookupIndexWord(pos, term1);
			if (iw==null)
				return false;
			PointerTargetTree hypernyms = PointerUtils.getInstance().getHypernymTree(iw.getSense(1));
			List list = (ArrayList)hypernyms.toList();
	    	for(int i=0; i<list.size(); i++) {
		    	PointerTargetNodeList ptnl = (PointerTargetNodeList)list.get(i);
		    	for(int j=0; j<ptnl.size(); j++) {
		    		PointerTargetNode ptn = (PointerTargetNode)ptnl.get(j);
		    		if (isTermContainedInWords(ptn.getSynset(),term2))
	    				return true;
		    	}
	    	}
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
    }
      
    protected boolean isHyponym(String term1, String term2) {
        List<POS> pts = POS.getAllPOS();
        for (POS pt : pts) 
        	if (isHyponym(term1,term2,pt))
        		return true;
        return false;
    }
    
    protected boolean isHypernym(String term1, String term2, POS pos) {
    	HashSet<String> res = new HashSet<String>();
    	IndexWord iw;
		try {
			iw = wordnet.lookupIndexWord(pos, term1);
			if (iw==null)
				return false;
		
			PointerTargetTree  hyponyms  = PointerUtils.getInstance().getHyponymTree(iw.getSense(1));
			List list = (ArrayList)hyponyms.toList();
	    	for(int i=0; i<list.size(); i++) {
	    		PointerTargetNodeList ptnl = (PointerTargetNodeList)list.get(i);
	    		for(int j=0; j<ptnl.size(); j++) {
	    			PointerTargetNode ptn = (PointerTargetNode)ptnl.get(j);
		    		if (isTermContainedInWords(ptn.getSynset(),term2))
	    				return true;
	    		}
	    	}

		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
    }
    
    protected boolean isHypernym(String term1, String term2) {
        List<POS> pts = POS.getAllPOS();
        for (POS pt : pts) 
        	if (isHypernym(term1,term2,pt))
        		return true;
        return false;
    }    

    protected boolean isMeronym(String term1, String term2, POS pos) {
    	HashSet<String> res = new HashSet<String>();
    	IndexWord iw;
		try {
			iw = wordnet.lookupIndexWord(pos, term1);
			if (iw==null)
				return false;
			
			PointerTargetTree  holonyms  = PointerUtils.getInstance().getInheritedHolonyms(iw.getSense(1));
			PointerTargetTree  partHolonyms  = PointerUtils.getInstance().getInheritedPartHolonyms(iw.getSense(1));
			PointerTargetTree  substanceHolonyms  = PointerUtils.getInstance().getInheritedSubstanceHolonyms(iw.getSense(1));
			PointerTargetTree  memberHolonyms  = PointerUtils.getInstance().getInheritedMemberHolonyms(iw.getSense(1));
			List list = (ArrayList)holonyms.toList();
			list.addAll((ArrayList)partHolonyms.toList());
			list.addAll((ArrayList)substanceHolonyms.toList());
			list.addAll((ArrayList)memberHolonyms.toList());			
	    	for(int i=0; i<list.size(); i++) {
	    		PointerTargetNodeList ptnl = (PointerTargetNodeList)list.get(i);
	    		for(int j=0; j<ptnl.size(); j++) {
	    			PointerTargetNode ptn = (PointerTargetNode)ptnl.get(j);
		    		if (isTermContainedInWords(ptn.getSynset(),term2))
	    				return true;
	    		    				
	    		}
	    	}
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
    }
    
    protected boolean isMeronym(String term1, String term2) {
    	try {

	        List<PointerType> pts = new ArrayList<PointerType>(); 
	        pts.add(PointerType.SUBSTANCE_HOLONYM); 
	        pts.add(PointerType.REGION);
	        pts.add(PointerType.PART_HOLONYM);
	        pts.add(PointerType.MEMBER_HOLONYM);
	
	        IndexWord[] concept1iWord = getIWords(term1);
			IndexWord[] concept2iWord = getIWords(term2);

			for (PointerType pt : pts) {
	        	
				for (int i=0; i< concept1iWord.length; i++)
					for (int j=0; j<concept2iWord.length; j++) {
						if (concept1iWord[i].getSenseCount()!=0 && concept2iWord[j].getSenseCount()!=0) {
							RelationshipList list = relationFinder.findRelationships(concept1iWord[i].getSenses()[0], concept2iWord[j].getSenses()[0], pt,RELATIONSHIP_DEEP);
							if (!list.isEmpty())
								return true;
						}
					}
	        	
	        	
	        }

		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
    }    
 
    protected boolean isHolonym(String term1, String term2) {
    	try {

	        List<PointerType> pts = new ArrayList<PointerType>(); 
	        pts.add(PointerType.MEMBER_MERONYM); 
	        pts.add(PointerType.PART_MERONYM);
	        //pts.add(PointerType.REGION_MEMBER);
	        pts.add(PointerType.SUBSTANCE_MERONYM);
	
	        IndexWord[] concept1iWord = getIWords(term1);
			IndexWord[] concept2iWord = getIWords(term2);

			for (PointerType pt : pts) {
	        	
				for (int i=0; i< concept1iWord.length; i++)
					for (int j=0; j<concept2iWord.length; j++) {
						if (concept1iWord[i].getSenseCount()!=0 && concept2iWord[j].getSenseCount()!=0) {
							RelationshipList list = relationFinder.findRelationships(concept1iWord[i].getSenses()[0], concept2iWord[j].getSenses()[0], pt,RELATIONSHIP_DEEP);
							
					    	for(int p=0; p<list.size(); p++) {
					    		AsymmetricRelationship ptnl = (AsymmetricRelationship)list.get(p);
						    	for(int q=0; q<ptnl.getSize(); q++) {
						    		//PointerTargetNode ptn = (PointerTargetNode)ptnl.
						    		if (isTermContainedInWords(ptnl.getSourceSynset(),term1) && (ptnl.getType().equals(PointerType.MEMBER_HOLONYM) || ptnl.getType().equals(PointerType.PART_HOLONYM) || ptnl.getType().equals(PointerType.SUBSTANCE_HOLONYM) ))
					    				return true;
						    	}
					    	}

							if (!list.isEmpty()) {
								Relationship shall = list.getShallowest(); 
							}
							
				        	

							if (!list.isEmpty())
								return true;
						}
					}
	        }

		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
    }    
    

    
    
    public String getRelation(String concept1, String concept2) {
		String relation = getTrivialSubsumption(concept1, concept2);
		
		if (relation == null || relation.equals("")) {
				
				if (isHyponym(concept1,concept2))
					return SUBCLASSOF_RELATION;
				else if (isHypernym(concept1, concept2))
					return SUPERCLASSOF_RELATION;
				//else if (isMeronym(concept2, concept1))//isMeronym(concept1, concept2))
					//return HASPART_RELATION;
				else if (isHolonym(concept2, concept1))//isHolonym(concept1, concept2)
					return PARTOF_RELATION;
				/*else if (isHyponym(concept2,concept1))
					return SUPERCLASSOF_RELATION;
				else if (isHypernym(concept2, concept1))
					return SUBCLASSOF_RELATION;
				else if (isHolonym(concept2, concept1))
					return PARTOF_RELATION;
				else if (isMeronym(concept2, concept1))
					return HASPART_RELATION;*/
		}
		return relation;
	}

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		

		
	WordNetConnector wc = new WordNetConnector();
		wc.init();
		/*
        IndexWord beautiful = Dictionary.getInstance().getIndexWord(POS.ADJECTIVE, "beautiful");
        IndexWord ugly = Dictionary.getInstance().getIndexWord(POS.ADJECTIVE, "ugly");
        
        Synset[] bea = beautiful.getSenses();
        Synset beaSynset = bea[0];
        
        Synset[] ug = ugly.getSenses();
        Synset uglySynset = ug[0];
        
        if (beaSynset != null && uglySynset != null) {
             RelationshipList list = RelationshipFinder.getInstance().findRelationships(beaSynset, uglySynset, PointerType.ANTONYM);
            Iterator i = list.iterator();
            while (i.hasNext()) {
                
                //PointerTargetNode ptr = (PointerTargetNode)i.next();
                SymmetricRelationship s = (SymmetricRelationship)i.next();
         
                 * There should be only ONE relationship found at this point, and it should match the ugly synset.

                if (!s.getTargetSynset().equals(uglySynset)) {
                    fail("Unmatched Synsets in relationship finder test");
                } 
            }
        }
                 		
		*/
	}

}

/*
class WNWord {
	IndexWord iWord;
	POS pos;
	
	public WNWord() {
		
	}
	
	public void setIndexWord(IndexWord iWord) {
		this.iWord = iWord;
	}
	
	public void setPOS(POS pos) {
		this.pos = pos;
	}
	
	public IndexWord getIndexWord() {
		return iWord;
	}
	
	public POS getPOS() {
		return pos;
	}
	
	
}
*/