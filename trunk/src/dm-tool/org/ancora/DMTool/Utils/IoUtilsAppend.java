/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.DMTool.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Joao Bispo
 */
public class IoUtilsAppend {

   public static String removeExtension(String filename, String separator) {
      int extIndex = filename.lastIndexOf(separator);
      return filename.substring(0, extIndex);
   }
/*
      public static List<File> getFilesRecursive(File folder, String extension) {
      List<File> fileList = new ArrayList<File>();
      File[] files = folder.listFiles(new ExtensionFilter(extension));

      for(File file : files) {
         fileList.add(file);
      }

      files = folder.listFiles();
      for(File file : files) {
         if(file.isDirectory()) {
            fileList.addAll(getFilesRecursive(file, extension));
         }
      }

      return fileList;
   }
 */

      public static List<File> getFilesRecursive(File folder) {
      List<File> fileList = new ArrayList<File>();
      File[] files = folder.listFiles();

      for(File file : files) {
         if(file.isFile()) {
            fileList.add(file);
         }
      }

      for(File file : files) {
         if(file.isDirectory()) {
            fileList.addAll(getFilesRecursive(file));
         }
      }

      return fileList;
   }

   /**
    * Returns files from a folder with a certain extension.
    *
    * @param folder
    * @param extension
    * @return
    */
      /*
   public static File[] getFiles(File folder, String extension) {
      return folder.listFiles(new ExtensionFilter(extension));
   }
       */
}
