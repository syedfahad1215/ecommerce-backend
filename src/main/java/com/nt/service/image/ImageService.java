package com.nt.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nt.dto.ImageDto;
import com.nt.exception.ResourceNotFoundException;
import com.nt.model.Image;
import com.nt.model.Product;
import com.nt.repo.ImageRepository;
import com.nt.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{

	private final ImageRepository imageRepository;
	
	private final IProductService productService;
	
	@Override
	public Image getImageById(Long id) {
		
		return imageRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No image found with this id: "+id));
	}

	@Override
	public void deleteImageById(Long id) {
		imageRepository
			.findById(id)
			.ifPresentOrElse(imageRepository:: delete, 
					() -> new ResourceNotFoundException("No image found with this id: "+id));
		
	}

	@Override
	public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
		Product product = productService.getProductById(productId);
		
		List<ImageDto> savedImageDto = new ArrayList<>();
		
		for(MultipartFile file: files) {
			try {
				
				Image image = new Image();
				image.setFileName(file.getOriginalFilename());
				image.setFileType(file.getContentType());
				image.setImage(new SerialBlob(file.getBytes()));
				image.setProduct(product);
				
				//Building the download url
				String buildDownloadUrl = "/api/v1/images/image/download/";
				String downloadUrl = buildDownloadUrl+image.getId();
				image.setDownloadUrl(downloadUrl);
				
				//Saving the image using imageRepository
				Image savedImage = imageRepository.save(image);
				
				savedImage.setDownloadUrl(buildDownloadUrl+savedImage.getId());
				imageRepository.save(savedImage);
				
				ImageDto imageDto = new ImageDto();
				imageDto.setImageId(savedImage.getId());
				imageDto.setImageName(savedImage.getFileName());
				imageDto.setDownloadUrl(savedImage.getDownloadUrl());
				savedImageDto.add(imageDto);
				
				
				
			}
			catch(IOException | SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		
		return savedImageDto;
	}

	@Override
	public void updateImage(MultipartFile file, Long imageId) {
		Image image = getImageById(imageId);
		try {
			image.setFileName(file.getOriginalFilename());
			image.setFileType(file.getContentType());
			image.setImage(new SerialBlob(file.getBytes()));
			imageRepository.save(image);
		}
		catch(IOException | SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}

}
