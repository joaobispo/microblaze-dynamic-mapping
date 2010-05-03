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

package org.ancora.DMTool.TraceProcessor;

import java.io.File;
import java.util.List;
import org.ancora.DynamicMapping.InstructionBlock.InstructionBlock;

/**
 *
 * @author Joao Bispo
 */
public class TraceProcessor {

   public TraceProcessor() {
      worker = new TraceProcessorWorker();
   }

   public List<InstructionBlock> processTrace(File trace) {
      System.out.println("Processing " + trace.getName() + "...");
      return worker.processTrace(trace);
   }

   public void selectPartBasicBlock() {
      worker.partitionerType = TraceProcessorWorker.PartitionerType.BasicBlock;
   }

   public void selectPartSuperBlock() {
      worker.partitionerType = TraceProcessorWorker.PartitionerType.SuperBlock;
   }

   public void selectPartMegaBlock(int maxSuperBlockPatternSize) {
      worker.partitionerType = TraceProcessorWorker.PartitionerType.MegaBlock;
      worker.maxSuperBlockPatternSize = maxSuperBlockPatternSize;
   }

   public void useGatherer() {
      worker.useGatherer = true;
   }

   public void useSelector(int repetitionsThreshold) {
      worker.useSelector = true;
      worker.selectorRepThreshold = repetitionsThreshold;
   }

   public void useFileWriter(){
      worker.useWriter = false;
   }

   /**
    * INSTANCE VARIABLES
    */
   TraceProcessorWorker worker;
}
