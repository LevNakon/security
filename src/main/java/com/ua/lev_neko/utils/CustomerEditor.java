package com.ua.lev_neko.utils;

import com.ua.lev_neko.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

@Component
public class CustomerEditor extends PropertyEditorSupport{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void setValue(Object value) {
        Customer customer = (Customer) value;
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
    }
}
