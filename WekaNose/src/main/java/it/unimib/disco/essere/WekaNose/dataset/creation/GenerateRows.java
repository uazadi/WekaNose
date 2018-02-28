package it.unimib.disco.essere.WekaNose.dataset.creation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import it.unimib.disco.essere.WekaNose.exceptions.WorkspaceException;
import it.unimib.disco.essere.WekaNose.utils.CustomLogger;

/** This class implements the sampling process, hence it extract the information from the file 
 *  that should be previously created through the queries to the SQLite DB and then and create the 
 *  actual dataset (the not labeled version)
 *  */
public class GenerateRows {
	
	/** the attribute through which the class can access the workspace */
	private WorkspaceHandler workspace;
	
	/** The list of pair: 
	 * 		< system_name/Advisor.toString , 
	 * 		  list of the instances that respect the rule that describe the Advisor> 
	 * 
	 * Basically associate a unique string to the content of the file that contain the instances (methods or classes)
	 * that respect the rule that identify a specific Advisor.
	 * */
	private Hashtable<String, List<String>> pair_AdvisorsPerSystem_Instances = new Hashtable<String, List<String>>();
	
	/** The list of pair: 
	 * 		< system_name/Advisor.toString , 
	 * 		  list of the instances that selected to be in the final dataset> 
	 * 
	 * Basically associate a unique string to the information concerning the instances (methods or classes)
	 * that respect the rule that identify a specific Advisor and that are selected to be in the final dataset.
	 * */
	private Hashtable<String, List<String>> chosenInstances = new Hashtable<String, List<String>>();
	
	/** The dataset represented as an array of row */
	private DatasetRow[] rows;
	
	/**
	 * Create each row of the dataset
	 * */
	public GenerateRows(WorkspaceHandler workspace, int numRows, boolean isMethodLevel) throws WorkspaceException{
		this.workspace = workspace;
		rows = new DatasetRow[numRows];
		
		CustomLogger.getInstance().info("Loading the advisors CSV files", this);
		loadAdvisorsResults();
		
		CustomLogger.getInstance().info("Selectiong the instances", this);
		sampleInstaces(numRows);

		CustomLogger.getInstance().info("Printing the chosen instances", this);
		try {
			this.printChosenInstaces();
		} catch (Exception e) {
			throw new WorkspaceException("unable to create the chosen_instaces.csv file");
		}	
		
		try {
			metricsRecordLinkage(isMethodLevel);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkspaceException("unable to generate the rows");
		}
	}

	/**
	 * For each chosen instances it load the metrics at class and package level, 
	 * it also load the metric at method only if the code smell is at method level.
	 * 
	 * */
	private void metricsRecordLinkage(boolean isMethodLevel) throws IOException {
		int i=0;
		for(String key: chosenInstances.keySet()) {
			for(String row: chosenInstances.get(key)){
				
				Integer instance_id = Integer.parseInt(row.split(",")[0]);
				Integer id_m = instance_id;
				Integer id_c = instance_id;
				
				String method_name = "";
				Hashtable<Integer, Hashtable<String, String>> method = null;
				
				if(isMethodLevel) {
					method = this.loadMetrics(key.substring(0, key.lastIndexOf('/')), "Method");
					method_name = method.get(id_m).get("name");
					id_c = Integer.parseInt(method.get(id_m).get("father").replaceAll(" ", ""));
				}
				
				Hashtable<Integer, Hashtable<String, String>> _class = this.loadMetrics(key.substring(0, key.lastIndexOf('/')), "Class");
				String class_name = _class.get(id_c).get("name");
				Integer id_p = Integer.parseInt(_class.get(id_c).get("father").replaceAll(" ", ""));

				Hashtable<Integer, Hashtable<String, String>> _package = this.loadMetrics(key.substring(0, key.lastIndexOf('/')), "Package");
				String package_name = _package.get(id_p).get("name");
				
				if(isMethodLevel) 
					rows[i++] = new DatasetRow( instance_id, 
												key.substring(0, key.lastIndexOf('/')), 
												package_name, 
												class_name, 
												method_name, 
												method.get(id_m), 
												_class.get(id_c), 
												_package.get(id_p));
				else
					rows[i++] = new DatasetRow( instance_id, 
												key.substring(0, key.lastIndexOf('/')), 
												package_name, 
												class_name, 
												_class.get(id_c), 
												_package.get(id_p));
				
			}
		}
	}

	
	/**
	 * For each Advisor and for each system this method load the instances (classes or methods)
	 * of the current system that respect the rule that identify the current Advisor.
	 * */
	public void loadAdvisorsResults() throws WorkspaceException {
		for(String group: this.workspace.getResultsQuery()) {
			for(String project : this.workspace.getProjectsCreated()) {
				try {
					loadAdvisorResults(project + "/" + group, this.workspace.getPath() + "/" + project + "/" + group + ".csv");
				} catch (Exception e) {
					throw new WorkspaceException("Result query's file not found");
				}
			}
		}
	}
	
