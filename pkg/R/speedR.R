.packageName <- "speedR"
.speedR_Env <- environment()

# library initialization:
.First.lib <- function(lib, pkg) {     
   assign(".speedRInitialized", FALSE, .speedR_Env)   
}

speedR.init<-function(maxmemory = NULL,loglevel = "WARNING",debug = FALSE){	
		
	if(!get(".speedRInitialized",.speedR_Env)){
	
		if(is.null(maxmemory) && .Platform$OS.type == "windows"){
			availablememory <- memory.limit() - memory.size()
			Xmx <- round(availablememory*0.7)
		
			jvmparameters = paste("-Xmx",Xmx,"m",sep="");
			if(debug){
				jvmparameters = c(jvmparameters,"-agentlib:jdwp=transport=dt_socket,server=y,address=localhost:8001")
			}			
			options(java.parameters = jvmparameters)
		}else if(!is.null(maxmemory)){
			jvmparameters = paste("-Xmx",maxmemory,"m",sep="");
			options(java.parameters = jvmparameters)
		}
		require(iplots)
		
		jarfolder <- system.file("jri", package = "rJava")
		jars <- grep(".*\\.jar", list.files(jarfolder,full.names = TRUE), TRUE, value = TRUE)
		if (length(jars))
            .jaddClassPath(jars)

		.jpackage(name="speedR")		
		.jengine(start=TRUE)
		
		init = FALSE
		level = .jfield("java/util/logging/Level",,loglevel)
		logdir <- system.file("log", package = "speedR")
		
		
		tryCatch( init <- .jcall("at/ac/ait/speedr/SpeedRInit","Z",method="init",level), 
		Exception = function(e){
			e$jobj$printStackTrace()
		} 
		)
		
		if(init)
			assign(".speedRInitialized", TRUE, .speedR_Env)
		else
			stop("speedR could not be initialized")
	}
}

speedR <- function(maxmemory = NULL,...){
	speedR.init(maxmemory,...)
	
	tryCatch( .jcall("at/ac/ait/speedr/SpeedRFrame","V",method="main",.jarray(character(0))), 
		Exception = function(e){
			e$jobj$printStackTrace()
		} 
	) 
	#.jcall("at/ac/ait/speedr/SpeedRFrame","V",method="main",.jarray(character(0)))
	
	# .jcall("rif/firefree/NewMain","V",method="main",.jarray(character(0)) )
}

speedR.importany<-function(file=NULL,rowstart=NULL,rowend=NULL,colstart=NULL,colend=NULL,
						   hasRowNames = FALSE, rowNamesColumnIndex = NULL,hasColumnNames = FALSE, 
						   columnNamesRowIndex = NULL, separator = NULL, quote = NULL,maxmemory = 70, loglevel = "INFO",debug = FALSE){	
    
	
	speedR.init(maxmemory,loglevel,debug)
	
	
	if(is.null(file)) stop("Please give a valid file path argument")
	
	if(! file.exists(file)) stop("file do not exists")
	
	if(is.null(rowstart)) rowstart=.jnull("java/lang/Integer") else rowstart = .jnew("java/lang/Integer",as.character(rowstart))
	
	if(is.null(rowend)) rowend=.jnull("java/lang/Integer") else rowend = .jnew("java/lang/Integer",as.character(rowend))
	
	if(is.null(colstart)) colstart=.jnull("java/lang/Integer") else colstart = .jnew("java/lang/Integer",as.character(colstart))
	
	if(is.null(colend)) colend=.jnull("java/lang/Integer") else colend = .jnew("java/lang/Integer",as.character(colend))
	
	if(hasRowNames) hasRowNames = .jnew("java/lang/Boolean",TRUE) else hasRowNames = .jnew("java/lang/Boolean",FALSE)
	
	if(is.null(rowNamesColumnIndex)) rowNamesColumnIndex = .jnull("java/lang/Integer") else rowNamesColumnIndex = .jnew("java/lang/Integer",as.character(rowNamesColumnIndex))
	
	#if(rowNamesNo < 1) stop ("the index of the column containing the row names must be equal or greater than 1.") 
	
	
	if(hasColumnNames) hasColumnNames = .jnew("java/lang/Boolean",TRUE) else hasColumnNames = .jnew("java/lang/Boolean",FALSE)
	
	if(is.null(columnNamesRowIndex)) columnNamesRowIndex = .jnull("java/lang/Integer") else columnNamesRowIndex = .jnew("java/lang/Integer",as.character(columnNamesRowIndex))
	
	if(is.null(separator)) separator = .jnull("java/lang/String")
	
	if(is.null(quote)) quote = .jnull("java/lang/String")	
    
	tryCatch(
	.jcall("at/ac/ait/speedr/importany/ImporterAnyFunction","V",method="importany",file,rowstart,rowend,colstart,colend,hasRowNames,rowNamesColumnIndex,hasColumnNames,columnNamesRowIndex,separator,quote),
	Exception = function(e){
			e$jobj$printStackTrace()
		} 
	)
	res <- get("speedrtemp",envir=.GlobalEnv)
	remove(speedrtemp,envir=.GlobalEnv)
	res
}

# refresh Objects (used by SyntaxHighlighting and ObjectManager)

._refreshObjects <- function() {
	# currently only use the objects we find in pos=1
	result <- c(ls(pos=1,all.names=TRUE))
	result
}

._getDataObjects <- function() {
    objects <- ls(pos=1)
    result <- c();
    if (length(objects) > 0) for (i in 1:length(objects)) {
    	d <- get(objects[i])
    	cls <- class(d)
        if ("data.frame" %in% cls || "table" %in% cls) 
        	result <- c(result,objects[i],cls[1])
    }
    result
}

._getOtherObjects <- function() {
    objects <- ls(pos=1)
    result <- c();
    if (length(objects) > 0) for (i in 1:length(objects)) {
    	if (objects[i] != "last.warning" && objects[i] != "*tmp*") {
	    	cls <- class(get(objects[i]))
   			if (!("data.frame" %in% cls || "table" %in% cls || "function" %in% cls)) 
   				result <- c(result,objects[i],cls[1])
        }
    }
    result
}

._getContent <- function (o, p = NULL)
{
    result <- c()
    if ("table" %in% class(o))
        o <- dimnames(o)
    if ("table" %in% class(p)) {
        dn <- o
        for (i in 1:length(dn)) {
            try(result <- c(result, dn[i], class((dn[[i]]))[1]),
                silent = TRUE)
        }
    }
    else if ("matrix" %in% class(o)) {
    	colnames <- colnames(o)
    	for (i in 1:dim(o)[2]) {
    		xname <- colnames[i]
    		if (is.null(xname)) xname <- "null"
            try(result <- c(result, xname, class((o[,i]))[1]),
                silent = TRUE)
        }

    }
    else {
        if (mode(o)=="list") {
	        for (i in 1:length(o)) {
		  		xname <- names(o)[i]
				if (is.null(xname)) xname <- "null"
            	try(result <- c(result, xname, class((o[[i]]))[1]), silent = TRUE)
	        }
        }
    }
    result
}