/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.common;

/**
 *
 * @author Joao Bispo
 */
public class IoUtilsAppend {

   public static String removeExtension(String filename, String separator) {
      int extIndex = filename.lastIndexOf(separator);
      return filename.substring(0, extIndex);
   }
}
