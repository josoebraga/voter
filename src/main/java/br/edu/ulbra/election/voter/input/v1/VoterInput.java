package br.edu.ulbra.election.voter.input.v1;

import br.edu.ulbra.election.voter.exception.GenericOutputException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Voter Input Information")
public class VoterInput {

    @ApiModelProperty(example = "john@doe.com", notes = "Voter email")
    private String email;
    @ApiModelProperty(example = "John Doe", notes = "Voter name")
    private String name;
    @ApiModelProperty(example = "12345678", notes = "Password")
    private String password;
    @ApiModelProperty(example = "12345678", notes = "Password Confirmation")
    private String passwordConfirm;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        if ((this.name.substring(0, (this.name.indexOf(" ") - 1))).length() >= 3 &
                (this.name.substring((this.name.indexOf(" ") + 1), this.name.length())).length() >= 3)
        {
            return name;
        } else{
            throw new GenericOutputException("Invalid name");
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
