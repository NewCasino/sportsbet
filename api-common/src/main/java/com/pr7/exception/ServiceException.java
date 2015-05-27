package com.pr7.exception;

import java.util.List;

public class ServiceException extends BaseException {

   private static final long serialVersionUID = -7739979564139033614L;

   public ServiceException(String errorMessage) {
      super(errorMessage);
   }

   public ServiceException(ErrorMessage errorMessage) {
      super(errorMessage);
   }

   public ServiceException(List<ErrorMessage> messages) {
      super(messages);
   }
}

