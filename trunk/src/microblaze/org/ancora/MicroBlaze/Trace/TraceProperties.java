/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.MicroBlaze.Trace;

import java.io.File;
import java.util.Properties;
import org.ancora.SharedLibrary.IoUtils;

/**
 *
 * @author Joao Bispo
 */
public class TraceProperties {

   private TraceProperties(Properties properties) {
      this.properties = properties;
   }

   /**
    * Given a File object representing a Trace Properties file, loads the
    * contents of the file into a TraceDefinitions object.
    *
    * <p>If an error occurs (ex.: the File argument does not represent a file,
    * could not load the Properties object) returns null.
    *
    * @param tracePropertiesFile
    * @return
    */
   public static TraceProperties getTraceProperties(File traceFile) {
      // Get Properties file corresponding to this trace file
      File tracePropertiesFile = TraceUtils.getTracePropertiesFile(traceFile, Key.values());
      // Check if file exists
      if(!tracePropertiesFile.exists()) {
         return null;
      }

      Properties properties = IoUtils.loadProperties(tracePropertiesFile);

      if(properties == null) {
         return null;
      }

      return new TraceProperties(properties);
   }



/*
   public String getValue(Key key) {
      return properties.getProperty(key.name());
   }
 */



   public float getCpi() {
      String value = TraceUtils.safeGet(Key.cpi, properties);
      return Float.parseFloat(value);
   }

   public int getInstructions() {
     String value = TraceUtils.safeGet(Key.instructions, properties);
      return Integer.parseInt(value);
   }

   public int getCycles() {
      String value = TraceUtils.safeGet(Key.cycles, properties);
      return Integer.parseInt(value);
   }

   public enum Key {
      instructions,
      cycles,
      cpi
   }

   /**
    * INSTANCE VARIABLES
    */
      final private Properties properties;
}
