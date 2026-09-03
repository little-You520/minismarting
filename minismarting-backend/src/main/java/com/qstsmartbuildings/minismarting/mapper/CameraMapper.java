package com.qstsmartbuildings.minismarting.mapper;

import com.qstsmartbuildings.minismarting.entity.Camera;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper   //
public interface CameraMapper {

    @Select("SELECT * FROM device_camera")
    List<Camera> findAll();
}