package fr.zecchini.keycloack.resources.io.user.input;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"username", "password", "offline"})
public class SigninUserInput {
    public String username;
    public String password;
    public boolean offline;
}
