package com.project.stationery_be_server.service.impl;

import com.project.stationery_be_server.dto.request.ColorRequest;
import com.project.stationery_be_server.dto.response.ColorResponse;
import com.project.stationery_be_server.entity.Color;
import com.project.stationery_be_server.repository.ColorRepository;
import com.project.stationery_be_server.service.ColorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColorServiceImpl implements ColorService {
    private final ColorRepository colorRepository;

    public ColorServiceImpl(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    public ColorResponse createColor(ColorRequest request){
        Color color = new Color();
        color.setColor_id(request.getColor_id());
        color.setName(request.getName());
        color.setHex(request.getHex());

        Color savedColor = colorRepository.save(color);

        return new ColorResponse(savedColor.getColor_id(), savedColor.getName(), savedColor.getHex());
    }

    @Override
    public List<ColorResponse> getAllColors() {
        List<Color> colors = colorRepository.findAll();
        return colors.stream()
                .map(color -> new ColorResponse(color.getColor_id(), color.getName(), color.getHex()))
                .collect(Collectors.toList());
    }

    @Override
    public ColorResponse updateColor(String id, ColorRequest request) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Color not found with ID: " + id));

        color.setName(request.getName());
        color.setHex(request.getHex());

        Color updatedColor = colorRepository.save(color);
        return new ColorResponse(updatedColor.getColor_id(), updatedColor.getName(), updatedColor.getHex());
    }
}

