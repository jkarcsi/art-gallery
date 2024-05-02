package jkarcsi.dto.user;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserResponse {

  @ApiModelProperty
  private Integer id;
  @ApiModelProperty(position = 1)
  private String username;
  @ApiModelProperty(position = 2)
  List<AppUserRole> appUserRoles;

}
