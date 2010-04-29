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
import org.ancora.IntermediateRepresentation.Operands.MicroblazeType;
import org.ancora.IntermediateRepresentation.Operation;
import org.ancora.Transformations.MbOperandUtils;
import org.ancora.Transformations.Transformation;

/**
 *
 * @author Joao Bispo
 */
public class RegisterZeroToLiteral implements Transformation {

   /**
    * Changes occurences of Register 0 to Literal 0.
    *
    * <p>Changes input operations.
    * 
    * @param operations
    * @return
    */
   public List<Operation> transform(List<Operation> operations) {
      //List<Operation> newList = new ArrayList<Operation>(operations.size());

      for(Operation operation : operations) {
         transformRegister0(operation.getInputs());
         transformRegister0(operation.getOutputs());
      }

      return operations;
   }

   /**
    * @param inputs
    * @return true if found register 0.
    */
   private boolean transformRegister0(List<Operand> operands) {
      boolean transformed = false;

      for(int i=0; i<operands.size(); i++) {
         // Check if operand is a MbOperand
         Operand operand = operands.get(i);
         if(operand.getType() == MicroblazeType.MbRegister) {
            Operand newOperand = MbOperandUtils.transformOperandToLiteral(operand);
            if(newOperand != null) {
               // Change register to literal
               operands.set(i, newOperand);
               transformed = true;
            }
         }
      }

      return transformed;
   }

   @Override
   public String toString() {
      //return RegisterZeroToLiteral.class.getName();
      return "RegisterZeroToLiteral";
   }

}
