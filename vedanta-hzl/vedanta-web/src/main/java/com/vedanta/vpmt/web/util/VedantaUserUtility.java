package com.vedanta.vpmt.web.util;

import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.model.User;

public class VedantaUserUtility {

	public static boolean isBusinessHead(User user) {
		if (!ObjectUtils.isEmpty(user)) {

			if ((Constants.ROLE_BUSINESS_UNIT_HEAD).equalsIgnoreCase(user.getAuthorities().trim())) {
				return true;
			}
			return false;
		}
		return false;
	}

	public static boolean isAdmin(User user) {

		if (!ObjectUtils.isEmpty(user)) {
			String authorities = user.getAuthorities();
			String[] auths = authorities.split(",");
			for (String str : auths) {
				if ((Constants.ROLE_ADMIN).equalsIgnoreCase(str.trim())) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isSuperAdmin(User user) {

		if (!ObjectUtils.isEmpty(user)) {
			String authorities = user.getAuthorities();

			if ((Constants.ROLE_ADMIN).equalsIgnoreCase(authorities.trim())) {

				if (user.getPlantCode().equals("0") && user.getBusinessUnitId() == 0) {

					return true;
				}
				return false;
			}
			return false;
		}
		return false;
	}

	public static boolean isBussinessUnitAdmin(User user) {

		if (!ObjectUtils.isEmpty(user)) {
			String authorities = user.getAuthorities();

			if ((Constants.ROLE_ADMIN).equalsIgnoreCase(authorities.trim())) {

				if (user.getPlantCode().equals("0") && user.getBusinessUnitId() > 0) {

					return true;
				}
				return false;
			}
			return false;
		}

		return false;
	}

	public static boolean isPlantUnitAdmin(User user) {

		if (!ObjectUtils.isEmpty(user)) {
			String authorities = user.getAuthorities();

			if ((Constants.ROLE_ADMIN).equalsIgnoreCase(authorities.trim())) {

				if (!user.getPlantCode().equals("0") && user.getBusinessUnitId() > 0) {

					return true;
				}
				return false;
			}
			return false;
		}

		return false;
	}

	public static boolean isPlantHead(User user) {

		if (!ObjectUtils.isEmpty(user)) {
			String authorities = user.getAuthorities();

			if ((Constants.ROLE_PLANT_HEAD).equalsIgnoreCase(authorities.trim())) {

				return true;
			}
			return false;
		}
		return false;
	}

}
