package org.code.airportitemstorage.library.entity.notification;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Notification {
    @TableId
    private long id;

    private String author;

    private String title;

    private String content;

    public boolean publish;

    @TableField("create_time")
    private Date createTime;

    public void setDefaultCommentDate() {
        if (this.createTime == null) {
            this.createTime = new Date();
        }
    }
}
