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

package org.ancora.DMTool.Shell;

import org.ancora.DMTool.TransformStudy.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;
import org.ancora.DMTool.Configuration.Definitions;
import org.ancora.DMTool.Configuration.FileType;
import org.ancora.DMTool.Shell.System.GeneralPreferences;
import org.ancora.DMTool.Shell.System.Executable;
import org.ancora.DMTool.Shell.System.MapperDispenser;
import org.ancora.DMTool.Shell.System.PartitionerDispenser;
import org.ancora.DMTool.Shell.System.Transform.OperationListStats;
import org.ancora.DMTool.Shell.System.TransformDispenser;
import org.ancora.DMTool.TraceProcessor.TraceProcessor;
import org.ancora.DMTool.TraceProcessor.TraceProcessorWorker;
import org.ancora.DMTool.Utils.EnumUtils;
import org.ancora.DMTool.Utils.IoUtilsAppend;
import org.ancora.DMTool.Utils.TransformUtils;
import org.ancora.DynamicMapping.InstructionBlock.InstructionBlock;
import org.ancora.DynamicMapping.InstructionBlock.InstructionBusReader;
import org.ancora.DynamicMapping.InstructionBlock.MbElfReader;
import org.ancora.DynamicMapping.InstructionBlock.MbInstructionBlockWriter;
import org.ancora.DynamicMapping.InstructionBlock.MbTraceReader;
import org.ancora.DynamicMapping.Partitioning.Partitioner;
import org.ancora.IntermediateRepresentation.Ilp.Mapper;
import org.ancora.IntermediateRepresentation.Operation;
import org.ancora.SharedLibrary.ParseUtils;
import org.ancora.SharedLibrary.Preferences.EnumPreferences;
import org.ancora.Transformations.Transformation;

/**
 *
 * @author Joao Bispo
 */
public class Transform implements Executable {

   public Transform() {
      logger = Logger.getLogger(Transform.class.getName());



   }

   private void setup() {
      EnumPreferences prefs = GeneralPreferences.getPreferences();
      blockExtension = prefs.getPreference(GeneralPreferences.blockExtension);
      elfExtension = prefs.getPreference(GeneralPreferences.elfExtension);
      traceExtension = prefs.getPreference(GeneralPreferences.traceExtension);
      mapper = MapperDispenser.getCurrentMapper();
      transf = TransformDispenser.getCurrentTransformations();

   }



   public boolean execute(List<String> arguments) {
      setup();

      if(arguments.size() < 1) {
         logger.info("Too few arguments for 'transform' ("+arguments.size()+"). Minimum is 1:");
         logger.info("transform <folder/file>");
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
         java.util.Set<String> supportedExtensions = new HashSet<String>();
         supportedExtensions.add(blockExtension);
         supportedExtensions.add(elfExtension);
         supportedExtensions.add(traceExtension);
         //inputFiles = IoUtilsAppend.getFilesRecursive(file);
         inputFiles = IoUtilsAppend.getFilesRecursive(file, supportedExtensions);
      }

      logger.info("Found "+inputFiles.size()+" files.");

      processFiles(inputFiles);

      //System.out.println("Input files:");
      //System.out.println(inputFiles);

      // Process files
      /*
      List<NamedBlock> blocks = getBlocks(inputFiles);
      System.out.println("Found blocks:");
      for(NamedBlock block : blocks) {
         System.out.println(block.getName() + "("+block.getBlock().getRepetitions()+" repetitions)");
      }
      */

      return true;
   }

   /*
   private List<NamedBlock> getBlocks(List<File> inputFiles) {
      List<NamedBlock> blocks = new ArrayList<NamedBlock>();

      for(File file : inputFiles) {
         addBlocks(file, blocks);
      }

      return blocks;
   }
    */

   /*
   private void addBlocks(File file, List<NamedBlock> blocks) {
      // Determine file extension and determine type of file
      String filename = file.getName();
      int separatorIndex = filename.lastIndexOf(Definitions.EXTENSION_SEPARATOR);
      String extension = filename.substring(separatorIndex+1);
      FileType fileType = FileType.getFileType(extension);
      String baseFilename = filename.substring(0, separatorIndex);

      if(fileType == null) {
         return;
      }

      if(fileType == FileType.block) {
         addBlocksLoader(file, blocks);
         return;
      }

      if(fileType == FileType.elf) {
         String systemConfig = "./Configuration Files/systemconfig.xml";
         InstructionBusReader busReader = MbElfReader.createMbElfReader(systemConfig, file.getAbsolutePath());
//         TraceProcessor.processReader(busReader, baseFilename, blocks);
         return;
      }

      
      if(fileType == FileType.trace) {
         InstructionBusReader busReader = MbTraceReader.createTraceReader(file);
//         TraceProcessor.processReader(busReader, baseFilename, blocks);
         return;
      }

      return;
   }
    */

   /**
    * File is a instruction block.
    * 
    * @param file
    * @param blocks
    */
   /*
   private List<InstructionBlock> addBlocksLoader(File file) {
   //private void addBlocksLoader(File file, List<NamedBlock> blocks) {
      InstructionBlock block = MbInstructionBlockWriter.loadInstructionBlock(file);
      if(block == null) {
         logger.warning("Could not load block file '"+file+"'.");
      }

      String blockName = file.getName();
      int separatorIndex = blockName.lastIndexOf(Definitions.EXTENSION_SEPARATOR);
      blockName = blockName.substring(0, separatorIndex);

      blocks.add(new NamedBlock(block, blockName));
   }
    */

