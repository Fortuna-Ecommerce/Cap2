package com.capstone.ecommerce;


import com.capstone.ecommerce.services.UserDetailsLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsLoader usersLoader;

    public SecurityConfiguration(UserDetailsLoader usersLoader) {
        this.usersLoader = usersLoader;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(usersLoader) // How to find users by their username
                .passwordEncoder(passwordEncoder()) // How to encode and verify passwords
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
/*
                    MAKE A PLAN FOR ALL ROLES AND PERMISSIONS
                    ENSURE ALL PATHS ARE ROUTED CORRECTLY

*/

  /* Login configuration */
                .formLogin()
                .loginPage("/login")
//                .defaultSuccessUrl("/ads") // user's home page, it can be any URL
                .defaultSuccessUrl("/users/profile") // user's home page, it can be any URL
                .permitAll() // Anyone can go to the login page
                /* Logout configuration */
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout") // append a query string value
                /* Pages that can be viewed without having to log in */
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/products/all") // anyone can see the home and the products pages
                .permitAll()
                /* Pages that require authentication */
                .and()
                .authorizeRequests()
                .antMatchers(
                       "/users/profile",
                        "/cart", //only authenticated users can see a shopping cart
                       "/checkout",
                        "/users/adminportal",
                        "/users/profile"//only authenticated users can see the checkout paget
//                        "/ads/create", // only authenticated users can create ads
//                        "/ads/{id}/edit", // only authenticated users can edit ads
//                        "/posts/create", // only authenticated users can create posts
//                         "/posts/{id}/edit" // only authenticated users can edit posts
                     //only authenticated users can see the checkout paget
                )
                .authenticated()
        ;
    }
}