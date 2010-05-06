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

package org.ancora.Transformations.MicroblazeInstructions.deprecated;

import java.util.ArrayList;
import org.ancora.IntermediateRepresentation.Operations.ConditionalExit.Op;
import static org.ancora.IntermediateRepresentation.Operations.ConditionalExit.Op.*;

import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.ancora.IntermediateRepresentation.Operand;
import org.ancora.IntermediateRepresentation.Operands.InternalData;
import org.ancora.IntermediateRepresentation.Operands.Literal;
import org.ancora.IntermediateRepresentation.Operation;
import org.ancora.IntermediateRepresentation.Operations.ArithmeticWithCarry;
import org.ancora.IntermediateRepresentation.Operations.Logic;
import org.ancora.IntermediateRepresentation.Operations.ConditionalExit;
import org.ancora.IntermediateRepresentation.Operations.deprecated.Exit;
import org.ancora.IntermediateRepresentation.Operations.MbOperation;
import org.ancora.IntermediateRepresentation.Operations.deprecated.Mux;
import org.ancora.MicroBlaze.InstructionName;
import org.ancora.MicroBlaze.InstructionProperties;
import org.ancora.IntermediateRepresentation.MbTransformUtils;
import org.ancora.IntermediateRepresentation.Transformation;

/**
 *
 * @author Joao Bispo
 */
public class ParseConditionalBranchUnroll implements Transformation {

   public List<Operation> transform(List<Operation> operations) {
      List<Operation> newList = new ArrayList<Operation>();
      //List<Operation> newList = new LinkedList<Operation>();

      for(int i=0; i<operations.size(); i++) {
        // Check if MicroBlaze Operation
        MbOperation branchOp = MbOperation.getMbOperation(operations.get(i));
        if(branchOp == null) {
           newList.add(operations.get(i));
           continue;
        }

        // Check if it is a conditional compare
        ConditionalExit.Op compareOperation = getCompareOp(branchOp.getMbType());
        if(compareOperation == null) {
           newList.add(operations.get(i));
           continue;
        }

        // Calculate nextAddress
        int nextAddress = calculateNextAddress(operations, i, branchOp.getMbType());
        // Calculate offset
        int offset = nextAddress - branchOp.getAddress();

        List<Operation> newOperations = createOperations(branchOp.getAddress(),
                compareOperation, offset, branchOp.getInputs().get(0),
                branchOp.getInputs().get(1));


        newList.addAll(newOperations);
        // Remove old operation
        /*
        System.out.println("before remove:"+operations.size());
        newList.remove(i);
        newList.addAll(i, newOperations);
        // Update i
        i += newOperations.size() - 1;
         */
      }

     return newList;
   }

   private ConditionalExit.Op getCompareOp(InstructionName instructionName) {
      CexitProperties props = instructionProperties.get(instructionName);
      if(props == null) {
         return null;
      } else {
         return props.operation;
      }
   }


   /**
    * Calculates the next address.
    * TODO: Public Static Candidate
    *
    * @param operations
    * @param i
    * @return
    */
   private int calculateNextAddress(List<Operation> operations, int i,
           InstructionName instructionName) {

      // Usually, the index of the next instruction would be i+1
      int nextInstructionIndex = i + 1;

      // Check if instruction has delay slot
      if (InstructionProperties.INSTRUCTIONS_WITH_DELAY_SLOT.contains(instructionName)) {
         nextInstructionIndex++;
      }

      if (nextInstructionIndex >= operations.size()) {
         nextInstructionIndex = 0;
      }

      return operations.get(nextInstructionIndex).getAddress();
   }


