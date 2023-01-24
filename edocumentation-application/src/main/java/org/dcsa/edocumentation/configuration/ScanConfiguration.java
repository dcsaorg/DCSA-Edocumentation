package org.dcsa.edocumentation.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.dcsa")
@EntityScan("org.dcsa")
public class ScanConfiguration {
}
