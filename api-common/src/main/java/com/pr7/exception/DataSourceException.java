package com.pr7.exception;

import java.util.List;

public class DataSourceException extends BaseException {

   private static final long serialVersionUID = -7739979564139033614L;

   public DataSourceException(String runningNo){
      super(new ErrorMessage(runningNo));
   }

   public DataSourceException(String runningNo, String[] parameters){
      super(new ErrorMessage(runningNo, parameters));
   }

   public DataSourceException(ErrorMessage errorMessage){
      super(errorMessage);
   }

   public DataSourceException(List<ErrorMessage> messages){
      super(messages);
   }

}

