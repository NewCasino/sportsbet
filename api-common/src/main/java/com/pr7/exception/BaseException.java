package com.pr7.exception;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;


@SuppressWarnings("serial")
public abstract class BaseException extends RuntimeException implements java.io.Serializable {

    private final List<ErrorMessage> messages = new ArrayList<ErrorMessage>();

    public BaseException(ErrorMessage errorMessage) {
        messages.add(errorMessage);
    }

    public BaseException(String errorMessage) {
        messages.add(new ErrorMessage(errorMessage));
    }

    /*
     * @TODO i18n
     */
    @Override
    public String getMessage() {
       return getMessage("\r\n");
    }
        
    public String getMessage(String seperator) {
        StringBuilder sb = new StringBuilder();
        for (ErrorMessage message : messages) {
            sb.append(message.toString()).append(seperator);
        }
        return StringUtils.trim(sb.toString());
    }

    public BaseException(List<ErrorMessage> messages) {
        this.messages.addAll(messages);
    }

    public BaseException(Throwable e) {
        super(e);
    }

    public boolean hasErrorMessages() {
        return !messages.isEmpty();
    }

    public List<ErrorMessage> getErrorMessages() {

        return messages;
    }

    public Boolean containsError(String errorConstant) {

        if (errorConstant == null) {
            return false;
        }

        List<ErrorMessage> errors = getErrorMessages();
        for (ErrorMessage error : errors) {
            String ec = error.getErrorCode().getRunningNo();
            if (errorConstant.equals(ec)) {
                return true;
            }
        }
        return false;
    }
}
