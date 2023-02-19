package jkarcsi.controller;

import static jkarcsi.utils.constants.GalleryMessages.LIST;
import static jkarcsi.utils.constants.GeneralConstants.ACCESS_ADMIN;
import static jkarcsi.utils.constants.GeneralConstants.ACCESS_BOTH;
import static jkarcsi.utils.constants.GeneralConstants.ME_PATH;
import static jkarcsi.utils.constants.GeneralConstants.REFRESH_PATH;
import static jkarcsi.utils.constants.GeneralConstants.SIGNIN_PATH;
import static jkarcsi.utils.constants.GeneralConstants.SIGNUP_PATH;
import static jkarcsi.utils.constants.GeneralConstants.USERNAME_PATH;
import static jkarcsi.utils.constants.GeneralConstants.USERS_BASE_PATH;
import static jkarcsi.utils.constants.UserMessages.ACCESS_DENIED;
import static jkarcsi.utils.constants.UserMessages.ALREADY_EXIST;
import static jkarcsi.utils.constants.UserMessages.DELETE;
import static jkarcsi.utils.constants.UserMessages.INVALID_DATA;
import static jkarcsi.utils.constants.UserMessages.ME;
import static jkarcsi.utils.constants.UserMessages.NOT_EXIST;
import static jkarcsi.utils.constants.UserMessages.SIGN_IN;
import static jkarcsi.utils.constants.UserMessages.SIGN_UP;
import static jkarcsi.utils.constants.UserMessages.TOKEN_ERROR;
import static jkarcsi.utils.constants.UserMessages.USER_ERROR;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import jkarcsi.dto.user.AppUser;
import jkarcsi.dto.gallery.Artwork;
import jkarcsi.service.GalleryService;
import jkarcsi.utils.helpers.IncludeSwaggerDocumentation;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import jkarcsi.dto.user.UserData;
import jkarcsi.dto.user.UserResponse;
import jkarcsi.service.UserService;

@RestController
@RequestMapping(USERS_BASE_PATH)
@Api(tags = "users")
@RequiredArgsConstructor
@IncludeSwaggerDocumentation
public class UserController {


  private final UserService userService;
  private final GalleryService galleryService;
  private final ModelMapper modelMapper;

  @PostMapping(SIGNIN_PATH)
  @ApiOperation(value = SIGN_IN)
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = USER_ERROR),
      @ApiResponse(code = 422, message = INVALID_DATA)})
  public String login(
      @ApiParam("Username") @RequestParam String username,
      @ApiParam(value = "Password", format = "password") @RequestParam String password) {
    return userService.signin(username, password);
  }

  @PostMapping(SIGNUP_PATH)
  @ApiOperation(value = SIGN_UP)
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = USER_ERROR),
      @ApiResponse(code = 403, message = ACCESS_DENIED),
      @ApiResponse(code = 422, message = ALREADY_EXIST)})
  public String signup(@ApiParam("Signup User") @RequestBody UserData user) {
    return userService.signup(modelMapper.map(user, AppUser.class));
  }

  @DeleteMapping(value = USERNAME_PATH)
  @PreAuthorize(ACCESS_ADMIN)
  @ApiOperation(value = DELETE, authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = USER_ERROR),
      @ApiResponse(code = 403, message = ACCESS_DENIED),
      @ApiResponse(code = 404, message = NOT_EXIST),
      @ApiResponse(code = 500, message = TOKEN_ERROR)})
  public String delete(@ApiParam("Username") @PathVariable String username) {
    userService.delete(username);
    return username;
  }

  @GetMapping(value = USERNAME_PATH)
  @PreAuthorize(ACCESS_ADMIN)
  @ApiOperation(value = LIST)
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = USER_ERROR),
      @ApiResponse(code = 403, message = ACCESS_DENIED),
      @ApiResponse(code = 404, message = NOT_EXIST),
      @ApiResponse(code = 500, message = TOKEN_ERROR)})
  public ResponseEntity<List<Artwork>> listOwnedArtworksByUser(@PathVariable final String username) {
    return ResponseEntity.ok(galleryService.listOwnedArtworks(username));
  }

  @GetMapping(value = ME_PATH)
  @PreAuthorize(ACCESS_BOTH)
  @ApiOperation(value = ME, response = UserResponse.class, authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = USER_ERROR),
      @ApiResponse(code = 403, message = ACCESS_DENIED),
      @ApiResponse(code = 500, message = TOKEN_ERROR)})
  public UserResponse whoami(HttpServletRequest req) {
    return modelMapper.map(userService.whoami(req), UserResponse.class);
  }

  @GetMapping(REFRESH_PATH)
  @PreAuthorize(ACCESS_BOTH)
  public String refresh(HttpServletRequest req) {
    return userService.refresh(req.getRemoteUser());
  }

}
