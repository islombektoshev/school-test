package uz.owl.schooltest.web.rest;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.owl.schooltest.dto.scenter.SCenterDto;
import uz.owl.schooltest.dto.scenter.SCenterPayload;
import uz.owl.schooltest.exception.CenterNotFoundException;
import uz.owl.schooltest.exception.CoundtUpdatedException;
import uz.owl.schooltest.exception.UserNotFoundException;
import uz.owl.schooltest.service.SCenterService;
import uz.owl.schooltest.web.Message;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController()
public class SCenterRestController {
    private final SCenterService sCenterService;
    private final String RESOURCE_URL = "/api/v1/centers";

    public SCenterRestController(SCenterService sCenterService) {
        this.sCenterService = sCenterService;
    }

    @GetMapping(RESOURCE_URL)
    public List<Resource<SCenterDto>> getAllByUser(Principal principal) throws UserNotFoundException {
        List<SCenterDto> allbyUser = sCenterService.findAllByUser(principal.getName());
        return sCenterService.findAllByUser(principal.getName())
                .stream().map(s -> {
                    Resource<SCenterDto> resource = new Resource<>(s);
                    allLinks(resource, principal, s.getName());
                    return resource;
                }).collect(Collectors.toList());
    }

    @GetMapping(RESOURCE_URL + "/{name}")
    public Resource<SCenterDto> getSingleCenter(Principal principal, @PathVariable String name) throws UserNotFoundException, CenterNotFoundException {
        SCenterDto byAuthorAndName = sCenterService.findByAuthorAndName(principal.getName(), name);
        Resource<SCenterDto> resourceCenterDto = new Resource<>(byAuthorAndName);
        allLinks(resourceCenterDto, principal, name);
        return resourceCenterDto;
    }

    @PostMapping(RESOURCE_URL)
    public ResponseEntity<Resource<Message>> addCenter(Principal principal, @Valid @RequestBody SCenterPayload sCenterPayload) {
        SCenterDto sCenterDto = sCenterService.saveForUser(principal.getName(), sCenterPayload);
        Resource<Message> message = new Resource<>(new Message(201, "Center created"));
        allLinks(message, principal, sCenterDto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PutMapping(RESOURCE_URL + "/{name}")
    public ResponseEntity<Resource<Message>> updateCenter(Principal principal, @PathVariable String name, @Valid @RequestBody SCenterPayload sCenterPayload) throws CoundtUpdatedException, UserNotFoundException {
        SCenterDto update = sCenterService.update(principal.getName(), name, sCenterPayload);
        Resource<Message> message = new Resource<>(new Message(201, "Updated"));
        allLinks(message, principal, update.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @DeleteMapping(RESOURCE_URL + "/{name}")
    public ResponseEntity<Message> deleteCenter(Principal principal, @PathVariable String name) throws UserNotFoundException {
        sCenterService.deleteByAuthorAndName(principal.getName(), name);
        return ResponseEntity.status(HttpStatus.OK).body(new Message(200, "Center deleted"));
    }

    private void allLinks(Resource resource, Principal principal, String name) {
        ControllerLinkBuilder controllerLinkBuilder = linkTo(methodOn(getClass()).getSingleCenter(principal, name));
        ControllerLinkBuilder subjects_links = linkTo(methodOn(SubjectRestController.class).getAllSubjects(principal, name));
        ControllerLinkBuilder groups_links = linkTo(methodOn(GroupRestController.class).getAllGroups(principal, name));
        ControllerLinkBuilder students_links = linkTo(methodOn(StudentRestController.class).getAllStudents(principal, name));
        ControllerLinkBuilder blocktests_link = linkTo(methodOn(BlockTestRestController.class).getAllBlockTest(principal, name));
        resource.add(controllerLinkBuilder.withSelfRel());
        resource.add(groups_links.withRel("groups"));
        resource.add(students_links.withRel("students"));
        resource.add(subjects_links.withRel("subjects"));
        resource.add(blocktests_link.withRel("blocktests"));
    }

}
