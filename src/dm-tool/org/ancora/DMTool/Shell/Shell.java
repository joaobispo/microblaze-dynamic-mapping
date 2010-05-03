/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.DMTool.Shell;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import org.ancora.DMTool.Configuration.GeneralPreferences;
import org.ancora.DMTool.Utils.EnumUtils;
import org.ancora.DMTool.Utils.LineReader;
import org.ancora.DMTool.Utils.ShellUtils;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.Preferences.EnumPreferences;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class Shell {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       // Configure Logger to capture all output to console
       LoggingUtils.setupConsoleOnly();

       if (args.length > 0) {
          // Try to get script file
          runScript(args);
       } else {
          // Run the shell
          runShell();
       }




      
    }

   private static void runScript(String[] args) {
      File scriptFile = IoUtils.existingFile(args[0]);
      if(scriptFile == null) {
         logger.info("Terminating...");
         return;
      }

      LineReader lineReader = LineReader.createLineReader(scriptFile);
      String command = lineReader.nextLine();
      int lineCounter = 1;
      while(command != null) {
         boolean success = executeCommand(command);
         if(!success) {
            logger.info("(Problems on line "+lineCounter+")");
         }
         command = lineReader.nextLine();
         lineCounter++;
      }

   }

   private static void runShell() {
       // Initialize Scanner
       Scanner scanner = new Scanner(System.in);

       // Show welcome message
       logger.info("Dynamic Mapping Shell (MicroBlaze version)");

       // Start cycle
       while(true) {
          String command = scanner.nextLine();
          executeCommand(command);
       }
   }

   private static boolean executeCommand(String command) {
      // Split String
      List<String> splitCommand = ShellUtils.splitCommand(command);

      // Check if there is a command
      if(splitCommand.size() == 0) {
         // Show current properties - dropped
         return true;
      }

      // Get Command
      Command commandEnum = EnumUtils.valueOf(Command.class, splitCommand.get(0));

      if(commandEnum == null) {
         logger.info("Invalid command '"+splitCommand.get(0)+"'");
         return false;
      }

      splitCommand = splitCommand.subList(1, splitCommand.size());

      // Check simple commands (exit, help, config, set)
      if(commandEnum == Command.exit) {
         logger.info("Bye...");
         System.exit(0);
      }

      if(commandEnum == Command.help) {
         showHelp();
         return true;
      }

      if(commandEnum == Command.set) {
         return executeSet(splitCommand);
      }

      if(commandEnum == Command.options) {
         return executeOptions();
      }

      Executable exec = commandEnum.getExecutable();
      if(exec != null) {
         return exec.execute(splitCommand);
      }

      return false;
   }

   private static void showHelp() {
      logger.info("Supported commands:");
      for(Command command : Command.values()) {
         String message = command.name() + " - " + command.helpMessage();
         logger.info(message);
      }
   }

   private static boolean executeSet(List<String> args) {
      EnumPreferences prefs = GeneralPreferences.getPreferences();

      if(args.size() < 2) {
         logger.info("Too few arguments for 'set' ("+args.size()+"). Minimum is 2.");
         return false;
      }

      // Get preference
      String prefString = args.get(0);
      GeneralPreferences prefEnum = EnumUtils.valueOf(GeneralPreferences.class, prefString);
      if(prefEnum == null) {
         logger.info("'"+args.get(0)+"' is not a valid setting. Avaliable:");
         //logger.info("");
         for(GeneralPreferences gPref : GeneralPreferences.values()) {
            logger.info("- "+gPref.name());
         }
         return false;
      }

      // Get value
      String value = args.get(1);

      // Update preferences
      prefs.putPreference(prefEnum, value);

      return true;
   }

   private static boolean executeOptions() {
      EnumPreferences prefs = GeneralPreferences.getPreferences();

      logger.info("Current values for program options:");
      for(GeneralPreferences gPref : GeneralPreferences.values()) {
         String value = prefs.getPreference(gPref);
         String message = gPref.name() + " - " + value;
         logger.info(message);
      }

      return true;
   }

   /**
    * INSTANCE VARIABLES
    */
   private static Logger logger = Logger.getLogger(Shell.class.getName());











}
