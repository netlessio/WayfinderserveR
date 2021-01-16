rluc.preload <- function(luc.pathToLibs, luc.pathToClasses, workingDir) {
  
#luc.pathToLibs <- paste("/home/home/Dropbox/jlucrum/lib");
#luc.pathToClasses <- paste("/home/home/NetBeansProjects/JLucrum/build/classes");

Sys.setenv(TZ="GMT")

#Set working path
#setwd("~/Dropbox/jlucrum/rengine");
setwd(workingDir);

library(rJava);
library(tseries);
library(quantmod);
library(gtools)
require(ggplot2)
require(glmulti)
library(TTR)
library(MASS)
#library(fGarch)

source("~/Dropbox/jlucrum/rexperimental/external/itall.R")
source("~/Dropbox/jlucrum/rexperimental/tests/testCosModel.r")


#load all functions under functions directory
list.of.funfiles <- system("ls functions", intern=T);
for (file in list.of.funfiles) {
  file.path <- paste(getwd(), "/functions/", file, sep="")
  print(paste("Loading functions from: ", file.path))
  source(file.path)
}


.jinit(classpath=luc.pathToClasses, force.init=T)

#Load jars from directory
command <- paste("ls ", luc.path