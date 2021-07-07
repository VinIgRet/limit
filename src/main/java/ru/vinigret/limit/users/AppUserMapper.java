package ru.vinigret.limit.users;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.vinigret.limit.urlico.uchetul.UchetULRepository;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class AppUserMapper {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private UchetULRepository uchrRepository;

    public AppUser toEntity(AppUserDTO dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, AppUser.class);
    }

    public AppUserDTO toDto(AppUser entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, AppUserDTO.class);
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(AppUser.class, AppUserDTO.class)
            .addMappings(
                m -> {
                    m.skip(AppUserDTO::setUchetUL);
                    m.skip(AppUserDTO::setPassword);
                }
            )
            .setPostConverter(toDtoConverter()
        );
        mapper.createTypeMap(AppUserDTO.class, AppUser.class)
            .addMappings(
                m -> {
                    m.skip(AppUser::setUchetUL);
                    m.skip(AppUser::setPassword);
                    m.skip(AppUser::setEmail);
                }
            )
            .setPostConverter(toEntityConverter()
        );
    }

    public Converter<AppUserDTO, AppUser> toEntityConverter() {
        return context -> {
            AppUserDTO source = context.getSource();
            AppUser destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    public Converter<AppUser, AppUserDTO> toDtoConverter() {
        return context -> {
            AppUser source = context.getSource();
            AppUserDTO destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFields(AppUser source, AppUserDTO destination) {
        destination.setUchetUL(Objects.isNull(source) || Objects.isNull(source.getUchetUL()) ? 0 : source.getUchetUL().getId());
        destination.setEmailOld(source.getEmail());
        destination.setPassword("");
     }

    void mapSpecificFields(AppUserDTO source, AppUser destination) {
        destination.setUchetUL(Objects.isNull(source) || Objects.isNull(source.getUchetUL()) ? null : uchrRepository.findById(source.getUchetUL()).orElse(null));
        if (!"".equals(source.getPassword())){
            destination.setPassword(new BCryptPasswordEncoder().encode(source.getPassword()));
        }
        if (!"".equals(source.getEmail())){
            destination.setEmail(source.getEmail());
        }
    }

}