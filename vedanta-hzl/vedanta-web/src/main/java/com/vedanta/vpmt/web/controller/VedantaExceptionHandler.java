package com.vedanta.vpmt.web.controller;

import com.vedanta.vpmt.web.VedantaWebException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@Slf4j
public class VedantaExceptionHandler {

	private final String ERROR_PAGE = "400";

	private final String ACCESS_DENIED = "403";
	private final String ERROR_CODE = "errorCode";
	private final String ERROR_MESSAGE = "errorMessage";
	public static final String ERROR_VIEW = "500";

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = VedantaWebException.class)
	public ModelAndView handleApplicationError(Exception e) {

		VedantaWebException es = (VedantaWebException) e;
		if (es.code == 401) {

			ModelAndView mav = new ModelAndView("signin");
			mav.addObject("tokenExpired", "session expired");
			return mav;
		}
		if (es.code == 403) {

			ModelAndView mav = new ModelAndView(ACCESS_DENIED);
			mav.addObject(ERROR_CODE, HttpStatus.FORBIDDEN);
			mav.addObject(ERROR_MESSAGE, e.getMessage());
			log.error("Exception: " + e);
			return mav;
		}
		if (es.code == 500) {

			ModelAndView mav = new ModelAndView(ERROR_VIEW);
			mav.addObject(ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
			mav.addObject(ERROR_MESSAGE, e.getMessage());
			log.error("Exception: " + e);
			return mav;
		}
		ModelAndView mav = new ModelAndView(ERROR_PAGE);

		mav.addObject(ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
		mav.addObject(ERROR_MESSAGE, e.getMessage());
		return mav;
	}

	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ExceptionHandler(value = AccessDeniedException.class)
	public ModelAndView handleAccessDeniedException(Exception e) {

		ModelAndView mav = new ModelAndView(ERROR_PAGE);

		mav.addObject(ERROR_CODE, HttpStatus.FORBIDDEN);
		mav.addObject(ERROR_MESSAGE, e.getMessage());
		return mav;
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = IllegalArgumentException.class)
	public ModelAndView handleIllegalArgumentException(Exception e) {

		ModelAndView mav = new ModelAndView(ERROR_PAGE);

		log.error("Bad Request exception: " + e);
		e.printStackTrace();
		mav.addObject(ERROR_CODE, HttpStatus.BAD_REQUEST);
		mav.addObject(ERROR_MESSAGE, e.getMessage());
		return mav;
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(final Exception e) {
		ModelAndView mav = new ModelAndView(ERROR_VIEW);
		mav.addObject(ERROR_CODE, HttpStatus.FORBIDDEN);
		mav.addObject(ERROR_MESSAGE, e.getMessage());
		log.error("Exception: " + e);
		e.printStackTrace();
		return mav;
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handle(NoHandlerFoundException ex) {

		return "404";
	}

}
