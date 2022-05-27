package org.isj.ing.annuarium.webapp.mapper;


import org.isj.ing.annuarium.webapp.model.dto.Actedto;
import org.isj.ing.annuarium.webapp.model.entities.Acte;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy =  NullValueCheckStrategy.ALWAYS)
public interface ActeMapper {

    Acte toEntity(Actedto actedto);

    Actedto toDto(Acte acte);

    void copy(Actedto actedto, @MappingTarget Acte acte);
}
