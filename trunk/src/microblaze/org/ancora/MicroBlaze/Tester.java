/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.MicroBlaze;

//import static org.ancora.MicroBlaze.Trace.TraceDefinitions.EXTENSION_SEPARATOR;
import static org.ancora.MicroBlaze.Trace.TraceDefinitions.TRACE_EXTENSION;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.ancora.DynamicMapping.InstructionBlock.InstructionBlock;
import org.ancora.DynamicMapping.InstructionBlock.InstructionBusReader;
import org.ancora.DynamicMapping.InstructionBlock.Listeners.InstructionBlockCollector;
import org.ancora.DynamicMapping.InstructionBlock.Listeners.InstructionBlockPrinter;
import org.ancora.DynamicMapping.InstructionBlock.Listeners.InstructionBlockStats;
import org.ancora.DynamicMapping.InstructionBlock.MbTraceReader;
import org.ancora.DynamicMapping.Partitioning.BasicBlock;
import org.ancora.DynamicMapping.Partitioning.Tools.Gatherer;
import org.ancora.DynamicMapping.Partitioning.MbJumpFilter;
import org.ancora.DynamicMapping.Partitioning.MegaBlock;
import org.ancora.DynamicMapping.Partitioning.Partitioner;
import org.ancora.DynamicMapping.Partitioning.SuperBlock;
import org.ancora.DynamicMapping.Partitioning.Tools.Selector;
import org.ancora.DynamicMapping.InstructionBlock.MbInstructionBlockWriter;
import org.ancora.IntermediateRepresentation.Dotty;
import org.ancora.IntermediateRepresentation.Ilp.MbIlpScene1;
import org.ancora.IntermediateRepresentation.Ilp.MbIlpScene2;
import org.ancora.IntermediateRepresentation.MbParser;
import org.ancora.IntermediateRepresentation.Operation;
import org.ancora.IntermediateRepresentation.Operations.MbMemoryTest;
import org.ancora.MicroBlaze.Trace.TraceDefinitions;
import org.ancora.MicroBlaze.Trace.TraceProperties;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.IntermediateRepresentation.MbTransformUtils;
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
        //executeTraceReader();
        //executeBlockReader();
        executeGetIlpFromTraces();
    }

    private static void setupProgram() {
      // Configure Logger to capture all output to console
      LoggingUtils.setupConsoleOnly();
   }

   private static void executeTraceReader() {
      
      String foldername = "../data/traces-without-optimization/";
      File folder = IoUtils.safeFolder(foldername);

      //File[] traces = getTraces(folder);
      File[] traces = IoUtilsAppend.getFiles(folder, TraceDefinitions.TRACE_EXTENSION);
      
      for(File trace : traces) {
         processTrace(trace);
      }
   }

   private static File[] getTraces(File traceFolder) {
      // Later, I can add a file filter by extension
      return traceFolder.listFiles(new ExtensionFilter(TRACE_EXTENSION));
   }

   /**
    * Returns files from a folder with a certain extension.
    *
    * @param folder
    * @param extension
    * @return
    */
   /*
   private static File[] getFiles(File folder, String extension) {
      return folder.listFiles(new ExtensionFilter(extension));
   }
    */

   private static void processTrace(File trace) {
      //String filename = IoUtilsAppend.removeExtension(trace.getName(), EXTENSION_SEPARATOR);
      System.out.println("Processing "+trace.getName()+"...");

      int maxMegablockPatternSize = 20;
      int repetitionsThreshold = 2;

      InstructionBusReader busReader = MbTraceReader.createTraceReader(trace);
      //Partitioner partitioner = new BasicBlock(new MbJumpFilter());
      //Partitioner partitioner = new SuperBlock(new MbJumpFilter());
      Partitioner partitioner = new MegaBlock(new MbJumpFilter(), maxMegablockPatternSize);
      Gatherer gatherer = new Gatherer();
      Selector selector = new Selector(repetitionsThreshold);
      InstructionBlockStats ibStats = new InstructionBlockStats();

               String EXTENSION_SEPARATOR = ".";
         int lastIndexOfSeparator = trace.getName().lastIndexOf(EXTENSION_SEPARATOR);
         String baseFilename = trace.getName().substring(0, lastIndexOfSeparator);
      //MbInstructionBlockWriter ibWriter = new MbInstructionBlockWriter(trace.getName());
      MbInstructionBlockWriter ibWriter = new MbInstructionBlockWriter(baseFilename);
      
      
      partitioner.addListener(gatherer);

      gatherer.addListener(ibStats);
      //gatherer.addListener(new InstructionBlockPrinter());
      gatherer.addListener(selector);

      //selector.addListener(new InstructionBlockPrinter());
      selector.addListener(ibWriter);

      partitioner.run(busReader);

      //System.out.println("Total Instructions:"+ibStats.getTotalInstructions());
      boolean passed = check(trace, ibStats);
      /*
      if(passed) {
         System.out.println("Passed Checks.");
      }
       */
      
   }

   private static boolean check(File trace, InstructionBlockStats ibStats) {
      // Get trace properties
      TraceProperties props = TraceProperties.getTraceProperties(trace);

      // Check if Partitioned Instructions Add Up
      long blockInst = ibStats.getTotalInstructions();
      long traceInst = props.getInstructions();

      if(blockInst != traceInst) {
         Logger.getLogger(Tester.class.getName()).
                 warning("Total instructions does not add up: Trace("+traceInst+") " +
                 "vs. Partitioner("+blockInst+")");
         return false;
      }

      return true;
   }

   private static void executeBlockReader() {
      String foldername = "blocks/adpcm-coder_trace_without_optimization/";
      File folder = IoUtils.safeFolder(foldername);

      File[] blocks = IoUtilsAppend.getFiles(folder, MbInstructionBlockWriter.BLOCK_EXTENSION);

      for(File block : blocks) {
         processBlock(block);
      }
   }

   private static void processBlock(File fileBlock) {
      // Recover block
      InstructionBlock block = MbInstructionBlockWriter.loadInstructionBlock(fileBlock);
      //System.out.println(block);
      // Transform block into IR
      List<Operation> operations = MbParser.parseMbInstructions(block.getInstructions());

      //MbIlpScene1 ilp = new MbIlpScene1(new MbImmutableTest(), new MbMemoryTest());
      MbIlpScene1 ilp = new MbIlpScene1(new MbMemoryTest());
      ilp.processOperations(operations);
      ilp.printStats();
      //applyTransformations(operations);
      //System.out.println(operations);
      //IoUtils.write(new File("E:\\dotty.dot"), Dotty.generateDot(operations));
   }

   /*
   private static void applyTransformations(List<Operation> operations) {
      // Transform R0 in literal 0
      //MbTransformUtils.transformRegister0(operations);
   }
    */

   private static void executeGetIlpFromTraces() {
      String foldername = "../data/";
      File folder = IoUtils.safeFolder(foldername);

      //File[] traces = getTraces(folder);
      List<File> traces = IoUtilsAppend.getFilesRecursive(folder, TraceDefinitions.TRACE_EXTENSION);

      for(File trace : traces) {
         processTraceIlp(trace);
      }
   }



   private static void processTraceIlp(File trace) {
 //String filename = IoUtilsAppend.removeExtension(trace.getName(), EXTENSION_SEPARATOR);
      System.out.println("Getting ILP for "+trace.getName()+"...");

      int maxMegablockPatternSize = 20;
      int repetitionsThreshold = 2;

      InstructionBusReader busReader = MbTraceReader.createTraceReader(trace);
      //Partitioner partitioner = new BasicBlock(new MbJumpFilter());
      //Partitioner partitioner = new SuperBlock(new MbJumpFilter());
      Partitioner partitioner = new MegaBlock(new MbJumpFilter(), maxMegablockPatternSize);
      Gatherer gatherer = new Gatherer();
      //Selector selector = new Selector(repetitionsThreshold);
      InstructionBlockStats ibStats = new InstructionBlockStats();
      //MbInstructionBlockWriter ibWriter = new MbInstructionBlockWriter(trace.getName());
      InstructionBlockCollector collector = new InstructionBlockCollector();

      partitioner.addListener(gatherer);
      //partitioner.addListener(collector);

      gatherer.addListener(ibStats);
      gatherer.addListener(collector);

      //gatherer.addListener(new InstructionBlockPrinter());
      //gatherer.addListener(selector);

      //selector.addListener(new InstructionBlockPrinter());
      //selector.addListener(ibWriter);

      partitioner.run(busReader);

      check(trace, ibStats);

      // Get ILP from Blocks
      getIlp(collector.getBlocks());
   }

   private static void getIlp(List<InstructionBlock> blocks) {
      int totalOperations = 0;
      int totalLines = 0;

      for(InstructionBlock block : blocks) {
         // Transform block into IR
      List<Operation> operations = MbParser.parseMbInstructions(block.getInstructions());

      // Collect ILP
      //MbIlpScene1 ilp = new MbIlpScene1(new MbImmutableTest(), new MbMemoryTest());
      //MbIlpScene2 ilp = new MbIlpScene2(new MbImmutableTest(), new MbMemoryTest());
      MbIlpScene2 ilp = new MbIlpScene2(new MbMemoryTest());
      ilp.processOperations(operations);

      // Collect data
      totalOperations+=(ilp.getNumberOfOps()*block.getRepetitions());
      totalLines+=(ilp.getNumberOfLines()*block.getRepetitions());
      //ilp.printStats();
      }

      System.out.println("ILP:"+((double)totalOperations/(double)totalLines));
   }



   


}
