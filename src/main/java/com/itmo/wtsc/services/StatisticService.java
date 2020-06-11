package com.itmo.wtsc.services;

import com.itmo.wtsc.dto.RequestChangesStatisticDto;
import com.itmo.wtsc.repositories.RequestChangeRepository;
import com.itmo.wtsc.utils.converters.DtoConverter;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class StatisticService {

    @Getter
    private final RequestChangeRepository requestChangeRepository;

    @Autowired
    public StatisticService(RequestChangeRepository requestChangeRepository) {
        this.requestChangeRepository = requestChangeRepository;
    }

    public RequestChangesStatisticDto getRequestChangesByTime(LocalDateTime start, LocalDateTime end) {
        RequestChangesStatisticDto result = new RequestChangesStatisticDto();
        result.setRequestChanges(getRequestChangeRepository()
                .findAllByUpdatedWhenBetween(start, end)
                .stream()
                .map(DtoConverter::getRequestChangeDto)
                .collect(Collectors.toList()));
        return result;
    }
}
