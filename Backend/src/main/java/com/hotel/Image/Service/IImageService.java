package com.hotel.Image.Service;

import com.hotel.Image.model.Image;
import com.hotel.Room.Model.Room;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface IImageService {

    Image getImageByUrl(String imageUrl);

    Set<Image> createListOfImages(List<MultipartFile> files, Room room);

    void processAndSaveImages(List<MultipartFile> files, Room room);

    void deleteImage(String imageUrl);

    void deleteAllImages(Integer roomId);
}