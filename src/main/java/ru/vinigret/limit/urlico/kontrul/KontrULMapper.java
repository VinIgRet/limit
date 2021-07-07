package ru.vinigret.limit.urlico.kontrul;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.vinigret.limit.usluga.Usluga;
import ru.vinigret.limit.usluga.UslugaRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class KontrULMapper {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private KontrULRepository kontrRepository;

    @Autowired
    private UslugaRepository uslugaRepository;

    public KontrUL toEntity(KontrULDTO dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, KontrUL.class);
    }

    public KontrULDTO toDto(KontrUL entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, KontrULDTO.class);
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(KontrUL.class, KontrULDTO.class)
            .addMappings(
                m -> {
                    m.skip(KontrULDTO::setUslugaList);
                }
            )
            .setPostConverter(toDtoConverter()
        );
        mapper.createTypeMap(KontrULDTO.class, KontrUL.class)
            .addMappings(
                m -> {
                    m.skip(KontrUL::setUslugaList);
                }
            )
            .setPostConverter(toEntityConverter()
        );
    }

    public Converter<KontrULDTO, KontrUL> toEntityConverter() {
        return context -> {
            KontrULDTO source = context.getSource();
            KontrUL destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    public Converter<KontrUL, KontrULDTO> toDtoConverter() {
        return context -> {
            KontrUL source = context.getSource();
            KontrULDTO destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFields(KontrUL source, KontrULDTO destination) {
        destination.setUslugaList(source.getUslugaList().stream().map(Usluga::getId).collect(Collectors.toList()));
     }

    void mapSpecificFields(KontrULDTO source, KontrUL destination) {
        destination.setUslugaList(Objects.isNull(source.getUslugaList()) ? (new ArrayList<Usluga>()) : source.getUslugaList().stream().map(s -> {return uslugaRepository.findById(s).orElse(null);}).filter(s -> s != null).collect(Collectors.toList()));
    }
}