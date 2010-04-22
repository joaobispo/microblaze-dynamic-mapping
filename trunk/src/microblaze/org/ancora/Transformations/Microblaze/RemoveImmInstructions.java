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

package org.ancora.Transformations.Microblaze;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.ancora.DynamicMapping.InstructionBlock.MbTraceReader;
import org.ancora.IntermediateRepresentation.Operand;
import org.ancora.IntermediateRepresentation.Operands.Literal;
import org.ancora.IntermediateRepresentation.Operands.MbOperandType;
import org.ancora.IntermediateRepresentation.Operation;
import org.ancora.IntermediateRepresentation.Operations.MbOperation;
import org.ancora.MicroBlaze.InstructionName;
import org.ancora.Transformations.GeneralMbTransformations;
import org.ancora.Transformations.Transformation;

/**
 * TODO
 * @author Joao Bispo
 */
public class RemoveImmInstructions implements Transformation {

   /**
    * Detects and removes MicroBlaze nops (or r0, r0, r0)
    *
    * <p>Changes input operations.
    * 
    * @param operations
    * @return
    */
   public List<Operation> transform(List<Operation> operations) {
      List<Operation> newList = new ArrayList<Operation>(operations.size());

      for(Operation operation : operations) {
         boolean remove = hasLiteralsAsOnlyOutput(operation.getOutputs());
         if(!remove) {
            newList.add(operation);
         } else {
            // Check if is instruction other than OR:
            boolean isOr = ((MbOperation)operation).getMbType() == InstructionName.or;
            if(!isOr) {
               Logger.getLogger(RemoveImmInstructions.class.getName()).
                       warning("Removed operation besides OR:"+operation);
            }
         }
      }

      return newList;
   }


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

   private boolean isLiteral(Operand operand) {
      // Check for Literals
      Operand newOperand = GeneralMbTransformations.transformOperandToLiteral(operand);
      if (newOperand != null) {
         return true;
      } else {
         return false;
      }
   }

   @Override
   public String toString() {
      //return RegisterZeroToLiteral.class.getName();
      return "RemoveNops";
   }

   /**
    * @param inputs
    * @return true if found register 0.
    */
   /*
   private boolean transformRegister0(List<Operand> operands) {
      boolean transformed = false;
      for(int i=0; i<operands.size(); i++) {
         // Check if operand is a MbOperand
         Operand operand = operands.get(i);
         if(operand.getType() == MbOperandType.register) {
            transformed = isRegisterZero(operand.getValue());
            if(transformed) {
               // Change register to literal
               Operand newOperand = new Literal(INTEGER_VALUE_ZERO, Literal.LiteralType.integer);
               operands.set(i, newOperand);
            }
         }
      }

      return false;
   }
    */
/*
   private boolean isZero(Operand operand) {
      // Check for "register 0"
      if(operand.getType() == MbOperandType.register) {
         return value.equals(INTEGER_VALUE_ZERO);
      }
      throw new UnsupportedOperationException("Not yet implemented");
   }

   private boolean isRegisterZero(String value) {
      return value.equals(INTEGER_VALUE_ZERO);
   }

   private static final String INTEGER_VALUE_ZERO = "0";
 */



}
