/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.common.web.wro4j.support;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import ro.isdc.wro.config.Context;
import ro.isdc.wro.model.WroModel;
import ro.isdc.wro.model.factory.WroModelFactory;
import ro.isdc.wro.model.factory.XmlModelFactory;
import ro.isdc.wro.model.group.Group;
import ro.isdc.wro.model.resource.Resource;
import ro.isdc.wro.model.resource.ResourceType;
import ro.isdc.wro.model.resource.locator.wildcard.IOFileFilterDecorator;

/**
 *
 * @author Admin
 */
public class FilesLocator {

    private static final Logger logger = Logger.getLogger(FilesLocator.class.getName());
    ResourceType type;
    String group = "";
    ServletContext servletContext;
    
    public FilesLocator(ServletContext sc, ResourceType type, String group) {
        this.type = type;
        this.group = group;
        servletContext = sc;
    }

    public List<String> locate() {

        Context.set(Context.standaloneContext());
        WroModelFactory wroModelFactory = new XmlModelFactory() {

            @Override
            protected InputStream getConfigResourceAsStream() {
                return servletContext.getResourceAsStream("/WEB-INF/" + XML_CONFIG_FILE);
            }
        };
        WroModel wroModel = wroModelFactory.create();
        Group modelGroup = wroModel.getGroupByName(this.group);
        List<Resource> resources = modelGroup.getResources();
        return listFiles(servletContext, resources);
    }

    private List<String> listFiles(final ServletContext servletContext, List<Resource> resources) {

        final List<String> result = new ArrayList<String>();

        for (Resource resource : resources) {

            if (resource.getType() != this.type) {
                continue;
            }

            String uri = resource.getUri();

            if (hasWildcard(uri)) {
                final String fullPath = FilenameUtils.getFullPath(uri);
                final String realPath = servletContext.getRealPath(fullPath);
                final File folder = new File(realPath);
                logger.log(Level.FINEST, "folder is: {0}", folder.getPath());
                final String wildcard = FilenameUtils.getName(uri);
                logger.log(Level.FINEST, "wildcard is: {0}", wildcard);

                final String uriFolder = FilenameUtils.getFullPathNoEndSeparator(uri);
                final String parentFolderPath = folder.getPath();

                final IOFileFilter fileFilter = new IOFileFilterDecorator(new WildcardFileFilter(wildcard)) {

                    @Override
                    public boolean accept(final File file) {
                        final boolean accept = super.accept(file);
                        if (accept && !file.isDirectory()) {
                            final String relativeFilePath = file.getPath().replace(parentFolderPath, "");
                            final String resourceUri = uriFolder + relativeFilePath.replace('\\', '/');
                            result.add(resourceUri);
                            logger.log(Level.FINEST, "resourceUri is: {0}", resourceUri);
                        }
                        return accept;
                    }
                };

                FileUtils.listFiles(folder, fileFilter, getFolderFilter(wildcard));
            } else {
                result.add(uri);
            }
        }

        return result;
    }
    /**
     * Character to distinguish wildcard inside the uri. If the file name contains '*' or '?' character, it is considered
     * a wildcard.
     * <p>
     * A string is considered to contain wildcard if it doesn't start with http(s) and contains at least one of the
     * following characters: [?*].
     */
    private static final String WILDCARD_REGEX = "^(?:(?!http))(.)*[\\*\\?]+(.)*";
    /**
     * Character to distinguish wildcard inside the uri.
     */
    private static final String RECURSIVE_WILDCARD = "**";

    /**
     * @param wildcard
     *          to use to determine if the folder filter should be recursive or not.
     * @return filter to be used for folders.
     */
    private IOFileFilter getFolderFilter(final String wildcard) {
        final boolean recursive = wildcard.contains(RECURSIVE_WILDCARD);
        return recursive ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasWildcard(final String uri) {
        return uri.matches(WILDCARD_REGEX);
    }
}
