package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.ActiveReeferSettings;
import org.dcsa.edocumentation.transferobjects.BookingActiveReeferSettingsTO;
import org.dcsa.edocumentation.transferobjects.BookingActiveReeferSettingsTO.BkgControlledAtmosphereTO;
import org.dcsa.edocumentation.transferobjects.BookingActiveReeferSettingsTO.BkgFreezerTO;
import org.dcsa.edocumentation.transferobjects.BookingActiveReeferSettingsTO.BkgRefrigeratedTO;
import org.dcsa.edocumentation.transferobjects.BookingActiveReeferSettingsTO.BkgSuperFreezerTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionActiveReeferSettingsTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionActiveReeferSettingsTO.EblControlledAtmosphereTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionActiveReeferSettingsTO.EblFreezerTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionActiveReeferSettingsTO.EblRefrigeratedTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionActiveReeferSettingsTO.EblSuperFreezerTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActiveReeferSettingsMapper {

  ActiveReeferSettings toDAO(BkgRefrigeratedTO refrigeratedReefer);

  ActiveReeferSettings toDAO(BkgFreezerTO freezerReefer);

  ActiveReeferSettings toDAO(BkgSuperFreezerTO superFreezerReefer);

  ActiveReeferSettings toDAO(BkgControlledAtmosphereTO controlledAtmosphereReefer);

  ActiveReeferSettings toDAO(EblFreezerTO freezerReefer);

  ActiveReeferSettings toDAO(EblSuperFreezerTO superFreezerReefer);

  ActiveReeferSettings toDAO(EblRefrigeratedTO refrigeratedReefer);

  ActiveReeferSettings toDAO(EblControlledAtmosphereTO controlledAtmosphereReefer);

  default ActiveReeferSettings toDAO(
      ShippingInstructionActiveReeferSettingsTO shippingInstructionActiveReeferSettings) {
    if (shippingInstructionActiveReeferSettings instanceof EblFreezerTO) {
      return toDAO((EblFreezerTO) shippingInstructionActiveReeferSettings);
    } else if (shippingInstructionActiveReeferSettings instanceof EblSuperFreezerTO) {
      return toDAO((EblSuperFreezerTO) shippingInstructionActiveReeferSettings);
    } else if (shippingInstructionActiveReeferSettings instanceof EblRefrigeratedTO) {
      return toDAO((EblRefrigeratedTO) shippingInstructionActiveReeferSettings);
    } else if (shippingInstructionActiveReeferSettings instanceof EblControlledAtmosphereTO) {
      return toDAO((EblControlledAtmosphereTO) shippingInstructionActiveReeferSettings);
    } else {
      throw ConcreteRequestErrorMessageException.invalidInput(
          "Reefer Settings in Shipping instruction cannot be processed.");
    }
  }

  default ActiveReeferSettings toDAO(BookingActiveReeferSettingsTO bookingActiveReeferSettingsTO) {
    if (bookingActiveReeferSettingsTO instanceof BkgFreezerTO) {
      return toDAO((BkgFreezerTO)bookingActiveReeferSettingsTO);
    } else if (bookingActiveReeferSettingsTO instanceof BkgSuperFreezerTO) {
      return toDAO((BkgSuperFreezerTO)bookingActiveReeferSettingsTO);
    } else if (bookingActiveReeferSettingsTO instanceof BkgRefrigeratedTO) {
      return toDAO((BkgRefrigeratedTO)bookingActiveReeferSettingsTO);
    } else if (bookingActiveReeferSettingsTO instanceof BkgControlledAtmosphereTO) {
      return toDAO((BkgControlledAtmosphereTO)bookingActiveReeferSettingsTO);
    } else {
      throw ConcreteRequestErrorMessageException.invalidInput(
          "Reefer Settings in Bookings cannot be processed.");
    }
  }
}
