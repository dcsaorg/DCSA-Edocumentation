package org.dcsa.edocumentation.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.ServiceService;
import org.dcsa.edocumentation.transferobjects.ServiceTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class DummyController {
  private final ServiceService serviceService;

  @GetMapping(path = "/services/")
  @ResponseStatus(HttpStatus.OK)
  public List<ServiceTO> findAll() {
    return serviceService.findAll();
  }
}
