package com.nimsoc.ticket.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Ticket {

    private int code;
    private LocalDateTime creationDate;
    private String severity;
    private String summary;
    private String description;
    private String status;
    private double internalBudget;
    private double externalBudget;
    private String module;
    private String partnerCode;
    private String partnerName;
    private String reporter;
    private String screener;
    private String assignedUser;
    private List<Attachment> attachmentList;
    private List<Comment> commentList;

    @Data
    public static class Attachment {
        private String filename;
        private int fileSize;
    }

    @Data
    public static class Comment {
        private int order;
        private String type;
        private String author;
        private String content;
        private LocalDateTime date;
    }
}