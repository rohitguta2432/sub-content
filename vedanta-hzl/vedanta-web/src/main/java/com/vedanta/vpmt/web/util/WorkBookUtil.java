package com.vedanta.vpmt.web.util;

import com.vedanta.vpmt.web.VedantaWebException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
public class WorkBookUtil {

	public Workbook getWorkBook(MultipartFile multipartFile) {

		if (ObjectUtils.isEmpty(multipartFile)) {
			throw new IllegalArgumentException("Cannot parse empty/null xlsx file.");
		}

		String fileName = multipartFile.getOriginalFilename();

		String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

		Workbook workbook;

		try (InputStream inputStream = multipartFile.getInputStream()) {
			try {
				if (VedantaConstant.FORMAT_XLSX.equalsIgnoreCase(fileExtension)
						|| VedantaConstant.FORMAT_XLSM.equalsIgnoreCase(fileExtension)) {
					workbook = new XSSFWorkbook(inputStream);
				} else if (VedantaConstant.FORMAT_XLX.equalsIgnoreCase(fileExtension)) {
					workbook = new HSSFWorkbook(inputStream);
				} else {
					throw new IllegalArgumentException("Unsupported file format.");
				}
			} catch (IOException e) {
				throw new VedantaWebException("Error while parsing file");
			}

		} catch (IOException e) {
			throw new VedantaWebException("Error while parsing file");
		}
		return workbook;
	}

	public Sheet getSheet(Workbook workbook, String sheetName) {

		if (workbook == null) {
			throw new IllegalArgumentException("Workbook cannot be empty/null.");
		}

		if (StringUtils.isEmpty(sheetName)) {
			throw new IllegalArgumentException("Sheet name cannot be empty/null.");
		}

		return workbook.getSheet(sheetName);
	}
}
