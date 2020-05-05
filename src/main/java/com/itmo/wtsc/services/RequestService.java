package com.itmo.wtsc.services;

import com.itmo.wtsc.dto.RequestDto;
import com.itmo.wtsc.entities.Point;
import com.itmo.wtsc.entities.Request;
import com.itmo.wtsc.entities.User;
import com.itmo.wtsc.repositories.PointRepository;
import com.itmo.wtsc.repositories.RequestRepository;
import com.itmo.wtsc.utils.enums.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.itmo.wtsc.utils.coverters.DtoConverter.*;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final PointRepository pointRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository, PointRepository pointRepository) {
        this.requestRepository = requestRepository;
        this.pointRepository = pointRepository;
    }

    public RequestDto createRequest(RequestDto requestDto, User user) {
        Point point = getPoint(requestDto);
        Request request = getRequest(requestDto);
        request.setPoint(point);
        request.setUser(user);
        request.setStatus(RequestStatus.WAITING);
        pointRepository.save(point);
        requestRepository.save(request);
        return getRequestDto(request, request.getPoint());
    }

    public List<RequestDto> getRequests() {
        List<String> a;
        return requestRepository.findAll().stream().map(request -> getRequestDto(request, request.getPoint())).collect(Collectors.toList());
    }

    public void deleteRequest(Integer id) {
        requestRepository.deleteById(id);
    }
}
