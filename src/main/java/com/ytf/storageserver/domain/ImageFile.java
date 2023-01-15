package com.ytf.storageserver.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ytf.storageserver.exception.BusinessException;

public class ImageFile {
	private static final Pattern VALIDATE_EXTENSION = Pattern.compile("^(png|jpg|jpeg)$");

	private final String originalFilename;
	private final String contentType;
	private final byte[] imageBytes;

	public ImageFile(String originalFilename, String contentType, byte[] imageBytes) {
		this.originalFilename = originalFilename;
		this.contentType = contentType;
		this.imageBytes = imageBytes;
	}

	public static ImageFile from(final MultipartFile multipartFile) {
		validateMultipartFile(multipartFile);

		try {
			return new ImageFile(
				multipartFile.getOriginalFilename(),
				multipartFile.getContentType(),
				multipartFile.getBytes());
		} catch (IOException e) {
			throw new IllegalArgumentException("잘못된 이미지 파일입니다.");
		}
	}

	private static void validateMultipartFile(final MultipartFile multipartFile) {
		validateFileIsNull(multipartFile);
		validateFileIsEmpty(multipartFile);
		validateFilenameIsEmpty(multipartFile);
		validateFileExtension(multipartFile);
	}

	private static void validateFileIsNull(final MultipartFile multipartFile) {
		if (Objects.isNull(multipartFile)) {
			throw new BusinessException("이미지 파일은 null 값이 들어올 수 없습니다.");
		}
	}

	private static void validateFileIsEmpty(final MultipartFile multipartFile) {
		if (multipartFile.isEmpty()) {
			throw new BusinessException("빈 이미지 파일은 들어올 수 없습니다.");
		}
	}

	private static void validateFilenameIsEmpty(final MultipartFile multipartFile) {
		if (multipartFile.getOriginalFilename().isEmpty()) {
			throw new BusinessException("이미지 파일 이름은 빈 문자열이 들어올 수 없습니다.");
		}
	}

	private static void validateFileExtension(final MultipartFile multipartFile) {
		String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
		if (!VALIDATE_EXTENSION.matcher(extension).matches()) {
			throw new BusinessException("이미지 파일의 확장자는 png, jpg, jpeg만 가능합니다.");
		}
	}

	public String getRandomName() {
		StringBuilder randomName = new StringBuilder();
		randomName.append(UUID.randomUUID())
			.append(".")
			.append(StringUtils.getFilenameExtension(originalFilename));

		return randomName.toString();
	}

	public InputStream getImageFileInputStream() {
		return new ByteArrayInputStream(imageBytes);
	}
}
