package com.vedanta.vpmt.web.parser;

import org.springframework.web.multipart.MultipartFile;

public interface VedantaParser<T> {

	public T parseWorkBook(MultipartFile multipartFile);

}
