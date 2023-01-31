package com.ytf.storageserver.dto.response;

public record ImageUploadResponse(String imageDownloadUrl) {
	public static ImageUploadResponse of(final String imageDownloadUrl) {
		return new ImageUploadResponse(imageDownloadUrl);
	}
}
