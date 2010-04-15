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

package org.ancora.MicroBlaze;

import java.util.logging.Logger;

/**
 * Represents the valid operations in the MicroBlaze Architecture, according
 * to MicroBlaze Processor Reference Guide v10.3.
 *
 * TODO: Add the names of the instructions; Change MicroBlaze.Instruction to
 * use the enum instead of a String.
 * @author Joao Bispo
 */
public enum InstructionName {

   add,
   addc,
   addk,
   addkc,
   addi,
   addic,
   addik,
   addikc,
   and,
   andi,
   andn,
   andni,
   beq,
   beqd,
   beqi,
   beqid,
   bge,
   bged,
   bgei,
   bgeid,
   bgt,
   bgtd,
   bgti,
   bgtid,
   ble,
   bled,
   blei,
   bleid,
   blt,
   bltd,
   blti,
   bltid,
   bne,
   bned,
   bnei,
   bneid,
   br,
   bra,
   brd,
   brad,
   brld,
   brald,
   bri,
   brai,
   brid,
   braid,
   brlid,
   bralid,
   brk,
   brki,
   bsrl,
   bsra,
   bsll,
   bsrli,
   bsrai,
   bslli,
   cmp,
   cmpu,
   fadd,
   frsub,
   fmul,
   fdiv,
   fcmp,
   flt,
   fint,
   fsqrt,
   get,
   getd,
   idiv,
   idivu,
   imm,
   lbu,
   lbui,
   lhu,
   lhui,
   lw,
   lwi,
   lwx,
   mfs,
   msrclr,
   msrset,
   mts,
   mul,
   mulh,
   mulhu,
   mulhsu,
   muli,
   or,
   ori,
   pcmpbf,
   pcmpeq,
   pcmpne,
   put,
   putd,
   // There are a lot of puts...
   rsub,
   rsubc,
   rsubk,
   rsubkc,
   rsubi,
   rsubic,
   rsubik,
   rsubikc,
   rtbd,
   rtid,
   rted,
   rtsd,
   sb,
   sbi,
   sext16,
   sext8,
   sh,
   shi,
   sra,
   src,
   srl,
   sw,
   swi,
   swx,
   wdc,
   // There are other wdc, with a '.'
   wdc_flush,
   wdc_clear,
   wic,
   xor,
   xori;

   /**
    * @param instructionName
    * @return the InstructionType with the same name as the given String, or
    * null if could not find an object with the same name
    */
   public static InstructionName getEnum(String instructionName) {
      try{
         return valueOf(instructionName.toLowerCase());
      } catch(IllegalArgumentException ex) {
         Logger.getLogger(InstructionName.class.getName()).
                 warning("Instruction not yet present in the list: '"+instructionName+"'");
         return null;
      }
   }

}
