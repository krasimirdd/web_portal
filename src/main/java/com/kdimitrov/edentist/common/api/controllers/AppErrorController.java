package com.kdimitrov.edentist.common.api.controllers;

import com.kdimitrov.edentist.common.model.annotation.DateInFuture;
import com.kdimitrov.edentist.common.model.annotation.TimeIsWorkable;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Basic Controller which is called for unhandled errors
 */
@Controller
public class AppErrorController implements ErrorController {

    /**
     * Error Attributes in the Application
     */
    private ErrorAttributes errorAttributes;

    private final static String ERROR_PATH = "/error";

    /**
     * Controller for the Error Controller
     *
     * @param errorAttributes
     */
    public AppErrorController(DefaultErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    /**
     * Supports the HTML Error View
     *
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request) {
        return new ModelAndView("/fragments/error", getErrorAttributes(request, false));
    }

    /**
     * Supports other formats like JSON, XML
     *
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, getTraceParameter(request));
        HttpStatus status = getStatus(request);
        return new ResponseEntity<>(body, status);
    }

    /**
     * Returns the path of the error page.
     *
     * @return the error path
     */
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }


    private boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        if (parameter == null) {
            return false;
        }
        return !"false".equals(parameter.toLowerCase());
    }

//    private Map<String, Object> getErrorAttributes(HttpServletRequest request,
//                                                   boolean includeStackTrace) {
//        WebRequest webRequest = new ServletWebRequest(request);
//        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(webRequest, includeStackTrace);
//        errorAttributes.put("details", this.errorAttributes.getError(webRequest).getLocalizedMessage());
//        return errorAttributes;
//    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request,
                                                   boolean includeStackTrace) {
        WebRequest webRequest = new ServletWebRequest(request);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(webRequest, includeStackTrace);
        String localizedMessage = this.errorAttributes.getError(webRequest).getLocalizedMessage();

        String rejectedValue = null;
        String detailedMessage = null;

        if (localizedMessage.contains("NotBlank") || localizedMessage.contains("Invalid property 'email'")) {
            detailedMessage = "No such dentist";
        } else if (localizedMessage.contains("DateInFuture")) {
            ErrorTokens errorTokens = new ErrorTokens(localizedMessage).invoke(DateInFuture.MESSAGE);
            rejectedValue = errorTokens.getRejectedValue();
            detailedMessage = errorTokens.getDetailedMessage();
        } else if (localizedMessage.contains("TimeIsWorkable")) {
            ErrorTokens errorTokens = new ErrorTokens(localizedMessage).invoke(TimeIsWorkable.MESSAGE);
            rejectedValue = errorTokens.getRejectedValue();
            detailedMessage = errorTokens.getDetailedMessage();
        }

        errorAttributes.put("message", detailedMessage);
        errorAttributes.put("rejectedValue", rejectedValue);
        return errorAttributes;
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        if (statusCode != null) {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (Exception ex) {
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    private class ErrorTokens {
        private String localizedMessage;
        private String rejectedValue;
        private String detailedMessage;

        public ErrorTokens(String localizedMessage) {
            this.localizedMessage = localizedMessage;
        }

        public String getRejectedValue() {
            if (rejectedValue.contains("]")) {
                return "empty";
            }
            return rejectedValue.substring(16);
        }

        public String getDetailedMessage() {
            return detailedMessage;
        }

        public ErrorTokens invoke(String detailedMessage) {
            this.detailedMessage = detailedMessage;
            rejectedValue = localizedMessage.substring(
                    localizedMessage.lastIndexOf("rejected value ["),
                    localizedMessage.lastIndexOf("rejected value [") + 32);
            return this;
        }
    }
}