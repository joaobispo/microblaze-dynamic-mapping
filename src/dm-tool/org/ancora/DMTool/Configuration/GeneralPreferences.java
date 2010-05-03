/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.DMTool.Configuration;

import org.ancora.SharedLibrary.Interfaces.EnumKey;
import org.ancora.SharedLibrary.Preferences.EnumPreferences;

/**
 *
 * @author Joao Bispo
 */
public enum GeneralPreferences implements EnumKey {

   outputFolder("./"),
   blockExtension("block"),
   elfExtension("elf"),
   traceExtension("trace");

   private GeneralPreferences(String defaultValue) {
      this.defaultValue = defaultValue;
   }

   public String getKey() {
      return this.name();
   }

   public String getDefaultValue() {
      return defaultValue;
   }

   /**
    * @return PreferencesEnum object associated with this program.
    */
   public static EnumPreferences getPreferences() {
      if(preferences == null) {
         preferences = initializePreferences();
      }

      return preferences;
   }


   /**
    * Initiallizes the Preferences object:
    *    - Asks for the Preferences associated with this package;
    *    - Looks for a Properties file and if found, loads and stores its
    *  definitions.
    *
    * @return a PreferencesEnum initialized for the ClientModule package.
    */
   private static EnumPreferences initializePreferences() {
      // Build Preferences
      EnumPreferences newPreferences = new EnumPreferences(GeneralPreferences.class, true);

      return newPreferences;
   }

   /**
    * INSTANCE VARIABLES
    */
   private static EnumPreferences preferences = null;
   private final String defaultValue;

}
