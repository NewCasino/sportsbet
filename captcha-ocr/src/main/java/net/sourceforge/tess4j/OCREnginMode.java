/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.tess4j;

/**
 *
 * @author Admin
 */
public enum OCREnginMode {
    /**
     * Run Tesseract only - fastest
     */
    OEM_TESSERACT_ONLY,
    
    /**
     * Run Cube only - better accuracy, but slower
     */
    OEM_CUBE_ONLY,
    /**
     * Run both and combine results - best accuracy
     */
    OEM_TESSERACT_CUBE_COMBINED,
    
    /**
     * Specify this mode to indicate that any of the above modes should be automatically 
     * inferred from the variables in the language-specific config,
     * or if not specified in any of the above should be set to the default OEM_TESSERACT_ONLY.
     */
    OEM_DEFAULT;
}
