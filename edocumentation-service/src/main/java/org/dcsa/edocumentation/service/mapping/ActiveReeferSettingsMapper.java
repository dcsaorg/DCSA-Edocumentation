package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.ActiveReeferSettings;
import org.dcsa.edocumentation.domain.persistence.entity.Setpoint;
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
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ActiveReeferSettingsMapper {

  @Mapping(expression = "java(org.dcsa.edocumentation.domain.persistence.entity.enums.ReeferType.REFR)", target = "type")
  ActiveReeferSettings toDAO(BkgRefrigeratedTO refrigeratedReefer);

  @Mapping(expression = "java(org.dcsa.edocumentation.domain.persistence.entity.enums.ReeferType.FREZ)", target = "type")
  @Mapping(expression = "java(Set.of(toSetpoint(freezerReefer)))", target = "setpoints")
  ActiveReeferSettings toDAO(BkgFreezerTO freezerReefer);

  @Mapping(expression = "java(org.dcsa.edocumentation.domain.persistence.entity.enums.ReeferType.SUPR)", target = "type")
  @Mapping(expression = "java(Set.of(toSetpoint(superFreezerReefer)))", target = "setpoints")
  ActiveReeferSettings toDAO(BkgSuperFreezerTO superFreezerReefer);

  @Mapping(expression = "java(org.dcsa.edocumentation.domain.persistence.entity.enums.ReeferType.CONA)", target = "type")
  ActiveReeferSettings toDAO(BkgControlledAtmosphereTO controlledAtmosphereReefer);

  @Mapping(expression = "java(org.dcsa.edocumentation.domain.persistence.entity.enums.ReeferType.FREZ)", target = "type")
  @Mapping(expression = "java(Set.of(toSetpoint(freezerReefer)))", target = "setpoints")
  ActiveReeferSettings toDAO(EblFreezerTO freezerReefer);

  @Mapping(expression = "java(org.dcsa.edocumentation.domain.persistence.entity.enums.ReeferType.SUPR)", target = "type")
  @Mapping(expression = "java(Set.of(toSetpoint(superFreezerReefer)))", target = "setpoints")
  ActiveReeferSettings toDAO(EblSuperFreezerTO superFreezerReefer);

  @Mapping(expression = "java(org.dcsa.edocumentation.domain.persistence.entity.enums.ReeferType.REFR)", target = "type")
  ActiveReeferSettings toDAO(EblRefrigeratedTO refrigeratedReefer);

  @Mapping(expression = "java(org.dcsa.edocumentation.domain.persistence.entity.enums.ReeferType.CONA)", target = "type")
  ActiveReeferSettings toDAO(EblControlledAtmosphereTO controlledAtmosphereReefer);

  @Mapping(source = "temperatureSetpoint", target = "temperature")
  Setpoint toSetpoint(BkgFreezerTO freezerReefer);

  @Mapping(source = "temperatureSetpoint", target = "temperature")
  Setpoint toSetpoint(BkgSuperFreezerTO freezerReefer);

  @Mapping(source = "temperatureSetpoint", target = "temperature")
  Setpoint toSetpoint(EblFreezerTO freezerReefer);

  @Mapping(source = "temperatureSetpoint", target = "temperature")
  Setpoint toSetpoint(EblSuperFreezerTO freezerReefer);

  default ActiveReeferSettings toDAO(ShippingInstructionActiveReeferSettingsTO shippingInstructionActiveReeferSettings) {
    if (shippingInstructionActiveReeferSettings == null) {
      return null;
    } else if (shippingInstructionActiveReeferSettings instanceof EblFreezerTO eblFreezerTO) {
      return toDAO(eblFreezerTO);
    } else if (shippingInstructionActiveReeferSettings instanceof EblSuperFreezerTO eblSuperFreezerTO) {
      return toDAO(eblSuperFreezerTO);
    } else if (shippingInstructionActiveReeferSettings instanceof EblRefrigeratedTO eblRefrigeratedTO) {
      return toDAO(eblRefrigeratedTO);
    } else if (shippingInstructionActiveReeferSettings instanceof EblControlledAtmosphereTO eblControlledAtmosphereTO) {
      return toDAO(eblControlledAtmosphereTO);
    } else {
      throw ConcreteRequestErrorMessageException.invalidInput(
          "Reefer Settings in Shipping instruction cannot be processed.");
    }
  }

  default ActiveReeferSettings toDAO(BookingActiveReeferSettingsTO bookingActiveReeferSettingsTO) {
    if (bookingActiveReeferSettingsTO == null) {
      return null;
    } else if (bookingActiveReeferSettingsTO instanceof BkgFreezerTO bkgFreezerTO) {
      return toDAO(bkgFreezerTO);
    } else if (bookingActiveReeferSettingsTO instanceof BkgSuperFreezerTO bkgSuperFreezerTO) {
      return toDAO(bkgSuperFreezerTO);
    } else if (bookingActiveReeferSettingsTO instanceof BkgRefrigeratedTO bkgRefrigeratedTO) {
      return toDAO(bkgRefrigeratedTO);
    } else if (bookingActiveReeferSettingsTO instanceof BkgControlledAtmosphereTO bkgControlledAtmosphereTO) {
      return toDAO(bkgControlledAtmosphereTO);
    } else {
      throw ConcreteRequestErrorMessageException.invalidInput(
          "Reefer Settings in Bookings cannot be processed.");
    }
  }

  BkgRefrigeratedTO toBkgRefrigeratedTO(ActiveReeferSettings activeReeferSettings);

  @Mapping(source = "setpoint.temperature", target = "temperatureSetpoint")
  BkgFreezerTO toBkgFreezerTO(ActiveReeferSettings activeReeferSettings, Setpoint setpoint);

  @Mapping(source = "setpoint.temperature", target = "temperatureSetpoint")
  BkgSuperFreezerTO toBkgSuperFreezerTO(ActiveReeferSettings activeReeferSettings, Setpoint setpoint);

  BkgControlledAtmosphereTO toBkgControlledAtmosphereTO(ActiveReeferSettings activeReeferSettings);

  @Mapping(source = "setpoint.temperature", target = "temperatureSetpoint")
  EblFreezerTO toEblFreezerTO(ActiveReeferSettings activeReeferSettings, Setpoint setpoint);

  @Mapping(source = "setpoint.temperature", target = "temperatureSetpoint")
  EblSuperFreezerTO toEblSuperFreezerTO(ActiveReeferSettings activeReeferSettings, Setpoint setpoint);

  EblRefrigeratedTO toEblRefrigeratedTO(ActiveReeferSettings activeReeferSettings);

  EblControlledAtmosphereTO toEblControlledAtmosphereTO(ActiveReeferSettings activeReeferSettings);

  @Named("toBookingActiveReeferSettingsTO")
  default BookingActiveReeferSettingsTO toBookingActiveReeferSettingsTO(ActiveReeferSettings activeReeferSettings) {
    return activeReeferSettings == null ? null : switch (activeReeferSettings.getType()) {
      case REFR -> toBkgRefrigeratedTO(activeReeferSettings);
      case FREZ -> toBkgFreezerTO(activeReeferSettings, activeReeferSettings.getSetpoints().iterator().next());
      case CONA -> toBkgControlledAtmosphereTO(activeReeferSettings);
      case SUPR -> toBkgSuperFreezerTO(activeReeferSettings, activeReeferSettings.getSetpoints().iterator().next());
    };
  }

  @Named("toShippingInstructionActiveReeferSettingsTO")
  default ShippingInstructionActiveReeferSettingsTO toShippingInstructionActiveReeferSettingsTO(ActiveReeferSettings activeReeferSettings) {
    return activeReeferSettings == null ? null : switch (activeReeferSettings.getType()) {
      case REFR -> toEblRefrigeratedTO(activeReeferSettings);
      case FREZ -> toEblFreezerTO(activeReeferSettings, activeReeferSettings.getSetpoints().iterator().next());
      case CONA -> toEblControlledAtmosphereTO(activeReeferSettings);
      case SUPR -> toEblSuperFreezerTO(activeReeferSettings, activeReeferSettings.getSetpoints().iterator().next());
    };
  }
}
