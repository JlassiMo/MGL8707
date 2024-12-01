package models;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class UserProfile {
    private String id;
    private String username;
    private String email;
    private String fullName;
    private List<String> enrolledCourses;
    private ZonedDateTime profileCreationDate;
    private ZonedDateTime lastLogin;
    private String accountStatus;
    private Preferences preferences;
    private PersonalDetails personalDetails;
}

@Getter
@Setter
class Preferences {
    private String language;
    private String timezone;
    private boolean emailNotifications;
}

@Getter
@Setter
class PersonalDetails {
    private int age;
    private String gender;
    private Address address;
}

@Getter
@Setter
class Address {
    private String street;
    private String city;
    private String province;
    private String postalCode;
    private String country;
}

