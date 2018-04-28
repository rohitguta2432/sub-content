package com.vedanta.vpmt.config;

import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.vedanta.vpmt.dao")
public class DataSourceConfiguration {

	private static final String CHARSET_UTF8 = "UTF-8";

	@Value("${db.driver}")
	private String PROPERTY_NAME_DATABASE_DRIVER;

//	@Value("#{T(com.vedanta.vpmt.util.PropertyValueEncryptor).decrypt('${db.p.string}')}")
    @Value("${db.p.string}")
	private String PROPERTY_NAME_DATABASE_PASSWORD;

	@Value("${db.url}")
	private String PROPERTY_NAME_DATABASE_URL;

	// @Value("#{T(com.vedanta.vpmt.util.PropertyValueEncryptor).decrypt('${db.username}')}")
	@Value("${db.username}")
	private String PROPERTY_NAME_DATABASE_USERNAME;

	@Value("${hibernate.dialect}")
	private String PROPERTY_NAME_HIBERNATE_DIALECT;

	@Value("${hibernate.show_sql}")
	private String PROPERTY_NAME_HIBERNATE_SHOW_SQL;

	@Value("${entitymanager.packages.to.scan}")
	private String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN;

	@Value("${hibernate.hbm2ddl.auto}")
	private String PROPERTY_DDL_AUTO;

	@Value("${db.connection.min.size}")
	private int connectionPoolMinSize;

	@Value("${db.connection.max.size}")
	private int connectionPoolMaxSize;

	@Value("${db.connection.time.out}")
	private int connectionTimeOut;

	@Value("${db.connection.max.stmt}")
	private int connectionMaxStatement;

	@Value("${db.connection.idle.test}")
	private int connectionIdealTestPeriod;

	private static final String PROPERTY_UNICODE = "hibernate.connection.useUnicode";
	private static final String PROPERTY_CHAR_ENCODING = "hibernate.connection.characterEncoding";
	private static final String PROPERTY_CHAR_SET = "hibernate.connection.charSet";

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(PROPERTY_NAME_DATABASE_DRIVER);
		dataSource.setUrl(PROPERTY_NAME_DATABASE_URL);
		dataSource.setUsername(PROPERTY_NAME_DATABASE_USERNAME);
		dataSource.setPassword(PROPERTY_NAME_DATABASE_PASSWORD);
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

		entityManagerFactoryBean.setDataSource(dataSource());

		entityManagerFactoryBean.setEntityManagerInterface(HibernateEntityManager.class);

		entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);

		entityManagerFactoryBean.setPackagesToScan(PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN);

		entityManagerFactoryBean.setJpaProperties(hibProperties());

		return entityManagerFactoryBean;

	}

	private Properties hibProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", PROPERTY_NAME_HIBERNATE_DIALECT);
		properties.put("hibernate.show_sql", false);
		properties.put("hibernate.hbm2ddl.auto", PROPERTY_DDL_AUTO);
		properties.put(PROPERTY_UNICODE, Boolean.TRUE);
		properties.put(PROPERTY_CHAR_ENCODING, CHARSET_UTF8);
		properties.put(PROPERTY_CHAR_SET, CHARSET_UTF8);

		properties.put("hibernate.c3p0.min_size", connectionPoolMinSize);
		properties.put("hibernate.c3p0.max_size", connectionPoolMaxSize);
		properties.put("hibernate.c3p0.timeout", connectionTimeOut);
		properties.put("hibernate.c3p0.max_statements", connectionMaxStatement);
		properties.put("hibernate.c3p0.idle_test_period", connectionIdealTestPeriod);

		return properties;

	}

	@Bean
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}

}
