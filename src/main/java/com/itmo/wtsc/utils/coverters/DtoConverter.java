package com.itmo.wtsc.utils.coverters;

import com.itmo.wtsc.dto.RequestDto;
import com.itmo.wtsc.entities.Point;
import com.itmo.wtsc.entities.Request;

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
        return requestDto;
    }
}