	/**
	 * Given an Advisor and a system specified through the key this method load the instances (classes or methods)
	 * of that system that respect the rule that identify that Advisor.
	 * */
	public void loadAdvisorResults(String key, String path) throws IOException {
		List<String> list = Files.readAllLines(new File(path).toPath(), Charset.defaultCharset() );
		pair_AdvisorsPerSystem_Instances.put(key, list);
		chosenInstances.put(key, new ArrayList<String>());
	}
	
	/**
	 * This method select the instances that will be included in the dataset  
	 * */
	private void sampleInstaces(int numRows) {
		int countRows = 0;
		int countIteration = 0;
		sampling_process:{
			while(countRows < numRows) {
				for(String group: this.workspace.getResultsQuery()) {
					for(String project : this.workspace.getProjectsCreated()) {
						if(countIteration > (numRows 
											 * this.workspace.getResultsQuery().size()
											 * this.workspace.getProjectsCreated().size())) {
							CustomLogger.getInstance().warning("The chosen dataset size is greater then the avaible instances", this);
							break sampling_process;
						}
						if(countRows < numRows) {
							if (selectRandom(project + "/" + group))
								countRows++;
						}
						else break sampling_process;
						countIteration++;
					}
				}
			}
		}
	}
	
	/**
	 * Given an Advisor and a system specified through the key, this method select randomly an instance 
	 * that will be included in the dataset. That instance is selected between the instances (classes or methods)
	 * of that system that respect the rule that identify that Advisor.
	 * */
	public boolean selectRandom(String key) {
		List<String> instances = pair_AdvisorsPerSystem_Instances.get(key);
		if(!instances.isEmpty()) {
			Random random = new Random();
			chosenInstances.get(key).add(instances.remove(random.nextInt(instances.size())));
			return true;
		}
		return false;
	}
	
	/**
	 * For each system this method generate the file chosen_instances.csv.
	 * That file contain all the instances (classes or methods) of that system that are included in the dataset. 
	 * */
	public void printChosenInstaces() throws IOException {
		for(String key: chosenInstances.keySet()) {
			File file = new File(this.workspace.getPath() + "/" + key.substring(0, key.lastIndexOf('/')) + "/" + "chosen_instances.csv");
			FileWriter writer = new FileWriter(file, true);
			String str = "";
			for(String row: chosenInstances.get(key)) 
				str += row + "\n";
			writer.write(str);
			writer.close();
		}
	}
	
	
	/**
	 * It load the metric at "type" level of the system called "project"
	 * 
	 * @param project the system of which the metrics has to load
	 * @param type can be Class, Method or Package
	 * @return
	 * @throws IOException
	 */
	public Hashtable<Integer, Hashtable<String, String>> loadMetrics(String project, String type) throws IOException {
		
		Hashtable<Integer, Hashtable<String, String>> metrics = new Hashtable<Integer, Hashtable<String, String>>();
		String path = workspace.getPath() + "/" + project + "/" + type + ".csv";
		List<String> file = Files.readAllLines(new File(path).toPath(), Charset.defaultCharset() );
		
		for(String line: file) {
			String[] attr = line.replaceAll(" ", "").split(",");
			
			Integer key = Integer.parseInt(attr[0]);
			if(!metrics.containsKey(key)) {
				metrics.put(Integer.parseInt(attr[0]), new Hashtable<String, String>());
				
				String name = "";
				for(int i=1; i<attr.length-3; i++)
					name += attr[i]+",";
				metrics.get(key).put("name", name.substring(0, name.length()-1));
				metrics.get(key).put("father", attr[attr.length-1]);
			}
			if("Class".equals(type) && Arrays.asList(DatasetRow.CLASS_METRICS).contains(attr[2])) {
				metrics.get(key).put(attr[2], attr[3]);
			}
			if("Method".equals(type) && Arrays.asList(DatasetRow.METHOD_METRICS).contains(attr[attr.length-3])) {
				metrics.get(key).put(attr[attr.length-3], attr[attr.length-2]);
			}
			if("Package".equals(type) && Arrays.asList(DatasetRow.PACKAGE_METRICS).contains(attr[2])) {
				metrics.get(key).put(attr[2], attr[3]);
			}
		}
		return metrics;
	}
	
	public DatasetRow[] getRows() {
		return this.rows;
	}
}
