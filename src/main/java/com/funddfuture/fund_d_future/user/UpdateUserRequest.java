// src/main/java/com/funddfuture/fund_d_future/user/UpdateUserRequest.java
package com.funddfuture.fund_d_future.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String firstname;
    private String lastname;
    private String bio;
    private String institution;
    private DegreeProgram degreeProgram;
    private String courseOfStudy;
    private int yearOfStudy;
    private String grade;
    private String proof;
    private List<String> shortTermGoals;
    private List<String> longTermGoals;
    private String extraCurricularActivities;
    private String volunteerWork;
    private CountryList residentCountry;
    private Role role;
}