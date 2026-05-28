package com.yamilog.userservice.adapter.out.persistence;

import com.yamilog.common.domain.model.ManiaLevel;
import com.yamilog.userservice.domain.model.Follow;
import com.yamilog.userservice.domain.model.User;
import com.yamilog.userservice.domain.model.UserLevel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        imports = {ManiaLevel.class, java.util.UUID.class})
interface UserMapper {

    @Mapping(target = "userId", expression = "java(entity.getPublicId().toString())")
    User toDomain(UserEntity entity);

    @Mapping(target = "publicId", expression = "java(UUID.fromString(user.getUserId()))")
    @Mapping(target = "seqId", ignore = true)
    UserEntity toEntity(User user);

    @Mapping(target = "maniaLevel", expression = "java(ManiaLevel.fromValue(entity.getManiaLevel()))")
    UserLevel levelToDomain(UserLevelEntity entity);

    @Mapping(target = "maniaLevel", expression = "java(level.getManiaLevel().getValue())")
    @Mapping(target = "seqId", ignore = true)
    UserLevelEntity levelToEntity(UserLevel level);

    @Mapping(target = "seqId", ignore = true)
    FollowEntity followToEntity(Follow follow);

    Follow followToDomain(FollowEntity entity);
}
