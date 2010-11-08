\name{speedR}
\alias{speedR}
\title{speedR: An R-package for interactive data import, filtering and complex ready-to-use code generation}
\description{
  Starts the speedR GUI window.
}
\usage{
  speedR(maxmemory=NULL,...)
}

\arguments{
  \item{maxmemory}{the maximum allocatable memory in Mb. If speedR is already once started, 
				   calling speedR with a different value will have no effect. To change the memory, 
				   R must be restarted and speedR has to be called with the new value.}
  \item{...}{Other optional parameters sent to \link{speedR.init} like debugging and log level.}
}

\details{
  speedR depends on rJava and iplots packages. To install these packages please call install.packages(c("rJava","iplots")).
  In case of an "OutOfMemoryError", please increase the memory by setting the maxmemory parameter to a higher value. Note that R must be restarted.
  Following examples can be used as an indicator for how much the speedR can approximately consume memory:
  
  512 Mb : 10 columns, 750000 rows
  1024 Mb: 20 columns, 750000 rows or 10 columns 1500000 rows
  
  Note that memory consume depends not only on the columns and rows number, but it depends on the content of each cell too (i.e. char count).
}

\examples{
\dontrun{
  speedR() #calls speedR with default memory value: on Windows 70% of the available free memory, on Unix like systems 512 Mb
  speedR(maxmemory = 1024) # start speedR with 1024 Mb memory
  }
}
