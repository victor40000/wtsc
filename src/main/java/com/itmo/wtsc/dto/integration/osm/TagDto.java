package com.itmo.wtsc.dto.integration.osm;

import lombok.Data;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class TagDto {

    @XmlAttribute(name = "k")
    private String key;

    @XmlAttribute(name = "v")
    private String value;
}
