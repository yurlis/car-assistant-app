package yurlis.carassistantapp.mapper;

import org.mapstruct.Mapper;

import java.sql.Timestamp;

@Mapper
public interface CommonMapper {

    default Long mapTimestampToLong(Timestamp timestamp) {
        return timestamp != null ? timestamp.getTime() : null;
    }


}