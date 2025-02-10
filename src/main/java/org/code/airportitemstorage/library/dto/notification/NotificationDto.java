package org.code.airportitemstorage.library.dto.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class NotificationDto {
    public long id;

    public String title;

    public String content;

    public String author;

    @JsonProperty("createTime")
    public Date createTime;

    public boolean publish;
}
