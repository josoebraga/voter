package br.edu.ulbra.election.voter.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class VoteClientService {

    private final VoteClient voteClientService;

    @Autowired
    public VoteClientService(VoteClient voteClientService){
        this.voteClientService = voteClientService;
    }

    public String getById(Long id){
        return this.voteClientService.getById(id);
    }

    @FeignClient(value="vote-service", url="${url.vote-service}")
    private interface VoteClient {

        @GetMapping("/v1/vote/findFirstByVoterId/{voterId}")
        String getById(@PathVariable(name = "voterId") Long voterId);
    }
}
