package com.vedanta.vpmt.util;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.model.VedantaUser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;

@Component
public class VedantaUtility {

	private final static String STATIC_URL = "/static/";

	@Value("${image.location}")
	private String imageLocation;

	@Value("${pdf.location}")
	private String pdfLocation;

	@Value("${web.server.url}")
	private String webServerURL;

	public String getAppUrl(HttpServletRequest request) {

		StringBuilder stemUrl = new StringBuilder();

		stemUrl.append(request.getScheme());
		stemUrl.append("://");
		stemUrl.append(request.getServerName());
		stemUrl.append(":");
		stemUrl.append(request.getServerPort());
		stemUrl.append(request.getContextPath());
		return stemUrl.toString();
	}

	public String createResourceUrl(HttpServletRequest request, String resourceLocation, String resourceType,
			String entity, long id, String fileName) {
		// StringBuilder baseUrl = new StringBuilder(getAppUrl(request));
		StringBuilder baseUrl = new StringBuilder(webServerURL);
		baseUrl.append(STATIC_URL);
		baseUrl.append(Constants.IMAGE.equalsIgnoreCase(resourceType) ? imageLocation : pdfLocation);
		baseUrl.append(entity);
		baseUrl.append("/");
		baseUrl.append(id);
		baseUrl.append("/");
		baseUrl.append(fileName);
		return baseUrl.toString();
	}

	public static boolean isCurrentUserAdmin() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		for (GrantedAuthority authorities : vedantaUser.getAuthorities()) {
			if ((authorities.getAuthority()).equals(Constants.ROLE_ADMIN)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isCurrentUserBusinessUnitHead() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		for (GrantedAuthority authorities : vedantaUser.getAuthorities()) {
			if ((authorities.getAuthority()).equals(Constants.ROLE_BUSINESS_UNIT_HEAD)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isCurrentUserPlantHead() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		for (GrantedAuthority authorities : vedantaUser.getAuthorities()) {
			if ((authorities.getAuthority()).equals(Constants.ROLE_PLANT_HEAD)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isCurrentUserContractManager() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		for (GrantedAuthority authorities : vedantaUser.getAuthorities()) {
			if ((authorities.getAuthority()).equals(Constants.ROLE_CONTRACT_MANAGER)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isPlantUnitAdmin() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		if (!ObjectUtils.isEmpty(vedantaUser)) {
			for (GrantedAuthority authorities : vedantaUser.getAuthorities()) {
				if ((authorities.getAuthority()).equals(Constants.ROLE_ADMIN)) {
					if (!vedantaUser.getPlantCode().equals("0") && vedantaUser.getBusinessUnitId() > 0) {

						return true;
					}
					return false;
				}
				return false;
			}
		}
		return false;
	}

	public static boolean isBussinessUnitAdmin() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		if (!ObjectUtils.isEmpty(vedantaUser)) {
			for (GrantedAuthority authorities : vedantaUser.getAuthorities()) {
				if ((authorities.getAuthority()).equals(Constants.ROLE_ADMIN)) {
					if (vedantaUser.getPlantCode().equals("0") && vedantaUser.getBusinessUnitId() > 0) {

						return true;
					}
					return false;
				}
				return false;
			}
		}
		return false;
	}

	public static boolean isSuperAdmin() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		if (!ObjectUtils.isEmpty(vedantaUser)) {
			for (GrantedAuthority authorities : vedantaUser.getAuthorities()) {
				if ((authorities.getAuthority()).equals(Constants.ROLE_ADMIN)) {
					if (vedantaUser.getPlantCode().equals("0") && vedantaUser.getBusinessUnitId() == 0) {

						return true;
					}
					return false;
				}
				return false;
			}
		}
		return false;
	}

	public static Long getCurrentUserBuId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		return vedantaUser.getBusinessUnitId();
	}

	public static String getCurrentUserPlantCode() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		return vedantaUser.getPlantCode();
	}

	public static boolean isAuthorized(Long businessUnitId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		return (VedantaUtility.isSuperAdmin() || vedantaUser.getBusinessUnitId() == businessUnitId);
	}
	
	
	public static boolean isCurrentUserHr() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		for (GrantedAuthority authorities : vedantaUser.getAuthorities()) {
			if ((authorities.getAuthority()).equals(Constants.ROLE_HR)) {
				return true;
			}
		}

		return false;
	}

}
