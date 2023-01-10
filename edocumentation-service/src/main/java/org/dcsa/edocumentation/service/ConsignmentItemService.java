package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.ConsignmentItem;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.repository.ConsignementItemRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ConsignmentItemService {

  private final ConsignementItemRepository repository;

  @Transactional(Transactional.TxType.MANDATORY)
  public void createConsignmentItems(Set<ConsignmentItem> consignmentItems, ShippingInstruction shippingInstruction) {
    if(consignmentItems != null && !consignmentItems.isEmpty()) {
      repository.saveAll(consignmentItems.stream().map(consignmentItem -> consignmentItem.toBuilder().shippingInstruction(shippingInstruction).build()).toList());
    }
  }
}
