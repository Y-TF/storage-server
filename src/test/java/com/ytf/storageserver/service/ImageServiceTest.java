package com.ytf.storageserver.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;

import com.ytf.storageserver.dto.response.ImageUploadResponse;

@SpringBootTest
class ImageServiceTest {
	private final ImageService imageService;

	public ImageServiceTest(@Autowired ImageService imageService) {
		this.imageService = imageService;
	}

	@DisplayName("이미지 파일이 주어지면 서버에 저장하고 다운로드 url를 반환한다.")
	@Test
	void givenMultipartImage_whenUploadImage_thenReturnsImageDownloadUrl() {
		// given
		MockMultipartFile mockMultipartFile = new MockMultipartFile("test-name",
			"test-image.jpeg",
			"image/jpg",
			"imageBytes".getBytes());

		// when
		ImageUploadResponse response = imageService.uploadImage(mockMultipartFile);

		// then
		assertAll(
			() -> assertThat(response).isNotNull(),
			() -> assertThat(StringUtils.getFilenameExtension(response.imageDownloadUrl()))
				.isEqualTo("jpeg")
		);
	}
}