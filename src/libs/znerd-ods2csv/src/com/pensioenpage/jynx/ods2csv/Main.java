// Copyright 2007-2009, PensioenPage B.V.
package com.pensioenpage.jynx.ods2csv;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Command-line program for converting a single ODS document to CSV text.
 * Input is expected to come from <em>stdin</em>, output goes to
 * <em>stdout</em> and errors to <em>stderr</em>.
 *
 * <p>This program returns 0 on succes. Any other exit code indicates failure.
 *
 * @author <a href="mailto:ernst@pensioenpage.com">Ernst de Haan</a>
 */
public final class Main extends Object {

   //-------------------------------------------------------------------------
   // Class functions
   //-------------------------------------------------------------------------

   /**
    * Converts from <em>stdin</em> to <em>stdout</em>.
    *
    * @param args
    *    the arguments for the program, can be <code>null</code>.
    */
   public static void main(String[] args) {

      // Convert
      try {
//         new Converter().convert(System.in, System.out);
         new Converter().convert(new BufferedInputStream(new FileInputStream("C:/Documents and Settings/visnei/My Documents/_2_decodersizes.ods")),
                 new BufferedOutputStream(new FileOutputStream("F:/oo.csv")));
         System.exit(0);

      // All exceptions are caught
      } catch (Throwable e) {
         e.printStackTrace();
         System.exit(1);
      }
   }


   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>Main</code> instance.
    */
   private Main() {
      // empty
   }
}
