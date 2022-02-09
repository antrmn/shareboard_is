package service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder @Data @AllArgsConstructor
public class BanDTO {
    private Integer banId;
    private Instant endTime;
    private Integer userId;
}
