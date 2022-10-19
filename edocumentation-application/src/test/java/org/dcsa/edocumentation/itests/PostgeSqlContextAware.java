package org.dcsa.edocumentation.itests;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(initializers = {PostgeSqlContextAware.Initializer.class})
public abstract class PostgeSqlContextAware {

  static final PostgreSQLContainer<?> postgresSQLContainer;

  static {
    postgresSQLContainer =
        new PostgreSQLContainer<>("postgres:10-alpine")
            .withCopyFileToContainer(
                MountableFile.forHostPath("../DCSA-Information-Model/datamodel/initdb.d"),
                "/docker-entrypoint-initdb.d")
            .withCopyFileToContainer(
                MountableFile.forHostPath("../DCSA-Information-Model/datamodel/testdata.d"),
                "/docker-entrypoint-initdb.d")
            .withCopyFileToContainer(
                MountableFile.forHostPath("../DCSA-Information-Model/datamodel/referencedata.d"),
                "/referencedata.d")
            .withCopyFileToContainer(
                MountableFile.forHostPath("../DCSA-Information-Model/datamodel/samples.d"),
                "/samples.d")
            .withReuse(true);
    postgresSQLContainer.start();
  }

  public static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      TestPropertyValues.of(
              "spring.datasource.url=jdbc:postgresql://"
                  + postgresSQLContainer.getHost()
                  + ":"
                  + postgresSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT)
                  + "/dcsa_openapi?currentSchema=dcsa_im_v3_0",
              "spring.datasource.username=dcsa_db_owner",
              "spring.datasource.password=9c072fe8-c59c-11ea-b8d1-7b6577e9f3f5",
              "spring.datasource.driver-class-name=org.postgresql.Driver",
              "spring.jpa.hibernate.ddl-auto=none",
              "spring.jpa.database-platform: org.hibernate.dialect.PostgreSQL10Dialect")
          .applyTo(configurableApplicationContext.getEnvironment());
    }
  }
}
