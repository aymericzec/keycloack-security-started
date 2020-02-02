package fr.zecchini.keycloack.resources.inputs;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"username", "password"})
public class LoginUserInput {
    public String username;
    public String password;
}
