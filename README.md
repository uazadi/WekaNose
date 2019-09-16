# WekaNose #
### Allows weka to smell your code  ###
<p align="center">
  <!-- <img src="http://essere.disco.unimib.it/wiki/_media/wekanose.png">) -->
  <img src="https://github.com/uazadi/WekaNose/blob/master/docs/pictures/WekaNode_github.png">
</p>

[![codebeat badge](https://codebeat.co/badges/23ad08d2-0214-4cca-a616-759c659aa3dc)](https://codebeat.co/projects/github-com-uazadi-wekanose-master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/024e7d4c94554e1f93ed8c33082ee360)](https://www.codacy.com/app/u.azadi/WekaNose?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=uazadi/WekaNose&amp;utm_campaign=Badge_Grade)

WekaNose is a tool that allows to perform an experiment, that aims to study code smell detection through machine learning techniques. The experiment's purpose is to select rules, or obtain trained algorithms, that can classify an instance (method or class) as affected or not by a code smell. These rules have the main advantage that they are extracted through an example-based approach, rather then a heuristic-based one.
This experiment is divided in two main part:  
* the first one concern the creation of the dataset 
* the second part where the machine learning algorithms are trained and tested using the dataset created in the first part.

#### Information ####
For every further information about this tool:      
* check out the [WekaNoseTutorial.pdf](https://github.com/UmbertoAzadi/WekaNose/blob/master/docs/WekaNoseTutorial.pdf)
* visit the [Website](http://www.essere.disco.unimib.it/wekanose/) 
* read the related paper published at [ICSE '18](https://www.icse2018.org/): [doi](https://doi.org/10.1145/3183440.3194974), [paper](https://www.researchgate.net/profile/Umberto_Azadi/publication/325568804_Machine_Learning_based_Code_Smell_Detection_through_WekaNose/links/5b1654a90f7e9bda0ffe74a5/Machine-Learning-based-Code-Smell-Detection-through-WekaNose.pdf?_sg%5B0%5D=Rz7ZZpKV3LX4YuCyBqTguOE7NxS1qNaYGtuqxVmM3dbI9m2nze17ex_flbRZD8V4CyoB-phAsQaTwFQtRpbaCg.eTdxkIp0KEHCXrrq01kk8QI_qsymeNNVA2V0uDwnecmm0RZnVCXAxzxQ1TBvvz8m6dxM34fhO6-OqSDm-z0b7A&_sg%5B1%5D=MQ4QDqEnkFeNfH10618KkDBJlesZtQgupnqFYhvpXwz6FmpuwJgVvu9OeOJ7NmilVvSKnPlQZ4daewKJwVbqF1Lk3f4LpWXzwZ6tkkm9av6v.eTdxkIp0KEHCXrrq01kk8QI_qsymeNNVA2V0uDwnecmm0RZnVCXAxzxQ1TBvvz8m6dxM34fhO6-OqSDm-z0b7A&_iepl=)
* check out the [presentation](https://www.slideshare.net/UmbertoAzadi/wekanose-presentation-123524644) (link to slideshare)

#### Problems ####
For report any problem please check if already exist an [Issue](https://github.com/UmbertoAzadi/WekaNose/issues) on GitHub about it:
* if there isn't one please add an Issue
* if there is one please leave a comment 

#### Tools used by this one ####
* [Weka](https://www.cs.waikato.ac.nz/ml/weka/)
* [JCodeOdor](http://www.essere.disco.unimib.it/jcodeodor/)
* [OUTLINE](https://github.com/UmbertoAzadi/OUTLINE)
