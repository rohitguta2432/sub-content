package com.vedanta.vpmt.web.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileValidationUtil {

	public Boolean getExcelFileValidation(MultipartFile multipartFile) {
		Boolean status = false;
		String fileName = multipartFile.getOriginalFilename();
		String fileExtnsion = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		if (VedantaConstant.FORMAT_XLSX.equalsIgnoreCase(fileExtnsion)
				|| VedantaConstant.FORMAT_XLSM.equalsIgnoreCase(fileExtnsion)) {
			status = true;
		} else if (VedantaConstant.FORMAT_XLX.equalsIgnoreCase(fileExtnsion)) {
			status = true;
		}
		return status;
	}

	public Boolean getImageFileValidation(MultipartFile multipartFile) {
		Boolean status = false;
		String fileName = multipartFile.getOriginalFilename();
		String fileExtnsion = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		if (VedantaConstant.FORMAT_JPEG.equalsIgnoreCase(fileExtnsion)
				|| VedantaConstant.FORMAT_JPG.equalsIgnoreCase(fileExtnsion)
				|| VedantaConstant.FORMAT_PNG.equalsIgnoreCase(fileExtnsion)) {
			status = true;
		} else {
			status = false;
		}
		return status;
	}

	public Boolean getPdfFileValidation(MultipartFile multipartFile) {
		Boolean status = false;
		String fileName = multipartFile.getOriginalFilename();
		String fileExtnsion = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		if (VedantaConstant.FORMAT_PDF.equalsIgnoreCase(fileExtnsion)) {
			status = true;
		} else {
			status = false;
		}
		return status;
	}

}
