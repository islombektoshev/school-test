package uz.owl.schooltest.web.rest;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.owl.schooltest.dto.user.UserDto;
import uz.owl.schooltest.dto.user.UserPayload;
import uz.owl.schooltest.service.UserService;
import uz.owl.schooltest.web.Message;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public Resource<UserDto> home(Principal principal) {
        UserDto userDto = userService.getUserDto(principal.getName());
        Resource<UserDto> userDtoResource = new Resource<>(userDto);
        links(userDtoResource, principal);
        return userDtoResource;
    }

    public void links(Resource resource, Principal principal) {
        Link all_centers = linkTo(methodOn(SCenterRestController.class).getAllByUser(principal)).withRel("all_centers");
        resource.add(all_centers);
    }

    @PostMapping("/signup")
    public ResponseEntity signUp(Principal principal, @RequestBody UserPayload userPayload) {
        UserDto userDto = userService.createUser(userPayload);
        if (userDto == null) {
            return ResponseEntity.badRequest().body(new Message(404, "Has User"));
        }
        Resource<UserDto> userDtoResource = new Resource<>(userDto);
        links(userDtoResource, principal);
        return ResponseEntity.ok(userDtoResource);
    }

    @GetMapping("/has")
    public Map hasUser(@RequestParam String username) {
        UserDto userDto = userService.getUserDto(username);
        if (userDto == null) return Collections.singletonMap("isEmpty", false);
        return Collections.singletonMap("isEmpty", true);
    }

}
