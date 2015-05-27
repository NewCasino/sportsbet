package com.pr7.exception;

import java.io.Serializable;

public class ErrorMessage implements Serializable {

   private static final long serialVersionUID = -556228163126119266L;

   private final ErrorCode errorCode;
   private String extraInformation;

   public ErrorMessage(String runningNo, String[] parameters) {
      this.errorCode = new ErrorCode(runningNo, parameters);
   }

   public ErrorMessage(String runningNo) {
      this(runningNo, null);
   }

   public ErrorCode getErrorCode() {
      return errorCode;
   }

   public String getExtraInformation() {
      return extraInformation;
   }

   public void setExtraInformation(String extraInformation) {
      this.extraInformation = extraInformation;
   }

   /**
    * @return
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      return errorCode.getRunningNo();
   }

}

