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

import org.ancora.IntermediateRepresentation.Operand;
import org.ancora.IntermediateRepresentation.Operation;

/**
 *
 * @author Joao Bispo
 */
public class ArithmeticWithCarry extends Operation {

   public ArithmeticWithCarry(ArithmeticWithCarry.Op operation, Operand input1, Operand input2, Operand output1, Operand carryIn, Operand carryOut) {
      this.operation = operation;
      this.input1 = input1;
      this.input2 = input2;
      this.output1 = output1;
      this.carryIn = carryIn;
      this.carryOut = carryOut;

      // Connect Inputs
      connectToInput(input1);
      connectToInput(input2);
      if(carryIn == null) {
         hasCarryIn = false;
      } else {
         connectToInput(carryIn);
         hasCarryIn = true;
      }

      // Connect Outputs
      connectToOutput(output1);
      if(carryOut == null) {
         hasCarryOut = false;
      } else {
         connectToOutput(carryOut);
         hasCarryOut = true;
      }
   }



   @Override
   public Enum getType() {
      return OperationType.IntegerArithmeticWithCarry;
   }

   @Override
   public String getValue() {
      return "ir-"+operation.name();
   }

   @Override
   public boolean hasSideEffects() {
      return false;
   }

   public enum Op {
      add,
      rsub;
   }

   /**
    * INSTANCE VARIABLES
    */
   private Operand input1;
   private Operand input2;
   private Operand output1;
   private Operand carryIn;
   private Operand carryOut;
   private boolean hasCarryIn;
   private boolean hasCarryOut;

   private ArithmeticWithCarry.Op operation;
}
