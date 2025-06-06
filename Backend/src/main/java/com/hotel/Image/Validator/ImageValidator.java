package com.hotel.Image.Validator;

import com.hotel.Common.Exception.CommonExceptions.InvalidRequestException;
import com.hotel.Image.Config.ImageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import static com.hotel.Common.Exception.ErrorCode.INVALID_REQUEST;

@Component
@RequiredArgsConstructor
public class ImageValidator {

    private final ImageProperties imageProperties;

    public void validateImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidRequestException(INVALID_REQUEST,
                    "¡El archivo enviado se encuentra vacío!");
        }
        validateSize(file);
        validateType(file);
        readImage(file);
    }

    void validateSize(MultipartFile file) {
        long maxSize = imageProperties.getMaxFileSizeInBytes();
        if (file.getSize() > maxSize) {
            throw new InvalidRequestException(INVALID_REQUEST,
                    "El archivo excede el tamaño máximo permitido");
        }
    }

    void validateType(MultipartFile file) {
        List<String> allowedTypes = imageProperties.getAllowedTypes();
        if (!allowedTypes.contains(file.getContentType())) {
            String allowedTypesStr = String.join(", ", allowedTypes);
            throw new InvalidRequestException(INVALID_REQUEST,
                    "¡Tipo de archivo no permitido! Solo se permiten: " + allowedTypesStr);
        }
    }

    void readImage(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new InvalidRequestException(INVALID_REQUEST,
                        "El archivo no es una imagen válida");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer la imagen", e);
        }
    }

}