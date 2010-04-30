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

package org.ancora.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.ancora.DynamicMapping.InstructionBlock.GenericInstruction;
import org.ancora.DynamicMapping.InstructionBlock.InstructionBlock;
import org.ancora.DynamicMapping.InstructionBlock.MbInstruction;
import org.ancora.DynamicMapping.InstructionBlock.MbInstructionBlockWriter;
import org.ancora.IntermediateRepresentation.Dotty;
import org.ancora.IntermediateRepresentation.Ilp.IlpScenario;
import org.ancora.IntermediateRepresentation.Ilp.MbIlpScene2;
import org.ancora.IntermediateRepresentation.MbParser;
import org.ancora.IntermediateRepresentation.Operation;
import org.ancora.MicroBlaze.InstructionProperties;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.Transformations.MicroblazeGeneral.RegisterZeroToLiteral;
import org.ancora.Transformations.MicroblazeGeneral.IdentifyNops;
import org.ancora.Transformations.MicroblazeGeneral.TransformImmToLiterals;
import org.ancora.Transformations.MicroblazeInstructions.ParseCarryArithmetic;
import org.ancora.Transformations.MicroblazeInstructions.ParseConditionalBranch;
import org.ancora.Transformations.MicroblazeInstructions.ParseUnconditionalBranches;
import org.ancora.Transformations.MicroblazeInstructions.deprecated.ParseConditionalBranchUnroll;
import org.ancora.Transformations.MicroblazeInstructions.RemoveImm;
import org.ancora.Transformations.Transformation;
import org.ancora.common.IoUtilsAppend;

