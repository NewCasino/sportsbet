package com.pr7.common.web.taglib;

import ro.isdc.wro.model.resource.ResourceType;

/**
 * @author mark.ma
 */
public class ScriptTagHandler extends ResourceTagHandler {

    public String extName = ".js";

    public String outputPrefix = "\t\t<script type='text/javascript' src='";

    public String outputSuffix = "'></script>\r\n";

    public ResourceType type = ResourceType.JS;

    public ScriptTagHandler () {
        super.extName = this.extName;
        super.outputPrefix = this.outputPrefix;
        super.outputSuffix = this.outputSuffix;
        super.type = this.type;
    }

}