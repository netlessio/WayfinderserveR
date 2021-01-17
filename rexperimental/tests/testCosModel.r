jluc.testModel1 <- function(asset="TESTcompany",
                            fromDate=as.Date(Sys.Date()-150),
                            toDate=Sys.Date(), 
                            lag=1)
{
  
  start<-as.double(difftime(toDate, fromDate, unit="days"))
  
  new.data<-NULL
  for (i in start:0) {
    new.dat