package it.unimib.disco.essere.WekaNose.dfcm4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.unimib.disco.essere.WekaNose.exception.WorkspaceException;

/**
 * Allow to handle and get access to the workspace
 * */
public class WorkspaceHandler{
	
	/** The path of the workspace */
	private String path;
	private ArrayList<String> projectsCreated;
	private ArrayList<String> resultsQuery;

	/** Initialize the workspace
	 * @param path the path were the workspace will be created
	 * @throws WorkspaceException
	 * */
	public WorkspaceHandler(String path) throws WorkspaceException{
		this.path = path;
		generateNewFolder(path, true);
		projectsCreated = new ArrayList<String>();
		resultsQuery = new ArrayList<String>();
	}

	/** Create a new folder in the workspace
	 * @param sourceName the name of the new folder
	 * @throws WorkspaceException 
	 * */
	public String addSource(String sourceName) throws WorkspaceException {
		generateNewFolder(path + "/" + sourceName, false);
		projectsCreated.add(sourceName);
		return path + "/" + sourceName;
	}
	
	private void generateNewFolder(String path, boolean isExperiment) throws WorkspaceException {
		boolean success = false;
		int count = 0;
		while(!success) {
			if(count == 0)
				success = (new File(path)).mkdirs();
			else {
				if(isExperiment)
					this.path = path + "_" + count;
				success = (new File(path + "_" + count)).mkdirs();
			}
			count++;
		}
	}
	
	public void removeSource(String sourceName) throws WorkspaceException {
		File currentFile = new File(path + "/" + sourceName);
		System.out.println(currentFile);
	    currentFile.delete();
		projectsCreated.remove(sourceName);
	}
	
	public List<String> databasesIterator(){
		ArrayList<String> dbs = new ArrayList<String>();
		for(String name: this.projectsCreated) {
			dbs.add(this.path + "/" + name + "/" + name + ".SQLite");
		}
		return dbs;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public ArrayList<String> getProjectsCreated() {
		return this.projectsCreated;
	}
	
	public ArrayList<String> getResultsQuery() {
		return resultsQuery;
	}

	public void addResultsQuery(String result) {
		this.resultsQuery.add(result);
	}
}
