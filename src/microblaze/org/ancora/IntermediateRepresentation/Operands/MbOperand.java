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

package org.ancora.IntermediateRepresentation.Operands;

import org.ancora.IntermediateRepresentation.Operand;

/**
 *
 * @author Joao Bispo
 */
public class MbOperand extends Operand {

   public MbOperand(MbOperandType type, String value) {
      this.type = type;
      this.value = value;
   }

   @Override
   public String getValue() {
      return value;
   }

   @Override
   public Enum getType() {
      return type;
   }

   /**
    * INSTANCE VARIABLES
    */
   private MbOperandType type;
   private String value;
}