/**
 * Get blocks, transform them
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class Transformer {

    /**
     * @param args the command line arguments
     */
   public static void main(String[] args) {
      // Configure Logger to capture all output to console
      LoggingUtils.setupConsoleOnly();
      // Name of the folder where the blocks are
      String foldername = "blocks/adpcm-coder_trace_without_optimization/";
      //String foldername = "blocks/";

      // Get instruction blocks
      List<InstructionBlock> blocks = getBlocks(foldername);

      for (int i = 0; i < blocks.size(); i++) {
         checkBranches(blocks.get(i));

         //int blockNumber = 2;
         //for(int i=blockNumber-1; i<blockNumber; i++) {
         //System.out.println("------------------------------------");
         System.out.println("Processing block " + i + "...");
         List<Operation> operations = processBlock(blocks.get(i));
         // Connect
         List<Operation> ops = Dotty.connectOperations(operations);

         IoUtils.write(new File("E:/dot." + i + ".txt"), Dotty.generateDot(ops));
      }
     

    }

   //private static processInstructionBlocks

   private static void executeTraceToBlocks() {


      String filename = "../data/traces-without-optimization/adpcm-coder_trace_without_optimization.trace";
      File trace = new File(filename);
      // Testing TraceProcessorWorker
      TraceProcessor traceProcessor = new TraceProcessor();

      traceProcessor.selectPartMegaBlock(32);
      traceProcessor.useGatherer();
      traceProcessor.useSelector(2);

      List<InstructionBlock> blocks = traceProcessor.processTrace(trace);

      double ilp = DataGatherer.getIlpBlock(blocks, new MbIlpScene2());

      System.out.println("ILP:"+ilp);
   }

   private static List<InstructionBlock> getBlocks(String foldername) {
      List<InstructionBlock> blocks = new ArrayList<InstructionBlock>();
      File folder = IoUtils.safeFolder(foldername);

      List<File> blockFiles = IoUtilsAppend.getFilesRecursive(folder, MbInstructionBlockWriter.BLOCK_EXTENSION);

      for (File blockFile : blockFiles) {
         // Recover block
         InstructionBlock block = MbInstructionBlockWriter.loadInstructionBlock(blockFile);
         blocks.add(block);
      }

      return blocks;
   }

   private static List<Operation> processBlock(InstructionBlock block) {
      IlpScenario ilpScenario = new MbIlpScene2();

      Transformation[] segmentA = {
         
         new TransformImmToLiterals(),
         new RegisterZeroToLiteral(),
         new IdentifyNops(),
         new RemoveImm(),
         new ParseCarryArithmetic(),
         new ParseConditionalBranch()
          
      };

      Transformation[] segmentB = {
         new ParseUnconditionalBranches()
      };


      System.out.println("Repetitions:"+block.getRepetitions());

      // Transform block in List of operations
      List<Operation> operations = MbParser.parseMbInstructions(block.getInstructions());


      
      // Apply SegmentA transformations
      operations = applyTransformations(operations, segmentA);
      //operations = applyTransformations(operations, getBaseMicroBlazeTransf());
      //operations = applyTransformations(operations, getMicroBlazeInstructionTransf());

      // Gather statistic before segmentB
      OperationListStats beforeTransf = OperationListStats.buildStats(operations, ilpScenario);

      // Apply SegmentB transformations
      operations = applyTransformations(operations, segmentB);


      System.out.println("\nSegmentA transformations:");
      showTransformations(segmentA);
      System.out.println("\nSegmentB transformations:");
      showTransformations(segmentB);
      System.out.println(" ");
      
      


      // Gather statistic after transformation
      OperationListStats afterTransf = OperationListStats.buildStats(operations, ilpScenario);

      showStats(beforeTransf, afterTransf);


      return operations;
   }

   /*
   private static List<Operation> transformOperations(List<Operation> operations) {
      operations = applyTransformations(operations, getBaseMicroBlazeTransf());

      return operations;
   }
    */

   public static List<Operation> applyTransformations(List<Operation> operations, List<Transformation> transformations) {
      for(Transformation transf : transformations) {
         operations = transf.transform(operations);
      }

      return operations;
   }

   /**
    * Transformations include, in this order:
    * <br>- Transformation of Register 0 to Literal 0, and removal of NOPs (add r0, r0, r0)
    * @return
    */
   public static List<Transformation> getBaseMicroBlazeTransf() {
      List<Transformation> transformations = new ArrayList<Transformation>();

      transformations.add(new TransformImmToLiterals());
      transformations.add(new RegisterZeroToLiteral());
      transformations.add(new IdentifyNops());

      return transformations;
   }

   /**
    * Transformations include, in this order:
    * <br>- Transformation of Register 0 to Literal 0, and removal of NOPs (add r0, r0, r0)
    * @return
    */
   public static List<Transformation> getMicroBlazeInstructionTransf() {
      List<Transformation> transformations = new ArrayList<Transformation>();

      transformations.add(new RemoveImm());
      transformations.add(new ParseCarryArithmetic());
      transformations.add(new ParseConditionalBranch());

      return transformations;
   }

   private static void checkBranches(InstructionBlock block) {
      int counter4=0, counterOther=0;
      for(int i=0; i<block.getInstructions().size(); i++) {
         MbInstruction mb = (MbInstruction) block.getInstructions().get(i);

         if(!InstructionProperties.JUMP_INSTRUCTIONS.contains(mb.getInstructionName())) {
            continue;
         }

         int instructionAddressIndex = i+1;
         if(InstructionProperties.INSTRUCTIONS_WITH_DELAY_SLOT.contains(mb.getInstructionName())) {
            instructionAddressIndex++;
         }

         if(instructionAddressIndex >= block.getInstructions().size()) {
            instructionAddressIndex = 0;
         }

         int nextAddress = block.getInstructions().get(instructionAddressIndex).getAddress();
         int thisAddress = mb.getAddress();

         if(thisAddress - nextAddress == 4) {
            counter4++;
         } else {
            counterOther++;
         }
      }

      System.out.println("Jumps not taken:"+counter4);
      System.out.println("Jumps taken:"+counterOther);
   }

   private static void showStats(OperationListStats beforeTransf, OperationListStats afterTransf) {
      // only show after stats that change
      String[] param = {"CommCosts", "Cpl", "Ilp", "Operations"};
      String[] before = {String.valueOf(beforeTransf.getCommunicationCost()),
        String.valueOf(beforeTransf.getCpl()),
        String.valueOf(beforeTransf.getIlp()),
        String.valueOf(beforeTransf.getNumberOfOperations())
      };
      String[] after = {String.valueOf(afterTransf.getCommunicationCost()),
        String.valueOf(afterTransf.getCpl()),
        String.valueOf(afterTransf.getIlp()),
        String.valueOf(afterTransf.getNumberOfOperations())
      };

      //boolean[] changes = new boolean[param.length];
      //boolean showAny = false;
      /*
      for(int i=0; i<changes.length; i++) {
         changes[i] = before[i].equals(after[i]);

         if(!showAny) {
            showAny = changes[i];
         }

      }
      */
      //boolean showComm = beforeTransf.getCommunicationCost() != afterTransf.getCommunicationCost();
      //boolean showCpl = beforeTransf.getCpl() != afterTransf.getCpl();
      //boolean showIlp = beforeTransf.getIlp() != afterTransf.getIlp();
      //boolean showOp = beforeTransf.getNumberOfOperations() != afterTransf.getNumberOfOperations();

      //boolean showAny = showComm || showCpl || showIlp || showOp;

      System.out.println("Before:");
      System.out.println(beforeTransf);
      //System.out.println(beforeTransf.getMappingString());

      System.out.println("After:");
      boolean noChanges = true;
      for (int i = 0; i < param.length; i++) {
         if (!before[i].equals(after[i])) {
            noChanges = false;
            System.out.println(param[i]+":"+before[i]+"->"+after[i]+";");
         }
      }

      if(noChanges) {
         System.out.println("No Changes");
      }
      /*
      if(!showAny) {
         System.out.println("No Changes");
      } else {
         show(showComm, beforeTransf.getCommunicationCost(), afterTransf.getCommunicationCost());
         show(showCpl, beforeTransf.getCpl(), afterTransf.getCommunicationCost());
         show(showComm, beforeTransf.getCommunicationCost(), afterTransf.getCommunicationCost());
         show(showComm, beforeTransf.getCommunicationCost(), afterTransf.getCommunicationCost());
      }
       */
      //System.out.println(afterTransf);
      //System.out.println(afterTransf.getMappingString());
      System.out.println("------------------------");
   }

   private static List<Operation> applyTransformations(List<Operation> operations, Transformation[] transformations) {
      for(Transformation transformation : transformations) {
         operations = transformation.transform(operations);
      }

      return operations;
   }

   private static void showTransformations(Transformation[] transformations) {
      for(Transformation transf : transformations) {
         System.out.println(transf);
      }
      /*
      List<Transformation> transfs = new ArrayList<Transformation>();
      transfs.addAll(Arrays.asList(segmentA));
      transfs.addAll(Arrays.asList(segmentB));

      System.out.println(" ");
       */
   }

}