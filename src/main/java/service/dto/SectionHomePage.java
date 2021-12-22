package service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SectionHomePage {

    private Integer id;
    private String name;
    private String picture;
}
