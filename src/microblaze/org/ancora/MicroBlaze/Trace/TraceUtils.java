/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.MicroBlaze.Trace;

import static org.ancora.MicroBlaze.Trace.TraceDefinitions.EXTENSION_SEPARATOR;
import static org.ancora.MicroBlaze.Trace.TraceDefinitions.PROPERTIES_SEPARATOR;
import static org.ancora.MicroBlaze.Trace.TraceDefinitions.PROPERTIES_EXTENSION;
import static org.ancora.MicroBlaze.Trace.TraceDefinitions.TRACE_PREFIX;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import org.ancora.DynamicMapping.InstructionBlock.Listeners.InstructionBlockStats;
import org.ancora.common.IoUtilsAppend;
import org.ancora.common.LineReader;

/**
 *
 * @author Joao Bispo
 */
public class TraceUtils {

   public static File getTracePropertiesFile(File traceFile, Enum[] keys) {
      // Get the filename
      String tracePropertiesFilename = IoUtilsAppend.removeExtension(traceFile.getName(), EXTENSION_SEPARATOR) + EXTENSION_SEPARATOR + PROPERTIES_EXTENSION;

      // Get the file
      File tracePropertiesFile = new File(tracePropertiesFilename);

      // Check if it exists
      if (!tracePropertiesFile.exists()) {
         boolean success = createTraceProperties(traceFile, tracePropertiesFile, keys);
         if (!success) {
            Logger.getLogger(TraceDefinitions.class.getName()).
                    warning("Could not find a TraceProperties file, and failed to create it.");
         }
      }

      return tracePropertiesFile;
   }



 public static Properties getPropertiesFromTrace(File traceFile, Enum[] keys) {
    

    // Open trace file for reading
      LineReader lineReader = LineReader.createLineReader(traceFile);

      // Prepare first line
      String line = lineReader.nextLine();

      // Create new properties object
      Properties newProps = new Properties();

      int counter = 0;
      int totalElements = keys.length;

      while(counter < totalElements || line != null) {
         // If line starts with trace prefix, skip it
         if(line.startsWith(TRACE_PREFIX)) {
            line = lineReader.nextLine();
            continue;
         }

         // Split line by the property separator
         String[] propertyLine = line.split(PROPERTIES_SEPARATOR);

         // Check if size equals two
         if (propertyLine.length != 2) {
            line = lineReader.nextLine();
            continue;
         }

         // We found a possibly key-value pair! Check if it matches any of the keys.
         for(Enum key : keys) {
            if(key.name().equals(propertyLine[0].trim())) {
               // Store pair in properties
               newProps.setProperty(key.name(), propertyLine[1].trim());
               counter++;
               continue;
            }
         }

         line = lineReader.nextLine();
      }


      if (counter == totalElements) {
         return new Properties(newProps);
      } else {
         return null;
      }
   }

   public static boolean createTraceProperties(File traceFile, File tracePropertiesFile, Enum[] keys) {
      Properties props = getPropertiesFromTrace(traceFile, keys);

      if(props == null) {
         return false;
      }

      try {
         props.store(new FileOutputStream(tracePropertiesFile), null);
         return true;
      } catch (IOException ex) {
         Logger.getLogger(TraceUtils.class.getName()).
                 warning("IOException:" + ex);
         return false;
      }
   }
   
   /*
      public static boolean createTraceProperties(File traceFile, File tracePropertiesFile, Enum[] keys) {
      // Open trace file for reading
      LineReader lineReader = LineReader.createLineReader(traceFile);

      // Prepare first line
      String line = lineReader.nextLine();

      // Create new properties object
      Properties newProps = new Properties();

      int counter = 0;
      int totalElements = keys.length;

      while(counter < totalElements || line != null) {
         // If line starts with trace prefix, skip it
         if(line.startsWith(TRACE_PREFIX)) {
            line = lineReader.nextLine();
            continue;
         }

         // Split line by the property separator
         String[] propertyLine = line.split(PROPERTIES_SEPARATOR);

         // Check if size equals two
         if (propertyLine.length != 2) {
            line = lineReader.nextLine();
            continue;
         }

         // We found a possibly key-value pair! Check if it matches any of the keys.
         for(Enum key : keys) {
            if(key.name().equals(propertyLine[0].trim())) {
               // Store pair in properties
               newProps.setProperty(key.name(), propertyLine[1].trim());
               counter++;
               continue;
            }
         }

         line = lineReader.nextLine();
      }


      if (counter == totalElements) {
         // Save file
         // Write properties file.
         try {
            newProps.store(new FileOutputStream(tracePropertiesFile), null);
         } catch (IOException ex) {
            Logger.getLogger(TraceUtils.class.getName()).
                    warning("IOException:"+ex);
            return false;
         }
         return true;
      } else {
         return false;
      }
   }
*/
    public static String safeGet(Enum key, Properties properties) {
      String cpiString = properties.getProperty(key.name());
      if(cpiString == null) {
         Logger.getLogger(TraceDefinitions.class.getName()).
             warning("Properties does not have a '"+key+"' value. Returning null.");
      }

      return cpiString;
   }

    public static boolean testStats(File trace, InstructionBlockStats ibStats) {
      // Get trace properties
      TraceProperties props = TraceProperties.getTraceProperties(trace);

      // Check if Partitioned Instructions Add Up
      long blockInst = ibStats.getTotalInstructions();
      long traceInst = props.getInstructions();

      if(blockInst != traceInst) {
         Logger.getLogger(TraceUtils.class.getName()).
                 warning("Total instructions does not add up: Trace("+traceInst+") " +
                 "vs. Partitioner("+blockInst+")");
         return false;
      }

      return true;
   }
}
