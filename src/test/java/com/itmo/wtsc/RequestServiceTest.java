package com.itmo.wtsc;

import com.itmo.wtsc.dto.RequestDto;
import com.itmo.wtsc.dto.RequestFilter;
import com.itmo.wtsc.entities.Point;
import com.itmo.wtsc.entities.Request;
import com.itmo.wtsc.entities.User;
import com.itmo.wtsc.repositories.PointRepository;
import com.itmo.wtsc.repositories.RequestRepository;
import com.itmo.wtsc.services.GeoService;
import com.itmo.wtsc.services.RequestService;
import com.itmo.wtsc.services.UserService;
import com.itmo.wtsc.utils.enums.DumpType;
import com.itmo.wtsc.utils.enums.RequestStatus;
import com.itmo.wtsc.utils.enums.UserRole;
import com.itmo.wtsc.utils.exceptions.DataNotFoundException;
import com.itmo.wtsc.utils.exceptions.ValidationException;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RequestServiceTest {

    @Getter
    @Setter
    private User authUser;

    @Getter
    @Setter
    private Request currRequest;

    @Autowired
    public RequestService requestService;

    @MockBean
    public RequestRepository requestRepository;

    @MockBean
    public PointRepository pointRepository;

    @MockBean
    public UserService userService;

    @MockBean
    public GeoService geoService;

    @BeforeEach
    public void init() {
        User user = new User();
        user.setId(1);
        user.setRole(UserRole.TOURIST);
        Request req1 = new Request();
        req1.setId(1);
        Request req2 = new Request();
        req2.setId(2);
        Request req3 = new Request();
        req3.setId(3);
        user.setRequests(Arrays.asList(req1, req2, req3));
        setAuthUser(user);
        Request req = new Request();
        setCurrRequest(req);
        when(geoService.isNationalPark(any(), any())).thenReturn(true);
        when(geoService.isLand(any(), any())).thenReturn(true);
        when(userService.getAuthenticatedUser()).thenReturn(getAuthUser());
        when(requestRepository.findById(any())).thenReturn(Optional.of(getCurrRequest()));
        when(requestRepository.findRequestsByStatusInAndDumpTypeInAndSizeLessThanEqualAndCreatedWhenBetween(any(), any(), any(), any(), any()))
                .thenReturn(getRequestTestList());
        when(pointRepository.save(any(Point.class))).thenAnswer(elem -> {
            Point point = (Point) elem.getArgument(0);
            point.setId(1);
            return point;
        });
        when(requestRepository.save(any(Request.class))).thenAnswer(elem -> {
            Request request = (Request) elem.getArgument(0);
            request.setId(1);
            return request;
        });
        doNothing().when(requestRepository).deleteById(any());
        doNothing().when(pointRepository).deleteById(any());
    }

    @Test
    void testAddRequest() {
        RequestDto result = requestService.createRequest(getDto());
        assertEquals(result.getStatus(), RequestStatus.WAITING);
        assertNotNull(result.getCreatedWhen());
        assertNotNull(result.getId());
    }

    @Test
    void testUpdateRequestByTourist() {
        changeCurrRequest(1, RequestStatus.WAITING, DumpType.MIXED, 24, 1, null);
        RequestDto request = getDto();
        request.setSize(111);
        request.setDumpType(DumpType.HOUSEHOLD);
        request.setDescription("1");
        RequestDto result = requestService.updateRequest(request);
        assertEquals(result.getStatus(), RequestStatus.WAITING);
        assertEquals(result.getDescription(), "1");
        assertEquals(result.getDumpType(), DumpType.HOUSEHOLD);
        assertEquals(result.getSize(), 111);
    }

    @Test
    void testDeleteRequestByTourist() {
        changeCurrRequest(1, RequestStatus.WAITING, DumpType.MIXED, 24, 1, null);
        requestService.deleteRequest(1);
    }

    @Test
    void testUpdateRequestByTouristForeignRequest() {
        changeCurrRequest(1, RequestStatus.WAITING, DumpType.MIXED, 24, 2, null);
        RequestDto request = getDto();
        request.setSize(111);
        request.setDumpType(DumpType.HOUSEHOLD);
        request.setDescription("1");
        assertThrows(DataNotFoundException.class, () -> requestService.updateRequest(request));
    }

    @Test
    void testDeleteRequestByTouristForeignRequest() {
        changeCurrRequest(1, RequestStatus.WAITING, DumpType.MIXED, 24, 2, null);
        assertThrows(DataNotFoundException.class, () -> requestService.deleteRequest(4));
    }

    @Test
    void testUpdateRequestByTouristRequestInProgress() {
        changeCurrRequest(1, RequestStatus.IN_PROGRESS, DumpType.MIXED, 24, 1, null);
        RequestDto request = getDto();
        request.setSize(111);
        request.setDumpType(DumpType.HOUSEHOLD);
        request.setDescription("1");
        assertThrows(ValidationException.class, () -> requestService.updateRequest(request));
    }

    @Test
    void testDeleteRequestByTouristRequestInProgress() {
        changeCurrRequest(1, RequestStatus.IN_PROGRESS, DumpType.MIXED, 24, 1, null);
        RequestDto request = getDto();
        request.setSize(111);
        request.setDumpType(DumpType.HOUSEHOLD);
        request.setDescription("1");
        assertThrows(ValidationException.class, () -> requestService.updateRequest(request));
    }

    @Test
    void testUpdateRequestNonExistingRequest() {
        when(requestRepository.findById(any())).thenReturn(Optional.empty());
        changeCurrRequest(1, RequestStatus.WAITING, DumpType.MIXED, 24, 1, null);
        RequestDto request = getDto();
        request.setStatus(RequestStatus.COMPLETED);
        assertThrows(DataNotFoundException.class, () -> requestService.updateRequest(request));
        when(requestRepository.findById(any())).thenReturn(Optional.of(getCurrRequest()));
    }

    @Test
    void testDeleteRequestByTouristNonExistingRequest() {
        changeCurrRequest(1, RequestStatus.WAITING, DumpType.MIXED, 24, 2, null);
        assertThrows(DataNotFoundException.class, () -> requestService.deleteRequest(4));
    }

    @Test
    void testUpdateRequestByVolunteer() {
        changeCurrRequest(1, RequestStatus.WAITING, DumpType.MIXED, 24, 1, null);
        changeAuthUser(1, UserRole.VOLUNTEER);
        RequestDto request = getDto();
        request.setStatus(RequestStatus.IN_PROGRESS);
        RequestDto result = requestService.updateRequest(request);
        assertEquals(result.getStatus(), RequestStatus.IN_PROGRESS);

        request.setStatus(RequestStatus.COMPLETED);
        result = requestService.updateRequest(request);
        assertEquals(result.getStatus(), RequestStatus.COMPLETED);

        changeCurrRequest(1, RequestStatus.IN_PROGRESS, DumpType.MIXED, 24, 1, null);
        request.setStatus(RequestStatus.CANCELLED);
        result = requestService.updateRequest(request);
        assertEquals(result.getStatus(), RequestStatus.CANCELLED);

        changeCurrRequest(1, RequestStatus.IN_PROGRESS, DumpType.MIXED, 24, 1, null);
        request.setStatus(RequestStatus.WAITING);
        result = requestService.updateRequest(request);
        assertEquals(result.getStatus(), RequestStatus.WAITING);
    }

    @Test
    void testUpdateRequestByVolunteerIncorrectStatusTransition() {
        changeCurrRequest(1, RequestStatus.WAITING, DumpType.MIXED, 24, 1, null);
        changeAuthUser(1, UserRole.VOLUNTEER);
        RequestDto request = getDto();
        request.setStatus(RequestStatus.COMPLETED);
        assertThrows(ValidationException.class, () -> requestService.updateRequest(request));

        request.setStatus(RequestStatus.CANCELLED);
        assertThrows(ValidationException.class, () -> requestService.updateRequest(request));

        changeCurrRequest(1, RequestStatus.COMPLETED, DumpType.MIXED, 24, 1, null);
        request.setStatus(RequestStatus.IN_PROGRESS);
        assertThrows(ValidationException.class, () -> requestService.updateRequest(request));

        changeCurrRequest(1, RequestStatus.CANCELLED, DumpType.MIXED, 24, 1, null);
        request.setStatus(RequestStatus.WAITING);
        assertThrows(ValidationException.class, () -> requestService.updateRequest(request));
    }

    @Test
    void testGetRequestsByTourist() {
        RequestFilter requestFilter = new RequestFilter(null, null, null, null, null);

        changeAuthUser(1, UserRole.TOURIST);
        assertEquals(3, requestService.getRequests(requestFilter).size());

        changeAuthUser(2, UserRole.TOURIST);
        assertEquals(4, requestService.getRequests(requestFilter).size());
    }

    @Test
    void testGetRequestsByVolunteer() {
        RequestFilter requestFilter = new RequestFilter(null, null, null, null, null);

        changeAuthUser(1, UserRole.VOLUNTEER);
        assertEquals(7, requestService.getRequests(requestFilter).size());
    }
    
    private List<Request> getRequestTestList() {
        Point point = new Point();
        
        Request r1 = new Request();
        r1.setUser(getTestUser(1));
        r1.setSize(5);
        r1.setDumpType(DumpType.HOUSEHOLD);
        r1.setStatus(RequestStatus.WAITING);
        r1.setCreatedWhen(LocalDateTime.now());
        r1.setPoint(point);

        Request r2 = new Request();
        r2.setUser(getTestUser(2));
        r2.setSize(5);
        r2.setDumpType(DumpType.MIXED);
        r2.setStatus(RequestStatus.IN_PROGRESS);
        r2.setCreatedWhen(LocalDateTime.now());
        r2.setPoint(point);

        Request r3 = new Request();
        r3.setUser(getTestUser(2));
        r3.setSize(5);
        r3.setDumpType(DumpType.LIQUID);
        r3.setStatus(RequestStatus.WAITING);
        r3.setCreatedWhen(LocalDateTime.now());
        r3.setPoint(point);

        Request r4 = new Request();
        r4.setUser(getTestUser(2));
        r4.setSize(5);
        r4.setDumpType(DumpType.SOLID);
        r4.setStatus(RequestStatus.IN_PROGRESS);
        r4.setCreatedWhen(LocalDateTime.now());
        r4.setPoint(point);

        Request r5 = new Request();
        r5.setUser(getTestUser(2));
        r5.setSize(5);
        r5.setDumpType(DumpType.LIQUID);
        r5.setStatus(RequestStatus.WAITING);
        r5.setCreatedWhen(LocalDateTime.now());
        r5.setPoint(point);

        Request r6 = new Request();
        r6.setUser(getTestUser(1));
        r6.setSize(11);
        r6.setDumpType(DumpType.SOLID);
        r6.setStatus(RequestStatus.IN_PROGRESS);
        r6.setCreatedWhen(LocalDateTime.now());
        r6.setPoint(point);

        Request r7 = new Request();
        r7.setUser(getTestUser(1));
        r7.setSize(11);
        r7.setDumpType(DumpType.HOUSEHOLD);
        r7.setStatus(RequestStatus.WAITING);
        r7.setCreatedWhen(LocalDateTime.now());
        r7.setPoint(point);

        return Arrays.asList(r1, r2, r3, r4, r5, r6, r7);
    }

    private User getTestUser(Integer id) {
        User user = new User();
        user.setId(id);
        return user;
    }
    
    private void changeAuthUser(Integer id, UserRole role) {
        getAuthUser().setRole(role);
        getAuthUser().setId(id);
    }

    private void changeCurrRequest(Integer id,
                                   RequestStatus status,
                                   DumpType type,
                                   Integer size,
                                   Integer userId,
                                   String description) {
        getCurrRequest().setId(id);
        getCurrRequest().setStatus(status);
        getCurrRequest().setDumpType(type);
        getCurrRequest().setSize(size);
        getCurrRequest().setDescription(description);
        User user = new User();
        user.setId(userId);
        getCurrRequest().setUser(user);
        Point point = new Point();
        point.setLongitude(1.1);
        point.setLatitude(1.1);
        getCurrRequest().setPoint(point);
    }

    private RequestDto getDto() {
        RequestDto requestDto = new RequestDto();
        requestDto.setLongitude(1.1);
        requestDto.setLatitude(1.1);
        requestDto.setStatus(RequestStatus.WAITING);
        requestDto.setDumpType(DumpType.HOUSEHOLD);
        requestDto.setSize(11);
        return requestDto;
    }
}
