package jkarcsi.configuration;

import static jkarcsi.utils.constants.GeneralConstants.AUTHORIZATION;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import jkarcsi.utils.helpers.IncludeSwaggerDocumentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicates;

import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile("prod")
public class SwaggerConfig {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.withClassAnnotation(IncludeSwaggerDocumentation.class))
        .paths(Predicates.not(PathSelectors.regex("/error")))
        .build()
        .apiInfo(metadata())
        .useDefaultResponseMessages(false)
        .securitySchemes(Collections.singletonList(apiKey()))
        .securityContexts(Collections.singletonList(securityContext()))
        .tags(new Tag("users", "Operations about users"), new Tag("gallery", "Operations about gallery"))
        .genericModelSubstitutes(Optional.class);

  }

  private ApiInfo metadata() {
    return new ApiInfoBuilder()
        .title("Art Gallery API")
        .description("This is a trial project for implementing a REST API that communicates with the API of the Art Institute of Chicago (ARTIC). The API has endpoints for authentication, retrieval of a single artwork or paginated artworks, purchasing artworks and listing owned artworks by a user. You can try it with an `admin` or a `client` user (username: 'user1@email.com', 'user2@email.com' respectively, password: 'password' for both users). To successfully pass through the authorization filters, first you have to logged in and obtained the token, then you should click on the right top button `Authorize` and introduce it with the prefix \"Bearer \". After all this, you can use the endpoints of the gallery, based on your user rights.")
        .version("1.0.0")
        .license("MIT License").licenseUrl("http://opensource.org/licenses/MIT")
        .contact(new Contact("Karoly Jugovits", null, "jugovitskaroly@gmail.com"))
        .build();
  }

  private ApiKey apiKey() {
    return new ApiKey(AUTHORIZATION, AUTHORIZATION, "header");
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(defaultAuth())
        .forPaths(PathSelectors.any())
        .build();
  }

  private List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return List.of(new SecurityReference(AUTHORIZATION, authorizationScopes));
  }

}
