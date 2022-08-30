package com.example.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPublicKey;
import java.util.List;

import static org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames.AUD;

@Configuration
public class SecurityConfig {

    @Value("${key.location}")
    private RSAPublicKey key;

    /**
     * Set up a security filter chain and configure the access levels for the different endpoints in
     * {@see MainController}. We then configure our application into a "Resource Server" that accepts "JWT"s
     *
     * Different endpoints have different access levels and you can find demo JWTs to use in the README.md file in
     * the root of this project.
     *
     * You can always do a request to /token to have a look at how Spring Security has parsed the given token.
     *
     * @param httpSecurity
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(authorize ->
            authorize
                    .mvcMatchers("/read/**").hasAuthority("SCOPE_read")
                    .mvcMatchers("/write/**").hasAuthority("SCOPE_write")
                    .mvcMatchers("/user/**").hasAnyRole("user", "admin")
                    .mvcMatchers("/admin/**").hasRole("admin")
                    .anyRequest().authenticated()
        )
        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
        .build();
    }


    /**
     * Build and configure the JwtDecoder that will be used when we receive a Jwt Token. Here we take in a
     * {@see RSAPublicKey} but you can also supply a JWK uri, or a {@see SecretKey}. By default, the decoder will
     * always verify the signature with the given key and validate the timestamp to check if the JWT is still valid.
     *
     * Our decoder can be customized with several options. We can for instance do custom validation on claims, do
     * rename, add and remove claims, and even change the datatype that the claim is mapped too.
     *
     * All we do below is to add some custom validation to the "issuer" claim and a custom validator to validate the aud claim, 
     * remember we must add the default timestamp validation back as we have overridden defaults. 
     * Per default a Public key will set the algorithm to RS256. If you want something different you can set this explicitly.
     *
     * @return JwtDecoder
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        final NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(this.key)
                .build();
        decoder.setJwtValidator(tokenValidator());
        return decoder;
    }

    /**
     * We can write custom validators to validate different parts of the JWT. Per default, the framework will always
     * validate the timestamp, but we can add validators to enhance security. For instance you should always
     * validate the issuer to make sure that the JWT was issued from a known source. Remember that if we customise the
     * validation we need to re-add the timestamp validator.
     *
     * Here we crate a list of validators. The {@see JwtTimestampValidator} and the {@see JwtIssuerValidator} are
     * from the spring security framework, but we have also added a custom one. Remember if you add a custom list, you
     * must always remember to add timestamp validation or else this will be removed.
     *
     * We then place these in a {@see DelegatingOAuth2TokenValidator} that we can set to our {@see JwtDecoder}.
     *
     * @return Oauth2TokenValidator<Jwt>
     */
    public OAuth2TokenValidator<Jwt> tokenValidator() {
        final List<OAuth2TokenValidator<Jwt>> validators =
                List.of(new JwtTimestampValidator(),
                        new JwtIssuerValidator("http://foobar.com"),
                        audienceValidator());
        return new DelegatingOAuth2TokenValidator<>(validators);
    }

    /**
     * You can write a custom validation by adding a {@see JwtClaimValidator} for instance below we add a custom
     * validator to the aud (audience) claim. And check that it contains a certain string.
     * {@see OAuth2TokenIntrospectionClaimNames} contains static string names of several default claims. Below we are
     * referencing the {@see OAuth2TokenIntrospectionClaimNames#AUD} string.
     *
     * @return Oauth2TokenValidator<T>
     */
    public OAuth2TokenValidator<Jwt> audienceValidator() {
        return new JwtClaimValidator<List<String>>(AUD, aud -> aud.contains("foobar"));
    }

    /**
     * In the given JWT the "scope" claim is the default property that spring will use to extract and build
     * authorities from. But some issuers will have a different name, for instance "authorities". So if we want to
     * map from something else than the default, we can add our own authentication converter.
     *
     * Per default if the "scope" claim is used, it is called an "Authority". Which means it will get prefixed by
     * "SCOPE_" so a claim can look like ex: "SCOPE_read". But you can change this prefixand  add your own, for
     * instance "ROLE_" so you get ROLE_USER".
     *
     * ROLES are usually a group of SCOPES. so SCOPES could be read, write, update certain resources. While a ROLE
     * could be a group of these authorities, so as a USER you are allowed to read and write for example your own
     * information. And read and write your own messages etc.
     *
     * @return a JwtAuthenticationConverter
     */
    @Bean
    @Profile("roles")
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
