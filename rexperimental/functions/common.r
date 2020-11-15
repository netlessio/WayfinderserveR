#Converts Map to time series object
jluc.convMapToTs <- function(map) {
  
  simpleMap <- .jstrVal(map)
  simpleMap2<-gsub("^\\{", "", simpleMap)
  simpleMap3<-gsub("}$", "", simpleMap2)
  dayAndValue<-unlist(strsplit(simpleMap3, split=","))
  retTimeSeries<-NULL
  for (i in dayAndValue) {
    line<-unlist(strsplit(i,spli