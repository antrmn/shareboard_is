package service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Builder
@Getter
@Setter
public class BanSectionInfo {
    private Integer section;
    private Boolean global;
    private Instant endTime;
}
