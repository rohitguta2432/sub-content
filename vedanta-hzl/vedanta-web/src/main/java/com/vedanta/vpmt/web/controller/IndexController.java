package com.vedanta.vpmt.web.controller;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.service.UserService;
import com.vedanta.vpmt.web.service.cache.CacheService;
import com.vedanta.vpmt.web.util.VedantaConstant;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping(value = "/")
public class IndexController {

	@Autowired
	private UserService userService;

	private final static String LOGOUT = "logout";

	@Autowired
	@Qualifier("memcacheService")
	private CacheService<String, Object> cacheService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(@RequestParam(value = "expired", required = false) String expired) {

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		// //(session.getAttribute(Constants.USER));

		if (!ObjectUtils.isEmpty(session.getAttribute(Constants.USER))) {
			return new ModelAndView("redirect:" + "/index");
		}
		return new ModelAndView("signin");
	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public ModelAndView index() {

		return new ModelAndView("redirect:" + "index");
	}

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public ModelAndView landingPage() {

		SecurityContext context = SecurityContextHolder.getContext();
		boolean isAdmin = false;
		boolean isUserManager = false;
		boolean isUser = false;
		boolean isHr = false;
		for (GrantedAuthority authority : context.getAuthentication().getAuthorities()) {
			if ("ROLE_ADMIN".equalsIgnoreCase(authority.getAuthority())
					|| "ROLE_PLANT_HEAD".equalsIgnoreCase(authority.getAuthority())
					|| "ROLE_BUSINESS_UNIT_HEAD".equalsIgnoreCase(authority.getAuthority())

			) {
				isAdmin = true;
				continue;
			}

			if ("ROLE_DATA_UPLOAD".equalsIgnoreCase(authority.getAuthority())
					|| "ROLE_CONTRACT_MANAGER".equalsIgnoreCase(authority.getAuthority())) {
				isUser = true;
				continue;
			}

			if ("ROLE_USER_MANAGER".equalsIgnoreCase(authority.getAuthority())) {
				isUserManager = true;
				continue;
			}
			if ("ROLE_HR".equalsIgnoreCase(authority.getAuthority())) {
				isHr = true;
				continue;
			}
		}

		if (isUserManager) {
			return new ModelAndView("redirect:" + "user/newUser");
		}
		if (isHr) {
			return new ModelAndView("redirect:" + "form-save");
		}
		return isAdmin ? new ModelAndView("redirect:" + "web/dashboard/get")
				: isUser ? new ModelAndView("redirect:" + "scorecard") : new ModelAndView("redirect:" + "403");
	}

	@RequestMapping(value = "403", method = RequestMethod.GET)
	public String accessDeniedHandler() {
		return "403";
	}

	@RequestMapping(value = "404", method = RequestMethod.GET)
	public String invalidURL() {
		return "404";
	}

	@RequestMapping(value = "500", method = RequestMethod.GET)
	public String invalidURL500() {
		return "500";
	}

	@RequestMapping(value = "400", method = RequestMethod.GET)
	public String invalidURL400() {
		return "400";
	}

	@RequestMapping(value = "error", method = RequestMethod.GET)
	public String error() {
		return "error";
	}

	@RequestMapping(value = "logoutsuccessfully", method = RequestMethod.GET)
	public ModelAndView logout() {

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		// (session.getAttribute(Constants.USER));
		/* userService.logout(); */
		ModelAndView mav = new ModelAndView(LOGOUT);
		// mav.addObject("Status", session);
		cacheService.remove(VedantaConstant.AUTH_HEADER);

		return mav;
	}

	@RequestMapping(value = "getUserDetails", method = RequestMethod.GET)
	public @ResponseBody User getUserDetails() {
		User userDetail;
		try {
			userDetail = userService.getCurrentUser();

			return userDetail;
		} catch (VedantaWebException e) {

			log.error("Error fetching user details by business id");
			throw new VedantaWebException("Error fetching  user details by business id", e.code);
		}
	}
}
