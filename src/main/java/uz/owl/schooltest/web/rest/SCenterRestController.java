package uz.owl.schooltest.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.owl.schooltest.dto.SCenterDto;
import uz.owl.schooltest.dto.SCenterPayload;
import uz.owl.schooltest.exception.CenterNotFoundException;
import uz.owl.schooltest.exception.CoundtCreatedExeption;
import uz.owl.schooltest.exception.CoundtUpdatedException;
import uz.owl.schooltest.exception.UserNotFoundException;
import uz.owl.schooltest.service.SCenterService;
import uz.owl.schooltest.web.Message;

import javax.validation.Valid;
import java.security.Principal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController()
public class SCenterRestController {
    private final SCenterService sCenterService;

    public SCenterRestController(SCenterService sCenterService) {
        this.sCenterService = sCenterService;
    }

    @GetMapping("${api.v1}centers")
    public List<SCenterDto> getAllByUser(Principal principal) throws UserNotFoundException {
        List<SCenterDto> allbyUser = sCenterService.findAllByUser(principal.getName());
        return allbyUser;
    }

    @GetMapping("${api.v1}centers/{name}")
    public SCenterDto getSingleCenter(Principal principal,
                                      @PathVariable String name)
            throws UserNotFoundException, CenterNotFoundException
    {
        SCenterDto byAuthorAndName = sCenterService.findByAuthorAndName(principal.getName(), name);
        return byAuthorAndName;
    }

    @PostMapping("${api.v1}centers")
    public ResponseEntity<Message> addCenter(Principal principal,
                                             @Valid @RequestBody SCenterPayload sCenterPayload)
            throws SQLIntegrityConstraintViolationException, CoundtCreatedExeption
    {
        sCenterService.saveSCenterForUser(principal.getName(), sCenterPayload);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Message(201,"Center created"));
    }

    @PutMapping("${api.v1}centers/{name}")
    public ResponseEntity<Message> updateCenter(Principal principal,
                                                @PathVariable String name,
                                                @Valid @RequestBody SCenterPayload sCenterPayload)
            throws CoundtUpdatedException, UserNotFoundException
    {
        sCenterService.update(principal.getName(), name, sCenterPayload);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Message(201,"Updated"));
    }

    @DeleteMapping("${api.v1}centers/{name}")
    public ResponseEntity<Message> deleteCenter(Principal principal, @PathVariable String name) throws UserNotFoundException {
        sCenterService.deleteByAuthorAndName(principal.getName(), name);
        return ResponseEntity.status(HttpStatus.OK).body(new Message(200, "Center deleted"));
    }




//    @ExceptionHandler(UserNotFoundException.class)
//    public void ifUserNotFound(UserNotFoundException e){
//        System.out.println(e.getMessage());
//    }
//
//    @ExceptionHandler(CoundtCreatedExeption.class)
//    public ResponseEntity<Message> ifCoundtCreated(CoundtCreatedExeption e){
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(400, e.getMessage()));
//    }
//
//    @ExceptionHandler(CoundtUpdatedException.class)
//    public ResponseEntity<Message> ifCoundtUpdated(CoundtUpdatedException e){
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(400, e.getMessage()));
//    }
//
//    @ExceptionHandler(CenterNotFoundException.class)
//    public ResponseEntity<Message> ifCenterNotFound(CoundtUpdatedException e){
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(400, e.getMessage()));
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Message> anyError(Exception e){
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(400, "Internal Server error"));
//    }
}
