/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.DMTool.TransformStudy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.ancora.DMTool.Configuration.Definitions;
import org.ancora.DMTool.Configuration.FileType;
import org.ancora.DMTool.Configuration.GeneralPreferences;
import org.ancora.DMTool.Shell.Executable;
import org.ancora.DMTool.Utils.EnumUtils;
import org.ancora.DMTool.Utils.IoUtilsAppend;
import org.ancora.DynamicMapping.InstructionBlock.InstructionBlock;
import org.ancora.DynamicMapping.InstructionBlock.MbInstructionBlockWriter;

/**
 *
 * @author Joao Bispo
 */
public class TransformExec implements Executable {

   public TransformExec() {
      logger = Logger.getLogger(TransformExec.class.getName());
   }



   public boolean execute(List<String> arguments) {
      if(arguments.size() < 1) {
         logger.info("Too few arguments for 'transform' ("+arguments.size()+"). Minimum is 1.");
         return false;
      }

      // Get type of files extension
      /*
      FileType extensionType = EnumUtils.valueOf(FileType.class, arguments.get(0));
      if(extensionType == null) {
         logger.info("Invalid file type '"+arguments.get(0)+"'. Avaliable:");
         for(FileType type : FileType.values()) {
            logger.info("- "+type.name());
         }
         return false;
      }

      String extension = getExtension(extensionType);
      */

      // Check file/folder
      File file = new File(arguments.get(0));
      if(!file.exists()) {
         logger.info("Path '"+arguments.get(0)+"' does not exist.");
         return false;
      }

      // Get files
      List<File> inputFiles;
      if(file.isFile()) {
         inputFiles = new ArrayList<File>(1);
         inputFiles.add(file);
      } else {
         inputFiles = IoUtilsAppend.getFilesRecursive(file);
      }

      // Process files
      List<NamedBlock> blocks = getBlocks(inputFiles);
      
      

      return true;
   }

   private List<NamedBlock> getBlocks(List<File> inputFiles) {
      List<NamedBlock> blocks = new ArrayList<NamedBlock>();

      for(File file : inputFiles) {
         addBlocks(file, blocks);
      }

      return blocks;
   }

   private void addBlocks(File file, List<NamedBlock> blocks) {
      // Determine file extension and determine type of file
      String filename = file.getName();
      int separatorIndex = filename.lastIndexOf(Definitions.EXTENSION_SEPARATOR);
      String extension = filename.substring(separatorIndex+1);
      FileType fileType = FileType.getFileType(extension);

      if(fileType == null) {
         return;
      }

      if(fileType == FileType.block) {
         addBlocksLoader(file, blocks);
         return;
      }
/*
      if(fileType == FileType.elf) {
         addBlocksTraceProcessor(file, blocks);
         return;
      }

      if(fileType == FileType.trace) {
         addBlocksTraceProcessor(file, blocks);
         return;
      }
*/
      return;
   }

   /**
    * File is a instruction block.
    * 
    * @param file
    * @param blocks
    */
   private void addBlocksLoader(File file, List<NamedBlock> blocks) {
      InstructionBlock block = MbInstructionBlockWriter.loadInstructionBlock(file);
      if(block == null) {
         logger.warning("Could not load block file '"+file+"'.");
      }

      String blockName = file.getName();
      int separatorIndex = blockName.lastIndexOf(Definitions.EXTENSION_SEPARATOR);
      blockName = blockName.substring(0, separatorIndex);

      blocks.add(new NamedBlock(block, blockName));
   }

   /**
    * INSTANCE VARIABLES
    */
   private Logger logger;









}