   private void processFiles(List<File> inputFiles) {


      for(File file : inputFiles) {
         logger.info("Processing file '"+file.getName()+"'...");
         String baseFilename = ParseUtils.removeSuffix(file.getName(), Definitions.EXTENSION_SEPARATOR);

         // Get InstructionBlocks
         List<InstructionBlock> blocks = getBlocks(file);
         for(int i=0; i<blocks.size(); i++) {

            InstructionBlock block = blocks.get(i);
            logger.info("Block "+i+", "+block.getRepetitions()+" repetitions.");

            // Transform Instruction Block into PureIR
            List<Operation> operations = TransformUtils.mbToPureIr(block);

            if(operations == null) {
               continue;
            }

            // Get stats before transformations
            OperationListStats beforeTransf = OperationListStats.buildStats(operations, mapper);

            // Transform
            for(Transformation t : transf) {
               operations = t.transform(operations);
            }

            // Get stats after transformation
            OperationListStats afterTransf = OperationListStats.buildStats(operations, mapper);

            showStats(beforeTransf, afterTransf);
            
         }
         

      }
   }

   private List<InstructionBlock> getBlocks(File file) {

      // Determine file extension and determine type of file
      String filename = file.getName();
      int separatorIndex = filename.lastIndexOf(Definitions.EXTENSION_SEPARATOR);
      String extension = filename.substring(separatorIndex+1);

      if(extension.equals(blockExtension)) {
         return TransformUtils.blockLoader(file);
      }

      if(extension.equals(elfExtension)) {
         String systemConfig = "./Configuration Files/systemconfig.xml";
         InstructionBusReader busReader = MbElfReader.createMbElfReader(systemConfig, file.getAbsolutePath());
         TraceProcessorWorker worker = getProcessorWorker(busReader);
         return worker.processTrace(busReader);
      }

      if(extension.equals(traceExtension)) {
         InstructionBusReader busReader = MbTraceReader.createTraceReader(file);
         TraceProcessorWorker worker = getProcessorWorker(busReader);
         return worker.processTrace(busReader);
      }
      /*
      if(fileType == FileType.elf) {
         String systemConfig = "./Configuration Files/systemconfig.xml";
         InstructionBusReader busReader = MbElfReader.createMbElfReader(systemConfig, file.getAbsolutePath());
//         TraceProcessor.processReader(busReader, baseFilename, blocks);
         return;
      }


      if(fileType == FileType.trace) {
         InstructionBusReader busReader = MbTraceReader.createTraceReader(file);
//         TraceProcessor.processReader(busReader, baseFilename, blocks);
         return;
      }
*/
      // Not of the type expected
      logger.warning("Could not process file with extension '"+extension+"'.");
      return new ArrayList<InstructionBlock>();
   }

   private TraceProcessorWorker getProcessorWorker(InstructionBusReader busReader) {
      EnumPreferences prefs = GeneralPreferences.getPreferences();

      // Get the partitioner
      Partitioner partitioner = PartitionerDispenser.getCurrentPartitioner();
      TraceProcessorWorker worker = new TraceProcessorWorker(partitioner);

      // Setup worker
      boolean useGatherer = Boolean.parseBoolean(prefs.getPreference(GeneralPreferences.useGatherer));
      boolean useSelector = Boolean.parseBoolean(prefs.getPreference(GeneralPreferences.useSelector));
      int selectorThreshold = ParseUtils.parseInt(prefs.getPreference(GeneralPreferences.selectorThreshold));
      
      worker.setUseGatherer(useGatherer);
      worker.setUseSelector(useSelector);
      worker.setSelectorRepThreshold(selectorThreshold);

      return worker;
   }

   private static void showStats(OperationListStats beforeTransf, OperationListStats afterTransf) {
      // only show after stats that change
      String[] param = {"CommCosts", "Cpl", "Ilp", "Operations", "MbOperations"};
      String[] before = {String.valueOf(beforeTransf.getCommunicationCost()),
        String.valueOf(beforeTransf.getCpl()),
        String.valueOf(beforeTransf.getIlp()),
        String.valueOf(beforeTransf.getNumberOfOperations()),
        String.valueOf(beforeTransf.getNumberOfMbOps())
      };
      String[] after = {String.valueOf(afterTransf.getCommunicationCost()),
        String.valueOf(afterTransf.getCpl()),
        String.valueOf(afterTransf.getIlp()),
        String.valueOf(afterTransf.getNumberOfOperations()),
        String.valueOf(afterTransf.getNumberOfMbOps())
      };


      //System.out.println("Before:");
      //System.out.println(beforeTransf);

      System.out.println("Changes:");
      boolean noChanges = true;
      for (int i = 0; i < param.length; i++) {
         if (!before[i].equals(after[i])) {
            noChanges = false;
            System.out.println(param[i]+":"+before[i]+"->"+after[i]+";");
         }
      }

      if(noChanges) {
         System.out.println("None");
      }

      System.out.println("------------------------");
   }

   /**
    * INSTANCE VARIABLES
    */
   private Logger logger;
   private String traceExtension;
   private String elfExtension;
   private String blockExtension;
   private Mapper mapper;
   private Transformation[] transf;













}
