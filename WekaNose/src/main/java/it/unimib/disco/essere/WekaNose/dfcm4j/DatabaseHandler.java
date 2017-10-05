package it.unimib.disco.essere.WekaNose.dfcm4j;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


//
//
// RICORDARSI DI AGGIUGENRE IL PREPARE STATEMENT
//
//
public class DatabaseHandler {

	private String dbPath;
	private WorkspaceHandler workspace; 

	public DatabaseHandler(WorkspaceHandler workspace, boolean isMethodLevel, List<Condition> advisors) 
			throws ClassNotFoundException, SQLException, FileNotFoundException, UnsupportedEncodingException {

		Connection c = null;
		Statement stmt = null;
		
		this.workspace = workspace;

		for(String dbPath: workspace.databasesIterator()) {
			
			this.dbPath = dbPath;
			
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
			c.setAutoCommit(false);

			DatasetLogger.getInstance().info("Opened database successfully...", this);

			stmt = c.createStatement();
			this.queryClass(stmt);
			if(isMethodLevel)
				this.queryMethod(stmt);
			this.queryPackage(stmt);
		
			for(Condition advisor: advisors) {
				this.queryAdvisor(stmt, advisor);
			}
		
			stmt.close();
			c.close();
		}

		DatasetLogger.getInstance().info("Query performed successfully...", this);
	}

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

	public void queryAdvisor(Statement stmt, Condition advisor) throws SQLException, FileNotFoundException, UnsupportedEncodingException {

		ResultSet rs = null;
		String metric = advisor.getMetric();
		String type = (advisor.getLevel().equals("class")) ? "Measurables.type <> \"package\" AND Measurables.type <> \"method\" "
															  + "AND Measurables.type <> \"project\"" 
														   : "Measurables.type = \"" + advisor.getLevel() + "\"";

		if(advisor.getSymbol().equals("between"))
			rs = stmt.executeQuery(
					"Select Measurables.id, Measurables.name, Measures.value, Measurables.parent " + 
					" From Measurables, Measures " + 
					"Where Measurables.id = Measures.measurable AND "+ type +" AND Measures.key = \""
							+ metric +"\" AND Measures.value >= " + advisor.getNum1() + " AND Measures.value <= " +advisor.getNum2());
		else
			rs = stmt.executeQuery(
					"Select Measurables.id, Measurables.name, Measures.value, Measurables.parent " + 
							" From Measurables, Measures " + 
							"Where Measurables.id = Measures.measurable AND "+ type +" AND Measures.key = \""
							+ metric +"\" AND Measures.value "+ advisor.getSymbol() +" "+ advisor.getNum1());
		
		

		this.printResultQuery(rs, advisor.toString());
		rs.close();
	}

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

	public static void main(String[] args) {
		try {
			Condition c = new Condition("NOP", "method", "<=", 4);
			ArrayList<Condition> a = new ArrayList<Condition>(); 
			a.add(c);
			//DatabaseHandler dh = new DatabaseHandler("/home/umberto/Documents/WekaNose/result/Test/freemind/freemind.SQLite", true, a);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}


