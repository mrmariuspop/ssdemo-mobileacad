package securitydemo.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import securitydemo.demo.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;

//    @PreAuthorize("hasAuthority('SMECHER')")
    @GetMapping("/all")
    public ResponseEntity findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }
}
