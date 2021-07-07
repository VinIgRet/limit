package ru.vinigret.limit.kbk.osgu;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;

@Component
public class OsguMapper {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private OsguRepository osguRepository;

    public Osgu toEntity(OsguDTO dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Osgu.class);
    }

    public OsguDTO toDto(Osgu entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, OsguDTO.class);
    }
}