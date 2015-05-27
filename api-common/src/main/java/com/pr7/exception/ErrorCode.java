package com.pr7.exception;

import java.io.Serializable;

public class ErrorCode implements Serializable {

   private String runningNo;
   private String[] parameters;

   public ErrorCode(String runningNo, String[] parameters) {
      this.runningNo = runningNo;
      this.parameters = parameters;
   }

   public String getRunningNo() {
      return runningNo;
   }

   public String[] getParameters() {
      return parameters;
   }

}

