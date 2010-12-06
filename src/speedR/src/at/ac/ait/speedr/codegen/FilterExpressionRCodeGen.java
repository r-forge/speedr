// $ANTLR 3.2 Sep 23, 2009 12:02:23 E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g 2010-12-06 21:13:15
 
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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "OR", "AND", "NOT", "LP", "RP", "STRING_LITERAL", "TO", "INTEGER", "FLOAT", "SIGN", "NONCONTROL_CHAR", "LETTER", "DIGIT", "SYMBOL", "SPACE", "LOWER", "UPPER", "WS", "'is.na'", "'contains'", "'equals'", "'='", "'<'", "'<='", "'>'", "'>='"
    };
    public static final int INTEGER=11;
    public static final int SIGN=13;
    public static final int T__29=29;
    public static final int T__28=28;
    public static final int T__27=27;
    public static final int SYMBOL=17;
    public static final int T__26=26;
    public static final int T__25=25;
    public static final int TO=10;
    public static final int T__24=24;
    public static final int RP=8;
    public static final int LETTER=15;
    public static final int T__23=23;
    public static final int T__22=22;
    public static final int LP=7;
    public static final int FLOAT=12;
    public static final int NOT=6;
    public static final int AND=5;
    public static final int SPACE=18;
    public static final int EOF=-1;
    public static final int STRING_LITERAL=9;
    public static final int WS=21;
    public static final int NONCONTROL_CHAR=14;
    public static final int OR=4;
    public static final int DIGIT=16;
    public static final int LOWER=19;
    public static final int UPPER=20;

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
    public String getGrammarFileName() { return "E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g"; }


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



    public static class rcode_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "rcode"
    // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:31:1: rcode : predicate -> selector(predicates=$predicate.st);
    public final FilterExpressionRCodeGen.rcode_return rcode() throws RecognitionException {
        FilterExpressionRCodeGen.rcode_return retval = new FilterExpressionRCodeGen.rcode_return();
        retval.start = input.LT(1);

        FilterExpressionRCodeGen.predicate_return predicate1 = null;


        try {
            // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:31:7: ( predicate -> selector(predicates=$predicate.st))
            // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:31:9: predicate
            {
            pushFollow(FOLLOW_predicate_in_rcode58);
            predicate1=predicate();

            state._fsp--;



            // TEMPLATE REWRITE
            // 31:19: -> selector(predicates=$predicate.st)
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
    // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:34:1: predicate : ( ^( OR a= predicate b= predicate ) -> or(a=$a.stb=$b.st) | ^( AND a= predicate b= predicate ) -> and(a=$a.stb=$b.st) | ^( NOT p= predicate ) -> not(a=$p.st) | 'is.na' -> isna(colIndex=col) | ^( '=' number ) -> equal(colIndex=cola=$number.text) | ^( '<' number ) -> less(colIndex=cola=$number.text) | ^( '<=' number ) -> lessOrEqual(colIndex=cola=$number.text) | ^( '>' number ) -> greater(colIndex=cola=$number.text) | ^( '>=' number ) -> greaterOrEqual(colIndex=cola=$number.text) | ^( 'contains' e= STRING_LITERAL ) -> {hasRowNames}? contains_rownames(rownamesindex=coltext=$e.text) -> contains(colIndex=coltext=$e.text) | ^( 'equals' ( TO )? e= STRING_LITERAL ) -> {hasRowNames}? equal_rownames(rownamesindex=coltext=$e.text) -> equal(colIndex=cola=$e.text));
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


        try {
            // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:34:10: ( ^( OR a= predicate b= predicate ) -> or(a=$a.stb=$b.st) | ^( AND a= predicate b= predicate ) -> and(a=$a.stb=$b.st) | ^( NOT p= predicate ) -> not(a=$p.st) | 'is.na' -> isna(colIndex=col) | ^( '=' number ) -> equal(colIndex=cola=$number.text) | ^( '<' number ) -> less(colIndex=cola=$number.text) | ^( '<=' number ) -> lessOrEqual(colIndex=cola=$number.text) | ^( '>' number ) -> greater(colIndex=cola=$number.text) | ^( '>=' number ) -> greaterOrEqual(colIndex=cola=$number.text) | ^( 'contains' e= STRING_LITERAL ) -> {hasRowNames}? contains_rownames(rownamesindex=coltext=$e.text) -> contains(colIndex=coltext=$e.text) | ^( 'equals' ( TO )? e= STRING_LITERAL ) -> {hasRowNames}? equal_rownames(rownamesindex=coltext=$e.text) -> equal(colIndex=cola=$e.text))
            int alt2=11;
            switch ( input.LA(1) ) {
            case OR:
                {
                alt2=1;
                }
                break;
            case AND:
                {
                alt2=2;
                }
                break;
            case NOT:
                {
                alt2=3;
                }
                break;
            case 22:
                {
                alt2=4;
                }
                break;
            case 25:
                {
                alt2=5;
                }
                break;
            case 26:
                {
                alt2=6;
                }
                break;
            case 27:
                {
                alt2=7;
                }
                break;
            case 28:
                {
                alt2=8;
                }
                break;
            case 29:
                {
                alt2=9;
                }
                break;
            case 23:
                {
                alt2=10;
                }
                break;
            case 24:
                {
                alt2=11;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:34:12: ^( OR a= predicate b= predicate )
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
                    // 34:42: -> or(a=$a.stb=$b.st)
                    {
                        retval.st = templateLib.getInstanceOf("or",
                      new STAttrMap().put("a", (a!=null?a.st:null)).put("b", (b!=null?b.st:null)));
                    }


                    }
                    break;
                case 2 :
                    // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:35:5: ^( AND a= predicate b= predicate )
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
                    // 35:36: -> and(a=$a.stb=$b.st)
                    {
                        retval.st = templateLib.getInstanceOf("and",
                      new STAttrMap().put("a", (a!=null?a.st:null)).put("b", (b!=null?b.st:null)));
                    }


                    }
                    break;
                case 3 :
                    // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:36:5: ^( NOT p= predicate )
                    {
                    match(input,NOT,FOLLOW_NOT_in_predicate135); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_predicate_in_predicate139);
                    p=predicate();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 36:24: -> not(a=$p.st)
                    {
                        retval.st = templateLib.getInstanceOf("not",
                      new STAttrMap().put("a", (p!=null?p.st:null)));
                    }


                    }
                    break;
                case 4 :
                    // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:37:4: 'is.na'
                    {
                    match(input,22,FOLLOW_22_in_predicate154); 


                    // TEMPLATE REWRITE
                    // 37:12: -> isna(colIndex=col)
                    {
                        retval.st = templateLib.getInstanceOf("isna",
                      new STAttrMap().put("colIndex", col));
                    }


                    }
                    break;
                case 5 :
                    // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:38:5: ^( '=' number )
                    {
                    match(input,25,FOLLOW_25_in_predicate170); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_number_in_predicate173);
                    number2=number();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 38:20: -> equal(colIndex=cola=$number.text)
                    {
                        retval.st = templateLib.getInstanceOf("equal",
                      new STAttrMap().put("colIndex", col).put("a", (number2!=null?(input.getTokenStream().toString(
                      input.getTreeAdaptor().getTokenStartIndex(number2.start),
                      input.getTreeAdaptor().getTokenStopIndex(number2.start))):null)));
                    }


                    }
                    break;
                case 6 :
                    // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:39:5: ^( '<' number )
                    {
                    match(input,26,FOLLOW_26_in_predicate194); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_number_in_predicate197);
                    number3=number();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 39:20: -> less(colIndex=cola=$number.text)
                    {
                        retval.st = templateLib.getInstanceOf("less",
                      new STAttrMap().put("colIndex", col).put("a", (number3!=null?(input.getTokenStream().toString(
                      input.getTreeAdaptor().getTokenStartIndex(number3.start),
                      input.getTreeAdaptor().getTokenStopIndex(number3.start))):null)));
                    }


                    }
                    break;
                case 7 :
                    // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:40:5: ^( '<=' number )
                    {
                    match(input,27,FOLLOW_27_in_predicate218); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_number_in_predicate221);
                    number4=number();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 40:21: -> lessOrEqual(colIndex=cola=$number.text)
                    {
                        retval.st = templateLib.getInstanceOf("lessOrEqual",
                      new STAttrMap().put("colIndex", col).put("a", (number4!=null?(input.getTokenStream().toString(
                      input.getTreeAdaptor().getTokenStartIndex(number4.start),
                      input.getTreeAdaptor().getTokenStopIndex(number4.start))):null)));
                    }


                    }
                    break;
                case 8 :
                    // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:41:5: ^( '>' number )
                    {
                    match(input,28,FOLLOW_28_in_predicate242); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_number_in_predicate245);
                    number5=number();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 41:20: -> greater(colIndex=cola=$number.text)
                    {
                        retval.st = templateLib.getInstanceOf("greater",
                      new STAttrMap().put("colIndex", col).put("a", (number5!=null?(input.getTokenStream().toString(
                      input.getTreeAdaptor().getTokenStartIndex(number5.start),
                      input.getTreeAdaptor().getTokenStopIndex(number5.start))):null)));
                    }


                    }
                    break;
                case 9 :
                    // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:42:5: ^( '>=' number )
                    {
                    match(input,29,FOLLOW_29_in_predicate266); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_number_in_predicate269);
                    number6=number();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 42:21: -> greaterOrEqual(colIndex=cola=$number.text)
                    {
                        retval.st = templateLib.getInstanceOf("greaterOrEqual",
                      new STAttrMap().put("colIndex", col).put("a", (number6!=null?(input.getTokenStream().toString(
                      input.getTreeAdaptor().getTokenStartIndex(number6.start),
                      input.getTreeAdaptor().getTokenStopIndex(number6.start))):null)));
                    }


                    }
                    break;
                case 10 :
                    // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:43:4: ^( 'contains' e= STRING_LITERAL )
                    {
                    match(input,23,FOLLOW_23_in_predicate289); 

                    match(input, Token.DOWN, null); 
                    e=(CommonTree)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_predicate293); 

                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 43:35: -> {hasRowNames}? contains_rownames(rownamesindex=coltext=$e.text)
                    if (hasRowNames) {
                        retval.st = templateLib.getInstanceOf("contains_rownames",
                      new STAttrMap().put("rownamesindex", col).put("text", (e!=null?e.getText():null)));
                    }
                    else // 44:7: -> contains(colIndex=coltext=$e.text)
                    {
                        retval.st = templateLib.getInstanceOf("contains",
                      new STAttrMap().put("colIndex", col).put("text", (e!=null?e.getText():null)));
                    }


                    }
                    break;
                case 11 :
                    // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:45:5: ^( 'equals' ( TO )? e= STRING_LITERAL )
                    {
                    match(input,24,FOLLOW_24_in_predicate335); 

                    match(input, Token.DOWN, null); 
                    // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:45:17: ( TO )?
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0==TO) ) {
                        alt1=1;
                    }
                    switch (alt1) {
                        case 1 :
                            // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:45:17: TO
                            {
                            match(input,TO,FOLLOW_TO_in_predicate338); 

                            }
                            break;

                    }

                    e=(CommonTree)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_predicate343); 

                    match(input, Token.UP, null); 


                    // TEMPLATE REWRITE
                    // 45:39: -> {hasRowNames}? equal_rownames(rownamesindex=coltext=$e.text)
                    if (hasRowNames) {
                        retval.st = templateLib.getInstanceOf("equal_rownames",
                      new STAttrMap().put("rownamesindex", col).put("text", (e!=null?e.getText():null)));
                    }
                    else // 46:9: -> equal(colIndex=cola=$e.text)
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
    // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:49:1: number : ( INTEGER | FLOAT );
    public final FilterExpressionRCodeGen.number_return number() throws RecognitionException {
        FilterExpressionRCodeGen.number_return retval = new FilterExpressionRCodeGen.number_return();
        retval.start = input.LT(1);

        try {
            // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:49:9: ( INTEGER | FLOAT )
            // E:\\projects\\r-forge\\speedr\\src\\speedR\\src\\at\\ac\\ait\\speedr\\codegen\\FilterExpressionRCodeGen.g:
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

    // Delegated rules


 

    public static final BitSet FOLLOW_predicate_in_rcode58 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OR_in_predicate77 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_predicate_in_predicate81 = new BitSet(new long[]{0x000000003FC00070L});
    public static final BitSet FOLLOW_predicate_in_predicate85 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_AND_in_predicate106 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_predicate_in_predicate110 = new BitSet(new long[]{0x000000003FC00070L});
    public static final BitSet FOLLOW_predicate_in_predicate114 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NOT_in_predicate135 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_predicate_in_predicate139 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_22_in_predicate154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_predicate170 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_number_in_predicate173 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_26_in_predicate194 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_number_in_predicate197 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_27_in_predicate218 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_number_in_predicate221 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_28_in_predicate242 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_number_in_predicate245 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_29_in_predicate266 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_number_in_predicate269 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_23_in_predicate289 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_predicate293 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_24_in_predicate335 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_TO_in_predicate338 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_predicate343 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_set_in_number0 = new BitSet(new long[]{0x0000000000000002L});

}