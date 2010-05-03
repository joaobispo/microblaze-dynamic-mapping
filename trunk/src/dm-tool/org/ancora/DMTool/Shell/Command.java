/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.DMTool.Shell;

import java.util.logging.Logger;
import org.ancora.DMTool.TransformStudy.TransformExec;

/**
 *
 * @author Joao Bispo
 */
public enum Command {
   help,
   exit,
   set,
   options,
   transform;

   public String helpMessage() {
      switch(this) {
         case help:
            return "This help message";
         case exit:
            return "Exit the program";
         case set:
            return "Set the value of a particular option";
         case options:
            return "Show the current value of avaliable options";
         case transform:
            return "Study transformation effects on code. Supports traces, elfs and blocks.";
         default:
            return "Help message not defined";
      }
   }

   public Executable getExecutable() {
      switch(this) {
         case transform:
            return new TransformExec();
         default:
            Logger.getLogger(Command.class.getName()).
                    warning("Executable not defined for '"+this.name()+"'");
            return null;
      }
   }
}
