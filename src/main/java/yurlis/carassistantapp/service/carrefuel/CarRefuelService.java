package yurlis.carassistantapp.service.carrefuel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import yurlis.carassistantapp.dto.carrefuel.CarRefuelResponseDto;
import yurlis.carassistantapp.dto.carrefuel.CreateCarRefuelRequestDto;
import yurlis.carassistantapp.dto.pagination.CustomPage;

import java.sql.Timestamp;

public interface CarRefuelService {
    CarRefuelResponseDto createRefuel(Long carId, CreateCarRefuelRequestDto requestDto);

    CarRefuelResponseDto updateRefuel(Long refuelId, CreateCarRefuelRequestDto refuelRequestDto);
    void deleteCarRefuel(Long refuelId);
    CustomPage<CarRefuelResponseDto> getRefuelsForCar(
            Long carId, Timestamp startDate, Timestamp endDate, Long fuelType, Pageable pageable
    );
}
