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

package org.ancora.IntermediateRepresentation;

import org.ancora.IntermediateRepresentation.Operands.Literal;
import org.ancora.IntermediateRepresentation.Operands.OperandType;

/**
 *
 * @author Joao Bispo
 */
public class OperandUtils {

   /**
    * If an operand can be transformed into literal, a new operand Literal operand is
    * returned. If not, returns null.
    *
    * @param operand
    * @return
    */
   public static Literal transformOperandToLiteral(Operand operand) {
      // Check if Literal
      if(operand.getType() == OperandType.literal) {
         return (Literal) operand;
      }

      return null;
   }

   /**
    * Supports Literal, MbImm
    * @param operand
    * @return
    */
   /*
   public static Integer getIntegerValue(Operand operand) {
      Integer integer = null;

      // Check if operand is literal
      integer = Literal.getInteger(operand);
      if(integer != null) {
         return integer;
      }

      return null;
   }
    */
}
