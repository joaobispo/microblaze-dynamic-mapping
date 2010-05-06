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

package org.ancora.IntermediateRepresentation.Operations.deprecated;

import org.ancora.IntermediateRepresentation.Operations.*;
import java.util.List;
import java.util.logging.Logger;
import org.ancora.IntermediateRepresentation.Operand;
import org.ancora.IntermediateRepresentation.Operation;

/**
 * <p><b>Inputs:</b>
 * <br>inputList - list of inputs.
 *
 * <p><b>Outputs:</b>
 * <br>outputList - list of results.
 *
 *  <b>Description</b>: Moves the contents of inputs to outputs.
 *
 * @author Joao Bispo
 */
public class Move extends Operation {

   public Move(int address, List<Operand> inputList, List<Operand> outputList) {

      super(address);

      int inputSize = inputList.size();
      int outputSize = outputList.size();

      int size = inputSize;
      if(inputSize != outputSize) {
         size = Math.min(inputSize, outputSize);
         Logger.getLogger(Move.class.getName()).
                 warning("Mismatch between size of input list ("+inputSize+") " +
                 "and output list ("+outputSize+").");
      }

      for (int i = 0; i < size; i++) {
         connectToInput(inputList.get(i));
         connectToOutput(outputList.get(i));
      }

   }


   @Override
   public Enum getType() {
      return null;//OperationType.Move;
   }

   @Override
   public String toString() {
      return "MOVE";
   }


   @Override
   public boolean hasSideEffects() {
      return false;
   }


   /*
   public Operand getInput() {
      return getInputs().get(0);
   }

   public Operand getOutput() {
      //return output1;
      return getOutputs().get(0);
   }
    */
   
}
