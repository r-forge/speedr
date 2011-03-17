tree grammar FilterExpressionRCodeGen;

options {
    tokenVocab=FilterExpression; // use the vocabulary from the parser
    ASTLabelType=CommonTree; // what kind of trees are we walking?
    output=template; // generate templates
}

@header { 
package at.ac.ait.speedr.codegen; 
}

@members{
private String col;
private String dataframecol;

private boolean hasRowNames = false;
private boolean isDataframe = false;
private boolean isPOSIXct = false;

public void setColumnIndex(String col){
	this.col = col;
}

public void setDataframeColumnIndex(String col){
	this.dataframecol = col;
}

public void setHasRowNames(boolean b){
	this.hasRowNames = b;
}

public boolean hasRowNames(){
 return hasRowNames;
}

public void setDataframe(boolean b){
	this.isDataframe = b;
}

public void setPOSIXct(boolean b){
     isPOSIXct = b;
}

}

rcode	:	predicate -> selector(predicates={$predicate.st})
	;

predicate:	^(OR a=predicate b=predicate) -> or(a={$a.st},b={$b.st})
	| 	^(AND a=predicate b=predicate) -> and(a={$a.st},b={$b.st})
	| 	^(NOT p=predicate) -> not(a={$p.st})
	|	'is.na' -> isna(colIndex={col})
	|	'is.null' -> isna(colIndex={col})

	| 	^('='  number) -> {isDataframe}? equal(colIndex={dataframecol},a={$number.text},isdataframe={isDataframe})
				-> equal(colIndex={col},a={$number.text})
				
	| 	^('<'  number) -> {isDataframe}? less(colIndex={dataframecol},a={$number.text},isdataframe={isDataframe})
				-> less(colIndex={col},a={$number.text})
				
	| 	^('<='  number) -> {isDataframe}? lessOrEqual(colIndex={dataframecol},a={$number.text},isdataframe={isDataframe})
				-> lessOrEqual(colIndex={col},a={$number.text})
				
	| 	^('>'  number) -> {isDataframe}? greater(colIndex={dataframecol},a={$number.text},isdataframe={isDataframe})
				-> greater(colIndex={col},a={$number.text})
				
	| 	^('>='  number) -> {isDataframe}? greaterOrEqual(colIndex={dataframecol},a={$number.text},isdataframe={isDataframe})
				-> greaterOrEqual(colIndex={col},a={$number.text})
				
	| 	^('='  date) -> {isDataframe}? equal(colIndex={dataframecol},a={$date.st},isdataframe={isDataframe})
			     -> equal(colIndex={col},a={$date.st})
	| 	^('='  time) -> {isDataframe}? equal(colIndex={dataframecol},a={$time.st},isdataframe={isDataframe})	
			     -> equal(colIndex={col},a={$time.st})			     
			     
	| 	^('<'  date) -> {isDataframe}? less(colIndex={dataframecol},a={$date.st},isdataframe={isDataframe})
			     -> less(colIndex={col},a={$date.st})
	| 	^('<'  time) -> {isDataframe}? less(colIndex={dataframecol},a={$time.st},isdataframe={isDataframe})
			     -> less(colIndex={col},a={$time.st})
			     		     
	| 	^('<='  date) -> {isDataframe}? lessOrEqual(colIndex={dataframecol},a={$date.st},isdataframe={isDataframe})
			      -> lessOrEqual(colIndex={col},a={$date.st})
	| 	^('<='  time) -> {isDataframe}? lessOrEqual(colIndex={dataframecol},a={$time.st},isdataframe={isDataframe})
			      -> lessOrEqual(colIndex={col},a={$time.st})		      
			      
	| 	^('>'  date) -> {isDataframe}? greater(colIndex={dataframecol},a={$date.st},isdataframe={isDataframe})
			     -> greater(colIndex={col},a={$date.st})
	| 	^('>'  time) -> {isDataframe}? greater(colIndex={dataframecol},a={$time.st},isdataframe={isDataframe})
			     -> greater(colIndex={col},a={$time.st})
			     		     
	| 	^('>='  date) -> {isDataframe}? greaterOrEqual(colIndex={dataframecol},a={$date.st},isdataframe={isDataframe})
			      -> greaterOrEqual(colIndex={col},a={$date.st})
	| 	^('>='  time) -> {isDataframe}? greaterOrEqual(colIndex={dataframecol},a={$time.st},isdataframe={isDataframe})
			      -> greaterOrEqual(colIndex={col},a={$time.st})		      

	|	^('contains' e=STRING_LITERAL) -> {hasRowNames}? contains_rownames(rownamesindex={col},text={$e.text})
						-> contains(colIndex={col},text={$e.text})
	| 	^('equals'  TO? e=STRING_LITERAL) -> {hasRowNames}? equal_rownames(rownamesindex={col},text={$e.text})
						  -> equal(colIndex={col},a={$e.text})
	;

number 	: SIGNEDINTEGER
	| INTEGER 
	| FLOAT
	;

time	:	^(TIME ^(DATE (year=INTEGER | year=DATENUM) (month=INTEGER | month=DATENUM) (day=INTEGER | day=DATENUM)) (hour=INTEGER | hour=DATENUM) (minute=INTEGER | minute=DATENUM) (second=INTEGER | second=DATENUM)) -> time(year={$year.text},month={$month.text},day={$day.text},hour={$hour.text},minute={$minute.text},second={$second.text});

date	:	^(DATE (year=INTEGER | year=DATENUM) (month=INTEGER | month=DATENUM) (day=INTEGER | day=DATENUM)) -> {!isPOSIXct}? date(year={$year.text},month={$month.text},day={$day.text})
													 	  -> time(year={$year.text},month={$month.text},day={$day.text},hour={null},minute={null},second={null})
	;													 	  
