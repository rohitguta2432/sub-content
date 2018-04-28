package com.vedanta.vpmt.config.core;

import com.vedanta.vpmt.config.AppConfiguration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

public class SpringMvcInitializer implements WebApplicationInitializer {

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {

    AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
    ctx.register(AppConfiguration.class);
    ctx.setServletContext(servletContext);

    Dynamic addServlet = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
    addServlet.addMapping("/");
    addServlet.setLoadOnStartup(1);
  }
}
