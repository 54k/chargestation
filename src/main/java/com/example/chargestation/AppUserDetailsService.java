package com.example.chargestation;

import com.example.chargestation.repository.CustomerRepository;
import com.example.chargestation.repository.schema.Customer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class AppUserDetailsService extends JdbcDaoImpl implements UserDetailsService {
    CustomerRepository customerRepository;

    @Autowired
    public AppUserDetailsService(CustomerRepository customerRepository, DataSource dataSource) {
        this.customerRepository = customerRepository;
        super.setDataSource(dataSource);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = super.loadUserByUsername(username);
        Customer customer = customerRepository.findCustomerByName(userDetails.getUsername());
        return new AppUserDetails(customer, userDetails);
    }
}
