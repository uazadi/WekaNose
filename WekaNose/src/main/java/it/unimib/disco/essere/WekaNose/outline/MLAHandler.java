package it.unimib.disco.essere.WekaNose.outline;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import it.unimib.disco.essere.WekaNose.dfcm4j.DatasetLogger;
import it.unimib.disco.essere.load.LoaderProperties;
import it.unimib.disco.essere.core.InputParser;

public class MLAHandler {

	private String path;
	private String pathProperties;
	private String result_path;


	public MLAHandler() {}

	public MLAHandler(String pathProperties) throws Exception {
		this.pathProperties = pathProperties;
		LoaderProperties p = new LoaderProperties();
		p.loadForClassification(pathProperties);
		this.result_path = p.getPathForResult();
		DatasetLogger.getInstance().info("Properties file found...", this);
	}

	public void generateProperties(String datasetPath, List<String> classifiers) throws Exception {
		try {
			if(System.getProperty("os.name").toLowerCase().contains("win"))
				path = datasetPath.substring(0, datasetPath.lastIndexOf('\\')).replace("\\", "/");
			else
				path = datasetPath.substring(0, datasetPath.lastIndexOf('/'));
			this.pathProperties = path + "/dataset.properties";
			DatasetLogger.getInstance().info("Creating properties file...", this);
			File file = new File(this.pathProperties);
			FileWriter writer = new FileWriter(file, false);
			writer.write("dataset = " + datasetPath.replace("\\", "/") + "\n");

			result_path = this.generateNewFolder(path + "/classification_result");
			writer.write("path = " + result_path + "\n");
			int i=0;
			for(String classifier: classifiers) {
				String name = classifier.substring("weka.classifiers.".length(), 
						classifier.indexOf(" ")) + "_" + (++i);
				writer.write(name + " = " + classifier + "\n");
			}

			writer.close();
			DatasetLogger.getInstance().info("Properties file created...", this);
		}catch(Exception e) {
			throw e;
		}
	}

	public void runExperiment(String exptype, String splittype, int run, int fold, boolean serialize) throws Exception {


		DatasetLogger.getInstance().info("Start the experiment...", this);
		String input = "-save -wekaExp "
				+ "-exptype " + exptype
				+ " -splittype " + splittype
				+ " -runs " + run
				+ " -folds " + fold
				+ " " + pathProperties;

		InputParser outline = new InputParser();
		try {
			outline.start(input.split(" "));
			outline.start(new String[] {"-save", pathProperties});
			if(serialize)
				outline.start(new String[] {"-ser", pathProperties});
			DatasetLogger.getInstance().info("Experiment terminated, the results were saved in: " + this.result_path, this);
		} catch (Exception e) {
			throw e;
		}
	}

	private String generateNewFolder(String path) {
		boolean success = false;
		String _path = path;
		int count = 0;
		while(!success) {
			if(count == 0)
				success = (new File(path)).mkdirs();
			else {
				_path = path + "_" + count;
				success = (new File(_path)).mkdirs();
			}
			count++;
		}
		return _path;
	}

	public String getResultsPath() {
		return this.result_path;
	}
}
