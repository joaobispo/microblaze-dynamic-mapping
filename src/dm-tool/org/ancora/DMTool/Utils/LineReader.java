/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.DMTool.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

/**
 * Reads lines from a File, one by one.
 *
 * @author Joao Bispo
 */
public class LineReader {

   /**
    * Private constructor for static creator method.
    *
    * @param reader
    */
    private LineReader(BufferedReader reader) {
       this.reader = reader;
    }

   /**
    * Builds a TraceReader from the given file. If the object could
    * not be created, returns null.
    *
    * <p>Creating a TraceReader involves File operations which can lead
    * to failure in creation of the object. That is why we use a public
    * static method instead of a constructor.
    *
    * @param traceFile a file representing a MicroBlaze Trace, as in the format
    * of the tool of the Ancora Group (not avaliable yet).
    * @return a TraceReader If the object could not be created, returns null.
    */
   public static LineReader createLineReader(File file) {

      FileInputStream stream = null;
      InputStreamReader streamReader = null;

        try {
            // Try to read the contents of the file into the StringBuilder
            stream = new FileInputStream(file);
            streamReader = new InputStreamReader(stream, DEFAULT_CHAR_SET);
            BufferedReader newReader = new BufferedReader(streamReader);
            return new LineReader(newReader);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LineReader.class.getName()).
                    warning("FileNotFoundException: "+ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LineReader.class.getName()).
                    warning("UnsupportedEncodingException: "+ex.getMessage());
        }

      return null;
   }


    /**
     * @return the next line in the file, or
     * null if the end of the stream has been reached.
     */
    public String nextLine() {
           try {
               // Read next line
              return reader.readLine();
           } catch (IOException ex) {
              Logger.getLogger(LineReader.class.getName()).
                      warning("IOException: "+ex.getMessage());
              return null;
           }
    }

   /**
    * INSTANCE VARIABLES
    */
    private final BufferedReader reader;

   /**
    * Default CharSet used in file operations.
    */
   public static final String DEFAULT_CHAR_SET = "UTF-8";



}
