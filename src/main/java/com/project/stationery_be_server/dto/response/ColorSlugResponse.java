package com.project.stationery_be_server.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ColorSlugResponse {
    private String colorId;
    private String hex;
    private String slug;

    public ColorSlugResponse(String colorId, String hex, String slug) {
        this.colorId = colorId;
        this.hex = hex;
        this.slug = slug;
    }
}
