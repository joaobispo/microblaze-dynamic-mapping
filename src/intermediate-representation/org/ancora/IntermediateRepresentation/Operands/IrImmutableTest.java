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

import java.util.logging.Logger;
import org.ancora.IntermediateRepresentation.ImmutableTest;
import org.ancora.IntermediateRepresentation.Operand;

/**
 *
 * @author Joao Bispo
 */
public class IrImmutableTest implements ImmutableTest {

   public boolean isImmutable(Operand operand) {
      if(operand.getType() == OperandType.literal) {
         return true;
      }
      else {
         Logger.getLogger(IrImmutableTest.class.getName()).
                 warning("Case for operand of type '"+operand.getType()+"' not defined.");
       return false;
      }
   }

}
