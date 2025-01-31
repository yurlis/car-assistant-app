package yurlis.carassistantapp.service.fueltype;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import yurlis.carassistantapp.dto.fueltypes.FuelTypeResponseDto;
import yurlis.carassistantapp.mapper.FuelTypeMapper;
import yurlis.carassistantapp.repository.fueltype.FuelTypeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FuelTypeService {

    private final FuelTypeRepository fuelTypeRepository;
    private final FuelTypeMapper fuelTypeMapper;

    public List<FuelTypeResponseDto> getAllFuelTypes() {
        return fuelTypeRepository.findAll(Sort.by(Sort.Order.asc("id")))
                .stream()
                .map(fuelTypeMapper::toDto)
                .toList();
    }
}