/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pr7.common.web.taglib;

import ro.isdc.wro.model.resource.ResourceType;

/**
 *
 * @author mark.ma
 */
@SuppressWarnings("FieldNameHidesFieldInSuperclass")
public class StyleTagHandler extends ResourceTagHandler {

    public String extName = ".css";

    public String outputPrefix = "\t\t<link type='text/css' href='";

    public String outputSuffix = "' rel='stylesheet' media='all' />\r\n";
    
    public ResourceType type = ResourceType.CSS;

    public StyleTagHandler () {
        super.extName = this.extName;
        super.outputPrefix = this.outputPrefix;
        super.outputSuffix = this.outputSuffix;
        super.type = this.type;
    }

}
