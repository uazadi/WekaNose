package it.unimib.disco.essere.WekaNose.MachineLearning;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import it.unimib.disco.essere.load.LoaderProperties;
import it.unimib.disco.essere.WekaNose.utils.CustomLogger;
import it.unimib.disco.essere.core.InputParser;

public class MLAHandler {

	private String path;
	private String pathProperties;
	private String result_path;


	public MLAHandler() {}

	/**
	 * Initialize the Machine Learning Approach and load the configuration file if specified 
	 * (https://github.com/UmbertoAzadi/OUTLINE)
	 * */
	public MLAHandler(String pathProperties) throws Exception {
		this.pathProperties = pathProperties;
		LoaderProperties p = new LoaderProperties();
		p.loadForClassification(pathProperties);
		this.result_path = p.getPathForResult();
		CustomLogger.getInstance().info("Properties file found...", this);
	}

	/**
	 * Generate the training configuration file (https://github.com/UmbertoAzadi/OUTLINE)
	 * */
	public void generateProperties(String datasetPath, List<String> classifiers) throws Exception {
		try {
			if(System.getProperty("os.name").toLowerCase().contains("win"))
				path = datasetPath.substring(0, datasetPath.lastIndexOf('\\')).replace("\\", "/");
			else
				path = datasetPath.substring(0, datasetPath.lastIndexOf('/'));
			this.pathProperties = path + "/dataset.properties";
			CustomLogger.getInstance().info("Creating properties file...", this);
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
			CustomLogger.getInstance().info("Properties file created...", this);
		}catch(Exception e) {
			throw e;
		}
	}

	/**
	 * This method preform a WEKA Experiment (https://weka.wikispaces.com/Using+the+Experiment+API) in order
	 * to provide a ranking of the Machine Learning Algorithms.
	 * It also save the human-readable results (when they are provided by the algorithms)
	 * and serialize the algorithms if the boolean variable "serialize" is true.
	 * 
	 * */
	public void runExperiment(String exptype, String splittype, int run, int fold, boolean serialize) throws Exception {


		CustomLogger.getInstance().info("Start the experiment...", this);
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
			CustomLogger.getInstance().info("Experiment terminated, the results were saved in: " + this.result_path, this);
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
