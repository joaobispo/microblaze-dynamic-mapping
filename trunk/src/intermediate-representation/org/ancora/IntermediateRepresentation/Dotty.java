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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ancora.IntermediateRepresentation.Operands.InternalData;
import org.ancora.IntermediateRepresentation.Operations.Control;
import org.ancora.IntermediateRepresentation.Operations.MockOperation;
import org.ancora.IntermediateRepresentation.Operations.OperationType;

/**
 *
 * @author Joao Bispo
 */
public class Dotty {

   public static String generateDot(List<Operation> operations) {
      // Collect Operands
      Set<Operand> operands = collectOperands(operations);

      StringBuilder builder = new StringBuilder();

      builder.append("digraph graphname {\n");
      declareOperations(operations, builder);
      declareOperands(operands, builder);
      builder.append("\n");

      for(Operation operation: operations) {
         if(operation.getType() == OperationType.Nop) {
            continue;
         }

         for(Operand operand : operation.getOutputs()) {
            builder.append(operation.hashCode());
            builder.append(" -> ");
            builder.append(operand.hashCode());
            builder.append(";\n");
         }
      }

      for(Operand operand : operands) {
         for(Operation operation : operand.getConsumers()) {
            builder.append(operand.hashCode());
            builder.append(" -> ");
            builder.append(operation.hashCode());
            builder.append(";\n");
         }
      }

      //appendOperationOutputs(start, builder, operands, operations);
      builder.append("}\n");

      return builder.toString();
   }

   private static Set<Operand> collectOperands(List<Operation> operations) {
      Set<Operand> operands = new HashSet<Operand>();

      for(Operation operation : operations) {
         operands.addAll(operation.getInputs());
         operands.addAll(operation.getOutputs());
         operands.addAll(operation.getParameters());
      }

      return operands;
   }

      private static void declareOperations(Collection<Operation> operations, StringBuilder builder) {
      for(Operation op : operations) {
         if(op.getType() == OperationType.Nop) {
            continue;
         }

         builder.append(op.hashCode());
         builder.append("[label=\"");
         //builder.append(op.getType()+"-"+op.getValue());
         //builder.append(op.getValue());
         builder.append(op);
         builder.append("\"];\n");
      }
   }

   private static void declareOperands(Collection<Operand> operands, StringBuilder builder) {
      for(Operand op : operands) {
         builder.append(op.hashCode());
         builder.append("[label=\"");
         builder.append(op);
         builder.append("\", shape=box];\n");
      }
   }


   /**
    * Changes input list.
    * 
    * @param operations
    * @return
    */
   public static List<Operation> connectOperations(List<Operation> operations) {
      List<Operation> newList = new ArrayList<Operation>();
      Map<String, Integer> variablesVersion = new Hashtable<String, Integer>();
      Map<String, Operand> operandsTable = new Hashtable<String, Operand>();
      Operation start = new Control(-1, Control.Op.start);
      newList.add(start);

      for(int i=0; i<operations.size(); i++) {
         Operation operation = operations.get(i);
         //Operation dotOp = new MockOperation(operation.getAddress(), operation.getValue());
         Operation dotOp = new MockOperation(operation.getAddress(), operation.toString());
         newList.add(dotOp);

         List<Operand> inputs = operation.getInputs();
         for(int j=0; j<inputs.size(); j++) {
            Operand input = inputs.get(j);
            // Check if immutable
            if(input.isImmutable()) {
               dotOp.connectToInput(input.copy());
               continue;
            }

            //String inputValue = input.getValue();
            String inputName = input.toString();
            // Get input index from table
            Integer version = variablesVersion.get(inputName);
            Operand newInput = operandsTable.get(inputName);

            if(version == null) {
               version = 0;
               //variablesVersion.put(input.getValue(), version);
               variablesVersion.put(inputName, version);
               String opValue = inputName+"."+version;
               newInput = new InternalData(opValue, input.getBits());
               operandsTable.put(inputName, newInput);
               newInput.connectToProducer(start);
            }

            // Add new Inputs to new operation
            dotOp.connectToInput(newInput);
         }

         List<Operand> outputs = operation.getOutputs();
         for(int j=0; j<outputs.size(); j++) {
            Operand output = outputs.get(j);
            //String outputValue = output.getValue();
            String outputName = output.toString();

             // Update output index from table
            Integer version = variablesVersion.get(outputName);
//            Operand newInput = operandsTable.get(outputValue);

            if(version == null) {
               version = 1;
            } else {
               version++;
            }

            // Create new operand
            variablesVersion.put(outputName, version);
            String opValue = outputName+"."+version;
            Operand newOutput = new InternalData(opValue, output.getBits());
            operandsTable.put(outputName, newOutput);

            // Add new Output to new operation
            dotOp.connectToOutput(newOutput);
         }

      }

      return newList;
   }
}
