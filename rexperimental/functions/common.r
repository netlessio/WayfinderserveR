#Converts Map to time series object
jluc.convMapToTs <- function(map) {
  
  simpleMap <- .jstrVal(map)
  simpleMap2<-gsub("^\\{", "", simpleMap)
  simpleMap3<-gsub("}$", "", simpleMap2)
  dayAndValue<-unlist(strsplit(simpleMap3, split=","))
  retTimeSeries<-NULL
  for (i in dayAndValue) {
    line<-unlist(strsplit(i,split="="))
    newvalue<-xts(as.double(line[2]), as.Date(line[1]))
    retTimeSeries<-rbind(retTimeSeries, newvalue)
  }
  
  return(retTimeSeries)

#  data <- data.frame(row.names=c("date", "value"))

#  entrySet <- map$entrySet()
#  iter<-entrySet$iterator()
#  while(.jsimplify(iter$hasNext())) {
#    nextEntry <- iter$"next"()
#    dayData<-data.frame(date=as.Date(nextEntry$getKey()), 
#                     value=nextEntry$getValue());
#    data <- rbind(data,dayData);
#  }
#  tsdata <- xts(data[,2], data[,1])
#  return(tsdata)
}


#wrapper for quantmod with jlucrum
jluc.fetch <- function(name, from=as.Date(Sys.Date()-252), to=Sys.Date(), src="jlucrum", type="close")
{
  if (!is.null(src) && src == "jlucrum") {
      tmpData <- fetcher$fetchPeriodData(name, format(from), format(to), type)
      stockValue<-jluc.convMapToTs(tmpData)
    } else {
      if (!is.null(src)) {
        newdata <- getSymbols(name, from=format(from), to=format(to), src=src)  
      } else {
        newdata <- getSymbols(name, from=format(from), to=format(to))
      }
      #stockData <- Cl(get(name))
      stockValue <- get(newdata)
    }

  if (length(stockValue) != 0) {
    colnames(stockValue) <- c(name)
  }

  return(stockValue)
}

# Volatility
# http://en.wikipedia.org/wiki/Volatility_(finance)
jluc.volatility<-function(data, period=-1, norm=F) {
  if (period > 0) {
    len <- period
  } else {
    len <- length(data)
  }
  
  if (norm) {
    volatility <- sd(na.omit(diff(log(data))))*sqrt(1/len)
  }else {
    volatility <- sd(data)*sqrt(1/len)  
  }
  
  return(volatility)
}


# no unit-root -> stationary process
# contains a unit-root -> non-stationary

jluc.detrend <- function(data, n=5, plot=F, name=NULL) {
  # shapiro.qqnorm
  if (n == 0) {
    final <- na.omit(data)
  } else if ( n==1 ) {
    final <- na.omit(diff(data))
  }else {
    sma <- SMA(na.omit(data[!is.infinite(data)]), n)
    merged<-merge(data, sma)
    names(merged) <- c("data", "sma")
    final <- na.omit(merged$data-merged$sma)
  }
  
  if (plot) {
    qqnorm(final)
    qqline(final)
    normTest<-shapiro.test(as.double(final))
    wvalue=paste("W",round(normTest$statistic, digits=4), sep="=")
    pvalue=paste("p",round(normTest$p.value, digits=4), sep="=")
    shapiroMesg=paste(wvalue, pvalue, sep=" ")
    
    stationaryTest<-SuppressWarnings(adf.test(as.double(final)))
    dvalue=paste("DF",round(stationaryTest$statistic, digits=4), sep="=")
    p2value=paste("p",round(stationaryTest$p.value, digits=4), sep="=")