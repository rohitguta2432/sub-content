package com.vedanta.vpmt.contollers.client;

import com.vedanta.vpmt.exception.VedantaException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping(value = "static")
@Slf4j
public class StaticController {

	@Value("${resource.location}")
	private String resourceLocation;

	@RequestMapping(value = "/{type}/{entity}/{id}/{fileName:.+}")
	public ResponseEntity<byte[]> getResource(@PathVariable("type") String type, @PathVariable("entity") String entity,
			@PathVariable("id") Long id, @PathVariable("fileName") String fileName) {

		try {
			StringBuilder path = new StringBuilder(type);
			path.append("/");
			path.append(entity);
			path.append("/");
			path.append(id);
			path.append("/");
			path.append(fileName);

			File file = new File(resourceLocation + path.toString());

			if (!file.exists()) {
				return new ResponseEntity<>("No data found".getBytes(), null, HttpStatus.OK);
			}

			byte[] fileStream = Files.readAllBytes(file.toPath());

			HttpHeaders headers = new HttpHeaders();
			if ("IMAGES".equalsIgnoreCase(type)) {

				String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
				if ("PNG".equalsIgnoreCase(ext)) {
					headers.setContentType(MediaType.IMAGE_PNG);
				} else if ("GIF".equalsIgnoreCase(ext)) {
					headers.setContentType(MediaType.IMAGE_GIF);
				} else {
					headers.setContentType(MediaType.IMAGE_JPEG);
				}
			} else {
				headers.setContentDispositionFormData(fileName, fileName);
			}
			headers.setContentLength(fileStream.length);
			return new ResponseEntity<>(fileStream, headers, HttpStatus.OK);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new VedantaException(e.getMessage(), e);
		}

	}
}
