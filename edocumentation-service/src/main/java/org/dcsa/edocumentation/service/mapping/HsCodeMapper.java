package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.HsCode;
import org.springframework.stereotype.Component;

@Component
public class HsCodeMapper {

  public HsCode toDAO(String hsCode) {
    return HsCode.builder().hsCode(hsCode).build();
  }

  public String toTO(HsCode hsCode) {
    return hsCode.getHsCode();
  }
}
