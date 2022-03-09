package usecase.ban;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/**
 * Classe DTO relativa al ban.
 */
@Builder @Data @AllArgsConstructor
public class BanDTO {
    private Integer banId;
    private Instant endTime;
    private Integer userId;
}
