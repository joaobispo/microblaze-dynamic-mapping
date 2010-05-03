/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.DMTool.TransformStudy;

import org.ancora.DynamicMapping.InstructionBlock.InstructionBlock;

/**
 *
 * @author Joao Bispo
 */
public class NamedBlock {

   public NamedBlock(InstructionBlock block, String name) {
      this.block = block;
      this.name = name;
   }

   public InstructionBlock getBlock() {
      return block;
   }

   public String getName() {
      return name;
   }

   

   /**
    * INSTANCE VARIABLES
    */
   private InstructionBlock block;
   private String name;
}
