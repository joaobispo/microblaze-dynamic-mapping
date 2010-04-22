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

package org.ancora.IntermediateRepresentation.Ilp;

import java.util.HashSet;
import org.ancora.IntermediateRepresentation.*;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ancora.IntermediateRepresentation.Operands.MbImmutableTest;
import org.ancora.IntermediateRepresentation.Operations.MbMemoryTest;
import org.ancora.SharedLibrary.ParseUtils;

/**
 *
 * @author Joao Bispo
 */
public class MbIlpScene2 implements IlpScenario {

   /**
    * Creates a MicroBlaze ILP Scenario 2 with MicroBlaze Immutable and Memory Tests
    */
   public MbIlpScene2() {
      this(new MbImmutableTest(), new MbMemoryTest());
   }



   public MbIlpScene2(ImmutableTest immutableTest, MemoryTest memoryTest) {
      this.immutableTest = immutableTest;
      this.memoryTest = memoryTest;
      //this.numLoads = numLoads;
   }

   public void processOperations(List<Operation> operations) {
      //reset();

      for(Operation operation : operations) {
         // Given the inputs, find the lower possible line where the operation
         // can be put.
         int lowestOperandsLine = processInputs(operation.getInputs());
         //System.out.println("Operation:"+operation);
         //System.out.println("Inputs:"+operation.getInputs());
         //System.out.println("Outputs:"+operation.getOutputs());

         // Calculate operation line, taking into account that it can be a
         // memory operation
         int operationLine  = calculateLineWithMemoryOperations(operation, lowestOperandsLine);
         // Destination line will be lowestLine + 1

         processOutputs(operation.getOutputs(), operationLine);

         // Update number of lines
         usedLines = Math.max(usedLines, operationLine);
         // Update mappedOps
         mappedOps++;
      }
   }

   private int processInputs(List<Operand> operands) {
      int lowestLine = 0;
      for(Operand operand : operands) {
         // Check if operand is immutable
         if(immutableTest.isImmutable(operand)) {
            continue;
         }

         // Get line where this operand was created
         //String operandRepresentation = operand.toString();
         String operandRepresentation = operand.getValue();
         Integer line = dataLines.get(operandRepresentation);

         // Check if Live-In
         if(line == null) {
            line = 0;
            dataLines.put(operandRepresentation, line);
            updateLiveins(operandRepresentation);
         }

         lowestLine = Math.max(lowestLine, line);
      }

      return lowestLine;
   }

   private int calculateLineWithMemoryOperations(Operation operation, int lowestOperandsLine) {
      if(!memoryTest.isMemoryOperation(operation)) {
         return lowestOperandsLine + 1;
      }


      int operationLine = Math.max(lowestOperandsLine, lastLineWithStore);

      if(memoryTest.isLoad(operation)) {
         operationLine = Math.max(lowestOperandsLine, lastLineWithStore);
      }

      // Put operation on the line below
      operationLine++;

      // Only update last line with memory operation if it is a store
      if(memoryTest.isStore(operation)) {
         lastLineWithStore = operationLine;
      }
      
      return operationLine;
   }

   private void processOutputs(List<Operand> operands, int operationLine) {
      for(Operand operand : operands) {
         // Check if operand is immutable
         if(immutableTest.isImmutable(operand)) {
            continue;
         }

         // Update consumer table
         //String operandRepresentation = operand.toString();
         String operandRepresentation = operand.getValue();
         dataLines.put(operandRepresentation, operationLine);

         //Update Liveouts
         updateLiveouts(operandRepresentation);
      }
   }

      private void updateLiveouts(String operandRepresentation) {
      liveOuts.add(ParseUtils.parseInt(operandRepresentation));
   }

   private void updateLiveins(String operandRepresentation) {
      liveIns.add(ParseUtils.parseInt(operandRepresentation));
   }

   public void reset() {
      mappedOps = 0;
      usedLines = 0;
      lastLineWithStore = 0;
      dataLines = new Hashtable<String, Integer>();

      liveIns = new HashSet<Integer>();
      liveOuts = new HashSet<Integer>();
   }


   public int getLiveIns() {
      //System.out.println("Liveins:"+liveIns);
      return liveIns.size();
   }

   public int getLiveOuts() {
     /*
      int liveOuts = 0;
      //Set<String> liveOuts = new HashSet<String>();

      for(String key : dataLines.keySet()) {
         int linePos = dataLines.get(key);
         // A line bigger than 0 means it was written
         if(linePos > 0) {
            liveOuts++;
         }
      }
*/
      //System.out.println("Liveouts:"+liveOuts);
      return liveOuts.size();
   }

   public double getIlp() {
      return (double)mappedOps/(double)usedLines;
   }

   public int getNumberOfOps() {
      return mappedOps;
   }

   public int getNumberOfLines() {
      return usedLines;
   }


   public void printStats() {
      StringBuilder builder = new StringBuilder();

      builder.append("ILP:"+getIlp()+"\n");
      builder.append("Live-Ins:"+getLiveIns()+"\n");
      builder.append("Live-Outs:"+getLiveOuts()+"\n");


      System.out.println(builder);
   }

   /**
    * INSTANCE VARIABLES
    */
   //private int numLoads;
   private int mappedOps;
   private int usedLines;
   private int lastLineWithStore;

   private Map<String,Integer> dataLines;

   private Set<Integer> liveIns;
   private Set<Integer> liveOuts;

   private ImmutableTest immutableTest;
   private MemoryTest memoryTest;







}