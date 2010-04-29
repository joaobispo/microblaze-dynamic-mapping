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

package org.ancora.Transformations.MicroblazeInstructions;

import java.util.ArrayList;
import java.util.List;
import org.ancora.IntermediateRepresentation.Operand;
import org.ancora.IntermediateRepresentation.Operands.Literal;
import org.ancora.IntermediateRepresentation.Operands.MbImm;
import org.ancora.IntermediateRepresentation.Operation;
import org.ancora.IntermediateRepresentation.Operations.MbOperation;
import org.ancora.IntermediateRepresentation.Operations.Nop;
import org.ancora.MicroBlaze.InstructionName;
import org.ancora.MicroBlaze.MbDefinitions;
import org.ancora.SharedLibrary.ParseUtils;
import org.ancora.Transformations.MbOperandUtils;
import org.ancora.Transformations.Transformation;

/**
 *
 * @author Joao Bispo
 */
public class RemoveImm implements Transformation {

   public List<Operation> transform(List<Operation> operations) {
     //List<Operation> newList = new ArrayList<Operation>(operations.size());

     for(int i=0; i<operations.size(); i++) {
        // Check if MicroBlaze operation
        MbOperation immOperation = MbOperation.getMbOperation(operations.get(i));
        if(immOperation == null) {
           //newList.add(immOperation);
           continue;
        }

        // Check if IMM
        if(immOperation.getMbType() != InstructionName.imm) {
           //newList.add(immOperation);
           continue;
        }

        // Collect imm value from IMM operation
        Integer upper16 = MbOperandUtils.getIntegerValue(immOperation.getInputs().get(0));
        //Integer upper16 = MbImm.getImmValue(immOperation.getInputs().get(0));
        //int upper16 = ParseUtils.parseInt(immOperation.getInputs().get(0).getValue());

        // Collect imm value and imm from next instruction
        MbOperation nextOperation = MbOperation.getMbOperation(operations.get(i+1));
        // Assume next operation is of type B
        List<Operand> inputs = nextOperation.getInputs();
        // Imm is always the last operand of a MicroBlaze instruction
        int immIndex = inputs.size()-1;
//System.out.println("Operation Before:"+nextOperation);
        Operand immOperand = inputs.get(immIndex);
//        System.out.println("immOP:"+immOperand.toString());
        int lower16 = MbOperandUtils.getIntegerValue(immOperand);
        //int lower16 = ParseUtils.parseInt(immOperand.getValue());
        int completeInt = fuseImm(upper16, lower16);

        // Replace Operand for a Literal
        inputs.set(immIndex, new Literal(Literal.LiteralType.integer,
                String.valueOf(completeInt), BIS_FUSED_IMM));
        //System.out.println("Imm Operand:"+immOperand);

// System.out.println("Operation After:"+nextOperation);
        //newList.add(nextOperation);
        // Since IMM was found and processed, advance an extra index
        //i++;

        operations.set(i, new Nop(immOperation.getAddress(), immOperation.toString()));
     }

     // Add last operation
     //newList.add(operations.get(operations.size()-1));


     return operations;
     //return newList;
   }

   /**
    * Fuses the lower 16 bits of two ints.
    *
    * TODO: Verify correcteness.
    * <p>Ex.:
    * upper16 = 1001
    * lower16 = 101
    * result = 00000000000010010000000000000101
    *
    * @param upper16
    * @param lower16
    * @return
    */
   public static int fuseImm(int upper16, int lower16){
      // Mask the 16 bits of each one
      upper16 = upper16 & Integer.parseInt("0000FFFF", 16);
      lower16 = lower16 & Integer.parseInt("0000FFFF", 16);
      // Shift Upper16
      upper16 = upper16 << 16;
      // Merge
      return upper16 | lower16;
   }

   @Override
   public String toString() {
      return "RemoveImm";
   }

   public static int BIS_FUSED_IMM = 32;


}
