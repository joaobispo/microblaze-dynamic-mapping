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
public class ConditionalExit extends Operation {

   public ConditionalExit(int address, ConditionalExit.Op operation) {
      super(address);
      this.operation = operation;
   }


   @Override
   public Enum getType() {
      return OperationType.ConditionalExit;
   }

   @Override
   public String getValue() {
      return operation.name();
   }

   @Override
   public boolean hasSideEffects() {
      return false;
   }

   public Operand getDestinationAddress() {
      return address;
   }

   public void setDestinationAddress(Operand address) {
      this.address = address;
      connectToInput(address);
   }

   public Operand getCompareToZero() {
      return compareToZero;
   }

   public void setCompareToZero(Operand compareToZero) {
      this.compareToZero = compareToZero;
      connectToInput(compareToZero);
   }

   



   /**
    * INSTANCE VARIABLES
    */
   private ConditionalExit.Op operation;
   private Operand compareToZero;
   private Operand address;

   public enum Op {
      equal,
      notEqual,
      greater,
      greaterOrEqual,
      less,
      lessOrEqual
   }
}
