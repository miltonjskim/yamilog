package com.yamilog.userservice.adapter.out.persistence;

import com.yamilog.common.domain.model.ManiaLevel;
import com.yamilog.userservice.domain.model.Follow;
import com.yamilog.userservice.domain.model.User;
import com.yamilog.userservice.domain.model.UserLevel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        imports = {ManiaLevel.class})
interface UserMapper {

    @Mapping(target = "userId", source = "id")
    User toDomain(UserEntity entity);

    @Mapping(target = "id", source = "userId")
    UserEntity toEntity(User user);

    @Mapping(target = "maniaLevel", expression = "java(ManiaLevel.fromValue(entity.getManiaLevel()))")
    UserLevel levelToDomain(UserLevelEntity entity);

    @Mapping(target = "maniaLevel", expression = "java(level.getManiaLevel().getValue())")
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    UserLevelEntity levelToEntity(UserLevel level);

    Follow followToDomain(FollowEntity entity);

    FollowEntity followToEntity(Follow follow);
}
