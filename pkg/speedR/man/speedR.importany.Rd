\name{speedR.importany}
\alias{speedR.importany}
\title{Data Import Without Using GUI}
\description{
  The speedR generates in interactive mode for every import R code. This code can be reused later without starting the GUI.
}
\usage{
  speedR.importany(file=NULL,rowstart=NULL,rowend=NULL,colstart=NULL,colend=NULL,
						   hasRowNames = FALSE, rowNamesColumnIndex = NULL,hasColumnNames = FALSE, 
						   columnNamesRowIndex = NULL, separator = NULL, quote = NULL,colClasses = NULL,maxmemory = NULL, ...)
}

\arguments{
  \item{file}{the name of the file which the data are to be read from. Each row of the table appears as one line of the file. If it does not contain an absolute path, the file name is relative to the current working directory}  
  \item{rowstart}{the index of line of the data file to start to read data.}
  \item{rowend}{the index of the last line to read.}
  \item{colstart}{the index of the first column in the range}
  \item{colend}{the index of the last column in the range}
  \item{hasRowNames}{a logical value indicating wether the data.frame has a row names column}
  \item{rowNamesColumnIndex}{the index of the column containing the row names. Has an effect only if the hasRowNames is TRUE}
  \item{hasColumnNames}{a logical value indicating whether the data.frame object has column names}
  \item{columnNamesRowIndex}{the index of the row containing the column names. Has an effect only if the hasColumnNames is TRUE}
  \item{separator}{the field separator character.}
  \item{quote}{the set of quoting characters.}
  \item{colClasses}{A vector of classes to be assumed for the columns. Possible classes are numeric,character,factor,Date[=format],POSIXct[=format].
					The Date and POSIXct formats can be added but optional. I.e. ``Date=\%d.\%m.\%Y" or ``POSIXct=\%Y-\%m-\%d \%H:\%M".}
  \item{maxmemory}{the maximum allocatable memory in Mb. If speedR is already once started, 
				   calling speedR with a different value will have no effect. To change the memory, 
				   R must be restarted and speedR has to be called with the new value.}
  \item{...}{Other optional parameters sent to \link{speedR.init} like debugging and log level.}  
}

\details{
  speedR depends on rJava and iplots packages. To install these packages please call install.packages(c("rJava","iplots")).
  In case of an "OutOfMemoryError", please increase the memory by setting the maxmemory parameter to a higher value. Note that R must be restarted.
  Following examples can be used as an indicator for how much the speedR can approximately consume memory:
  
  512 Mb : 10 columns, 800000 rows
  1024 Mb: 20 columns, 800000 rows or 10 columns 1600000 rows
  
  Note that memory consume depends not only on the columns and rows number, but it depends on the content of each cell too (i.e. char count). 
}

\examples{
\dontrun{
  temp <- speedR.importany(file = "C:/decodersizes.txt",separator = "\t")
  temp <- speedR.importany(file = "C:/decodersizes.txt",separator = ",",quote = "'")
  temp <- speedR.importany(file = "C:/decodersizes.txt",separator = "\t",quote = "\"",rowstart = 3,rowend = 18313,colstart = 3,colend = 23,hasRowNames = TRUE,rowNamesColumnIndex = 2,hasColumnNames = TRUE,columnNamesRowIndex = 2)
  temp <- speedR.importany(file = "F:/date.txt",colClasses=c("numeric","factor","Date=%d.%m.%Y"))
  }
}
