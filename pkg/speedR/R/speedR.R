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
		
		.jpackage(name="speedRlibs")
		.jpackage(name="speedRlibTF")
		.jpackage(name="speedR")
		
		jarfolder <- system.file("jri", package = "rJava")
		jars <- grep(".*\\.jar", list.files(jarfolder,full.names = TRUE), TRUE, value = TRUE)
		if (length(jars))
            .jaddClassPath(jars)

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
}

speedR.importany<-function(file=NULL,rowstart=NULL,rowend=NULL,colstart=NULL,colend=NULL,
						   hasRowNames = FALSE, rowNamesColumnIndex = NULL,hasColumnNames = FALSE, 
						   columnNamesRowIndex = NULL, separator = NULL, quote = NULL,maxmemory = NULL, ...){	
    
	
	speedR.init(maxmemory,...)
	
	
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

.reloadworkspace <- function(){
	supportedtypes = c("character","numeric","array","integer","table","matrix","data.frame","double","list","pairlist")
	objects <- ls(pos=1)
    result <- NULL;
    if (length(objects) > 0){
		classes = c()
		supportedobjects = c()
		#dimANDlengthINFO = c()
		
		for (i in 1:length(objects)) {
			o <- get(objects[i])
			cls <- class(o)
			if( !is.null(o) && cls %in% supportedtypes){
				supportedobjects <- c(supportedobjects,objects[i])
				classes <- c(classes,cls)				
			}
			
			#o_dim = dim(o)
			
			#if(is.null(o_dim) || length(o_dim) == 1){
			#	dimANDlengthINFO <- c(dimANDlengthINFO, length(o))
			#}else{
			#	dimANDlengthINFO <- c(dimANDlengthINFO, o_dim)
			#}
			
		}		
		if(length(supportedobjects) > 0){
			result <- data.frame(var_name = supportedobjects, var_cls = classes)
			result$var_name <- as.character(supportedobjects)
			result$var_cls <- as.character(classes)
		}
	}
    result
}


.collectListOjectMetaData <- function(listobj){
	result <- NULL;
	children = names(listobj)
	
	classes = c()
	if(is.null(children)){
		children = 1:length(listobj)
	}
	
	for (i in 1:length(listobj)) {
		o <- listobj[[children[i]]]
		cls <- class(o)
		classes <- c(classes,cls)
	}
		
	if(length(classes) > 0){	
		result <- data.frame(var_name = children, var_cls = classes)
		result$var_name <- as.character(children)
		result$var_cls <- as.character(classes)
	}
	
	result
}