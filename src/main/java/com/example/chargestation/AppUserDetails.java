package com.example.chargestation;

import com.example.chargestation.repository.schema.Customer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AppUserDetails implements UserDetails {
    Customer customer;
    @Delegate
    UserDetails userDetails;
}
