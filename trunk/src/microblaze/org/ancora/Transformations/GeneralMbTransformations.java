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

package org.ancora.Transformations;

import java.util.logging.Logger;
import org.ancora.IntermediateRepresentation.Operand;
import org.ancora.IntermediateRepresentation.Operands.Literal;
import org.ancora.IntermediateRepresentation.Operands.MbOperandType;
import org.ancora.IntermediateRepresentation.Operands.OperandType;
import org.ancora.MicroBlaze.MbDefinitions;

/**
 *
 * @author Joao Bispo
 */
public class GeneralMbTransformations {

   /*
   public static List<Operation> transformRegister0(List<Operation> operations) {
      for(Operation operation : operations) {
         transformRegister0(operation.getInputs());
         transformRegister0(operation.getOutputs());
      }

      return operations;
   }
    */

   /*
   private static void transformRegister0(List<Operand> operands) {
      for(int i=0; i<operands.size(); i++) {
         // Check if operand is a MbOperand
         Operand operand = operands.get(i);
         if(operand.getType() == MbOperandType.register) {
            System.out.println("True:"+operand);
         }
         
      }

   }
    */

   /**
    * If an operand can be transformed into literal, a new operand Literal operand is
    * returned. If not, returns null.
    *
    * @param operand
    * @return
    */
   public static Operand transformOperandToLiteral(Operand operand) {
      // Check if Literal
      if(operand.getType() == OperandType.literal) {
         return operand;
      }

      // Check if it is a MicroBlaze immediate value
      if(operand.getType() == MbOperandType.immediate) {
         return new Literal(Literal.LiteralType.integer, operand.getValue(), 
                 MbDefinitions.BITS_IMMEDIATE);
      }

      if(operand.getType() == MbOperandType.register) {
         // Check if is register zero
         if(isRegisterZero(operand.getValue())) {
            return new Literal(Literal.LiteralType.integer, INTEGER_VALUE_ZERO, BITS_ZERO);
         }
         else {
            return null;
         }
      }

      Logger.getLogger(GeneralMbTransformations.class.getName()).
              warning("Could not find case to transform operand '"+operand+"'.");
      return null;
   }

   private static boolean isRegisterZero(String value) {
      return value.equals(INTEGER_VALUE_ZERO);
   }

   private static final String INTEGER_VALUE_ZERO = "0";
   private static final int BITS_ZERO = 0;
}
