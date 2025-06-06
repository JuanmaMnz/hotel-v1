package com.hotel.infrastructure.Cloudinary.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hotel.Common.Exception.CommonExceptions.CloudinaryException;
import com.hotel.Common.Exception.CommonExceptions.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static com.hotel.Common.Exception.ErrorCode.CLOUDINARY_ERROR;
import static com.hotel.Common.Exception.ErrorCode.INVALID_REQUEST;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final String INVALID_URL_MESSAGE = "La URL proporcionada es inválida.";
    private final String IMAGE_UPLOAD_FAILED = "No se pudo subir la imagen. Intenta nuevamente más tarde.";
    private final String IMAGE_DELETE_FAILED = "No se pudo eliminar la imagen. Intenta nuevamente más tarde.";

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new CloudinaryException(CLOUDINARY_ERROR, IMAGE_UPLOAD_FAILED, e);
        }
    }

    public void deleteImage(String imageUrl){
        try {
            String publicId = extractPublicIdFromUrl(imageUrl);

            if (publicId == null) {
                throw new InvalidRequestException(INVALID_REQUEST, INVALID_URL_MESSAGE);
            }

            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new CloudinaryException(CLOUDINARY_ERROR, IMAGE_DELETE_FAILED, e);
        }
    }

    public String extractPublicIdFromUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new InvalidRequestException(INVALID_REQUEST, INVALID_URL_MESSAGE);
        }

        if (!imageUrl.contains("res.cloudinary.com")) {
            throw new InvalidRequestException(INVALID_REQUEST, INVALID_URL_MESSAGE);
        }

        int uploadIndex = imageUrl.indexOf("/upload/");
        if (uploadIndex == -1) {
            throw new InvalidRequestException(INVALID_REQUEST, INVALID_URL_MESSAGE);
        }

        String publicId = imageUrl.substring(uploadIndex + 8);

        if (publicId.startsWith("v") && publicId.substring(1).matches("\\d+/.+")) {
            publicId = publicId.substring(publicId.indexOf('/') + 1);
        }

        int dotIndex = publicId.lastIndexOf('.');
        if (dotIndex != -1) {
            publicId = publicId.substring(0, dotIndex);
        }

        return publicId;
    }

}