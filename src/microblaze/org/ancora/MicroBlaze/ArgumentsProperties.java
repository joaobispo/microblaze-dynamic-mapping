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

import static org.ancora.MicroBlaze.ArgumentsProperties.ArgumentProperty.*;
import static org.ancora.MicroBlaze.InstructionName.*;


import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;


/**
 * Indicates, for each trace instruction, which registers are for read or
 * write, by the order they appear in the trace instruction.
 *
 * @author Joao Bispo
 */
public class ArgumentsProperties {

   private static final ArgumentProperty[] writeReadRead = {write, read, read};
   private static final ArgumentProperty[] readRead = {read, read};
   private static final ArgumentProperty[] singleRead = {read};
   private static final ArgumentProperty[] writeRead = {write, read};
   private static final ArgumentProperty[] readReadRead = {read, read, read};

   /**
    * Maps MicroBlaze Instruction Types to arrays with the read/write properties
    * of the instruction arguments.
    *
    * <p>It has an incomplete set of instructions.
    */
   public static final Map<InstructionName, ArgumentProperty[]> argumentsProperties;

   static {
      Map<InstructionName, ArgumentProperty[]> aMap = new Hashtable<InstructionName, ArgumentProperty[]>();
      aMap.put(add, writeReadRead);
      aMap.put(addc, writeReadRead);
      aMap.put(addk, writeReadRead);
      aMap.put(addkc, writeReadRead);
      aMap.put(addi, writeReadRead);
      aMap.put(addic, writeReadRead);
      aMap.put(addik, writeReadRead);
      aMap.put(addikc, writeReadRead);
      aMap.put(and, writeReadRead);
      aMap.put(andi, writeReadRead);
      aMap.put(andn, writeReadRead);
      aMap.put(andni, writeReadRead);

      //--- Only used in traces
      aMap.put(beqi, readRead);
      aMap.put(beqid, readRead);
      aMap.put(bgei, readRead);
      aMap.put(bgeid, readRead);
      aMap.put(bgti, readRead);
      aMap.put(bgtid, readRead);
      aMap.put(blei, readRead);
      aMap.put(bleid, readRead);
      aMap.put(blti, readRead);
      aMap.put(bltid, readRead);
      aMap.put(bnei, readRead);
      aMap.put(bneid, readRead);

      aMap.put(br, singleRead);
      aMap.put(bra, singleRead);
      aMap.put(brd, singleRead);
      aMap.put(brad, singleRead);
      aMap.put(brld, writeRead);
      aMap.put(brald, writeRead);
      aMap.put(bri, singleRead);
      aMap.put(brai, singleRead);
      aMap.put(brid, singleRead);
      aMap.put(braid, singleRead);
      aMap.put(brlid, writeRead);
      aMap.put(bralid, writeRead);
      aMap.put(bsrli, writeReadRead);
      aMap.put(bsrai, writeReadRead);
      aMap.put(bslli, writeReadRead);
      aMap.put(cmp, writeReadRead);
      aMap.put(cmpu, writeReadRead);
      aMap.put(idiv, writeReadRead);
      aMap.put(idivu, writeReadRead);
      aMap.put(imm, singleRead);
      aMap.put(lbu, writeReadRead);
      aMap.put(lbui, writeReadRead);
      aMap.put(lhu, writeReadRead);
      aMap.put(lhui, writeReadRead);
      aMap.put(lw, writeReadRead);
      aMap.put(lwi, writeReadRead);
      aMap.put(mul, writeReadRead);
      aMap.put(muli, writeReadRead);
      aMap.put(or, writeReadRead);
      aMap.put(ori, writeReadRead);
      aMap.put(rsub, writeReadRead);
      aMap.put(rsubc, writeReadRead);
      aMap.put(rsubk, writeReadRead);
      aMap.put(rsubkc, writeReadRead);
      aMap.put(rsubi, writeReadRead);
      aMap.put(rsubic, writeReadRead);
      aMap.put(rsubik, writeReadRead);
      aMap.put(rsubikc, writeReadRead);
      aMap.put(rtsd, readRead);
      aMap.put(sb, readReadRead);
      aMap.put(sbi, readReadRead);
      aMap.put(sext16, writeRead);
      aMap.put(sext8, writeRead);
      aMap.put(sh, readReadRead);
      aMap.put(shi, readReadRead);
      aMap.put(sra, writeRead);
      aMap.put(srl, writeRead);
      aMap.put(sw, readReadRead);
      aMap.put(swi, readReadRead);

      aMap.put(xori, writeReadRead);

      argumentsProperties = Collections.unmodifiableMap(aMap);
   }

   public static ArgumentProperty[] getProperties(InstructionName type) {
      ArgumentProperty[] props = argumentsProperties.get(type);
      if (props == null) {
         Logger.getLogger(ArgumentsProperties.class.getName()).
                 warning("Instruction '" + type.name() + "' not present in table. Returning " +
                 "empty array.");
         return new ArgumentProperty[0];
      } else {
         return props;
      }
   }

   public enum ArgumentProperty {
      write,
      read
   };
}


