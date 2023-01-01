package com.ytf.storageserver.domain;

import static org.assertj.core.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@DisplayName("[Domain] - ImageFile 테스트")
@ExtendWith(MockitoExtension.class)
class ImageFileTest {
	@DisplayName("MultipartFile이 주어지면 파일을 검증하는데 성공한다.")
	@MethodSource("provideImageFile")
	@ParameterizedTest
	void givenMultipartFile_whenCreateImageFile_thenReturnsImageFile(String originalFilename, String contentType) {
		// given
		MockMultipartFile mockMultipartFile = new MockMultipartFile("testImage",
			originalFilename,
			contentType,
			"mock".getBytes(StandardCharsets.UTF_8));

		// when
		ImageFile imageFile = ImageFile.from(mockMultipartFile);

		// then
		assertThat(imageFile)
			.hasFieldOrPropertyWithValue("originalFilename", originalFilename)
			.hasFieldOrPropertyWithValue("contentType", contentType)
			.hasFieldOrPropertyWithValue("imageBytes", "mock".getBytes(StandardCharsets.UTF_8));
	}

	private static Stream<Arguments> provideImageFile() {
		return Stream.of(
			Arguments.of("image.png", MediaType.IMAGE_PNG_VALUE),
			Arguments.of("image.jpg", MediaType.IMAGE_JPEG_VALUE),
			Arguments.of("image.jpeg", MediaType.IMAGE_JPEG_VALUE)
		);
	}

	@DisplayName("MultipartFile이 null로 주어지면 예외를 던진다.")
	@Test
	void givenNullMultipartFile_whenCreateImageFile_thenThrowsException() {
		// given
		MockMultipartFile mockMultipartFile = null;

		// when & then
		assertThatThrownBy(() -> ImageFile.from(mockMultipartFile))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("이미지 파일은 null 값이 들어올 수 없습니다.");
	}

	@DisplayName("비어있는 MultipartFile이 주어지면 예외를 던진다.")
	@Test
	void givenEmptyMultipartFile_whenCreateImageFile_thenThrowsException() {
		// given
		MockMultipartFile mockMultipartFile = new MockMultipartFile("testImage",
			"originalFilename.jpg",
			MediaType.IMAGE_JPEG_VALUE,
			"".getBytes(StandardCharsets.UTF_8));

		// when & then
		assertThatThrownBy(() -> ImageFile.from(mockMultipartFile))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("빈 이미지 파일은 들어올 수 없습니다.");
	}

	@DisplayName("null이거나 빈 문자열이 파일이름인 MultipartFile이 주어지면 예외를 던진다.")
	@NullAndEmptySource
	@ParameterizedTest
	void givenEmptyFilenameMultipartFile_whenCreateImageFile_thenThrowsException(String originalFilename) {
		// given
		MockMultipartFile mockMultipartFile = new MockMultipartFile("testImage",
			originalFilename,
			MediaType.IMAGE_JPEG_VALUE,
			"mock".getBytes(StandardCharsets.UTF_8));

		// when & then
		assertThatThrownBy(() -> ImageFile.from(mockMultipartFile))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("이미지 파일 이름은 빈 문자열이 들어올 수 없습니다.");
	}

	@DisplayName("잘못돈 확장자를 가진 MultipartFile이 주어지면 예외를 던진다.")
	@ValueSource(strings = {"image.txt", "image.pdf", "image.avi", "image.wav", "image.pngjpgjpeg"})
	@ParameterizedTest
	void givenInvalidFileExtensionMultipartFile_whenCreateImageFile_thenThrowsException(String originalFilename) {
		// given
		MockMultipartFile mockMultipartFile = new MockMultipartFile("testImage",
			originalFilename,
			MediaType.IMAGE_JPEG_VALUE,
			"mock".getBytes(StandardCharsets.UTF_8));

		// when & then
		assertThatThrownBy(() -> ImageFile.from(mockMultipartFile))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("이미지 파일의 확장자는 png, jpg, jpeg만 가능합니다.");
	}
}