// $ANTLR 3.3 Nov 30, 2010 12:45:30 E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g 2011-03-23 10:47:45
 
package at.ac.ait.speedr.codegen; 


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.stringtemplate.*;
import org.antlr.stringtemplate.language.*;
import java.util.HashMap;
public class FilterExpressionRCodeGen extends TreeParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "DATE", "TIME", "OR", "AND", "NOT", "LP", "RP", "STRING_LITERAL", "TO", "INTEGER", "SIGNEDINTEGER", "FLOAT", "DATENUM", "DIGIT", "SIGN", "NONCONTROL_CHAR", "LETTER", "SYMBOL", "SPACE", "LOWER", "UPPER", "WS", "'is.na'", "'is.null'", "'contains'", "'equals'", "'='", "'<'", "'<='", "'>'", "'>='", "':'", "'-'", "'.'"
    };
    public static final int EOF=-1;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int T__29=29;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int T__37=37;
    public static final int DATE=4;
    public static final int TIME=5;
    public static final int OR=6;
    public static final int AND=7;
    public static final int NOT=8;
    public static final int LP=9;
    public static final int RP=10;
    public static final int STRING_LITERAL=11;
    public static final int TO=12;
    public static final int INTEGER=13;
    public static final int SIGNEDINTEGER=14;
    public static final int FLOAT=15;
    public static final int DATENUM=16;
    public static final int DIGIT=17;
    public static final int SIGN=18;
    public static final int NONCONTROL_CHAR=19;
    public static final int LETTER=20;
    public static final int SYMBOL=21;
    public static final int SPACE=22;
    public static final int LOWER=23;
    public static final int UPPER=24;
    public static final int WS=25;

    // delegates
    // delegators


        public FilterExpressionRCodeGen(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public FilterExpressionRCodeGen(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected StringTemplateGroup templateLib =
      new StringTemplateGroup("FilterExpressionRCodeGenTemplates", AngleBracketTemplateLexer.class);

    public void setTemplateLib(StringTemplateGroup templateLib) {
      this.templateLib = templateLib;
    }
    public StringTemplateGroup getTemplateLib() {
      return templateLib;
    }
    /** allows convenient multi-value initialization:
     *  "new STAttrMap().put(...).put(...)"
     */
    public static class STAttrMap extends HashMap {
      public STAttrMap put(String attrName, Object value) {
        super.put(attrName, value);
        return this;
      }
      public STAttrMap put(String attrName, int value) {
        super.put(attrName, new Integer(value));
        return this;
      }
    }

    public String[] getTokenNames() { return FilterExpressionRCodeGen.tokenNames; }
    public String getGrammarFileName() { return "E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g"; }


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



    public static class rcode_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "rcode"
    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:47:1: rcode : predicate -> selector(predicates=$predicate.st);
    public final FilterExpressionRCodeGen.rcode_return rcode() throws RecognitionException {
        FilterExpressionRCodeGen.rcode_return retval = new FilterExpressionRCodeGen.rcode_return();
        retval.start = input.LT(1);

        FilterExpressionRCodeGen.predicate_return predicate1 = null;


        try {
            // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:47:7: ( predicate -> selector(predicates=$predicate.st))
            // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:47:9: predicate
            {
            pushFollow(FOLLOW_predicate_in_rcode58);
            predicate1=predicate();

            state._fsp--;



            // TEMPLATE REWRITE
            // 47:19: -> selector(predicates=$predicate.st)
            {
                retval.st = templateLib.getInstanceOf("selector",
              new STAttrMap().put("predicates", (predicate1!=null?predicate1.st:null)));
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "rcode"

    public static class predicate_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "predicate"
    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:50:1: predicate : ( ^( OR a= predicate b= predicate ) -> or(a=$a.stb=$b.st) | ^( AND a= predicate b= predicate ) -> and(a=$a.stb=$b.st) | ^( NOT p= predicate ) -> not(a=$p.st) | 'is.na' -> isna(colIndex=col) | 'is.null' -> isna(colIndex=col) | ^( '=' number ) -> {isDataframe}? equal(colIndex=dataframecola=$number.textisdataframe=isDataframe) -> equal(colIndex=cola=$number.text) | ^( '<' number ) -> {isDataframe}? less(colIndex=dataframecola=$number.textisdataframe=isDataframe) -> less(colIndex=cola=$number.text) | ^( '<=' number ) -> {isDataframe}? lessOrEqual(colIndex=dataframecola=$number.textisdataframe=isDataframe) -> lessOrEqual(colIndex=cola=$number.text) | ^( '>' number ) -> {isDataframe}? greater(colIndex=dataframecola=$number.textisdataframe=isDataframe) -> greater(colIndex=cola=$number.text) | ^( '>=' number ) -> {isDataframe}? greaterOrEqual(colIndex=dataframecola=$number.textisdataframe=isDataframe) -> greaterOrEqual(colIndex=cola=$number.text) | ^( '=' date ) -> {isDataframe}? equal(colIndex=dataframecola=$date.stisdataframe=isDataframe) -> equal(colIndex=cola=$date.st) | ^( '=' time ) -> {isDataframe}? equal(colIndex=dataframecola=$time.stisdataframe=isDataframe) -> equal(colIndex=cola=$time.st) | ^( '<' date ) -> {isDataframe}? less(colIndex=dataframecola=$date.stisdataframe=isDataframe) -> less(colIndex=cola=$date.st) | ^( '<' time ) -> {isDataframe}? less(colIndex=dataframecola=$time.stisdataframe=isDataframe) -> less(colIndex=cola=$time.st) | ^( '<=' date ) -> {isDataframe}? lessOrEqual(colIndex=dataframecola=$date.stisdataframe=isDataframe) -> lessOrEqual(colIndex=cola=$date.st) | ^( '<=' time ) -> {isDataframe}? lessOrEqual(colIndex=dataframecola=$time.stisdataframe=isDataframe) -> lessOrEqual(colIndex=cola=$time.st) | ^( '>' date ) -> {isDataframe}? greater(colIndex=dataframecola=$date.stisdataframe=isDataframe) -> greater(colIndex=cola=$date.st) | ^( '>' time ) -> {isDataframe}? greater(colIndex=dataframecola=$time.stisdataframe=isDataframe) -> greater(colIndex=cola=$time.st) | ^( '>=' date ) -> {isDataframe}? greaterOrEqual(colIndex=dataframecola=$date.stisdataframe=isDataframe) -> greaterOrEqual(colIndex=cola=$date.st) | ^( '>=' time ) -> {isDataframe}? greaterOrEqual(colIndex=dataframecola=$time.stisdataframe=isDataframe) -> greaterOrEqual(colIndex=cola=$time.st) | ^( 'contains' e= STRING_LITERAL ) -> {hasRowNames}? contains_rownames(rownamesindex=coltext=$e.text) -> contains(colIndex=coltext=$e.text) | ^( 'equals' ( TO )? e= STRING_LITERAL ) -> {hasRowNames}? equal_rownames(rownamesindex=coltext=$e.text) -> {isDataframe}? equal(colIndex=dataframecola=$e.textisdataframe=isDataframe) -> equal(colIndex=cola=$e.text));
    public final FilterExpressionRCodeGen.predicate_return predicate() throws RecognitionException {
        FilterExpressionRCodeGen.predicate_return retval = new FilterExpressionRCodeGen.predicate_return();
        retval.start = input.LT(1);

        CommonTree e=null;
        FilterExpressionRCodeGen.predicate_return a = null;

        FilterExpressionRCodeGen.predicate_return b = null;

        FilterExpressionRCodeGen.predicate_return p = null;

        FilterExpressionRCodeGen.number_return number2 = null;

        FilterExpressionRCodeGen.number_return number3 = null;

        FilterExpressionRCodeGen.number_return number4 = null;

        FilterExpressionRCodeGen.number_return number5 = null;

        FilterExpressionRCodeGen.number_return number6 = null;

        FilterExpressionRCodeGen.date_return date7 = null;

        FilterExpressionRCodeGen.time_return time8 = null;

        FilterExpressionRCodeGen.date_return date9 = null;

        FilterExpressionRCodeGen.time_return time10 = null;

        FilterExpressionRCodeGen.date_return date11 = null;

        FilterExpressionRCodeGen.time_return time12 = null;

        FilterExpressionRCodeGen.date_return date13 = null;

        FilterExpressionRCodeGen.time_return time14 = null;

        FilterExpressionRCodeGen.date_return date15 = null;

        FilterExpressionRCodeGen.time_return time16 = null;


        try {
            // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:50:10: ( ^( OR a= predicate b= predicate ) -> or(a=$a.stb=$b.st) | ^( AND a= predicate b= predicate ) -> and(a=$a.stb=$b.st) | ^( NOT p= predicate ) -> not(a=$p.st) | 'is.na' -> isna(colIndex=col) | 'is.null' -> isna(colIndex=col) | ^( '=' number ) -> {isDataframe}? equal(colIndex=dataframecola=$number.textisdataframe=isDataframe) -> equal(colIndex=cola=$number.text) | ^( '<' number ) -> {isDataframe}? less(colIndex=dataframecola=$number.textisdataframe=isDataframe) -> less(colIndex=cola=$number.text) | ^( '<=' number ) -> {isDataframe}? lessOrEqual(colIndex=dataframecola=$number.textisdataframe=isDataframe) -> lessOrEqual(colIndex=cola=$number.text) | ^( '>' number ) -> {isDataframe}? greater(colIndex=dataframecola=$number.textisdataframe=isDataframe) -> greater(colIndex=cola=$number.text) | ^( '>=' number ) -> {isDataframe}? greaterOrEqual(colIndex=dataframecola=$number.textisdataframe=isDataframe) -> greaterOrEqual(colIndex=cola=$number.text) | ^( '=' date ) -> {isDataframe}? equal(colIndex=dataframecola=$date.stisdataframe=isDataframe) -> equal(colIndex=cola=$date.st) | ^( '=' time ) -> {isDataframe}? equal(colIndex=dataframecola=$time.stisdataframe=isDataframe) -> equal(colIndex=cola=$time.st) | ^( '<' date ) -> {isDataframe}? less(colIndex=dataframecola=$date.stisdataframe=isDataframe) -> less(colIndex=cola=$date.st) | ^( '<' time ) -> {isDataframe}? less(colIndex=dataframecola=$time.stisdataframe=isDataframe) -> less(colIndex=cola=$time.st) | ^( '<=' date ) -> {isDataframe}? lessOrEqual(colIndex=dataframecola=$date.stisdataframe=isDataframe) -> lessOrEqual(colIndex=cola=$date.st) | ^( '<=' time ) -> {isDataframe}? lessOrEqual(colIndex=dataframecola=$time.stisdataframe=isDataframe) -> lessOrEqual(colIndex=cola=$time.st) | ^( '>' date ) -> {isDataframe}? greater(colIndex=dataframecola=$date.stisdataframe=isDataframe) -> greater(colIndex=cola=$date.st) | ^( '>' time ) -> {isDataframe}? greater(colIndex=dataframecola=$time.stisdataframe=isDataframe) -> greater(colIndex=cola=$time.st) | ^( '>=' date ) -> {isDataframe}? greaterOrEqual(colIndex=dataframecola=$date.stisdataframe=isDataframe) -> greaterOrEqual(colIndex=cola=$date.st) | ^( '>=' time ) -> {isDataframe}? greaterOrEqual(colIndex=dataframecola=$time.stisdataframe=isDataframe) -> greaterOrEqual(colIndex=cola=$time.st) | ^( 'contains' e= STRING_LITERAL ) -> {hasRowNames}? contains_rownames(rownamesindex=coltext=$e.text) -> contains(colIndex=coltext=$e.text) | ^( 'equals' ( TO )? e= STRING_LITERAL ) -> {hasRowNames}? equal_rownames(rownamesindex=coltext=$e.text) -> {isDataframe}? equal(colIndex=dataframecola=$e.textisdataframe=isDataframe) -> equal(colIndex=cola=$e.text))
            int alt2=22;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:50:12: ^( OR a= predicate b= predicate )
                    {
                    match(input,OR,FOLLOW_OR_in_predicate77); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_predicate_in_predicate81);
                    a=predicate();

                    state._fsp--;

                    pushFollow(FOLLOW_predicate_in_predicate85);
                    b=predicate();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 50:42: -> or(a=$a.stb=$b.st)
                    {
                        retval.st = templateLib.getInstanceOf("or",
                      new STAttrMap().put("a", (a!=null?a.st:null)).put("b", (b!=null?b.st:null)));
                    }


                    }
                    break;
                case 2 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:51:5: ^( AND a= predicate b= predicate )
                    {
                    match(input,AND,FOLLOW_AND_in_predicate106); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_predicate_in_predicate110);
                    a=predicate();

                    state._fsp--;

                    pushFollow(FOLLOW_predicate_in_predicate114);
                    b=predicate();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 51:36: -> and(a=$a.stb=$b.st)
                    {
                        retval.st = templateLib.getInstanceOf("and",
                      new STAttrMap().put("a", (a!=null?a.st:null)).put("b", (b!=null?b.st:null)));
                    }


                    }
                    break;
                case 3 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:52:5: ^( NOT p= predicate )
                    {
                    match(input,NOT,FOLLOW_NOT_in_predicate135); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_predicate_in_predicate139);
                    p=predicate();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 52:24: -> not(a=$p.st)
                    {
                        retval.st = templateLib.getInstanceOf("not",
                      new STAttrMap().put("a", (p!=null?p.st:null)));
                    }


                    }
                    break;
                case 4 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:53:4: 'is.na'
                    {
                    match(input,26,FOLLOW_26_in_predicate154); 


                    // TEMPLATE REWRITE
                    // 53:12: -> isna(colIndex=col)
                    {
                        retval.st = templateLib.getInstanceOf("isna",
                      new STAttrMap().put("colIndex", col));
                    }


                    }
                    break;
                case 5 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:54:4: 'is.null'
                    {
                    match(input,27,FOLLOW_27_in_predicate168); 


                    // TEMPLATE REWRITE
                    // 54:14: -> isna(colIndex=col)
                    {
                        retval.st = templateLib.getInstanceOf("isna",
                      new STAttrMap().put("colIndex", col));
                    }


                    }
                    break;
                case 6 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:56:5: ^( '=' number )
                    {
                    match(input,30,FOLLOW_30_in_predicate185); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_number_in_predicate188);
                    number2=number();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 56:20: -> {isDataframe}? equal(colIndex=dataframecola=$number.textisdataframe=isDataframe)
                    if (isDataframe) {
                        retval.st = templateLib.getInstanceOf("equal",
                      new STAttrMap().put("colIndex", dataframecol).put("a", (number2!=null?(input.getTokenStream().toString(
                      input.getTreeAdaptor().getTokenStartIndex(number2.start),
                      input.getTreeAdaptor().getTokenStopIndex(number2.start))):null)).put("isdataframe", isDataframe));
                    }
                    else // 57:5: -> equal(colIndex=cola=$number.text)
                    {
                        retval.st = templateLib.getInstanceOf("equal",
                      new STAttrMap().put("colIndex", col).put("a", (number2!=null?(input.getTokenStream().toString(
                      input.getTreeAdaptor().getTokenStartIndex(number2.start),
                      input.getTreeAdaptor().getTokenStopIndex(number2.start))):null)));
                    }


                    }
                    break;
                case 7 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:59:5: ^( '<' number )
                    {
                    match(input,31,FOLLOW_31_in_predicate237); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_number_in_predicate240);
                    number3=number();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 59:20: -> {isDataframe}? less(colIndex=dataframecola=$number.textisdataframe=isDataframe)
                    if (isDataframe) {
                        retval.st = templateLib.getInstanceOf("less",
                      new STAttrMap().put("colIndex", dataframecol).put("a", (number3!=null?(input.getTokenStream().toString(
                      input.getTreeAdaptor().getTokenStartIndex(number3.start),
                      input.getTreeAdaptor().getTokenStopIndex(number3.start))):null)).put("isdataframe", isDataframe));
                    }
                    else // 60:5: -> less(colIndex=cola=$number.text)
                    {
                        retval.st = templateLib.getInstanceOf("less",
                      new STAttrMap().put("colIndex", col).put("a", (number3!=null?(input.getTokenStream().toString(
                      input.getTreeAdaptor().getTokenStartIndex(number3.start),
                      input.getTreeAdaptor().getTokenStopIndex(number3.start))):null)));
                    }


                    }
                    break;
                case 8 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:62:5: ^( '<=' number )
                    {
                    match(input,32,FOLLOW_32_in_predicate289); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_number_in_predicate292);
                    number4=number();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 62:21: -> {isDataframe}? lessOrEqual(colIndex=dataframecola=$number.textisdataframe=isDataframe)
                    if (isDataframe) {
                        retval.st = templateLib.getInstanceOf("lessOrEqual",
                      new STAttrMap().put("colIndex", dataframecol).put("a", (number4!=null?(input.getTokenStream().toString(
                      input.getTreeAdaptor().getTokenStartIndex(number4.start),
                      input.getTreeAdaptor().getTokenStopIndex(number4.start))):null)).put("isdataframe", isDataframe));
                    }
                    else // 63:5: -> lessOrEqual(colIndex=cola=$number.text)
                    {
                        retval.st = templateLib.getInstanceOf("lessOrEqual",
                      new STAttrMap().put("colIndex", col).put("a", (number4!=null?(input.getTokenStream().toString(
                      input.getTreeAdaptor().getTokenStartIndex(number4.start),
                      input.getTreeAdaptor().getTokenStopIndex(number4.start))):null)));
                    }


                    }
                    break;
                case 9 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:65:5: ^( '>' number )
                    {
                    match(input,33,FOLLOW_33_in_predicate341); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_number_in_predicate344);
                    number5=number();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 65:20: -> {isDataframe}? greater(colIndex=dataframecola=$number.textisdataframe=isDataframe)
                    if (isDataframe) {
                        retval.st = templateLib.getInstanceOf("greater",
                      new STAttrMap().put("colIndex", dataframecol).put("a", (number5!=null?(input.getTokenStream().toString(
                      input.getTreeAdaptor().getTokenStartIndex(number5.start),
                      input.getTreeAdaptor().getTokenStopIndex(number5.start))):null)).put("isdataframe", isDataframe));
                    }
                    else // 66:5: -> greater(colIndex=cola=$number.text)
                    {
                        retval.st = templateLib.getInstanceOf("greater",
                      new STAttrMap().put("colIndex", col).put("a", (number5!=null?(input.getTokenStream().toString(
                      input.getTreeAdaptor().getTokenStartIndex(number5.start),
                      input.getTreeAdaptor().getTokenStopIndex(number5.start))):null)));
                    }


                    }
                    break;
                case 10 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:68:5: ^( '>=' number )
                    {
                    match(input,34,FOLLOW_34_in_predicate393); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_number_in_predicate396);
                    number6=number();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 68:21: -> {isDataframe}? greaterOrEqual(colIndex=dataframecola=$number.textisdataframe=isDataframe)
                    if (isDataframe) {
                        retval.st = templateLib.getInstanceOf("greaterOrEqual",
                      new STAttrMap().put("colIndex", dataframecol).put("a", (number6!=null?(input.getTokenStream().toString(
                      input.getTreeAdaptor().getTokenStartIndex(number6.start),
                      input.getTreeAdaptor().getTokenStopIndex(number6.start))):null)).put("isdataframe", isDataframe));
                    }
                    else // 69:5: -> greaterOrEqual(colIndex=cola=$number.text)
                    {
                        retval.st = templateLib.getInstanceOf("greaterOrEqual",
                      new STAttrMap().put("colIndex", col).put("a", (number6!=null?(input.getTokenStream().toString(
                      input.getTreeAdaptor().getTokenStartIndex(number6.start),
                      input.getTreeAdaptor().getTokenStopIndex(number6.start))):null)));
                    }


                    }
                    break;
                case 11 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:71:5: ^( '=' date )
                    {
                    match(input,30,FOLLOW_30_in_predicate445); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_date_in_predicate448);
                    date7=date();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 71:18: -> {isDataframe}? equal(colIndex=dataframecola=$date.stisdataframe=isDataframe)
                    if (isDataframe) {
                        retval.st = templateLib.getInstanceOf("equal",
                      new STAttrMap().put("colIndex", dataframecol).put("a", (date7!=null?date7.st:null)).put("isdataframe", isDataframe));
                    }
                    else // 72:9: -> equal(colIndex=cola=$date.st)
                    {
                        retval.st = templateLib.getInstanceOf("equal",
                      new STAttrMap().put("colIndex", col).put("a", (date7!=null?date7.st:null)));
                    }


                    }
                    break;
                case 12 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:73:5: ^( '=' time )
                    {
                    match(input,30,FOLLOW_30_in_predicate496); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_time_in_predicate499);
                    time8=time();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 73:18: -> {isDataframe}? equal(colIndex=dataframecola=$time.stisdataframe=isDataframe)
                    if (isDataframe) {
                        retval.st = templateLib.getInstanceOf("equal",
                      new STAttrMap().put("colIndex", dataframecol).put("a", (time8!=null?time8.st:null)).put("isdataframe", isDataframe));
                    }
                    else // 74:9: -> equal(colIndex=cola=$time.st)
                    {
                        retval.st = templateLib.getInstanceOf("equal",
                      new STAttrMap().put("colIndex", col).put("a", (time8!=null?time8.st:null)));
                    }


                    }
                    break;
                case 13 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:76:5: ^( '<' date )
                    {
                    match(input,31,FOLLOW_31_in_predicate565); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_date_in_predicate568);
                    date9=date();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 76:18: -> {isDataframe}? less(colIndex=dataframecola=$date.stisdataframe=isDataframe)
                    if (isDataframe) {
                        retval.st = templateLib.getInstanceOf("less",
                      new STAttrMap().put("colIndex", dataframecol).put("a", (date9!=null?date9.st:null)).put("isdataframe", isDataframe));
                    }
                    else // 77:9: -> less(colIndex=cola=$date.st)
                    {
                        retval.st = templateLib.getInstanceOf("less",
                      new STAttrMap().put("colIndex", col).put("a", (date9!=null?date9.st:null)));
                    }


                    }
                    break;
                case 14 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:78:5: ^( '<' time )
                    {
                    match(input,31,FOLLOW_31_in_predicate616); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_time_in_predicate619);
                    time10=time();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 78:18: -> {isDataframe}? less(colIndex=dataframecola=$time.stisdataframe=isDataframe)
                    if (isDataframe) {
                        retval.st = templateLib.getInstanceOf("less",
                      new STAttrMap().put("colIndex", dataframecol).put("a", (time10!=null?time10.st:null)).put("isdataframe", isDataframe));
                    }
                    else // 79:9: -> less(colIndex=cola=$time.st)
                    {
                        retval.st = templateLib.getInstanceOf("less",
                      new STAttrMap().put("colIndex", col).put("a", (time10!=null?time10.st:null)));
                    }


                    }
                    break;
                case 15 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:81:5: ^( '<=' date )
                    {
                    match(input,32,FOLLOW_32_in_predicate683); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_date_in_predicate686);
                    date11=date();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 81:19: -> {isDataframe}? lessOrEqual(colIndex=dataframecola=$date.stisdataframe=isDataframe)
                    if (isDataframe) {
                        retval.st = templateLib.getInstanceOf("lessOrEqual",
                      new STAttrMap().put("colIndex", dataframecol).put("a", (date11!=null?date11.st:null)).put("isdataframe", isDataframe));
                    }
                    else // 82:10: -> lessOrEqual(colIndex=cola=$date.st)
                    {
                        retval.st = templateLib.getInstanceOf("lessOrEqual",
                      new STAttrMap().put("colIndex", col).put("a", (date11!=null?date11.st:null)));
                    }


                    }
                    break;
                case 16 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:83:5: ^( '<=' time )
                    {
                    match(input,32,FOLLOW_32_in_predicate735); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_time_in_predicate738);
                    time12=time();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 83:19: -> {isDataframe}? lessOrEqual(colIndex=dataframecola=$time.stisdataframe=isDataframe)
                    if (isDataframe) {
                        retval.st = templateLib.getInstanceOf("lessOrEqual",
                      new STAttrMap().put("colIndex", dataframecol).put("a", (time12!=null?time12.st:null)).put("isdataframe", isDataframe));
                    }
                    else // 84:10: -> lessOrEqual(colIndex=cola=$time.st)
                    {
                        retval.st = templateLib.getInstanceOf("lessOrEqual",
                      new STAttrMap().put("colIndex", col).put("a", (time12!=null?time12.st:null)));
                    }


                    }
                    break;
                case 17 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:86:5: ^( '>' date )
                    {
                    match(input,33,FOLLOW_33_in_predicate805); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_date_in_predicate808);
                    date13=date();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 86:18: -> {isDataframe}? greater(colIndex=dataframecola=$date.stisdataframe=isDataframe)
                    if (isDataframe) {
                        retval.st = templateLib.getInstanceOf("greater",
                      new STAttrMap().put("colIndex", dataframecol).put("a", (date13!=null?date13.st:null)).put("isdataframe", isDataframe));
                    }
                    else // 87:9: -> greater(colIndex=cola=$date.st)
                    {
                        retval.st = templateLib.getInstanceOf("greater",
                      new STAttrMap().put("colIndex", col).put("a", (date13!=null?date13.st:null)));
                    }


                    }
                    break;
                case 18 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:88:5: ^( '>' time )
                    {
                    match(input,33,FOLLOW_33_in_predicate856); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_time_in_predicate859);
                    time14=time();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 88:18: -> {isDataframe}? greater(colIndex=dataframecola=$time.stisdataframe=isDataframe)
                    if (isDataframe) {
                        retval.st = templateLib.getInstanceOf("greater",
                      new STAttrMap().put("colIndex", dataframecol).put("a", (time14!=null?time14.st:null)).put("isdataframe", isDataframe));
                    }
                    else // 89:9: -> greater(colIndex=cola=$time.st)
                    {
                        retval.st = templateLib.getInstanceOf("greater",
                      new STAttrMap().put("colIndex", col).put("a", (time14!=null?time14.st:null)));
                    }


                    }
                    break;
                case 19 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:91:5: ^( '>=' date )
                    {
                    match(input,34,FOLLOW_34_in_predicate923); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_date_in_predicate926);
                    date15=date();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 91:19: -> {isDataframe}? greaterOrEqual(colIndex=dataframecola=$date.stisdataframe=isDataframe)
                    if (isDataframe) {
                        retval.st = templateLib.getInstanceOf("greaterOrEqual",
                      new STAttrMap().put("colIndex", dataframecol).put("a", (date15!=null?date15.st:null)).put("isdataframe", isDataframe));
                    }
                    else // 92:10: -> greaterOrEqual(colIndex=cola=$date.st)
                    {
                        retval.st = templateLib.getInstanceOf("greaterOrEqual",
                      new STAttrMap().put("colIndex", col).put("a", (date15!=null?date15.st:null)));
                    }


                    }
                    break;
                case 20 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:93:5: ^( '>=' time )
                    {
                    match(input,34,FOLLOW_34_in_predicate975); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_time_in_predicate978);
                    time16=time();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 93:19: -> {isDataframe}? greaterOrEqual(colIndex=dataframecola=$time.stisdataframe=isDataframe)
                    if (isDataframe) {
                        retval.st = templateLib.getInstanceOf("greaterOrEqual",
                      new STAttrMap().put("colIndex", dataframecol).put("a", (time16!=null?time16.st:null)).put("isdataframe", isDataframe));
                    }
                    else // 94:10: -> greaterOrEqual(colIndex=cola=$time.st)
                    {
                        retval.st = templateLib.getInstanceOf("greaterOrEqual",
                      new STAttrMap().put("colIndex", col).put("a", (time16!=null?time16.st:null)));
                    }


                    }
                    break;
                case 21 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:96:4: ^( 'contains' e= STRING_LITERAL )
                    {
                    match(input,28,FOLLOW_28_in_predicate1035); 

                    match(input, Token.DOWN, null); 
                    e=(CommonTree)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_predicate1039); 

                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 96:35: -> {hasRowNames}? contains_rownames(rownamesindex=coltext=$e.text)
                    if (hasRowNames) {
                        retval.st = templateLib.getInstanceOf("contains_rownames",
                      new STAttrMap().put("rownamesindex", col).put("text", (e!=null?e.getText():null)));
                    }
                    else // 97:48: -> contains(colIndex=coltext=$e.text)
                    {
                        retval.st = templateLib.getInstanceOf("contains",
                      new STAttrMap().put("colIndex", col).put("text", (e!=null?e.getText():null)));
                    }


                    }
                    break;
                case 22 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:98:5: ^( 'equals' ( TO )? e= STRING_LITERAL )
                    {
                    match(input,29,FOLLOW_29_in_predicate1122); 

                    match(input, Token.DOWN, null); 
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:98:17: ( TO )?
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0==TO) ) {
                        alt1=1;
                    }
                    switch (alt1) {
                        case 1 :
                            // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:98:17: TO
                            {
                            match(input,TO,FOLLOW_TO_in_predicate1125); 

                            }
                            break;

                    }

                    e=(CommonTree)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_predicate1130); 

                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 98:39: -> {hasRowNames}? equal_rownames(rownamesindex=coltext=$e.text)
                    if (hasRowNames) {
                        retval.st = templateLib.getInstanceOf("equal_rownames",
                      new STAttrMap().put("rownamesindex", col).put("text", (e!=null?e.getText():null)));
                    }
                    else // 99:51: -> {isDataframe}? equal(colIndex=dataframecola=$e.textisdataframe=isDataframe)
                    if (isDataframe) {
                        retval.st = templateLib.getInstanceOf("equal",
                      new STAttrMap().put("colIndex", dataframecol).put("a", (e!=null?e.getText():null)).put("isdataframe", isDataframe));
                    }
                    else // 100:9: -> equal(colIndex=cola=$e.text)
                    {
                        retval.st = templateLib.getInstanceOf("equal",
                      new STAttrMap().put("colIndex", col).put("a", (e!=null?e.getText():null)));
                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "predicate"

    public static class number_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "number"
    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:103:1: number : ( SIGNEDINTEGER | INTEGER | FLOAT );
    public final FilterExpressionRCodeGen.number_return number() throws RecognitionException {
        FilterExpressionRCodeGen.number_return retval = new FilterExpressionRCodeGen.number_return();
        retval.start = input.LT(1);

        try {
            // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:103:9: ( SIGNEDINTEGER | INTEGER | FLOAT )
            // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:
            {
            if ( (input.LA(1)>=INTEGER && input.LA(1)<=FLOAT) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "number"

    public static class time_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "time"
    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:1: time : ^( TIME ^( DATE (year= INTEGER | year= DATENUM ) (month= INTEGER | month= DATENUM ) (day= INTEGER | day= DATENUM ) ) (hour= INTEGER | hour= DATENUM ) (minute= INTEGER | minute= DATENUM ) (second= INTEGER | second= DATENUM ) ) -> time(year=$year.textmonth=$month.textday=$day.texthour=$hour.textminute=$minute.textsecond=$second.text);
    public final FilterExpressionRCodeGen.time_return time() throws RecognitionException {
        FilterExpressionRCodeGen.time_return retval = new FilterExpressionRCodeGen.time_return();
        retval.start = input.LT(1);

        CommonTree year=null;
        CommonTree month=null;
        CommonTree day=null;
        CommonTree hour=null;
        CommonTree minute=null;
        CommonTree second=null;

        try {
            // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:6: ( ^( TIME ^( DATE (year= INTEGER | year= DATENUM ) (month= INTEGER | month= DATENUM ) (day= INTEGER | day= DATENUM ) ) (hour= INTEGER | hour= DATENUM ) (minute= INTEGER | minute= DATENUM ) (second= INTEGER | second= DATENUM ) ) -> time(year=$year.textmonth=$month.textday=$day.texthour=$hour.textminute=$minute.textsecond=$second.text))
            // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:8: ^( TIME ^( DATE (year= INTEGER | year= DATENUM ) (month= INTEGER | month= DATENUM ) (day= INTEGER | day= DATENUM ) ) (hour= INTEGER | hour= DATENUM ) (minute= INTEGER | minute= DATENUM ) (second= INTEGER | second= DATENUM ) )
            {
            match(input,TIME,FOLLOW_TIME_in_time1269); 

            match(input, Token.DOWN, null); 
            match(input,DATE,FOLLOW_DATE_in_time1272); 

            match(input, Token.DOWN, null); 
            // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:22: (year= INTEGER | year= DATENUM )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==INTEGER) ) {
                alt3=1;
            }
            else if ( (LA3_0==DATENUM) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:23: year= INTEGER
                    {
                    year=(CommonTree)match(input,INTEGER,FOLLOW_INTEGER_in_time1277); 

                    }
                    break;
                case 2 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:38: year= DATENUM
                    {
                    year=(CommonTree)match(input,DATENUM,FOLLOW_DATENUM_in_time1283); 

                    }
                    break;

            }

            // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:52: (month= INTEGER | month= DATENUM )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==INTEGER) ) {
                alt4=1;
            }
            else if ( (LA4_0==DATENUM) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:53: month= INTEGER
                    {
                    month=(CommonTree)match(input,INTEGER,FOLLOW_INTEGER_in_time1289); 

                    }
                    break;
                case 2 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:69: month= DATENUM
                    {
                    month=(CommonTree)match(input,DATENUM,FOLLOW_DATENUM_in_time1295); 

                    }
                    break;

            }

            // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:84: (day= INTEGER | day= DATENUM )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==INTEGER) ) {
                alt5=1;
            }
            else if ( (LA5_0==DATENUM) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:85: day= INTEGER
                    {
                    day=(CommonTree)match(input,INTEGER,FOLLOW_INTEGER_in_time1301); 

                    }
                    break;
                case 2 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:99: day= DATENUM
                    {
                    day=(CommonTree)match(input,DATENUM,FOLLOW_DATENUM_in_time1307); 

                    }
                    break;

            }


            match(input, Token.UP, null); 
            // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:113: (hour= INTEGER | hour= DATENUM )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==INTEGER) ) {
                alt6=1;
            }
            else if ( (LA6_0==DATENUM) ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:114: hour= INTEGER
                    {
                    hour=(CommonTree)match(input,INTEGER,FOLLOW_INTEGER_in_time1314); 

                    }
                    break;
                case 2 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:129: hour= DATENUM
                    {
                    hour=(CommonTree)match(input,DATENUM,FOLLOW_DATENUM_in_time1320); 

                    }
                    break;

            }

            // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:143: (minute= INTEGER | minute= DATENUM )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==INTEGER) ) {
                alt7=1;
            }
            else if ( (LA7_0==DATENUM) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:144: minute= INTEGER
                    {
                    minute=(CommonTree)match(input,INTEGER,FOLLOW_INTEGER_in_time1326); 

                    }
                    break;
                case 2 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:161: minute= DATENUM
                    {
                    minute=(CommonTree)match(input,DATENUM,FOLLOW_DATENUM_in_time1332); 

                    }
                    break;

            }

            // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:177: (second= INTEGER | second= DATENUM )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==INTEGER) ) {
                alt8=1;
            }
            else if ( (LA8_0==DATENUM) ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:178: second= INTEGER
                    {
                    second=(CommonTree)match(input,INTEGER,FOLLOW_INTEGER_in_time1338); 

                    }
                    break;
                case 2 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:108:195: second= DATENUM
                    {
                    second=(CommonTree)match(input,DATENUM,FOLLOW_DATENUM_in_time1344); 

                    }
                    break;

            }


            match(input, Token.UP, null); 


            // TEMPLATE REWRITE
            // 108:212: -> time(year=$year.textmonth=$month.textday=$day.texthour=$hour.textminute=$minute.textsecond=$second.text)
            {
                retval.st = templateLib.getInstanceOf("time",
              new STAttrMap().put("year", (year!=null?year.getText():null)).put("month", (month!=null?month.getText():null)).put("day", (day!=null?day.getText():null)).put("hour", (hour!=null?hour.getText():null)).put("minute", (minute!=null?minute.getText():null)).put("second", (second!=null?second.getText():null)));
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "time"

    public static class date_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "date"
    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:110:1: date : ^( DATE (year= INTEGER | year= DATENUM ) (month= INTEGER | month= DATENUM ) (day= INTEGER | day= DATENUM ) ) -> {!isPOSIXct}? date(year=$year.textmonth=$month.textday=$day.text) -> time(year=$year.textmonth=$month.textday=$day.texthour=nullminute=nullsecond=null);
    public final FilterExpressionRCodeGen.date_return date() throws RecognitionException {
        FilterExpressionRCodeGen.date_return retval = new FilterExpressionRCodeGen.date_return();
        retval.start = input.LT(1);

        CommonTree year=null;
        CommonTree month=null;
        CommonTree day=null;

        try {
            // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:110:6: ( ^( DATE (year= INTEGER | year= DATENUM ) (month= INTEGER | month= DATENUM ) (day= INTEGER | day= DATENUM ) ) -> {!isPOSIXct}? date(year=$year.textmonth=$month.textday=$day.text) -> time(year=$year.textmonth=$month.textday=$day.texthour=nullminute=nullsecond=null))
            // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:110:8: ^( DATE (year= INTEGER | year= DATENUM ) (month= INTEGER | month= DATENUM ) (day= INTEGER | day= DATENUM ) )
            {
            match(input,DATE,FOLLOW_DATE_in_date1384); 

            match(input, Token.DOWN, null); 
            // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:110:15: (year= INTEGER | year= DATENUM )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==INTEGER) ) {
                alt9=1;
            }
            else if ( (LA9_0==DATENUM) ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:110:16: year= INTEGER
                    {
                    year=(CommonTree)match(input,INTEGER,FOLLOW_INTEGER_in_date1389); 

                    }
                    break;
                case 2 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:110:31: year= DATENUM
                    {
                    year=(CommonTree)match(input,DATENUM,FOLLOW_DATENUM_in_date1395); 

                    }
                    break;

            }

            // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:110:45: (month= INTEGER | month= DATENUM )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==INTEGER) ) {
                alt10=1;
            }
            else if ( (LA10_0==DATENUM) ) {
                alt10=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }
            switch (alt10) {
                case 1 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:110:46: month= INTEGER
                    {
                    month=(CommonTree)match(input,INTEGER,FOLLOW_INTEGER_in_date1401); 

                    }
                    break;
                case 2 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:110:62: month= DATENUM
                    {
                    month=(CommonTree)match(input,DATENUM,FOLLOW_DATENUM_in_date1407); 

                    }
                    break;

            }

            // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:110:77: (day= INTEGER | day= DATENUM )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==INTEGER) ) {
                alt11=1;
            }
            else if ( (LA11_0==DATENUM) ) {
                alt11=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:110:78: day= INTEGER
                    {
                    day=(CommonTree)match(input,INTEGER,FOLLOW_INTEGER_in_date1413); 

                    }
                    break;
                case 2 :
                    // E:\\projects\\r-forge\\speedR\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:110:92: day= DATENUM
                    {
                    day=(CommonTree)match(input,DATENUM,FOLLOW_DATENUM_in_date1419); 

                    }
                    break;

            }


            match(input, Token.UP, null); 


            // TEMPLATE REWRITE
            // 110:106: -> {!isPOSIXct}? date(year=$year.textmonth=$month.textday=$day.text)
            if (!isPOSIXct) {
                retval.st = templateLib.getInstanceOf("date",
              new STAttrMap().put("year", (year!=null?year.getText():null)).put("month", (month!=null?month.getText():null)).put("day", (day!=null?day.getText():null)));
            }
            else // 111:18: -> time(year=$year.textmonth=$month.textday=$day.texthour=nullminute=nullsecond=null)
            {
                retval.st = templateLib.getInstanceOf("time",
              new STAttrMap().put("year", (year!=null?year.getText():null)).put("month", (month!=null?month.getText():null)).put("day", (day!=null?day.getText():null)).put("hour", null).put("minute", null).put("second", null));
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "date"

    // Delegated rules


    protected DFA2 dfa2 = new DFA2(this);
    static final String DFA2_eotS =
        "\41\uffff";
    static final String DFA2_eofS =
        "\41\uffff";
    static final String DFA2_minS =
        "\1\6\5\uffff\5\2\2\uffff\5\4\17\uffff";
    static final String DFA2_maxS =
        "\1\42\5\uffff\5\2\2\uffff\5\17\17\uffff";
    static final String DFA2_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\5\uffff\1\25\1\26\5\uffff\1\6\1\13"+
        "\1\14\1\7\1\15\1\16\1\10\1\17\1\20\1\11\1\21\1\22\1\12\1\23\1\24";
    static final String DFA2_specialS =
        "\41\uffff}>";
    static final String[] DFA2_transitionS = {
            "\1\1\1\2\1\3\21\uffff\1\4\1\5\1\13\1\14\1\6\1\7\1\10\1\11\1"+
            "\12",
            "",
            "",
            "",
            "",
            "",
            "\1\15",
            "\1\16",
            "\1\17",
            "\1\20",
            "\1\21",
            "",
            "",
            "\1\23\1\24\7\uffff\3\22",
            "\1\26\1\27\7\uffff\3\25",
            "\1\31\1\32\7\uffff\3\30",
            "\1\34\1\35\7\uffff\3\33",
            "\1\37\1\40\7\uffff\3\36",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "50:1: predicate : ( ^( OR a= predicate b= predicate ) -> or(a=$a.stb=$b.st) | ^( AND a= predicate b= predicate ) -> and(a=$a.stb=$b.st) | ^( NOT p= predicate ) -> not(a=$p.st) | 'is.na' -> isna(colIndex=col) | 'is.null' -> isna(colIndex=col) | ^( '=' number ) -> {isDataframe}? equal(colIndex=dataframecola=$number.textisdataframe=isDataframe) -> equal(colIndex=cola=$number.text) | ^( '<' number ) -> {isDataframe}? less(colIndex=dataframecola=$number.textisdataframe=isDataframe) -> less(colIndex=cola=$number.text) | ^( '<=' number ) -> {isDataframe}? lessOrEqual(colIndex=dataframecola=$number.textisdataframe=isDataframe) -> lessOrEqual(colIndex=cola=$number.text) | ^( '>' number ) -> {isDataframe}? greater(colIndex=dataframecola=$number.textisdataframe=isDataframe) -> greater(colIndex=cola=$number.text) | ^( '>=' number ) -> {isDataframe}? greaterOrEqual(colIndex=dataframecola=$number.textisdataframe=isDataframe) -> greaterOrEqual(colIndex=cola=$number.text) | ^( '=' date ) -> {isDataframe}? equal(colIndex=dataframecola=$date.stisdataframe=isDataframe) -> equal(colIndex=cola=$date.st) | ^( '=' time ) -> {isDataframe}? equal(colIndex=dataframecola=$time.stisdataframe=isDataframe) -> equal(colIndex=cola=$time.st) | ^( '<' date ) -> {isDataframe}? less(colIndex=dataframecola=$date.stisdataframe=isDataframe) -> less(colIndex=cola=$date.st) | ^( '<' time ) -> {isDataframe}? less(colIndex=dataframecola=$time.stisdataframe=isDataframe) -> less(colIndex=cola=$time.st) | ^( '<=' date ) -> {isDataframe}? lessOrEqual(colIndex=dataframecola=$date.stisdataframe=isDataframe) -> lessOrEqual(colIndex=cola=$date.st) | ^( '<=' time ) -> {isDataframe}? lessOrEqual(colIndex=dataframecola=$time.stisdataframe=isDataframe) -> lessOrEqual(colIndex=cola=$time.st) | ^( '>' date ) -> {isDataframe}? greater(colIndex=dataframecola=$date.stisdataframe=isDataframe) -> greater(colIndex=cola=$date.st) | ^( '>' time ) -> {isDataframe}? greater(colIndex=dataframecola=$time.stisdataframe=isDataframe) -> greater(colIndex=cola=$time.st) | ^( '>=' date ) -> {isDataframe}? greaterOrEqual(colIndex=dataframecola=$date.stisdataframe=isDataframe) -> greaterOrEqual(colIndex=cola=$date.st) | ^( '>=' time ) -> {isDataframe}? greaterOrEqual(colIndex=dataframecola=$time.stisdataframe=isDataframe) -> greaterOrEqual(colIndex=cola=$time.st) | ^( 'contains' e= STRING_LITERAL ) -> {hasRowNames}? contains_rownames(rownamesindex=coltext=$e.text) -> contains(colIndex=coltext=$e.text) | ^( 'equals' ( TO )? e= STRING_LITERAL ) -> {hasRowNames}? equal_rownames(rownamesindex=coltext=$e.text) -> {isDataframe}? equal(colIndex=dataframecola=$e.textisdataframe=isDataframe) -> equal(colIndex=cola=$e.text));";
        }
    }
 

    public static final BitSet FOLLOW_predicate_in_rcode58 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OR_in_predicate77 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_predicate_in_predicate81 = new BitSet(new long[]{0x00000007FC0001C0L});
    public static final BitSet FOLLOW_predicate_in_predicate85 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_AND_in_predicate106 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_predicate_in_predicate110 = new BitSet(new long[]{0x00000007FC0001C0L});
    public static final BitSet FOLLOW_predicate_in_predicate114 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NOT_in_predicate135 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_predicate_in_predicate139 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_26_in_predicate154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_predicate168 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_predicate185 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_number_in_predicate188 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_31_in_predicate237 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_number_in_predicate240 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_32_in_predicate289 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_number_in_predicate292 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_33_in_predicate341 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_number_in_predicate344 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_34_in_predicate393 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_number_in_predicate396 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_30_in_predicate445 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_date_in_predicate448 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_30_in_predicate496 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_time_in_predicate499 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_31_in_predicate565 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_date_in_predicate568 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_31_in_predicate616 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_time_in_predicate619 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_32_in_predicate683 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_date_in_predicate686 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_32_in_predicate735 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_time_in_predicate738 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_33_in_predicate805 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_date_in_predicate808 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_33_in_predicate856 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_time_in_predicate859 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_34_in_predicate923 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_date_in_predicate926 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_34_in_predicate975 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_time_in_predicate978 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_28_in_predicate1035 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_predicate1039 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_29_in_predicate1122 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_TO_in_predicate1125 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_predicate1130 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_set_in_number0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TIME_in_time1269 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DATE_in_time1272 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INTEGER_in_time1277 = new BitSet(new long[]{0x0000000000012000L});
    public static final BitSet FOLLOW_DATENUM_in_time1283 = new BitSet(new long[]{0x0000000000012000L});
    public static final BitSet FOLLOW_INTEGER_in_time1289 = new BitSet(new long[]{0x0000000000012000L});
    public static final BitSet FOLLOW_DATENUM_in_time1295 = new BitSet(new long[]{0x0000000000012000L});
    public static final BitSet FOLLOW_INTEGER_in_time1301 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DATENUM_in_time1307 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_INTEGER_in_time1314 = new BitSet(new long[]{0x0000000000012000L});
    public static final BitSet FOLLOW_DATENUM_in_time1320 = new BitSet(new long[]{0x0000000000012000L});
    public static final BitSet FOLLOW_INTEGER_in_time1326 = new BitSet(new long[]{0x0000000000012000L});
    public static final BitSet FOLLOW_DATENUM_in_time1332 = new BitSet(new long[]{0x0000000000012000L});
    public static final BitSet FOLLOW_INTEGER_in_time1338 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DATENUM_in_time1344 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DATE_in_date1384 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INTEGER_in_date1389 = new BitSet(new long[]{0x0000000000012000L});
    public static final BitSet FOLLOW_DATENUM_in_date1395 = new BitSet(new long[]{0x0000000000012000L});
    public static final BitSet FOLLOW_INTEGER_in_date1401 = new BitSet(new long[]{0x0000000000012000L});
    public static final BitSet FOLLOW_DATENUM_in_date1407 = new BitSet(new long[]{0x0000000000012000L});
    public static final BitSet FOLLOW_INTEGER_in_date1413 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DATENUM_in_date1419 = new BitSet(new long[]{0x0000000000000008L});

}