package org.dcsa.edocumentation.configuration;

import jakarta.validation.Path;
import jakarta.validation.TraversableResolver;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.lang.annotation.ElementType;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomBeanConfiguration {

  /**
   * By default, Spring will provide a validator that checks if attributes are lazily loaded
   * and skips the attributes that have not been loaded.
   *
   * <p>This is great for performance. Not so great for ensuring that our entities are correct
   * leading to the surprising "validator says valid, but it should have said invalid" behaviour
   * for nested objects. Example, validating the SI will be valid even with invalid consignment
   * items if the consignment items have never been loaded.</p>
   *
   * <p>To combat this, we have this validator that ignores the lazy loading logic and just
   * forces everything to be loaded if missing.  It gives the right result at the cost of
   * being slower. The slowness can then be mitigated by ensuring the entity is fully loaded
   * before attempting to validate it. (@NamedEntityGraph can be useful for that purpose).</p>
   */
  @Bean("eagerValidator")
  public Validator eagerValidator() {
    try (var factory = Validation.byDefaultProvider().configure()
      .traversableResolver(new EagerResolver())
      .buildValidatorFactory()) {
      return factory.getValidator();
    }
  }

  private static class EagerResolver implements TraversableResolver {
    @Override
    public boolean isReachable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
      return true;
    }

    @Override
    public boolean isCascadable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
      return true;
    }
  }
}
