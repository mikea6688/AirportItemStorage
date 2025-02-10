package org.code.airportitemstorage.library.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class UserComment {
    @TableId
    private long id;

    @TableField("user_id")
    private long userId;

    private String comment;

    @TableField("contact_info")
    private String ContactInfo;

    @TableField("comment_date")
    private Date commentDate;

    public void setDefaultCommentDate() {
        if (this.commentDate == null) {
            this.commentDate = new Date();
        }
    }
}
