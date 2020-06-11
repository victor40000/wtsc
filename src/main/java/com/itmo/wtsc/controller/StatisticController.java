package com.itmo.wtsc.controller;

import com.itmo.wtsc.dto.RequestChangesStatisticDto;
import com.itmo.wtsc.dto.UserDto;
import com.itmo.wtsc.services.StatisticService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("statistic")
public class StatisticController {

    @Getter
    private final StatisticService statisticService;

    @Autowired
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @RequestMapping(value = "/requestChanges", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Transactional
    public RequestChangesStatisticDto getRequestChangesByTime(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        if (startDate == null) {
            startDate = LocalDateTime.parse("2000-05-20T15:23:30");
        }
        if (endDate == null) {
            endDate = LocalDateTime.parse("2100-05-20T15:23:30");
        }
        return statisticService.getRequestChangesByTime(startDate, endDate);
    }
}
