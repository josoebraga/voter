package br.edu.ulbra.election.voter.api.v1;

import br.edu.ulbra.election.voter.exception.GenericOutputException;
import br.edu.ulbra.election.voter.input.v1.VoterInput;
import br.edu.ulbra.election.voter.model.Voter;
import br.edu.ulbra.election.voter.output.v1.GenericOutput;
import br.edu.ulbra.election.voter.output.v1.VoterOutput;
import br.edu.ulbra.election.voter.repository.VoterRepository;
import br.edu.ulbra.election.voter.service.VoterService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/voter")
public class VoterApi {

    private final VoterRepository voterRepository;
    private final VoterService voterService;

    private static final String MESSAGE_EMAIL_FOUND = "This e-mail is already registered";


    @Autowired
    public VoterApi(VoterRepository voterRepository, VoterService voterService) {
        this.voterRepository = voterRepository;
        this.voterService = voterService;
    }

    @GetMapping("/")
    @ApiOperation(value = "Get voters List")
    public List<VoterOutput> getAll() {
        return voterService.getAll();
    }

    @GetMapping("/{voterId}")
    @ApiOperation(value = "Get voter by Id")
    public VoterOutput getById(@PathVariable(name = "voterId") Long voterId) {
        return voterService.getById(voterId);
    }

    @PostMapping("/")
    @ApiOperation(value = "Create new voter")
    public VoterOutput create(@RequestBody VoterInput voterInput) {

        if(voterInput.getEmail().equals(ListEmail(voterInput.getEmail(), model))) {

            throw new GenericOutputException(MESSAGE_EMAIL_FOUND);

        } else {

            return voterService.create(voterInput);
        }

    }

    @PutMapping("/{voterId}")
    @ApiOperation(value = "Update voter")
    public VoterOutput update(@PathVariable Long voterId, @RequestBody VoterInput voterInput) {
        return voterService.update(voterId, voterInput);
    }

    @DeleteMapping("/{voterId}")
    @ApiOperation(value = "Delete voter")
    public GenericOutput delete(@PathVariable Long voterId) {
        return voterService.delete(voterId);
    }

    /*************************************************************************/


    Model model = new Model() {
        @Override
        public Model addAttribute(String attributeName, Object attributeValue) {
            return null;
        }

        @Override
        public Model addAttribute(Object attributeValue) {
            return null;
        }

        @Override
        public Model addAllAttributes(Collection<?> attributeValues) {
            return null;
        }

        @Override
        public Model addAllAttributes(Map<String, ?> attributes) {
            return null;
        }

        @Override
        public Model mergeAttributes(Map<String, ?> attributes) {
            return null;
        }

        @Override
        public boolean containsAttribute(String attributeName) {
            return false;
        }

        @Override
        public Map<String, Object> asMap() {
            return null;
        }
    };

    @RequestMapping(value = "/{email}", method = RequestMethod.GET)
    public String ListEmail(@PathVariable("email") String email, Model model) {
        List<Voter> ListEmail = voterRepository.findByEmail(email);
        if (ListEmail != null) {
            model.addAttribute("email", ListEmail);
        }
        return "ListEmail";
    }
/*************************************************************************/

}