
#Refrence: source(url("http://www.stat.pitt.edu/stoffer/tsa2/Rcode/itall.R"))
lag.plot1=function(series,max.lag=1,corr=TRUE,smooth=FALSE){ 
   name1=paste(deparse(substitute(series)),"(t-",sep="")
   name2=paste(deparse(substitute(series)),"(t)",sep="")
   data1=as.ts(series)
   max.lag=as.integer(max.lag)
   prow=ceiling(sqrt(max.lag))
   pcol=ceiling(max.lag/prow)
   a=acf(series,max.lag,plot=FALSE)$acf[-1]
   old.par <- par(no.readonly = TRUE)
   par(mfrow=c(prow,pcol), mar=c(2.5, 4, 2.5, 1), cex.main=1.1, font.main=1)
  for(h in 1:max.lag){                       
   plot(lag(series,-h), data1, xy.labels=FALSE, main=paste(name1,h,")",sep=""), ylab=name2, xlab="") 
    if (smooth==TRUE) 
    lines(lowess(ts.intersect(lag(series,-h),series)[,1],
                 ts.intersect(lag(series,-h),series)[,2]), col="red")
    if (corr==TRUE)
    legend("topright", legend=round(a[h], digits=2), text.col ="blue", bg="white", x.intersp=0)
   on.exit(par(old.par))
   }
}

lag.plot2=function(series1,series2,max.lag=0,corr=TRUE,smooth=FALSE){ 
   name1=paste(deparse(substitute(series1)),"(t-",sep="")
   name2=paste(deparse(substitute(series2)),"(t)",sep="")
   data1=as.ts(series1)
   data2=as.ts(series2)
   max.lag=as.integer(max.lag)