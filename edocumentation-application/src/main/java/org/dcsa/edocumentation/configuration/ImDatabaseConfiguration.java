package org.dcsa.edocumentation.configuration;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
  basePackages = { "org.dcsa.edocumentation.domain.persistence.repository", "org.dcsa.skernel.domain.persistence.repository" },
  entityManagerFactoryRef = "imEntityManagerFactory"
)
public class ImDatabaseConfiguration {
  @Bean("imDataSourceProperties")
  @ConfigurationProperties("spring.im-datasource")
  public DataSourceProperties dataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean(name = "imDataSource")
  @ConfigurationProperties("spring.im-datasource.hikari")
  public DataSource dataSource(@Qualifier("imDataSourceProperties") DataSourceProperties dataSourceProperties) {
    return dataSourceProperties.initializeDataSourceBuilder().build();
  }

  @Bean(name = "imEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean localEntityManagerFactoryBean(
    EntityManagerFactoryBuilder builder,
    @Qualifier("imDataSource") DataSource dataSource
  ) {
    return builder
      .dataSource(dataSource)
      .packages("org.dcsa.edocumentation.domain.persistence.entity", "org.dcsa.skernel.domain.persistence.entity")
      .persistenceUnit("im")
      .build();
  }

  @Primary
  @Bean(name = "transactionManager")
  public PlatformTransactionManager transactionManager(
    @Qualifier("imEntityManagerFactory") EntityManagerFactory entityManagerFactory
  ) {
    return new JpaTransactionManager(entityManagerFactory);
  }
}
