package ru.vinigret.limit.urlico.uchetul;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class UchetULMapper {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UchetULRepository uchrRepository;

    public UchetUL toEntity(UchetULDTO dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, UchetUL.class);
    }

    public UchetULDTO toDto(UchetUL entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, UchetULDTO.class);
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(UchetUL.class, UchetULDTO.class)
            .addMappings(
                m -> {
                    m.skip(UchetULDTO::setUchreditel);
                    m.skip(UchetULDTO::setParent);
                    m.skip(UchetULDTO::setUchetULKind);
                    m.skip(UchetULDTO::setRazdelBK);
                }
            )
            .setPostConverter(toDtoConverter()
        );
        mapper.createTypeMap(UchetULDTO.class, UchetUL.class)
            .addMappings(
                m -> {
                    m.skip(UchetUL::setUchreditel);
                    m.skip(UchetUL::setParent);
                    m.skip(UchetUL::setUchetULKind);
                    m.skip(UchetUL::setGrbs);
                    m.skip(UchetUL::setRazdelBK);
                }
            )
            .setPostConverter(toEntityConverter()
        );
    }

    public Converter<UchetULDTO, UchetUL> toEntityConverter() {
        return context -> {
            UchetULDTO source = context.getSource();
            UchetUL destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    public Converter<UchetUL, UchetULDTO> toDtoConverter() {
        return context -> {
            UchetUL source = context.getSource();
            UchetULDTO destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFields(UchetUL source, UchetULDTO destination) {
        destination.setUchreditel(Objects.isNull(source) || Objects.isNull(source.getUchreditel()) ? 0 : source.getUchreditel().getId());
        destination.setParent(Objects.isNull(source) || Objects.isNull(source.getParent()) ? 0 : source.getParent().getId());
        destination.setUchetULKind(source.getUchetULKind().ordinal());
        destination.setRazdelBK(source.getRazdelBK().substring(0,2));
        destination.setPodrazdelBK(source.getRazdelBK().length() == 4 ? source.getRazdelBK().substring(2,4):"00");
     }

    void mapSpecificFields(UchetULDTO source, UchetUL destination) {
        destination.setUchreditel(uchrRepository.findById(source.getUchreditel()).orElse(null));
        destination.setParent(uchrRepository.findById(source.getParent()).orElse(null));
        destination.setGrbs(destination.getUchreditel() == null);
        destination.setUchetULKind(UchetULKind.forInt(source.getUchetULKind()));
        destination.setLevel(Objects.isNull(destination.getParent()) ? 1 : destination.getParent().getLevel() + 1);
        destination.setIerKod(destination.getRazdelBK() + destination.getUchetULKind().ordinal());
        destination.setRazdelBK(source.getRazdelBK() + ("00".equals(source.getPodrazdelBK()) ? "" : source.getPodrazdelBK()));
    }

}