package service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SectionLite {

    private Integer id;
    private String name;
}
