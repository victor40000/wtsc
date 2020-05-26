package com.itmo.wtsc.controller;

import com.itmo.wtsc.dto.NewRequestCase;
import com.itmo.wtsc.dto.RequestDto;
import com.itmo.wtsc.dto.RequestFilter;
import com.itmo.wtsc.dto.UpdateRequestCase;
import com.itmo.wtsc.osm.OsmRestClient;
import com.itmo.wtsc.services.RequestService;
import com.itmo.wtsc.services.UserService;
import com.itmo.wtsc.utils.enums.DumpType;
import com.itmo.wtsc.utils.enums.RequestStatus;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Log4j2
@Controller
public class RequestController {

    @Getter
    private final RequestService requestService;

    @Getter
    private final UserService userService;

    @Autowired
    public RequestController(RequestService requestService, UserService userService) {
        this.requestService = requestService;
        this.userService = userService;
    }

    @RequestMapping(value = "/requests", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Transactional
    public RequestDto addRequest(@Validated(NewRequestCase.class) @RequestBody RequestDto requestDto) {
        log.info("request for adding request received:\n" + requestDto);
        return getRequestService().createRequest(requestDto);
    }

    @RequestMapping(value = "/requests", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Transactional
    public List<RequestDto> getRequests(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                        @RequestParam(required = false) List<DumpType> dumpTypes,
                                        @RequestParam(required = false) Integer maxSize,
                                        @RequestParam(required = false) List<RequestStatus> statuses) {
        RequestFilter filter = new RequestFilter(statuses, dumpTypes, maxSize, startDate, endDate);
        log.info("request for getting requests received:\n" + filter);
        return getRequestService().getRequests(filter);
    }

    @RequestMapping(value = "/requests/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Transactional
    public RequestDto updateRequest(@PathVariable Integer id,
                              @Validated(UpdateRequestCase.class) @RequestBody RequestDto requestDto) {
        requestDto.setId(id);
        log.info("request for updating request received:\n" + requestDto);
        return getRequestService().updateRequest(requestDto);
    }

    @RequestMapping(value = "/requests/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Transactional
    public void deleteRequest(@PathVariable Integer id) {
        log.info("request for deleting request received: " + id);
        getRequestService().deleteRequest(id);
    }

/*    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleServiceNotFoundException(EmptyResultDataAccessException ex) {
    }*/
}
