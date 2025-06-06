package com.hotel.Image.Service;

import com.hotel.Common.Exception.CommonExceptions.ResourceNotFoundException;
import com.hotel.Image.Repository.ImageRepository;
import com.hotel.Image.Validator.ImageValidator;
import com.hotel.Image.model.Image;
import com.hotel.Room.Model.Room;
import com.hotel.infrastructure.Cloudinary.Service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.hotel.Common.Exception.ErrorCode.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements IImageService {

    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;
    private final ImageValidator imageValidator;

    @Override
    public Image getImageByUrl(String imageUrl) {
        return imageRepository.findByImageUrl(imageUrl)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND,
                        "La imagen con la URL '" + imageUrl + "' no fue encontrada."));
    }

    @Override
    public Set<Image> createListOfImages(List<MultipartFile> files, Room room) {
        Set<Image> images = new HashSet<>();

        for (int i=0; i<files.size(); i++) {
            MultipartFile file = files.get(i);
            imageValidator.validateImage(file);
            String imageUrl = cloudinaryService.uploadImage(file);
            Image image = Image.builder()
                    .imageUrl(imageUrl)
                    .room(room)
                    .build();
            room.getImagesUrls().add(imageUrl);
            images.add(image);
        }
        return images;
    }

    @Override
    public void processAndSaveImages(List<MultipartFile> files, Room room) {
        Set<Image> images = createListOfImages(files, room);
        imageRepository.saveAll(images);
    }

    @Override
    public void deleteImage(String imageUrl) {
        Image image = getImageByUrl(imageUrl);
        cloudinaryService.deleteImage(imageUrl);
        imageRepository.delete(image);
    }

    @Override
    public void deleteAllImages(Integer roomId) {
        Set<Image> images = imageRepository.findAllByRoom_RoomId(roomId);
        images.forEach(image -> {
            deleteImage(image.getImageUrl());
            cloudinaryService.deleteImage(image.getImageUrl());
        });
    }

}