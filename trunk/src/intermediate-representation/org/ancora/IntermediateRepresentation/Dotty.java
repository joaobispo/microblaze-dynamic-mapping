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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
         builder.append(op.hashCode());
         builder.append("[label=\"");
         builder.append(op.getType()+"-"+op.getValue());
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

}
