package com.pr7.exception;

import java.util.List;

public class BusinessError extends BaseException {

   private static final long serialVersionUID = -7739979564139033614L;

   public BusinessError(String errorMessage) {
      super(errorMessage);
   }

   public BusinessError(ErrorMessage errorMessage) {
      super(errorMessage);
   }

   public BusinessError(List<ErrorMessage> messages) {
      super(messages);
   }
}

