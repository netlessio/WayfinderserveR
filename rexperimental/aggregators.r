
agrdayOfWeek<-function(target) {
  target<-jluc.fetch("Metso Oyj", from=Sys.Date()-250, type="close");
  dtarget<-diff(target)
  dates <- as.POSIXct(index(dtarget), format = "%Y-%m-%d")
  dayLookup <- 1:7