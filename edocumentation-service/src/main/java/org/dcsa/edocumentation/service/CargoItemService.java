package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.CargoItem;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.repository.CargoItemRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CargoItemService {

  private final CargoItemRepository repository;

  public void createCargoItem(CargoItem cargoItem, ShippingInstruction shippingInstruction) {
    repository.save(
        cargoItem.toBuilder()
            .consignmentItem(
                cargoItem.getConsignmentItem().toBuilder()
                    .shippingInstruction(shippingInstruction)
                    .build())
            .build());
  }
}
