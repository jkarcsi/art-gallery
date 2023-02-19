package jkarcsi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jkarcsi.utils.validation.Password;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Email
  @Column(unique = true, nullable = false)
  private String username;

  @Password
  private String password;

  @ElementCollection(fetch = FetchType.EAGER)
  List<AppUserRole> appUserRoles;

}
