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

package org.ancora.IntermediateRepresentation.Operations;

import org.ancora.IntermediateRepresentation.MbOperationType;
import org.ancora.IrMapping.deprecated.IrMemoryTest;
import org.ancora.IntermediateRepresentation.MemoryTest;
import org.ancora.IntermediateRepresentation.Operation;
import org.ancora.MicroBlaze.InstructionName;
import org.ancora.MicroBlaze.InstructionProperties;

/**
 *
 * @author Joao Bispo
 */
public class MbMemoryTest implements MemoryTest {

   public boolean isMemoryOperation(Operation operation) {
      return isLoad(operation) || isStore(operation);
   }

   public boolean isLoad(Operation operation) {
      // Check if is MicroBlaze operation
      if(operation.getType() == MbOperationType.MicroBlazeOperation) {
         InstructionName mbOp = ((MbOperation) operation).getMbType();
         return InstructionProperties.LOAD_INSTRUCTIONS.contains(mbOp);
      } else {
         return irMemoryTest.isLoad(operation);
      }
   }

   public boolean isStore(Operation operation) {
      // Check if is MicroBlaze operation
      if(operation.getType() == MbOperationType.MicroBlazeOperation) {
         InstructionName mbOp = ((MbOperation) operation).getMbType();
         return InstructionProperties.STORE_INSTRUCTIONS.contains(mbOp);
      } else {
         return irMemoryTest.isStore(operation);
      }
   }

   private static MemoryTest irMemoryTest = new IrMemoryTest();

}
