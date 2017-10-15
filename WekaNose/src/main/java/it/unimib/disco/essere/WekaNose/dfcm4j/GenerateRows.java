package it.unimib.disco.essere.WekaNose.dfcm4j;

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

import it.unimib.disco.essere.WekaNose.exception.WorkspaceException;

public class GenerateRows {
	
	private WorkspaceHandler workspace;
	private Hashtable<String, List<String>> projects = new Hashtable<String, List<String>>();
	private Hashtable<String, List<String>> chosenInstances = new Hashtable<String, List<String>>();
	private DatasetRow[] rows;
	
	public GenerateRows(WorkspaceHandler workspace, int numRows, boolean isMethodLevel) throws WorkspaceException{
		this.workspace = workspace;
		rows = new DatasetRow[numRows];
		
		for(String group: this.workspace.getResultsQuery()) {
			for(String project : this.workspace.getProjectsCreated()) {
				try {
					loadProject(project + "/" + group, this.workspace.getPath() + "/" + project + "/" + group + ".csv");
				} catch (Exception e) {
					throw new WorkspaceException("Result query's file not found");
				}
			}
		}
		
		DatasetLogger.getInstance().info("Selectiong the chosen instances", this);
		sampleInstaces(numRows);

		DatasetLogger.getInstance().info("Printing the chosen instances", this);
		try {
			this.printChosenInstace();
		} catch (Exception e) {
			throw new WorkspaceException("unable to create the chosen_instaces.csv file");
		}	
		
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkspaceException("unable to generate the rows");
		}
	}

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
							DatasetLogger.getInstance().warning("The chosen dataset size is greater then the avaible instances", this);
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
	
	public void loadProject(String key, String path) throws IOException {
		List<String> list = Files.readAllLines(new File(path).toPath(), Charset.defaultCharset() );
		projects.put(key, list);
		chosenInstances.put(key, new ArrayList<String>());
	}
	
	public boolean selectRandom(String key) {
		List<String> instances = projects.get(key);
		if(!instances.isEmpty()) {
			Random random = new Random();
			chosenInstances.get(key).add(instances.remove(random.nextInt(instances.size())));
			return true;
		}
		return false;
	}
	
	public void printChosenInstace() throws IOException {
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
