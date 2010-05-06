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

import org.ancora.IntermediateRepresentation.OperationType;
import org.ancora.IntermediateRepresentation.Operations.*;
import org.ancora.IntermediateRepresentation.Operand;
import org.ancora.IntermediateRepresentation.Operation;

/**
 *
 * @author Joao Bispo
 */
public class Exit extends Operation {

   public Exit(int address, Operand exitAddress, Operand exitFlag) {
      super(address);
      this.exitAddress = exitAddress;
      this.exitFlag = exitFlag;

      connectToInput(exitFlag);
      connectToInput(exitAddress);
   }



   @Override
   public Enum getType() {
      return null;//OperationType.Exit;
   }

   /*
   @Override
   public String getValue() {
      return "Exit";
   }
    */

   @Override
   public boolean hasSideEffects() {
      return false;
   }

   /**
    * INSTANCE VARIABLES
    */
   private Operand exitAddress;
   private Operand exitFlag;

}
