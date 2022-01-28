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
//    private User admin;
    private Integer userId;
    private Integer section;
    private Instant startTime;
    private Instant endTime;

}
