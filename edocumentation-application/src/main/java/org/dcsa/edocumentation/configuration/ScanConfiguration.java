package org.dcsa.edocumentation.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan({
        // The Shared-Kernel has some conflicting beans at the moment (such as Location/LocationMapper).
        // They should be removed from Shared-Kernel
        "org.dcsa.edocumentation",
        "org.dcsa.skernel.dataloader",
        "org.dcsa.skernel.errors",
        "org.dcsa.skernel.infrastructure.http",
        "org.dcsa.skernel.infrastructure.jackson",
        "org.dcsa.skernel.infrastructure.security",
        "org.dcsa.skernel.infrastructure.validation",
})
@EntityScan("org.dcsa.edocumentation")
@EnableJpaRepositories("org.dcsa.edocumentation")
public class ScanConfiguration {
}
