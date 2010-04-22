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

package org.ancora.Main;

import java.util.List;
import org.ancora.IntermediateRepresentation.Ilp.IlpScenario;
import org.ancora.IntermediateRepresentation.Operation;

/**
 *
 * @author Joao Bispo
 */
public class OperationListStats {

   private OperationListStats(int numberOfLines, int numberOfOperations, int liveins, int liveouts) {
      this.numberOfLines = numberOfLines;
      this.numberOfOperations = numberOfOperations;
      this.liveins = liveins;
      this.liveouts = liveouts;
   }





   public static OperationListStats buildStats(List<Operation> operations, IlpScenario ilpScene) {
      // Create data
      ilpScene.reset();
      ilpScene.processOperations(operations);

      // Gather data
      return new OperationListStats(ilpScene.getNumberOfLines(), ilpScene.getNumberOfOps(),
              ilpScene.getLiveIns(), ilpScene.getLiveOuts());
      
   }

   /**
    * ILP calculated as the number of operations / number of occupied lines
    * 
    * @return
    */
   public double getIlp() {
      return ((double)numberOfOperations/(double)numberOfLines);
   }

   /**
    * Critical Path Lenght calculated as the number of occupied lines.
    * @return
    */
   public int getCpl() {
      return numberOfLines;
   }

   /**
    *
    * @return number of operations
    */
   public int getNumberOfOperations() {
      return numberOfOperations;
   }

   /**
    * Calculated as the number of live-ins + number of live-outs.
    *
    * @return
    */
   public int getCommunicationCost() {
      return liveins + liveouts;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      builder.append("ILP:"+getIlp()+"\n");
      builder.append("CPL:"+getCpl()+"\n");
      builder.append("#Inst:"+getNumberOfOperations()+"\n");
      builder.append("CommunicationCosts:"+getCommunicationCost()+"\n");

      return builder.toString();
   }



   /**
    * INSTANCE VARIABLES
    */
   private int numberOfLines;
   private int numberOfOperations;
   private int liveins;
   private int liveouts;
}
