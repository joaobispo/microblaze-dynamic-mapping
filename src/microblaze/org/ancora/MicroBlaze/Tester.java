/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.MicroBlaze;

import java.io.File;
import org.ancora.DynamicMapping.InstructionBlock.InstructionBusReader;
import org.ancora.DynamicMapping.InstructionBlock.Listeners.InstructionBlockPrinter;
import org.ancora.DynamicMapping.InstructionBlock.MbTraceReader;
import org.ancora.DynamicMapping.Partitioning.BasicBlock;
import org.ancora.DynamicMapping.Partitioning.MbJumpFilter;
import org.ancora.DynamicMapping.Partitioning.Partitioner;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.common.ExtensionFilter;

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
      partitioner.addListener(new InstructionBlockPrinter());
      
      partitioner.run(busReader);
   }

   private static final String TRACE_EXTENSION = "trace";
   //private static final String EXTENSION_SEPARATOR = ".";

}
