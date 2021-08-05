package securitydemo.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import securitydemo.demo.config.IAuthenticationFacade;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/sec")
public class SecurityController {

    @Autowired
    IAuthenticationFacade iAuthenticationFacade;

    @GetMapping("/user")
    public ResponseEntity getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return ResponseEntity.status(HttpStatus.OK).body(currentPrincipalName);
    }

    //    An improvement to this snippet is first checking if there is an authenticated user before trying to access it:
    @GetMapping("/is-auth-user")
    public ResponseEntity getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return ResponseEntity.status(HttpStatus.OK).body(currentUserName);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not authenticated");
    }

    @GetMapping("/username")
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal.getName();
    }

    //An alternative to /username api
    @GetMapping("/username-auth")
    @ResponseBody
    public String currentUserName(Authentication authentication) {
        return authentication.getName();
//        return authentication.getName() + authentication.getAuthorities() + authentication.getDetails() + authentication.getCredentials() + authentication.getPrincipal();
    }

    //    The API of the Authentication class is very open so that the framework remains as flexible as possible.
//    Because of this, the Spring Security principal can only be retrieved as an Object and needs to be cast
//    to the correct UserDetails instance:
    @GetMapping("/user-details")
    @ResponseBody
    public ResponseEntity getViaUserDetails(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(userDetails.toString());
    }

    //    And finally, directly from the HTTP request:
    @GetMapping("/https")
    @ResponseBody
    public ResponseEntity getViaHttp(HttpServletRequest httpServletRequest) {
        Principal principal = httpServletRequest.getUserPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(principal.toString());
    }

    @GetMapping("/auth-facade")
    @ResponseBody
    public ResponseEntity getViaFacade() {
        Authentication authentication = iAuthenticationFacade.getAuthentication();
        return ResponseEntity.status(HttpStatus.OK).body(authentication.getName());
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest httpServletRequest) throws ServletException {
        httpServletRequest.logout();
    }
}
