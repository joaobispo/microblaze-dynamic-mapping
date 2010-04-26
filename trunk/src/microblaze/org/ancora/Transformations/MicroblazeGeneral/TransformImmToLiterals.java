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

package org.ancora.Transformations.MicroblazeGeneral;

import java.util.List;
import org.ancora.IntermediateRepresentation.Operand;
import org.ancora.IntermediateRepresentation.Operands.MbOperandType;
import org.ancora.IntermediateRepresentation.Operation;
import org.ancora.Transformations.OperandUtils;
import org.ancora.Transformations.Transformation;

/**
 * Transforms MicroBlaze Immediate operands to Literal Operands
 *
 * @author Joao Bispo
 */
public class TransformImmToLiterals implements Transformation {

   /**
    * Transforms MicroBlaze Immediate operands to General Literal Operands
    *
    * <p>Changes input operations.
    * 
    * @param operations
    * @return
    */
   public List<Operation> transform(List<Operation> operations) {
      for(Operation operation : operations) {
         transformOperands(operation.getInputs());
         transformOperands(operation.getOutputs());
      }

      return operations;
   }

   private void transformOperands(List<Operand> operands) {
      for(int i=0; i<operands.size(); i++) {
         if(operands.get(i).getType() == MbOperandType.immediate) {
            Operand newOperand = OperandUtils.transformOperandToLiteral(operands.get(i));
            if(newOperand != null) {
               operands.set(i, newOperand);
            }
         }
      }

   }

/*
   private boolean hasLiteralsAsOnlyOutput(List<Operand> outputs) {
      // Check if all outputs are to register 0
      boolean isZero = false;

      // If it finds one "not zero", returns false.
      for(Operand operand : outputs) {
         isZero = isLiteral(operand);
         if(!isZero) {
            return false;
         }
      }

      return isZero;
   }
 */

   private boolean isLiteral(Operand operand) {
      // Check for Literals
      Operand newOperand = OperandUtils.transformOperandToLiteral(operand);
      if (newOperand != null) {
         return true;
      } else {
         return false;
      }
   }

   @Override
   public String toString() {
      //return RegisterZeroToLiteral.class.getName();
      return "TransformImmToLiterals";
   }



}
