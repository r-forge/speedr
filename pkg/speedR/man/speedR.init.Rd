\name{speedR.init}
\alias{speedR.init}
\title{Initialize speedR (classpath, heap size etc..)}
\description{
  Initializes speedR. Sets the classpath, heap size, log level etc. User does not need to call this function. It will be called from \link{speedR}
  and \link{speedR.importany}.
}
\usage{
  speedR.init(maxmemory = NULL,loglevel = "WARNING",debug = FALSE)
}

\arguments{
  \item{maxmemory}{the maximum allocatable memory in Mb. If speedR is already once started, 
				   calling speedR with a different value will have no effect. To change the memory, 
				   R must be restarted and speedR has to be called with the new value.}
	\item{loglevel}{Only for developer. Sets the granularity of the logging. Levels are ALL, SEVERE, WARNING, INFO, FINE, FINER, OFF}
	\item{debug}{Only for developer. A logical value indicating wether debuggin should be enabled}
}

\examples{
\dontrun{
  speedR.init(maxmemory = 1024, loglevel="FINE", debug = FALSE)
  }
}
