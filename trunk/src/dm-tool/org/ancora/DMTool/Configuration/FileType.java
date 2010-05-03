/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.DMTool.Configuration;

import java.util.logging.Logger;
import org.ancora.SharedLibrary.Preferences.EnumPreferences;

/**
 *
 * @author Joao Bispo
 */
public enum FileType {
   trace,
   elf,
   block;

   public static FileType getFileType(String extension) {
      EnumPreferences prefs = GeneralPreferences.getPreferences();

      if(prefs.getPreference(GeneralPreferences.blockExtension).equals(extension)) {
         return block;
      }

      if(prefs.getPreference(GeneralPreferences.elfExtension).equals(extension)) {
         return elf;
      }

      if(prefs.getPreference(GeneralPreferences.traceExtension).equals(extension)) {
         return trace;
      }

      return null;
   }

      public static String getExtension(FileType fileType) {
      GeneralPreferences pref = null;
      switch(fileType) {
         case block:
            pref = GeneralPreferences.blockExtension;
            break;
         case elf:
            pref = GeneralPreferences.elfExtension;
            break;
         case trace:
            pref = GeneralPreferences.traceExtension;
            break;
         default:
            Logger.getLogger(FileType.class.getName()).
                    warning("File type not defined: "+fileType.name());
            return null;
      }

      return GeneralPreferences.getPreferences().getPreference(pref);
   }

}
