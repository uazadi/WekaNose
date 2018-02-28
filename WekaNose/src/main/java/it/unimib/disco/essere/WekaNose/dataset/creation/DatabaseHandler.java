package it.unimib.disco.essere.WekaNose.dataset.creation;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import it.unimib.disco.essere.WekaNose.utils.CustomLogger;

public class DatabaseHandler {

	private String dbPath;
	private WorkspaceHandler workspace; 

	
	/**
	 * Once the database is created all the useful information are immediately extracted by the SQLite DB 
	 * previously created. These information are: metrics at package and class level, metrics at method level (only
	 * if the code smell is at method level) and the instances (classes or methods) that respect the Advisors' rules.
	 *  
	 * */
	public DatabaseHandler(WorkspaceHandler workspace, boolean isMethodLevel, List<Advisor> advisors) 
			throws ClassNotFoundException, SQLException, FileNotFoundException, UnsupportedEncodingException {

		Connection c = null;
		Statement stmt = null;
		
		this.workspace = workspace;

		for(String dbPath: workspace.databasesIterator()) {
			
			this.dbPath = dbPath;
			
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
			c.setAutoCommit(false);

			CustomLogger.getInstance().info("Opened database successfully...", this);
			
			stmt = c.createStatement();
			this.queryClass(stmt);
			if(isMethodLevel)
				this.queryMethod(stmt);
			this.queryPackage(stmt);
		
			for(Advisor advisor: advisors) {
				this.queryAdvisor(stmt, advisor);
			}
		
			stmt.close();
			c.close();
		}

		CustomLogger.getInstance().info("Query performed successfully...", this);
	}

	/**
	 * Perform a query that extract and print in a file called Method.csv the metrics at method level 
	 * */
	public void queryMethod(Statement stmt) throws SQLException, FileNotFoundException, UnsupportedEncodingException {
		ResultSet rs = stmt.executeQuery(
				"Select Measurables.id,  Measurables.name, Measures.key, Measures.value, "
						+ "Measurables.parent " + 
						"From Measurables, Measures " + 
						"Where Measures.measurable = Measurables.id AND Measures.type = \"metric\" "
						+ "AND Measurables.type = \"method\" " + 
				"Order by Measures.id");
		this.printResultQuery(rs, "Method");
		rs.close();
	}
	
	/**
	 * Perform a query that extract and print in a file called Class.csv the metrics at class level 
	 * */
	public void queryClass(Statement stmt) throws SQLException, FileNotFoundException, UnsupportedEncodingException {
		ResultSet rs = stmt.executeQuery(
				"Select Measurables.id,  Measurables.name, Measures.key, Measures.value, "
						+ "Measurables.parent " + 
				"From Measurables, Measures " + 
				"Where Measures.measurable = Measurables.id AND Measures.type = \"metric\" "
						+ "AND Measurables.type <> \"package\" AND Measurables.type <> \"method\" "
						+ "AND Measurables.type <> \"project\" " + 
				"Order by Measures.id");
		this.printResultQuery(rs, "Class");
		rs.close();
	}

	/**
	 * Perform a query that extract and print in a file called Package.csv the metrics at package level 
	 * */
	public void queryPackage(Statement stmt) throws SQLException, FileNotFoundException, UnsupportedEncodingException {
		ResultSet rs = stmt.executeQuery(
				"Select Measurables.id,  Measurables.name, Measures.key, Measures.value, "
						+ "Measurables.parent " + 
				"From Measurables, Measures " + 
				"Where Measures.measurable = Measurables.id AND Measures.type = \"metric\" "
						+ "AND Measurables.type = \"package\" " + 
				"Order by Measures.id ");
		this.printResultQuery(rs, "Package");
		rs.close();
	}

	
	/**
	 * Perform a query that extract and print in a file the id, the name, the value and the id of the parent 
	 * of all the instances (classes or methods) that respect a specific Advisor.
	 * (where the class is the parent of the methods, the package is the parent of the classes)
	 * */
	public void queryAdvisor(Statement stmt, Advisor advisor) throws SQLException, FileNotFoundException, UnsupportedEncodingException {

		ResultSet rs = null;
		String metric = advisor.getMetric();
		
		// The "class" value for the "type" column doesn't work, so it is implemented as not package, not method and not project.
		String type = (advisor.getLevel().equals("class")) ? "Measurables.type <> \"package\" AND Measurables.type <> \"method\" "
															  + "AND Measurables.type <> \"project\"" 
														   : "Measurables.type = \"" + advisor.getLevel() + "\"";

		if(advisor.getSymbol().equals("between"))
			rs = stmt.executeQuery(
					"Select Measurables.id, Measurables.name, Measures.value, Measurables.parent " + 
					" From Measurables, Measures " + 
					"Where Measurables.id = Measures.measurable AND "+ type +" AND Measures.key = \""
							+ metric +"\" AND Measures.value >= " + advisor.getThreshold1() + " AND Measures.value <= " +advisor.getThreshold2());
		else
			rs = stmt.executeQuery(
					"Select Measurables.id, Measurables.name, Measures.value, Measurables.parent " + 
							" From Measurables, Measures " + 
							"Where Measurables.id = Measures.measurable AND "+ type +" AND Measures.key = \""
							+ metric +"\" AND Measures.value "+ advisor.getSymbol() +" "+ advisor.getThreshold1());
		
		

		this.printResultQuery(rs, advisor.toString());
		rs.close();
	} 

	/**
	 * Print the result of query in a file (CSV) in the current Workspace.
	 * 
	 * @param rs is the result of the SQL query 
	 * @param name is the name of the file that will be saved in the workspace (with extension .csv)
	 * 
	 */
	public void printResultQuery(ResultSet rs, String name) throws SQLException, FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(
				this.dbPath.substring(0, this.dbPath.lastIndexOf('/')) + "/" + name + ".csv", 
				"UTF-8");
		
		if(!("Class".equals(name) || "Method".equals(name) || "Package".equals(name)))
			this.workspace.addResultsQuery(name);
		while (rs.next()) {
			String row = "";
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
				row += rs.getString(i) + ", ";          
			}
			writer.println(row);

		}
		writer.close();
	}

	/** just for testing */
	public static void main(String[] args) {
		try {
			Advisor c = new Advisor("NOP", "method", "<=", 4);
			ArrayList<Advisor> a = new ArrayList<Advisor>(); 
			a.add(c);
			//DatabaseHandler dh = new DatabaseHandler("/home/umberto/Documents/WekaNose/result/Test/freemind/freemind.SQLite", true, a);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}


