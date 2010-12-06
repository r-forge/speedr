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
private boolean hasRowNames = false;

public void setColumnIndex(String col){
	this.col = col;
}

public void setHasRowNames(boolean b){
	this.hasRowNames = b;
}

public boolean hasRowNames(){
 return hasRowNames;
}

}

rcode	:	predicate -> selector(predicates={$predicate.st})
	;

predicate:	^(OR a=predicate b=predicate) -> or(a={$a.st},b={$b.st})
	| 	^(AND a=predicate b=predicate) -> and(a={$a.st},b={$b.st})
	| 	^(NOT p=predicate) -> not(a={$p.st})
	|	'is.na' -> isna(colIndex={col})
	| 	^('='  number) -> equal(colIndex={col},a={$number.text})
	| 	^('<'  number) -> less(colIndex={col},a={$number.text})
	| 	^('<='  number) -> lessOrEqual(colIndex={col},a={$number.text})
	| 	^('>'  number) -> greater(colIndex={col},a={$number.text})
	| 	^('>='  number) -> greaterOrEqual(colIndex={col},a={$number.text})
	|	^('contains' e=STRING_LITERAL) -> {hasRowNames}? contains_rownames(rownamesindex={col},text={$e.text})
						-> contains(colIndex={col},text={$e.text})
	| 	^('equals'  TO? e=STRING_LITERAL) -> {hasRowNames}? equal_rownames(rownamesindex={col},text={$e.text})
						  -> equal(colIndex={col},a={$e.text})
	;

number 	: INTEGER 
	| FLOAT
	;
