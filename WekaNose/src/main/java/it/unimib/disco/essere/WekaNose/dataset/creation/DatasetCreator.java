package it.unimib.disco.essere.WekaNose.dataset.creation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.unimib.disco.essere.WekaNose.exceptions.DFCM4JException;
import it.unimib.disco.essere.WekaNose.exceptions.WorkspaceException;
import it.unimib.disco.essere.WekaNose.utils.CustomLogger;
import it.unimib.disco.essere.sandbox.MetricDetector;

/**
 * Contain the workflow for generate the dataset 
 */
public class DatasetCreator {

	/** the attribute through which the class can access the workspace */
	private WorkspaceHandler workspace; 
	
	private boolean isMethodLevel;
	
	/** The number of the dataset instances */
	private int numRows;
	
	/** The name of the experimentation */
	private String experimentName;

	/**
	 * Initialize the experiment by setting the workspace
	 * @param experimentName the name of the experiment
	 * @throws WorkspaceException 
	 */
	public DatasetCreator(String experimentName, boolean isMethodLevel, int numRows) throws WorkspaceException{
		this.isMethodLevel = isMethodLevel;
		this.numRows = numRows;
		this.experimentName = experimentName;

		String path = new java.io.File("").getAbsolutePath();
		
		if(System.getProperty("os.name").toLowerCase().contains("win")
				&& path.contains("\\WekaNose\\WekaNose")){
			workspace = new WorkspaceHandler(
					path.substring(0, path.lastIndexOf('\\'))
					+ "\\result\\"
					+ experimentName);
		}
		else if(path.contains("/WekaNose/WekaNose")){
			workspace = new WorkspaceHandler(
					path.substring(0, path.lastIndexOf('/'))
					+ "/result/"
					+ experimentName);
		}
		else{
			workspace = new WorkspaceHandler(
					path
					+ "/result/"
					+ experimentName);
		}
	}

	/** Use the JCodeOdor jar to generate the SQLite file 
	 * @param name the name of the new project/source code
	 * @param args the option available for JCodeOdor (http://essere.disco.unimib.it/wiki/jcodeodor_doc)
	 * @throws DFCM4JException 
	 * @throws WorkspaceException 
	 * @return the path where the .SQLite file was saved 
	 * */
	public String genereateSQLite(String name, String args) throws DFCM4JException, WorkspaceException{
		String fullPath = "";
		fullPath = workspace.addSource(name) + "/" + name + ".SQLite";
		args += " -output " + fullPath;
		CustomLogger.getInstance().info("Loading " + name + "...", this);
		try {
			MetricDetector.execute(args.split(" "));
		} catch (Exception e) {
			this.workspace.removeSource(name);
			throw new DFCM4JException(e); 
		} 
		CustomLogger.getInstance().info(name + " loaded...", this);
		return fullPath;
	}

	/**
	 * Extract the information from the SQLite DB and print them on files
	 * */
	public void performQueries(List<Advisor> advisor) 
			throws ClassNotFoundException, FileNotFoundException, UnsupportedEncodingException, 
			SQLException, WorkspaceException {

		for(String system: workspace.getProjectsCreated()) {
			DatabaseHandler dh = new DatabaseHandler(workspace, 
												 	 isMethodLevel,
													 advisor);
		}
	}

	/**
	 * Create the dataset based on the information extracted through the queries on the DB
	 * */
	public void generateDataset(String className) throws WorkspaceException, IOException {
		GenerateRows generator = new GenerateRows(workspace, numRows, isMethodLevel);

		CustomLogger.getInstance().info("Creating dataset...", this);
		DatasetRow[] rows = generator.getRows();
		File file = new File(this.workspace.getPath() + "/dataset.csv");
		FileWriter writer = new FileWriter(file, false);
		writer.write(DatasetRow.getHeader(className, isMethodLevel) + "\n");
		for(DatasetRow row: rows) 
			writer.write(row.toString() + "\n");

		writer.close();
		CustomLogger.getInstance().info("Dataset created and save in: " + workspace.getPath(), this);

	}

	public boolean isMethodLevel() {
		return isMethodLevel;
	}

	public String getName() {
		return this.experimentName;
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
			Advisor c = new Advisor("NOP", "method", "<=", 4);
			Advisor c1 = new Advisor("NOP", "method", "between", 5, 7);
			ArrayList<Advisor> a = new ArrayList<Advisor>(); 
			a.add(c);
			a.add(c1);
			dc.performQueries(a);
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
