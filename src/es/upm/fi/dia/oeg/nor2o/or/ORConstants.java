package es.upm.fi.dia.oeg.nor2o.or;

public interface ORConstants {

	//public static final String SEPARATOR = "";
	
	public static final String OR_DESCRIPTION_FILE="or.xml";
	
	public static final String OR_TAG = "Or";
	
	public static final String OR_LOGICAL_URI_ATT = "ontologyURI";
	public static final String OR_PHYSICAL_URI_ATT = "ontologyFile";	
	public static final String OR_NAME_ATT = "name";	
	public static final String OR_IMPL_ATT = "implementation";	
	public static final String OR_EXIST_ATT = "alreadyExist";	
	public static final String OR_SEPARATOR_ATT = "separator";

	public static final String OWL = "OWL";
	public static final String RDFS = "RDFS";
	
	public static final String RDF_XML = "RDF/XML";
	
	public static final int OWL_LANGUAGE = 0;
	public static final int RDFS_LANGUAGE = 1;	
	
	public static final String[] CLASS = {"owl:Class","rdfs:Class"};
	public static final String[] EQUIVALENT_CLASS = {"owl:equivalentClass","owl:equivalentClass"};
	public static final String[] SUBCLASSOF = {"rdfs:subClassOf","rdfs:subClassOf"};
	
	public static final String YES = "yes";
	public static final String NO = "no";	
	
	public static final String DEFAULT_ENCODING="UTF-8";
	
	public static final int ADHOC_RELATION_CLASS_AXIOM = -1;
	public static final int EQUIVALENT_CLASS_AXIOM = 0;
	public static final int SUBCLASSOF_AXIOM = 1;
	
	
	public static final String STRING = "string";
	public static final String DECIMAL = "decimal";
	public static final String FLOAT = "float";
	public static final String DOUBLE = "double";
	public static final String INTEGER = "integer";
	
	
	public static final String LAST_SEPARATOR = "/";
	
	
}
