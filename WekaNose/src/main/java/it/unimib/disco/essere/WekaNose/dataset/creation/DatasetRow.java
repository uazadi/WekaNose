package it.unimib.disco.essere.WekaNose.dataset.creation;

import java.util.Hashtable;

/**
 * This class represent an instance of the dataset, i.e. a row of a CSV file 
 * */
public class DatasetRow {

	/** The metrics at method level extracted and inserted in the dataset (only if the code smell is at method level) */
	public final static String[] METHOD_METRICS = {"NOP", "CC", "ATFD", "FDP", "CM", "MAXNESTING", 
													"LOC", "CYCLO", "NMCS", "NOLV", "MaMCL", "NOAV", 
													"LAA", "FANOUT", "CFNAMM", "ATLD", "CLNAMM", 
													"CINT", "MeMCL", "CDISP"};
	
	/** The metrics at class level extracted and inserted in the dataset */
	public final static String[] CLASS_METRICS =  {"NOII", "NOAM", "NOCS", "NOM", "NMO", "ATFD", 
													"FANOUT", "NOMNAMM", "NOA", "NIM", "DIT", "LOC", 
													"LOCNAMM", "CFNAMM", "TCC", "NOPA", "CBO", "RFC", 
													"NOC", "WMC", "LCOM5", "WOC", "WMCNAMM", "AMW", 
													"AMWNAMM"};
	
	/** The metrics at package level extracted and inserted in the dataset */
	public final static String[] PACKAGE_METRICS = {"NOCS", "NOMNAMM", "NOI", "LOC", "NOM"};

	private int m_ID;
	private String m_Project;
	private String m_Package;
	private String m_Class;
	private String m_Method;
	Hashtable<String, String> method_metrics;
	Hashtable<String, String> class_metrics;
	Hashtable<String, String> package_metrics;
	

	/**
	 * To create an instance all the information that have to appear in the dataset are required.
	 * The instance created through this constructor are for a code smell at method level.
	 * 
	 * @param m_ID
	 * @param m_Project
	 * @param m_Package
	 * @param m_Class
	 * @param m_Method
	 * @param method_metrics
	 * @param class_metrics
	 * @param package_metrics
	 */
	public DatasetRow(int m_ID, String m_Project, String m_Package, String m_Class, String m_Method, 
						Hashtable<String, String> method_metrics, 
						Hashtable<String, String> class_metrics,
						Hashtable<String, String> package_metrics
						) {
		this.m_ID = m_ID;
		this.m_Project = m_Project;
		this.m_Package = m_Package;
		this.m_Class = m_Class;
		this.m_Method = m_Method;
		this.method_metrics = method_metrics;
		this.class_metrics = class_metrics;
		this.package_metrics = package_metrics;
	}
	

	/**
	 * To create an instance all the information that have to appear in the dataset are required.
	 * The instance created through this constructor are for a code smell at class level.
	 * @param m_ID
	 * @param m_Project
	 * @param m_Package
	 * @param m_Class
	 * @param class_metrics
	 * @param package_metrics
	 */
	public DatasetRow(int m_ID, String m_Project, String m_Package, String m_Class,
						Hashtable<String, String> class_metrics,
						Hashtable<String, String> package_metrics
						) {
		this.m_ID = m_ID;
		this.m_Project = m_Project;
		this.m_Package = m_Package;
		this.m_Class = m_Class;
		this.class_metrics = class_metrics;
		this.package_metrics = package_metrics;
	}

	/**
	 * @param className
	 * @param isMethodLevel
	 * @return the header of the dataset, i.e. the first row that explain the semantic of the column
	 */
	public static String getHeader(String className, boolean isMethodLevel) {
		String header = "ID,Project,Package,Class,";	
		if(isMethodLevel) {
			header += "Method,";
			for(String metric : METHOD_METRICS) {
				header += metric + "_method,";
			}
		}
		for(String metric : CLASS_METRICS) {
			header += metric + "_class,";
		}
		for(String metric : PACKAGE_METRICS) {
			header += metric + "_package,";
		}
		header += className;
		return header;
	}
	
	/** Return the row as should be included in the dataset, i.e. with the values separated by a comma */
	@Override
	public String toString() {
		String string = m_ID + "," + m_Project  +"," + m_Package + "," + m_Class + ",";

		if(this.method_metrics != null) {
			string += "\"" + m_Method + "\",";
			for(String metric : METHOD_METRICS) {
				String value = this.method_metrics.get(metric);
				if(value != null) 
					string += this.method_metrics.get(metric) + ",";
				else 
					string += "?,";
			}
		}
			
		for(String metric : CLASS_METRICS) {
			String value = this.class_metrics.get(metric);
			if(value != null) 
				string += this.class_metrics.get(metric) + ",";
			else 
				string += "?,";
		}

		for(String metric : PACKAGE_METRICS) {
			try {
				string += this.package_metrics.get(metric) + ",";
			}catch(NullPointerException e) {
				string += "?,";
			}
		}
		
		string += ""; //< className
		
		return string;
	}
}
