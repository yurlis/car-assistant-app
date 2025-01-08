package yurlis.carassistantapp.service;

import yurlis.carassistantapp.dto.car.CarWithoutPhotosDto;
import yurlis.carassistantapp.dto.car.CreateCarWithoutPhotosRequestDto;
import yurlis.carassistantapp.dto.car.UpdateCarWithoutPhotosRequestDto;

import java.util.List;

public interface CarService {
    CarWithoutPhotosDto save(Long userId, CreateCarWithoutPhotosRequestDto carDto);
    List<CarWithoutPhotosDto> findAllByUserId(Long userId);
    CarWithoutPhotosDto getById(Long id);
    CarWithoutPhotosDto update(Long id, UpdateCarWithoutPhotosRequestDto updateCarWithoutPhotosRequestDto);
    void deleteById(Long id);
}
