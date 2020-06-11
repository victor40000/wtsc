package com.itmo.wtsc.utils.converters;

import com.itmo.wtsc.dto.RequestChangeDto;
import com.itmo.wtsc.dto.RequestDto;
import com.itmo.wtsc.dto.UserDto;
import com.itmo.wtsc.entities.Point;
import com.itmo.wtsc.entities.Request;
import com.itmo.wtsc.entities.RequestChange;
import com.itmo.wtsc.entities.User;

public class DtoConverter {
    public static Request getRequest(RequestDto requestDto) {
        Request request = new Request();
        request.setDescription(requestDto.getDescription());
        request.setDumpType(requestDto.getDumpType());
        request.setSize(requestDto.getSize());
        request.setStatus(requestDto.getStatus());
        return request;
    }

    public static Point getPoint(RequestDto requestDto) {
        Point point = new Point();
        point.setLatitude(requestDto.getLatitude());
        point.setLongitude(requestDto.getLongitude());
        return point;
    }

    public static RequestDto getRequestDto(Request request, Point point) {
        RequestDto requestDto = new RequestDto();
        requestDto.setDescription(request.getDescription());
        requestDto.setDumpType(request.getDumpType());
        requestDto.setSize(request.getSize());
        requestDto.setStatus(request.getStatus());
        requestDto.setLatitude(point.getLatitude());
        requestDto.setLongitude(point.getLongitude());
        requestDto.setId(request.getId());
        requestDto.setCreatedWhen(request.getCreatedWhen());
        return requestDto;
    }

    public static UserDto getUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setLogin(user.getLogin());
        userDto.setId(user.getId());
        userDto.setRole(user.getRole());
        userDto.setActive(user.isActive());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static RequestChangeDto getRequestChangeDto(RequestChange requestChange) {
        RequestChangeDto requestChangeDto = new RequestChangeDto();
        requestChangeDto.setId(requestChange.getId());
        requestChangeDto.setFrom(requestChange.getFrom());
        requestChangeDto.setTo(requestChange.getTo());
        requestChangeDto.setUpdatedWhen(requestChange.getUpdatedWhen());
        requestChangeDto.setRequest(getRequestDto(requestChange.getRequest(), requestChange.getRequest().getPoint()));
        return requestChangeDto;
    }
}
