/*
 *  Copyright 2010 Ancora Research Group.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.ancora.MicroBlaze.Trace;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.common.IoUtilsAppend;
import org.ancora.common.LineReader;

/**
 * Parses Properties files with information about the trace.
 *
 * @author Joao Bispo
 */
public class TraceDefinitions {


/*
   private TraceDefinitions(Properties traceProperties) {
      properties = traceProperties;
   }
*/

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
   /*
   public static TraceDefinitions getTraceProperties(File traceFile) {
      // Get Properties file corresponding to this trace file
      File tracePropertiesFile = getTracePropertiesFile(traceFile);
      // Check if file exists
      if(!tracePropertiesFile.exists()) {
         return null;
      }

      Properties properties = IoUtils.loadProperties(tracePropertiesFile);

      if(properties == null) {
         return null;
      }

      return new TraceDefinitions(properties);
   }
*/

   





   /**
    * INSTANCE VARIABLES
    */

   public static final String TRACE_PREFIX = "0x";
   public static final String TRACE_EXTENSION = "trace";
   public static final String PROPERTIES_EXTENSION = "properties";
   public static final String EXTENSION_SEPARATOR = ".";
   public static final String PROPERTIES_SEPARATOR = ":";
}
