package com.itmo.wtsc.dto.integration.osm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.math.BigInteger;
import java.util.List;

@Data
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.FIELD)
public class AreaDto {

    @XmlAttribute(name = "id")
    private BigInteger id;

    @EqualsAndHashCode.Exclude
    @XmlElement(name = "tag")
    private List<TagDto> tags;
}
