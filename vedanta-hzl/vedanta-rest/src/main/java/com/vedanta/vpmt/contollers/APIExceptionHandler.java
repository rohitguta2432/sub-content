package com.vedanta.vpmt.contollers;

import com.vedanta.vpmt.dto.Response;
import com.vedanta.vpmt.exception.DuplicateLoginException;
import com.vedanta.vpmt.exception.ResourceNotFoundExecption;
import com.vedanta.vpmt.exception.VedantaException;

import org.apache.commons.fileupload.FileUploadBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class APIExceptionHandler {

	@ExceptionHandler(value = { BadCredentialsException.class, UsernameNotFoundException.class })
	public ResponseEntity<Response<String>> handleAuthenticationFailedException(Exception e) {
		return new ResponseEntity<Response<String>>(new Response<String>(
				HttpStatus.UNAUTHORIZED.value(), e.getMessage(), null), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = AccessDeniedException.class)
	public ResponseEntity<Response<String>> handleAccessDeniedException(HttpServletRequest request) {
		return new ResponseEntity<Response<String>>(new Response<String>(
				HttpStatus.FORBIDDEN.value(), String.format("Access denied for path : %s",
						request.getServletPath()), null), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(value = VedantaException.class)
	public ResponseEntity<Response<String>> handleSCBException(Exception e) {
		return new ResponseEntity<Response<String>>(new Response<String>(
				HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage() != null ? e.getMessage()
						: "Internal Server Error.", null), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
/*	@ExceptionHandler(value = VedantaException.class)
	public ResponseEntity<Response<String>> handleBadrequestException(HttpMessageNotReadableException e) {
		return new ResponseEntity<Response<String>>(new Response<String>(
				HttpStatus.BAD_REQUEST.value(), e.getMessage() != null ? e.getMessage()
						: "Bad Request Error.", null), HttpStatus.BAD_REQUEST);
	}*/

	@ExceptionHandler(value = ResourceNotFoundExecption.class)
	public ResponseEntity<Response<String>> handleResourceNotFoundException(Exception e) {
		return new ResponseEntity<Response<String>>(new Response<String>(
				HttpStatus.NO_CONTENT.value(), e.getMessage() != null ? e.getMessage()
						: "Request resource not found.", null), HttpStatus.NO_CONTENT);
	}

	@ExceptionHandler(value = IllegalArgumentException.class)
	public ResponseEntity<Response<String>> handleIllegalArgumentException(Exception e) {
		return new ResponseEntity<Response<String>>(new Response<String>(
				HttpStatus.BAD_REQUEST.value(), e.getMessage() != null ? e.getMessage()
						: "Bad Request", null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DuplicateLoginException.class)
	public ResponseEntity<Response<String>> handleDuplicateLoginException(Exception e) {
		return new ResponseEntity<Response<String>>(new Response<String>(
				HttpStatus.CONFLICT.value(), e.getMessage() != null ? e.getMessage()
						: "Unprocessable Entity.", null), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(value = DisabledException.class)
	public ResponseEntity<Response<String>> handleDisabledException(Exception e) {
		return new ResponseEntity<Response<String>>(new Response<String>(900,
				e.getMessage() != null ? e.getMessage() : "User account disabled", null),
				HttpStatus.UNAUTHORIZED);
	}
	
	
	@Autowired
    private MessageSource messageSource;
	@ExceptionHandler(value = MultipartException.class)
    public RedirectView handleMultipartException(Exception ex, HttpServletRequest request){
        RedirectView model = new RedirectView("error");
        FlashMap flash = RequestContextUtils.getOutputFlashMap(request);
        if (ex instanceof MultipartException) {
            MultipartException mEx = (MultipartException)ex;

            if (ex.getCause() instanceof FileUploadBase.FileSizeLimitExceededException){
                FileUploadBase.FileSizeLimitExceededException flEx = (FileUploadBase.FileSizeLimitExceededException)mEx.getCause();
                float permittedSize = flEx.getPermittedSize() / 1024;
                String message = messageSource.getMessage(
                        "file.maxsize",
                        new Object[]{"file", permittedSize},		//file-- name of file
                        LocaleContextHolder.getLocale());
                flash.put("errors", message);
            } else {
                flash.put("errors", "Please contact your administrator: " + ex.getMessage());
            }
        } else {
            flash.put("errors", "Please contact your administrator: " + ex.getMessage());
        }
        return model;
    }
	
	
	
	

}
