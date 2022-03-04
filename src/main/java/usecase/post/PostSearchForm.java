package usecase.post;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class PostSearchForm {
    public enum SortCriteria {OLDEST, NEWEST, MOSTVOTED};
    private final String content;
    private final String sectionName;
    private final String authorName;
    private final Instant postedAfter;
    private final Instant postedBefore;
    @Builder.Default private final SortCriteria orderBy = SortCriteria.NEWEST;
    @Builder.Default private final int page = 1;
    @Builder.Default private final boolean onlyFollow = false;
    @Builder.Default private final boolean includeBody = false;
}
