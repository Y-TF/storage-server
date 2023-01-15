package com.ytf.storageserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ytf.storageserver.dto.response.ImageUploadResponse;
import com.ytf.storageserver.service.ImageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ImageController {
	private final ImageService imageService;

	@PostMapping("/api/image")
	public ResponseEntity<ImageUploadResponse> uploadImage(@RequestPart MultipartFile multipartFile) {
		ImageUploadResponse response = imageService.uploadImage(multipartFile);

		return ResponseEntity.ok(response);
	}
}
