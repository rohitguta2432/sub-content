package com.vedanta.vpmt.util;

import org.springframework.util.StringUtils;

public class DifferenceUtil {

	public static boolean isChanged(String existing, String updated, boolean ignoreCase) {

		if ((StringUtils.isEmpty(existing) && !StringUtils.isEmpty(updated))
				|| (!StringUtils.isEmpty(existing) && StringUtils.isEmpty(updated))
				|| (!StringUtils.isEmpty(existing) && !StringUtils.isEmpty(updated)
						&& (ignoreCase ? !existing.equalsIgnoreCase(updated) : !existing.equals(updated)))) {
			return true;
		}
		return false;
	}

	public static boolean isChanged(Number existing, Number updated) {

		if ((existing == null && updated != null) || (existing != null && updated == null)
				|| (existing != null && updated != null && (!existing.equals(updated)))) {
			return true;
		}
		return false;

	}

}
