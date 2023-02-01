package com.ytf.storageserver.domain;

import java.util.Arrays;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import com.ytf.storageserver.exception.BusinessException;

public enum FileExtension {
	PNG("png") {
		@Override
		public MediaType getMediaType() {
			return MediaType.IMAGE_PNG;
		}
	},
	JPG("jpg") {
		@Override
		public MediaType getMediaType() {
			return MediaType.IMAGE_JPEG;
		}
	},
	JPEG("jpeg") {
		@Override
		public MediaType getMediaType() {
			return MediaType.IMAGE_JPEG;
		}
	},
	GIF("gif") {
		@Override
		public MediaType getMediaType() {
			return MediaType.IMAGE_GIF;
		}
	};

	private final String extension;

	FileExtension(final String extension) {
		this.extension = extension;
	}

	public static FileExtension from(final String imageUrl) {
		String extension = StringUtils.getFilenameExtension(imageUrl);
		return Arrays.stream(FileExtension.values())
			.filter(fileExtension -> fileExtension.extension.equals(extension))
			.findFirst()
			.orElseThrow(() -> new BusinessException("이미지 파일 확장자(png, jpg, jpeg, gif)만 들어올 수 있습니다."));
	}

	public abstract MediaType getMediaType();
}
