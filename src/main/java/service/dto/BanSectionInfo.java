package service.dto;

import java.time.Instant;

public class BanSectionInfo {
    private SectionID section;
    private Boolean global;
    private Instant endTime;

    public SectionID getSection() {
        return section;
    }

    public void setSection(SectionID section) {
        this.section = section;
    }

    public Boolean getGlobal() {
        return global;
    }

    public void setGlobal(Boolean global) {
        this.global = global;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }
}
