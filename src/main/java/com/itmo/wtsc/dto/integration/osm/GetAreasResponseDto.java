package com.itmo.wtsc.dto.integration.osm;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement(name = "osm")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetAreasResponseDto {

    @XmlElement(name = "area")
    private List<AreaDto> areas;
}
