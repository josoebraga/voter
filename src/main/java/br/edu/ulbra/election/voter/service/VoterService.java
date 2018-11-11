package br.edu.ulbra.election.voter.service;

import br.edu.ulbra.election.voter.exception.GenericOutputException;
import br.edu.ulbra.election.voter.input.v1.VoterInput;
import br.edu.ulbra.election.voter.model.Voter;
import br.edu.ulbra.election.voter.output.v1.GenericOutput;
import br.edu.ulbra.election.voter.output.v1.VoterOutput;
import br.edu.ulbra.election.voter.repository.VoterRepository;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class VoterService {

    private final VoterRepository voterRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_VOTER_NOT_FOUND = "Voter not found";
    private static final String MESSAGE_EMAIL_FOUND = "This e-mail is already registered";
    private static final String MESSAGE_VOTER_PASSWORD_NOT_MATCH = "Password doesn't match";

    @Autowired
    public VoterService(VoterRepository voterRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder){
        this.voterRepository = voterRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<VoterOutput> getAll(){
        Type voterOutputListType = new TypeToken<List<VoterOutput>>(){}.getType();
        return modelMapper.map(voterRepository.findAll(), voterOutputListType);
    }

    public VoterOutput create(VoterInput voterInput) {
        validateInput(voterInput, false);
        Voter voter = modelMapper.map(voterInput, Voter.class);
        if(voter.getPassword().equals(voterInput.getPasswordConfirm())) {
            voter.setPassword(passwordEncoder.encode(voter.getPassword()));
        } else {
            throw new GenericOutputException(MESSAGE_VOTER_PASSWORD_NOT_MATCH);
        }
        voter = voterRepository.save(voter);
        return modelMapper.map(voter, VoterOutput.class);
    }

    public VoterOutput getById(Long voterId){
        if (voterId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Voter voter = voterRepository.findById(voterId).orElse(null);
        if (voter == null){
            throw new GenericOutputException(MESSAGE_VOTER_NOT_FOUND);
        }

        return modelMapper.map(voter, VoterOutput.class);
    }

    public VoterOutput update(Long voterId, VoterInput voterInput) {
        if (voterId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }
        validateInput(voterInput, true);

        Voter voter = voterRepository.findById(voterId).orElse(null);
        if (voter == null){
            throw new GenericOutputException(MESSAGE_VOTER_NOT_FOUND);
        }

    try {
        voter.setEmail(voterInput.getEmail()); //Tratado no voter api
    } catch (Exception e) {
        throw new GenericOutputException(MESSAGE_EMAIL_FOUND);
    }

            voter.setName(voterInput.getName()); /* Tratado no voter input */

        if (!StringUtils.isBlank(voterInput.getPassword())) {

            if(voterInput.getPassword().equals(voterInput.getPasswordConfirm())) {
                voter.setPassword(passwordEncoder.encode(voterInput.getPassword()));
            } else {
                throw new GenericOutputException(MESSAGE_VOTER_PASSWORD_NOT_MATCH);
            }

        }
        voter = voterRepository.save(voter);

        return modelMapper.map(voter, VoterOutput.class);

    }

    public GenericOutput delete(Long voterId) {
        if (voterId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Voter voter = voterRepository.findById(voterId).orElse(null);
        if (voter == null){
            throw new GenericOutputException(MESSAGE_VOTER_NOT_FOUND);
        }

        voterRepository.delete(voter);

        return new GenericOutput("Voter deleted");
    }

    private void validateInput(VoterInput voterInput, boolean isUpdate){
        if (StringUtils.isBlank(voterInput.getEmail())){
            throw new GenericOutputException("Invalid email");
        }
        if (StringUtils.isBlank(voterInput.getName())){
            throw new GenericOutputException("Invalid name");
        }
        if (!StringUtils.isBlank(voterInput.getPassword())){
            if (!voterInput.getPassword().equals(voterInput.getPasswordConfirm())){
                throw new GenericOutputException("Passwords doesn't match");
            }
        } else {
            if (!isUpdate) {
                throw new GenericOutputException("Password doesn't match");
            }
        }
    }

}
