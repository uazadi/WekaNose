package it.unimib.disco.essere.WekaNose.dfcm4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.unimib.disco.essere.WekaNose.exception.DFCM4JException;
import it.unimib.disco.essere.WekaNose.exception.WorkspaceException;
import it.unimib.disco.essere.sandbox.MetricDetector;

/**
 * Contain the workflow for generate the dataset 
 */
public class DatasetCreator {
	
	/** the attribute through which the class can access the workspace */
	private WorkspaceHandler workspace; 
	private boolean isMethodLevel;
	private int numRows;
	private String name;

	/**
	 * Initialize the experiment
	 * @param experimentName the name of the experiment
	 * @throws WorkspaceException 
	 */
	public DatasetCreator(String experimentName, boolean isMethodLevel, int numRows) throws WorkspaceException{
		this.isMethodLevel = isMethodLevel;
		this.numRows = numRows;
		this.name = experimentName;

		String path = new java.io.File("").getAbsolutePath();
		if(path.contains("/WekaNose/WekaNose")){
			workspace = new WorkspaceHandler(
					path.substring(0, path.lastIndexOf('/'))
					+ "/result/"
					+ experimentName);
		}else{
			workspace = new WorkspaceHandler(
					path
					+ "/result/"
					+ experimentName);
		}

	}

	/** Use the JCodeOdor jar to generate the SQLite file 
	 * @param name the name of the new project/source code
	 * @param args the option available for JCodeOdor (http://essere.disco.unimib.it/wiki/jcodeodor_doc)
	 * @throws DFCM4JException, WorkspaceException 
	 * @return the path where the .SQLite file was saved
	 * @throws InterruptedException 
	 * */
	public String genereateSQLite(String name, String args) throws DFCM4JException, WorkspaceException{
		String fullPath = "";
		fullPath = workspace.addSource(name) + "/" + name + ".SQLite";
		args += " -output " + fullPath;
		DatasetLogger.getInstance().info("Loading " + name + "...", this);
		try {
			MetricDetector.execute(args.split(" "));
		} catch (Exception e) {
			this.workspace.removeSource(name);
			throw new DFCM4JException(e); 
		} 
		DatasetLogger.getInstance().info(name + " loaded...", this);
		return fullPath;
	}

	public void performQuery(List<Condition> advisor) 
			throws ClassNotFoundException, FileNotFoundException, UnsupportedEncodingException, 
			SQLException, WorkspaceException {

		for(String name: workspace.getProjectsCreated()) {
			DatabaseHandler dh = new DatabaseHandler(workspace, 
					isMethodLevel,
					advisor);
		}
	}

	public void generateDataset(String className) throws WorkspaceException, IOException {
		GenerateRows generator = new GenerateRows(workspace, numRows, isMethodLevel);
		
		DatasetLogger.getInstance().info("Creating dataset...", this);
		DatasetRow[] rows = generator.getRows();
		File file = new File(this.workspace.getPath() + "/dataset.csv");
		FileWriter writer = new FileWriter(file, false);
		writer.write(DatasetRow.getHeader(className, isMethodLevel) + "\n");
		for(DatasetRow row: rows) 
			writer.write(row.toString() + "\n");

		writer.close();
		DatasetLogger.getInstance().info("Dataset created and save in: " + workspace.getPath(), this);

	}
	
	public boolean isMethodLevel() {
		return isMethodLevel;
	}

	public String getName() {
		return this.name;
	}
	
	public WorkspaceHandler getWorkspace() {
		return this.workspace;
	}

	/** just for test */
	public static void main(String[] args) {
		try {


			//			METHOD TEST
			DatasetCreator dc = new DatasetCreator(args[0], true, 10);
			dc.genereateSQLite("freemind", args[1].replaceAll(",", " "));
			Condition c = new Condition("NOP", "method", "<=", 4);
			Condition c1 = new Condition("NOP", "method", "between", 5, 7);
			ArrayList<Condition> a = new ArrayList<Condition>(); 
			a.add(c);
			a.add(c1);
			dc.performQuery(a);
			dc.generateDataset("className");

//			CLASS TEST
//			DatasetCreator dc = new DatasetCreator(args[0], false, 10);
//			dc.genereateSQLite("freemind", args[1].replaceAll(",", " "));
//			Condition c = new Condition("LOC", "class", "<=", "200");
//			Condition c1 = new Condition("LOC", "class", "between", "250", "500");
//			ArrayList<Condition> a = new ArrayList<Condition>(); 
//			a.add(c);
//			a.add(c1);
//			dc.performQuery(a);
//			dc.generateDataset("is_long_class");

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