   private List<Operation> createOperations(int address, ConditionalExit.Op op, int offset,
          Operand input1, Operand input2) {

      List<Operation> newList = new ArrayList<Operation>();

      Operand rA = input1;
      Operand rB = input2;
      Operand l0 = new Literal(Literal.LiteralType.integer,
              String.valueOf(0), 1);
      Operand lOffsetOp = new Literal(Literal.LiteralType.integer,
              String.valueOf(offset), 32);
      Operand lCurrentAddress = new Literal(Literal.LiteralType.integer,
              String.valueOf(address), 32);
      Operand lAddressNoBranch = new Literal(Literal.LiteralType.integer,
              String.valueOf(address+4), 32);

      int exitSignalNoBranch;
      if(offset != 4) {
         exitSignalNoBranch = 1;
      } else {
         exitSignalNoBranch = 0;
      }
      Operand lExitSignalNoBranchOp = new Literal(Literal.LiteralType.integer,
              String.valueOf(exitSignalNoBranch), 32);

      Operand[] temp = new Operand[5];
      for(int i=0; i<5; i++) {
         temp[i] = new InternalData("T"+(i+1), 32);
      }
      // First Logic
      Operation firstCmp = null;
      Operation secondCmp = null;
      //Operation firstCmp = new Logic(address, rA, l0, temp[0], op, true);
      //Operation secondCmp = new Logic(address, rB, lOffsetOp, temp[1], ConditionalExit.Op.notEqual, true);
      Operation add = new ArithmeticWithCarry(address, ArithmeticWithCarry.Op.add, lCurrentAddress, rB, temp[2], null, null);
      Operation mux1 = new Mux(address, temp[0], "0", temp[3], lExitSignalNoBranchOp, temp[1]);
      Operation mux2 = new Mux(address, temp[0], "0", temp[4], temp[2], lAddressNoBranch);
      Operation exit = new Exit(address, temp[3], temp[4]);


      newList.add(firstCmp);
      newList.add(secondCmp);
      newList.add(add);
      newList.add(mux1);
      newList.add(mux2);
      newList.add(exit);

      return newList;
   }

   @Override
   public String toString() {
      return "ParseConditionalBranches";
   }

   /**
    * INSTANCE VARIABLES
    */
   private static final Map<InstructionName, CexitProperties> instructionProperties;
   static {
      Map<InstructionName, CexitProperties> aMap = new Hashtable<InstructionName, CexitProperties>();

      aMap.put(InstructionName.beq, new CexitProperties(equal, false));
      aMap.put(InstructionName.beqd, new CexitProperties(equal, true));
      aMap.put(InstructionName.beqi, new CexitProperties(equal, false));
      aMap.put(InstructionName.beqid, new CexitProperties(equal, true));

      aMap.put(InstructionName.bge, new CexitProperties(greaterOrEqual, false));
      aMap.put(InstructionName.bged, new CexitProperties(greaterOrEqual, true));
      aMap.put(InstructionName.bgei, new CexitProperties(greaterOrEqual, false));
      aMap.put(InstructionName.bgeid, new CexitProperties(greaterOrEqual, true));

      aMap.put(InstructionName.bgt, new CexitProperties(greater, false));
      aMap.put(InstructionName.bgtd, new CexitProperties(greater, true));
      aMap.put(InstructionName.bgti, new CexitProperties(greater, false));
      aMap.put(InstructionName.bgtid, new CexitProperties(greater, true));

      aMap.put(InstructionName.ble, new CexitProperties(lessOrEqual, false));
      aMap.put(InstructionName.bled, new CexitProperties(lessOrEqual, true));
      aMap.put(InstructionName.blei, new CexitProperties(lessOrEqual, false));
      aMap.put(InstructionName.bleid, new CexitProperties(lessOrEqual, true));

      aMap.put(InstructionName.blt, new CexitProperties(less, false));
      aMap.put(InstructionName.bltd, new CexitProperties(less, true));
      aMap.put(InstructionName.blti, new CexitProperties(less, false));
      aMap.put(InstructionName.bltid, new CexitProperties(less, true));

      aMap.put(InstructionName.bne, new CexitProperties(notEqual, false));
      aMap.put(InstructionName.bned, new CexitProperties(notEqual, true));
      aMap.put(InstructionName.bnei, new CexitProperties(notEqual, false));
      aMap.put(InstructionName.bneid, new CexitProperties(notEqual, true));


      instructionProperties = Collections.unmodifiableMap(aMap);
   }




   

   /**
    * INNER CLASS
    */
   static class CexitProperties {

      public CexitProperties(ConditionalExit.Op operation, boolean hasDelaySlot) {
         this.operation = operation;
         this.hasDelaySlot = hasDelaySlot;
      }



      /**
       * INSTANCE VARIABLES
       */
      private ConditionalExit.Op operation;
      private boolean hasDelaySlot;
   }


}
