package com.example.jingangfarmmanagement.uitl;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

public class ObjectMapperUtils {

    private static final ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(STRICT);
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }

    private ObjectMapperUtils() {
    }
    public static <S, D> D map(final S source, D destination) {
        modelMapper.map(source, destination);
        return destination;
    }
}
