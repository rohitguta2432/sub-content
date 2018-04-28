package com.vedanta.vpmt.web.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedanta.vpmt.mapper.ApproverUpdateMapper;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.util.RestServiceUtil;
import com.vedanta.vpmt.web.util.URLConstants;
import com.vedanta.vpmt.web.util.VedantaConstant;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MultipartFileService {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	@Autowired
	private RestServiceUtil restServiceUtil;
	
	@Value("${file.location.data}")
	private String fileDataLocation;
	
	@Value("${excel.format.file.name.scorecard.approver}")
	private String excelFormatFileName; 
	
	public void importFile(MultipartFile fileBean,Long businessUnitId , Integer monthId) {

        
        Workbook workbook;
        try {
        	ByteArrayInputStream bis = new ByteArrayInputStream(fileBean.getBytes());
        	
        	if (fileBean.getOriginalFilename().endsWith("xls")) {
                workbook = new HSSFWorkbook(bis);
            } else if (fileBean.getOriginalFilename().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(bis);
            } else {
            	log.error("Received file does not have a standard excel extension.");
                throw new IllegalArgumentException("Received file does not have a standard excel extension.");
            }

        	final List<ApproverUpdateMapper> dataList = new ArrayList<>();
        	
        	String cellValueString = null;
        	
            for (Row row : workbook.getSheetAt(0)) {
            	ApproverUpdateMapper approverUpdateMapper = null;
            	if(row.getRowNum()!=0){
            		
            		approverUpdateMapper = new ApproverUpdateMapper();
            		
            		Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                       Cell cell = cellIterator.next();
                       
                       // For ContractNumber Column
                       if(cell.getColumnIndex() == 0){
                    	   cell.setCellType(Cell.CELL_TYPE_STRING);
                    	   cellValueString = cell.getStringCellValue();
                    	   approverUpdateMapper.setContractNumber(cellValueString); 
                       }
                       
                       // For ContractManagerID Column
                       if(cell.getColumnIndex() == 1){
                    	   cell.setCellType(Cell.CELL_TYPE_STRING);
                    	   cellValueString = cell.getStringCellValue();
                    	   approverUpdateMapper.setContractManagerId(cellValueString); 
                       }
                       
                       // For ApproverID Column
                       if(cell.getColumnIndex() == 2){
                    	   cell.setCellType(Cell.CELL_TYPE_STRING);
                    	   cellValueString = cell.getStringCellValue();
                    	   approverUpdateMapper.setApproverById(cellValueString); 
                       }
                       
                       // For Approver Name Column
                       if(cell.getColumnIndex() == 3){
                    	   cell.setCellType(Cell.CELL_TYPE_STRING);
                    	   cellValueString = cell.getStringCellValue();
                    	   approverUpdateMapper.setApproverByName(cellValueString);
                       }
                       approverUpdateMapper.setBusinessUnitId(businessUnitId);
                       approverUpdateMapper.setMonthId(monthId);
                    }
            	}
            	if(approverUpdateMapper!= null)
            		dataList.add(approverUpdateMapper);
            }
            apiCall(dataList);
        } catch (IOException  e) {
        	log.error("File Data extraction failed.");
			throw new VedantaWebException("File Data extraction failed.");
        }
    }
	
	public List<ApproverUpdateMapper> apiCall(List<ApproverUpdateMapper> entity) {
		if (entity == null) {
			throw new VedantaWebException("ApproverUpdateMapper cannot be null");
		}
		int status;
		try {

			JsonNode response = restServiceUtil.makeRequest(URLConstants.UPDATE_ALL_APPROVER_FOR_SCORECARD, entity, null, HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while updating ScoreCard for approver.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<ApproverUpdateMapper>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while updatinging ScoreCard for approver", 401);
			}
			log.error("API not responded while updating ScoreCard for approver.");
			throw new VedantaWebException("API not responded while updating ScoreCard for approver.");
		}
	}
	
	public void getDownload(HttpServletRequest request,
            HttpServletResponse response){
		
		try{
			int BUFFER_SIZE = 4096;
	        
	        String filePath = fileDataLocation+excelFormatFileName;
	    	
	        // get absolute path of the application
	        ServletContext context = request.getServletContext();
	        
	        // construct the complete absolute path of the file
	        String fullPath = filePath;      
	        File downloadFile = new File(fullPath);
	        FileInputStream inputStream = new FileInputStream(downloadFile);
	         
	        // get MIME type of the file
	        String mimeType = context.getMimeType(fullPath);
	        if (mimeType == null) {
	            // set to binary type if MIME mapping not found
	            mimeType = "application/octet-stream";
	        }
	        System.out.println("MIME type: " + mimeType);
	 
	        // set content attributes for the response
	        response.setContentType(mimeType);
	        response.setContentLength((int) downloadFile.length());
	 
	        // set headers for the response
	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"%s\"",
	                downloadFile.getName());
	        response.setHeader(headerKey, headerValue);
	 
	        // get output stream of the response
	        OutputStream outStream = response.getOutputStream();
	 
	        byte[] buffer = new byte[BUFFER_SIZE];
	        int bytesRead = -1;
	 
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }
	 
	        inputStream.close();
	        outStream.close();
		}catch(VedantaWebException | IOException e){
			log.error("File is not found on server. server location shoulld be :"+fileDataLocation+excelFormatFileName);
			throw new VedantaWebException("File is not found on server.server location shoulld be :"+fileDataLocation+excelFormatFileName);
		}
	}
}
