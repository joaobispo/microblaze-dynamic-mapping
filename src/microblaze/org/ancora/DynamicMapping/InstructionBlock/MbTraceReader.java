/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.DynamicMapping.InstructionBlock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;
import org.ancora.MicroBlaze.InstructionName;
import org.ancora.SharedLibrary.ParseUtils;

/**
 * Reads MicroBlaze traces as if they where executing in an instruction bus.
 *
 * @author Joao Bispo
 */
public class MbTraceReader implements InstructionBusReader {

   /**
    * Private constructor for static creator method.
    *
    * @param reader
    */
    private MbTraceReader(BufferedReader reader) {
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
   public static MbTraceReader createTraceReader(File traceFile) {

      FileInputStream stream = null;
      InputStreamReader streamReader = null;

        try {
            // Try to read the contents of the file into the StringBuilder
            stream = new FileInputStream(traceFile);
            streamReader = new InputStreamReader(stream, DEFAULT_CHAR_SET);
            BufferedReader newReader = new BufferedReader(streamReader);
            return new MbTraceReader(newReader);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MbTraceReader.class.getName()).
                    warning("FileNotFoundException: "+ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MbTraceReader.class.getName()).
                    warning("UnsupportedEncodingException: "+ex.getMessage());
        }

      return null;
   }

   public static MbInstruction createMicroBlazeInstruction(String line) {
      /// Split the trace instruction in parts
      int whiteSpaceIndex = ParseUtils.indexOfFirstWhiteSpace(line);

      /// Get Address
      String addressString = line.substring(0, whiteSpaceIndex);
      // Remove prefix
      addressString = addressString.substring("0x".length());
      // Parse to integer
      int instructionAddress = Integer.valueOf(addressString, 16);

      /// Get Instruction
      String instruction = line.substring(whiteSpaceIndex).trim();

      /// Get InstructionName
      whiteSpaceIndex = ParseUtils.indexOfFirstWhiteSpace(instruction);
      String instNameString = instruction.substring(0, whiteSpaceIndex);
      InstructionName instName = InstructionName.getEnum(instNameString);

      return new MbInstruction(instructionAddress, instruction, instName);
   }


    /**
     * @return the next line in the file which qualifies as an instruction, or
     * null if the end of the stream has been reached.
     * A line is considered as a trace instruction if it starts with "0x".
     */
    public GenericInstruction nextInstruction() {

        // While there are lines and a trace instruction was not found, loop.
        String line = null;
        while(true) {

           try {
               // Read next line
              line = reader.readLine();

              // Check if end of stream has arrived.
              if (line == null) {
                 return null;
              }

              // Check if current line is a trace instruction
              if (line.startsWith(TRACE_PREFIX)) {
                 // Create MicroBlazeInstruction
                 MbInstruction instruction = createMicroBlazeInstruction(line);
                 return instruction;
              }

           } catch (IOException ex) {
              Logger.getLogger(MbTraceReader.class.getName()).
                      warning("IOException: "+ex.getMessage());
              return null;
           }
        }

    }

   /**
    * INSTANCE VARIABLES
    */
    private final BufferedReader reader;

    // Definitions
    private final String TRACE_PREFIX = "0x";

   /**
    * Default CharSet used in file operations.
    */
   public static final String DEFAULT_CHAR_SET = "UTF-8";



}
