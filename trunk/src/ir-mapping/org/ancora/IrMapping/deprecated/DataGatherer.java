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

package org.ancora.IrMapping.deprecated;

import org.ancora.IrMapping.Mapper;
import java.util.List;
//import org.ancora.DynamicMapping.InstructionBlock.InstructionBlock;
//import org.ancora.IntermediateRepresentation.MbParser;
import org.ancora.IntermediateRepresentation.Operation;

/**
 *
 * @author Joao Bispo
 */
public class DataGatherer {

   // Done this way because of floating-point arithmetic. Never sum doubles
   // if you can sum integers and then calculate the double.
   /*
   public static double getIlp(InstructionBlock block, Mapper ilpScene) {
      List<Operation> operations = MbParser.parseMbInstructions(block.getInstructions());
      // Create ILP Data
      ilpScene.processOperations(operations);
      
      return calcIlp(ilpScene.getNumberOfOps(), ilpScene.getNumberOfLines());

      //List<InstructionBlock> blocks = new ArrayList<InstructionBlock>();
      //blocks.add(block);
      //return getIlp(blocks, ilpScene);
   }
    */

   /*
   public static double getIlp(List<InstructionBlock> blocks, Mapper ilpScene) {
      int totalOperations = 0;
      int totalLines = 0;

      for(InstructionBlock block : blocks) {
         getIlpBlock(block, ilpScene);
         // Transform block into IR
         //List<Operation> operations = MbParser.parseMbInstructions(block.getInstructions());

      // Collect ILP
      //MbIlpScene1 ilp = new MbIlpScene1(new MbImmutableTest(), new MbMemoryTest());
      //Mapper ilp = new MbIlpScene2(new MbImmutableTest(), new MbMemoryTest());
      //ilpScene.processOperations(operations);

      // Collect data
      totalOperations+=(ilpScene.getNumberOfOps()*block.getRepetitions());
      totalLines+=(ilpScene.getNumberOfLines()*block.getRepetitions());
      }

      return ((double)totalOperations/(double)totalLines);
   }
    */


   public static double getOperations(List<Operation> operations, Mapper ilpScene) {
      // Create ILP Data
      ilpScene.reset();
      ilpScene.processOperations(operations);

      return calcIlp(ilpScene.getNumberOfOps(), ilpScene.getNumberOfLines());
   }

  

   public static double calcIlp(int numberOfOperations, int numberOfLines) {
      return ((double)numberOfOperations/(double)numberOfLines);
   }
}
