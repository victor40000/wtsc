package com.itmo.wtsc.services;

import com.itmo.wtsc.dto.RequestFilter;
import com.itmo.wtsc.utils.ErrorMessages;
import com.itmo.wtsc.dto.RequestDto;
import com.itmo.wtsc.entities.Point;
import com.itmo.wtsc.entities.Request;
import com.itmo.wtsc.entities.User;
import com.itmo.wtsc.utils.exceptions.DataNotFoundException;
import com.itmo.wtsc.utils.exceptions.ValidationException;
import com.itmo.wtsc.repositories.PointRepository;
import com.itmo.wtsc.repositories.RequestRepository;
import com.itmo.wtsc.utils.enums.RequestStatus;
import com.itmo.wtsc.utils.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.itmo.wtsc.utils.ErrorMessages.REQUEST_ALREADY_EXISTS;
import static com.itmo.wtsc.utils.converters.DtoConverter.*;

@Service
public class RequestService {

    private final Map<RequestStatus, List<RequestStatus>> statusTransitions = new HashMap<>() {{
        put(RequestStatus.WAITING, Arrays.asList(RequestStatus.IN_PROGRESS, RequestStatus.WAITING));
        put(RequestStatus.IN_PROGRESS, Arrays.asList(RequestStatus.IN_PROGRESS, RequestStatus.WAITING,
                RequestStatus.COMPLETED, RequestStatus.CANCELLED));
        put(RequestStatus.COMPLETED, new ArrayList<>());
        put(RequestStatus.CANCELLED, new ArrayList<>());
    }};

    private final RequestRepository requestRepository;
    private final PointRepository pointRepository;
    private final UserService userService;
    private final GeoService geoService;

    @Autowired
    public RequestService(RequestRepository requestRepository, PointRepository pointRepository, UserService userService, GeoService geoService) {
        this.requestRepository = requestRepository;
        this.pointRepository = pointRepository;
        this.userService = userService;
        this.geoService = geoService;
    }

    public RequestDto createRequest(RequestDto requestDto) {
        User user = userService.getAuthenticatedUser();
        validateCoordinates(requestDto.getLatitude(), requestDto.getLongitude());
        Point point = getPoint(requestDto);
        Request request = getRequest(requestDto);
        request.setPoint(point);
        request.setUser(user);
        request.setStatus(RequestStatus.WAITING);
        request.setCreatedWhen(LocalDateTime.now());
        pointRepository.save(point);
        requestRepository.save(request);
        return getRequestDto(request, request.getPoint());
    }

    public RequestDto updateRequest(RequestDto requestDto) {
        User user = userService.getAuthenticatedUser();
        Integer id = requestDto.getId();
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(String.format(ErrorMessages.REQUEST_NOT_FOUND_ERROR, id)));

        switch (user.getRole()) {
            case TOURIST:
                if (!Objects.equals(user.getId(), request.getUser().getId())) {
                    throw new DataNotFoundException(String.format(ErrorMessages.REQUEST_NOT_FOUND_ERROR, id));
                }
                validateTouristCanChangeRequest(request.getStatus());
                request.setSize(requestDto.getSize());
                request.setDescription(requestDto.getDescription());
                request.setDumpType(requestDto.getDumpType());
                break;
            case VOLUNTEER:
                validateStatusChange(request.getStatus(), requestDto.getStatus());
                request.setStatus(requestDto.getStatus());
                break;
        }
        requestRepository.save(request);
        return getRequestDto(request, request.getPoint());
    }

    public List<RequestDto> getRequests(RequestFilter filter) {
        User user = userService.getAuthenticatedUser();
        Predicate<Request> userPredicate = request -> true;
        if (UserRole.TOURIST.equals(user.getRole())) {
            userPredicate = request -> user.getId().equals(request.getUser().getId());
            filter.setStatuses(Arrays.asList(RequestStatus.WAITING, RequestStatus.IN_PROGRESS));
        }
        return requestRepository.findRequestsByStatusInAndDumpTypeInAndSizeLessThanEqualAndCreatedWhenBetween(
                filter.getStatuses(), filter.getTypes(), filter.getMaxSize(), filter.getStartTime(), filter.getEndTime())
                .stream()
                .filter(userPredicate)
                .map(request -> getRequestDto(request, request.getPoint()))
                .collect(Collectors.toList());
    }

    public void deleteRequest(Integer id) {
        User user = userService.getAuthenticatedUser();
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(String.format(ErrorMessages.REQUEST_NOT_FOUND_ERROR, id)));
        if (user.getRequests().stream().noneMatch(req -> req.getId().equals(id))) {
            throw new DataNotFoundException(String.format(ErrorMessages.REQUEST_NOT_FOUND_ERROR, id));
        }
        validateTouristCanChangeRequest(request.getStatus());
        pointRepository.deleteById(request.getPoint().getId());
        requestRepository.deleteById(id);
    }

    private void validateStatusChange(RequestStatus source, RequestStatus destination) {
        if (statusTransitions.get(source).contains(destination)) {
            return;
        }
        throw new ValidationException(String.format(ErrorMessages.INVALID_STATUS_TRANSITION_ERROR, source, destination));
    }

    private void validateTouristCanChangeRequest(RequestStatus status) {
        if (RequestStatus.WAITING.equals(status)) {
            return;
        }
        throw new ValidationException(String.format(ErrorMessages.UNCHANGEABLE_REQUEST_ERROR, status));
    }

    private void validateCoordinates(Double latitude, Double longitude) {
        geoService.validateCoordinates(latitude, longitude);
        List<Request> requests = requestRepository
                .findRequestByStatusIn(Arrays.asList(RequestStatus.WAITING, RequestStatus.IN_PROGRESS));
        requests.stream()
                .filter(req -> geoService.isRadiusIntersecting(latitude, longitude, req.getPoint().getLatitude(),
                        req.getPoint().getLongitude()))
                .findFirst().ifPresent(a -> {
            throw new ValidationException(REQUEST_ALREADY_EXISTS);
        });
    }
}
