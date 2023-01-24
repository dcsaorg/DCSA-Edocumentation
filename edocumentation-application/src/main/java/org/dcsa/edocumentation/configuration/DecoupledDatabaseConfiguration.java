package org.dcsa.edocumentation.configuration;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
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
  basePackages = { "org.dcsa.edocumentation.domain.decoupled.repository" },
  entityManagerFactoryRef = "decoupledEntityManagerFactory"
)
public class DecoupledDatabaseConfiguration {
  @Primary
  @Bean(name = "decoupledDataSource")
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }

  @Primary
  @Bean(name = "decoupledEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean localEntityManagerFactoryBean(
    EntityManagerFactoryBuilder builder,
    @Qualifier("decoupledDataSource") DataSource dataSource
  ) {
    return builder
      .dataSource(dataSource)
      .packages("org.dcsa.edocumentation.domain.decoupled.entity")
      .persistenceUnit("decoupled")
      .build();
  }

  @Bean(name = "decoupledTransactionManager")
  public PlatformTransactionManager transactionManager(
    @Qualifier("decoupledEntityManagerFactory") EntityManagerFactory entityManagerFactory
  ) {
    return new JpaTransactionManager(entityManagerFactory);
  }
}
