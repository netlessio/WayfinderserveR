#Refrence: http://stackoverflow.com/questions/1358003/tricks-to-manage-the-available-memory-in-an-r-session

# improved list of objects
.ls.objects <- function (pos = 1, pattern, order.by,
                        decreasing=FALSE, head=FALSE, n=5) {
    napply <- function(names, fn) sapply(names, function(x)
                                         fn(get(x, pos = pos)))
 