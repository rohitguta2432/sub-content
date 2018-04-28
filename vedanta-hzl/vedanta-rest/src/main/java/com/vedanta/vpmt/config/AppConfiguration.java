package com.vedanta.vpmt.config;

import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.contstant.Constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletContext;
import java.net.MalformedURLException;
import java.util.Properties;



@Configuration
@Import(value = {WebSecurityConfiguration.class, DataSourceConfiguration.class, WebConfig.class})
public class AppConfiguration {


  private static final String APPLICATION_PROPS = "application.properties";

  	@Value("${smtp.email.user}")
	private String username;
	
	@Value("${smtp.email.password}")
	private String password;
	
	@Value("${smtp.email.port}")
	private int port;
	
	@Value("${smtp.email.host}")
	private String host;

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(
      ServletContext context) {
    String configLocation = context.getInitParameter(Constants.CONFIG_LOCATION);
    PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer =
        new PropertySourcesPlaceholderConfigurer();

    try {
      propertySourcesPlaceholderConfigurer.setLocation(new UrlResource(String.format(
          Constants.PROP_LOCATION_FROMAT, configLocation, APPLICATION_PROPS)));
    } catch (MalformedURLException e) {
      throw new VedantaException("Error while loading props file from location : " + configLocation, e);
    }

    return propertySourcesPlaceholderConfigurer;
  }
  
  @Bean
  public JavaMailSender getMailSender(){
      JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
      

      mailSender.setHost(host);
      mailSender.setPort(port);
      mailSender.setUsername(username);
      mailSender.setPassword(password);

       
      Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", "true");
		javaMailProperties.put("mail.smtp.auth", "true");
		javaMailProperties.put("mail.transport.protocol", "smtp");
		javaMailProperties.put("mail.debug", "false");// Prints out everything on screen
		javaMailProperties.put("mail.smtp.ssl.trust", host);
		javaMailProperties.put("mail.mime.charset", "utf8");
      
		mailSender.setJavaMailProperties(javaMailProperties);
      
      return mailSender;
  }
  
  @Bean
  public MultipartResolver multipartResolver() {
    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    /*multipartResolver.setMaxUploadSize(1 * 1024 * 1024);*/
    multipartResolver.setMaxUploadSize( 1024);
    return multipartResolver;
  }
}
