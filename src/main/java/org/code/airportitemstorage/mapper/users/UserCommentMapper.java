package org.code.airportitemstorage.mapper.users;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.code.airportitemstorage.library.entity.user.UserComment;

@Mapper
public interface UserCommentMapper extends BaseMapper<UserComment> {
}
