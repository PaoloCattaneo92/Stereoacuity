package utils;

/**
 * Created by paolo on 19/11/2015.
 * The User class contains the data of the User, everything that might be useful.
 * The "username" variable in particular must be a primary key (unique).
 */
public class User {

    public String username;
    //TODO more User caracteristics (full name, address, birth date...)
    public Certification_Value highestCertification;

    public User(String username, String certification_value_string) {
        this.username=username;
        this.highestCertification= Certification_Value.StringToCertificationValue(certification_value_string);
    }

    /**
     * You know.
     * @return a string describing the user.
     */
    public String toString() {
        return "User: "+username+". Highest certification is "+highestCertification;
    }

}
