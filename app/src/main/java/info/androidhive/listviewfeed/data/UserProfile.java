package info.androidhive.listviewfeed.data;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by sh on 6/12/2016.
 */
public class UserProfile {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userImage;
    private Set feedses = new HashSet(0);

    public UserProfile() {
    }


    public UserProfile(String firstName, String lastName, String email, String password, String userImage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userImage = userImage;
    }
    public UserProfile(String firstName, String lastName, String email, String password, String userImage, Set feedses) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userImage = userImage;
        this.feedses = feedses;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getUserImage() {
        return this.userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
    public Set getFeedses() {
        return this.feedses;
    }

    public void setFeedses(Set feedses) {
        this.feedses = feedses;
    }

}
