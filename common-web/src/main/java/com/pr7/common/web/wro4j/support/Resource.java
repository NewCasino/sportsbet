package com.pr7.common.web.wro4j.support;

/**
 * reflects a File in the virtual file system
 * @author dapeng.liu
 */
public class Resource {

   private String path;
   private byte[] content;
   private long lastModified;

   public Resource(String path, byte[] content, long lastModified) {

      if (content == null) {
         throw new IllegalArgumentException("content can not be empty");
      }

      if (path == null || path.isEmpty()) {
         throw new IllegalArgumentException("path can not be empty");
      }

      this.path = path;
      this.content = content;
      this.lastModified = lastModified;
   }

   /**
    * the content of the resource, default to UTF-8
    * @return
    */
   public byte[] getContent() {
      return content;
   }

   public void setContent(byte[] content) {
      this.content = content;
   }

   public long getLastModified() {
      return lastModified;
   }

   public void setLastModified(long lastModified) {
      this.lastModified = lastModified;
   }

   /**
    * relative to context path, start with / 
    * @return
    */
   public String getPath() {
      return path;
   }

   public void setPath(String path) {
      this.path = path;
   }
}
