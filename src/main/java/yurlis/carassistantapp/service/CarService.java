package yurlis.carassistantapp.service;

import yurlis.carassistantapp.dto.car.CarWithoutPhotosDto;
import yurlis.carassistantapp.dto.car.CreateCarWithoutPhotosDto;
import yurlis.carassistantapp.dto.car.UpdateCarRequestDto;

import java.util.List;

public interface CarService {
    CarWithoutPhotosDto save(Long userId, CreateCarWithoutPhotosDto carDto);
    List<CarWithoutPhotosDto> findAllByUserId(Long userId);
    CarWithoutPhotosDto getById(Long id);
    CarWithoutPhotosDto update(Long id, UpdateCarRequestDto updateCarRequestDto);
    void deleteById(Long id);
}
