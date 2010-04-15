/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.MicroBlaze;

//import static org.ancora.MicroBlaze.Trace.TraceDefinitions.EXTENSION_SEPARATOR;
import static org.ancora.MicroBlaze.Trace.TraceDefinitions.TRACE_EXTENSION;


import java.io.File;
import java.util.logging.Logger;
import org.ancora.DynamicMapping.InstructionBlock.InstructionBusReader;
import org.ancora.DynamicMapping.InstructionBlock.Listeners.InstructionBlockPrinter;
import org.ancora.DynamicMapping.InstructionBlock.Listeners.InstructionBlockStats;
import org.ancora.DynamicMapping.InstructionBlock.MbTraceReader;
import org.ancora.DynamicMapping.Partitioning.BasicBlock;
import org.ancora.DynamicMapping.Partitioning.MbJumpFilter;
import org.ancora.DynamicMapping.Partitioning.Partitioner;
import org.ancora.MicroBlaze.Trace.TraceDefinitions;
import org.ancora.MicroBlaze.Trace.TraceProperties;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.common.ExtensionFilter;
import org.ancora.common.IoUtilsAppend;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class Tester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        setupProgram();
        executeTraceReader();
    }

    private static void setupProgram() {
      // Configure Logger to capture all output to console
      LoggingUtils.setupConsoleOnly();
   }

   private static void executeTraceReader() {
      
      String foldername = "../data/traces-without-optimization/";
      File folder = IoUtils.safeFolder(foldername);

      File[] traces = getTraces(folder);
      
      for(File trace : traces) {
         processTrace(trace);
      }
   }

   private static File[] getTraces(File traceFolder) {
      // Later, I can add a file filter by extension
      return traceFolder.listFiles(new ExtensionFilter(TRACE_EXTENSION));
   }

   private static void processTrace(File trace) {
      //String filename = IoUtilsAppend.removeExtension(trace.getName(), EXTENSION_SEPARATOR);
      System.out.println("Processing "+trace.getName()+"...");


      InstructionBusReader busReader = MbTraceReader.createTraceReader(trace);
      Partitioner partitioner = new BasicBlock(new MbJumpFilter());
      InstructionBlockStats ibStats = new InstructionBlockStats();
      partitioner.addListener(ibStats);
      
      partitioner.run(busReader);

      check(trace, ibStats);
      
   }

   private static boolean check(File trace, InstructionBlockStats ibStats) {
      // Get trace properties filename
      //TraceDefinitions.getPropertiesFilename(IoUtilsAppend.removeExtension(trace.getName(), EXTENSION_SEPARATOR));
      //String tracePropFilename = IoUtilsAppend.removeExtension(trace.getName(), EXTENSION_SEPARATOR)
      //        + EXTENSION_SEPARATOR + PROPERTIES_EXTENSION;
      // Get trace properties
      TraceProperties props = TraceProperties.getTraceProperties(trace);

      // Check if Partitioned Instructions Add Up
      int blockInst = ibStats.getTotalInstructions();
      int traceInst = props.getInstructions();

      if(blockInst != traceInst) {
         Logger.getLogger(Tester.class.getName()).
                 warning("Total instructions does not add up: Trace("+traceInst+") " +
                 "vs. Partitioner("+blockInst+")");
         return false;
      }

      return true;
   }

   


}
