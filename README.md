# WekaNose #
### Allows weka to smell your code  ###
<p align="center">
  <img src="http://essere.disco.unimib.it/wiki/_media/wekanose.png">
</p>

WekaNose is a tool that allows to perform an experiment, that aims to study code smell detection through machine learning techniques. The experiment's purpose is to select rules, or obtain trained algorithms, that can classify an instance (method or class) as affected or not by a code smell. These rules have the main advantage that they are extracted through an example-based approach, rather then a heuristic-based one.
This experiment is divided in two main part:  
* the first one concern the creation of the dataset 
* the second part where the machine learning algorithms are trained and tested using the dataset created in the first part.

#### Information ####
For every further information about this tool:      
* check out the [WekaNoseTutorial.pdf](https://github.com/UmbertoAzadi/WekaNose/blob/master/docs/WekaNoseTutorial.pdf)
* visit the [Website](http://essere.disco.unimib.it/wiki/wekanose) 
* read the related paper published at ICSE '18: [doi](https://doi.org/10.1145/3183440.3194974), [paper](http://essere.disco.unimib.it/wiki/_media/publications/machine_learning_based_code_smell_detection_through_wekanose.pdf)

#### Problems ####
For report any problem please check if already exist an [Issue](https://github.com/UmbertoAzadi/WekaNose/issues) on GitHub about it:
* if there isn't one please add an Issue
* if there is one please leave a comment 

#### Tool used by this one ####
* [Weka](https://www.cs.waikato.ac.nz/ml/weka/)
* [JCodeOdor](http://essere.disco.unimib.it/wiki/jcodeodor)
* [OUTLINE](https://github.com/UmbertoAzadi/OUTLINE)
