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

import java.util.EnumSet;

/**
 *
 * @author Joao Bispo
 */
public interface InstructionProperties {

   /**
    * Which Instructions are Branches
    */
   EnumSet<InstructionName> JUMP_INSTRUCTIONS = EnumSet.of(
           InstructionName.beq,
           InstructionName.beqd,
           InstructionName.beqi,
           InstructionName.beqid,
           InstructionName.bge,
           InstructionName.bged,
           InstructionName.bgei,
           InstructionName.bgeid,
           InstructionName.bgt,
           InstructionName.bgtd,
           InstructionName.bgti,
           InstructionName.bgtid,
           InstructionName.ble,
           InstructionName.bled,
           InstructionName.blei,
           InstructionName.bleid,
           InstructionName.blt,
           InstructionName.bltd,
           InstructionName.blti,
           InstructionName.bltid,
           InstructionName.bne,
           InstructionName.bned,
           InstructionName.bnei,
           InstructionName.bneid,
           InstructionName.br,
           InstructionName.bra,
           InstructionName.brd,
           InstructionName.brad,
           InstructionName.brld,
           InstructionName.brald,
           InstructionName.bri,
           InstructionName.brai,
           InstructionName.brid,
           InstructionName.braid,
           InstructionName.brlid,
           InstructionName.bralid,
           InstructionName.brk,
           InstructionName.brki,
           InstructionName.rtbd,
           InstructionName.rtid,
           InstructionName.rted,
           InstructionName.rtsd);


   /**
    * Which instructions have delay slot
    */
   EnumSet<InstructionName> INSTRUCTIONS_WITH_DELAY_SLOT = EnumSet.of(
           InstructionName.beqd,
           InstructionName.beqid,
           InstructionName.bged,
           InstructionName.bgeid,
           InstructionName.bgtd,
           InstructionName.bgtid,
           InstructionName.bled,
           InstructionName.bleid,
           InstructionName.bltd,
           InstructionName.bltid,
           InstructionName.bned,
           InstructionName.bneid,
           InstructionName.brd,
           InstructionName.brad,
           InstructionName.brld,
           InstructionName.brald,
           InstructionName.brid,
           InstructionName.braid,
           InstructionName.brlid,
           InstructionName.bralid,
           InstructionName.rtbd,
           InstructionName.rtid,
           InstructionName.rted,
           InstructionName.rtsd);
}
