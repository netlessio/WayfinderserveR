jluc.testModel1 <- function(asset="TESTcompany",
                            fromDate=as.Date(Sys.Date()-150),
                            toDate=Sys.Date(), 
                            lag=1)
{
  
  start<-as.double(difftime(toDate, fromDate, unit="days"))
  
  new.data<-NULL
  for (i in start:0) {
    new.date <- toDate - i
    new.data <-rbind(new.data, xts(as.numeric(new.date) - 10000 , new.date))
  }
  
  #--------------------------------------------------------
  targetPrice<-cos(new.data) + 3;
  
  dailyReturn<-jluc.detrend(log(targetPrice), n=1)
