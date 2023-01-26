package com.erymanthian.dance.dtos.auth;

import com.erymanthian.dance.enums.CompanyType;

public record ProfileCompanyIn(String businessName, CompanyType type, String state, String city, String street, String street2, int zip) {
}
