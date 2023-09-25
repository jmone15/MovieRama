package gr.jmone.movierama.mapper;

import gr.jmone.movierama.dto.UserDto;
import gr.jmone.movierama.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = MappingHelper.class)
public interface UserMapper {
  UserDto userToUserDto(User user);
}
