package service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import persistence.model.User;
import java.time.Instant;

@Builder
@Getter
@Setter
public class ShowBans {

    private Integer id;
    private Integer userId;
    private Instant startTime;
    private Instant endTime;

}
