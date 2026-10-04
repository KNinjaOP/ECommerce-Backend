package com.krishna.ecommerce.controller;

import com.krishna.ecommerce.dto.AddressRequest;
import com.krishna.ecommerce.dto.AddressResponse;
import com.krishna.ecommerce.security.SecurityUtils;
import com.krishna.ecommerce.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public AddressResponse createAddress(
            @Valid @RequestBody AddressRequest request) {

        return addressService.createAddress(
                SecurityUtils.getCurrentUser().getId(),
                request
        );
    }

    @GetMapping("/{addressId}")
    public AddressResponse getAddressById(
            @PathVariable Long addressId) {

        return addressService.getAddressById(
                addressId,
                SecurityUtils.getCurrentUser().getId()
        );
    }

    @GetMapping("/my-addresses")
    public List<AddressResponse> getMyAddresses() {

        return addressService.getAddressesByUserId(
                SecurityUtils.getCurrentUser().getId()
        );
    }

    @PutMapping("/{addressId}")
    public AddressResponse updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request) {

        return addressService.updateAddress(
                addressId,
                SecurityUtils.getCurrentUser().getId(),
                request
        );
    }

    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddress(@PathVariable Long addressId) {

        addressService.deleteAddress(
                addressId,
                SecurityUtils.getCurrentUser().getId()
        );
    }
}