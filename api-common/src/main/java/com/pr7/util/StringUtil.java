/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Admin
 */
public class StringUtil {
/**
   Replace characters having special meaning in regular expressions
   with their escaped equivalents, preceded by a '\' character.

   <P>The escaped characters include :
  <ul>
  <li>.
  <li>\
  <li>?, * , and +
  <li>&
  <li>:
  <li>{ and }
  <li>[ and ]
  <li>( and )
  <li>^ and $
  </ul>
  */
  public static String escapeRegex(String aRegexFragment){
    final StringBuilder result = new StringBuilder();

    final StringCharacterIterator iterator =
      new StringCharacterIterator(aRegexFragment)
    ;
    char character =  iterator.current();
    while (character != CharacterIterator.DONE ){
      /*
       All literals need to have backslashes doubled.
      */
      if (character == '.') {
        result.append("\\.");
      }
      else if (character == '\\') {
        result.append("\\\\");
      }
      else if (character == '?') {
        result.append("\\?");
      }
      else if (character == '*') {
        result.append("\\*");
      }
      else if (character == '+') {
        result.append("\\+");
      }
      else if (character == '&') {
        result.append("\\&");
      }
      else if (character == ':') {
        result.append("\\:");
      }
      else if (character == '{') {
        result.append("\\{");
      }
      else if (character == '}') {
        result.append("\\}");
      }
      else if (character == '[') {
        result.append("\\[");
      }
      else if (character == ']') {
        result.append("\\]");
      }
      else if (character == '(') {
        result.append("\\(");
      }
      else if (character == ')') {
        result.append("\\)");
      }
      else if (character == '^') {
        result.append("\\^");
      }
      else if (character == '$') {
        result.append("\\$");
      }
      else {
        //the char is not a special one
        //add it to the result as is
        result.append(character);
      }
      character = iterator.next();
    }
    return result.toString();
  }
  
  	public static String extractNumber(String source) {  		
  		StringBuilder builder = new StringBuilder();
		Pattern p = Pattern.compile("[0-9]+");
		Matcher m = p.matcher(source);
		while (m.find()) {
			builder.append(m.group());
		}
		
		return builder.toString();
  	}
  	
  	public static String replaceNonDigit(String source) {
  		if (source == null)
  			return null;
  		
  		StringBuilder builder = new StringBuilder();  		
  		for(int i=0; i < source.length(); i++) {
  			char cval = source.charAt(i);
  			System.out.println("cval = " + cval + ", int val = " + (int) cval);
  			if ((int)cval >= (int) '0' && (int)cval <= (int) '9') {
  				//do nothing
  				builder.append(cval);
  			} else {
  				builder.append((int)cval);
  			}
  		}
  		
  		return builder.toString();
  	}
}
