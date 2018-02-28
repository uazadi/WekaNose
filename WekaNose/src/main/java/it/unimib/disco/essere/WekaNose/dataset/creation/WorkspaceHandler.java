package it.unimib.disco.essere.WekaNose.dataset.creation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.unimib.disco.essere.WekaNose.exceptions.WorkspaceException;

/**
 * Allow to handle and get access to the workspace
 * */
public class WorkspaceHandler{
	
	/** The path of the workspace */
	private String path;
	
	/** The names given to the system taken in consideration */
	private ArrayList<String> projectsCreated;
	
	/** The string representation of the specified Advisor */
	private ArrayList<String> resultsQuery;

	/** 
	 * Initialize the workspace
	 * @param path the path were the workspace will be created
	 * @throws WorkspaceException
	 * */
	public WorkspaceHandler(String path) throws WorkspaceException{
		this.path = path;
		generateNewFolder(path, true);
		projectsCreated = new ArrayList<String>();
		resultsQuery = new ArrayList<String>();
	}

	/** 
	 * Create a new folder in the workspace, that should contain the files concering the system called "sourceName" 
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
	
	/** 
	 * Delete the folder in the workspace called "sourceName"
	 * @param sourceName the name of the folder that has to be deleted
	 * @throws WorkspaceException 
	 * */
	public void removeSource(String sourceName) throws WorkspaceException {
		File currentFile = new File(path + "/" + sourceName);
		System.out.println(currentFile);
	    currentFile.delete();
		projectsCreated.remove(sourceName);
	}
	
	/**
	 * Return a list of string that represent the path to each SQLite DB 
	 * generated (one for each system selected)
	 * */
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

	/** Add the string representation of a new Advisor */
	public void addResultsQuery(String result) {
		this.resultsQuery.add(result);
	}
}
