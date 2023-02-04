package com.ytf.storageserver.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ytf.storageserver.domain.ImageFile;
import com.ytf.storageserver.dto.response.ImageUploadResponse;
import com.ytf.storageserver.exception.BusinessException;
import com.ytf.storageserver.exception.ImageFileNotFoundException;

@Service
public class ImageService {
	private final Path storagePath;
	private final String serverPrefix;

	public ImageService(
		@Value("${file.upload-dir}") String storagePath,
		@Value("${file.server-prefix}") String serverPrefix) {
		this.storagePath = Paths.get(storagePath);
		this.serverPrefix = serverPrefix;
	}

	public ImageUploadResponse uploadImage(final MultipartFile image) {
		try {
			ImageFile imageFile = ImageFile.from(image);

			String imageFileName = imageFile.getRandomName();
			Path imageStoragePath = storagePath.resolve(imageFileName);
			Files.copy(imageFile.getImageFileInputStream(), imageStoragePath, StandardCopyOption.REPLACE_EXISTING);

			return ImageUploadResponse.of(serverPrefix + imageFileName);
		} catch (IOException e) {
			throw new BusinessException("이미지 저장에 실패했습니다.");
		}
	}

	public byte[] downloadImage(final String imageUrl) {
		Path imageStoredPath = storagePath.resolve(imageUrl);
		File imageFile = imageStoredPath.toFile();

		try (FileInputStream fis = new FileInputStream(imageFile)) {
			return StreamUtils.copyToByteArray(fis);
		} catch (FileNotFoundException e) {
			throw new ImageFileNotFoundException("해당 파일이 경로에 존재하지 않습니다.");
		} catch (IOException e) {
			throw new BusinessException("이미지 파일 변환에 실패했습니다.");
		}
	}
}
