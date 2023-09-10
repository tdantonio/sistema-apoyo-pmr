package domain.converters;

import domain.mensajes.Configuraciones.*;

import javax.persistence.AttributeConverter;

public class MedioConfiguradoAttributeConverter implements AttributeConverter<MedioConfigurado, String> {

    @Override
    public String convertToDatabaseColumn(MedioConfigurado medioConfigurado) {
        return null;
    }

    @Override
    public MedioConfigurado convertToEntityAttribute(String s) {
        return switch (s) {
            case "MensajeWhatsapp" -> new MensajeWhatsApp();
            case "MensajeEmail" -> new MensajeEmail();
            default -> null;
        };
    }
}