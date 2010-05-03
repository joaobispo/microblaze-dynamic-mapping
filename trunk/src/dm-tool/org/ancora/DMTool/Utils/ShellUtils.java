/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.DMTool.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joao Bispo
 */
public class ShellUtils {

   /**
    * TODO: Currently does not support arguments with spaces in the middle.
    * Change this to support paths which have spaces using "".
    * @param command
    * @return
    */
   public static List<String> splitCommand(String command) {
      List<String> commands = new ArrayList<String>();

      String[] splitResult = command.split(COMMAND_SEPARATOR);
      for(String split : splitResult) {
         String cleanString = split.trim();
         if(cleanString.length() > 0) {
            commands.add(cleanString);
         }
      }

      return commands;
   }

   public static final String COMMAND_SEPARATOR = " ";
}
