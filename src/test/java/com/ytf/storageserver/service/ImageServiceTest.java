package com.ytf.storageserver.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;

import com.ytf.storageserver.dto.response.ImageUploadResponse;
import com.ytf.storageserver.exception.ImageFileNotFoundException;

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

	@DisplayName("이미지 다운로드 테스트")
	@Nested
	class ImageDownloadTest {
		@DisplayName("이미지 다운로드 url이 주어지면 이미지를 다운로드하고 이미지 바이트를 반환한다.")
		@Test
		void givenImageUrl_whenDownloadImage_thenReturnsImageByteArray() {
			// given
			String imageUrl = "test-image.jpg";

			// when
			byte[] response = imageService.downloadImage(imageUrl);

			// then
			assertThat(response).isNotNull();
		}

		@DisplayName("경로에 없는 이미지 다운로드 url이 주어지면 ImageFileNotFoundException을 던진다.")
		@Test
		void givenWrongImageUrl_whenDownloadImage_thenThrowsImageFileNotFoundException() {
			// given
			String imageUrl = "wrong-image.jpg";

			// when & then
			assertThatThrownBy(() -> imageService.downloadImage(imageUrl))
				.isInstanceOf(ImageFileNotFoundException.class);
		}
	}
}