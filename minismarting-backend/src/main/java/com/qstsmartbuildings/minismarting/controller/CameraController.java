package com.qstsmartbuildings.minismarting.controller;

import com.qstsmartbuildings.minismarting.entity.Camera;
import com.qstsmartbuildings.minismarting.mapper.CameraMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class CameraController {

    @Autowired
    private CameraMapper cameraMapper;

    @GetMapping("/api/cameras")
    public List<Camera> getCameras() {
        return cameraMapper.findAll();
    }
}
