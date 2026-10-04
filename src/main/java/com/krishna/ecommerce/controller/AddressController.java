package com.krishna.ecommerce.controller;

import com.krishna.ecommerce.dto.AddressRequest;
import com.krishna.ecommerce.dto.AddressResponse;
import com.krishna.ecommerce.security.SecurityUtils;
import com.krishna.ecommerce.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Addresses",
        description = "APIs for managing customer addresses"
)
@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Operation(
            summary = "Create an address",
            description = "Creates a new address for the currently authenticated user."
    )
    @PostMapping
    public AddressResponse createAddress(
            @Valid @RequestBody AddressRequest request) {

        return addressService.createAddress(
                SecurityUtils.getCurrentUser().getId(),
                request
        );
    }

    @Operation(
            summary = "Get an address",
            description = "Returns a specific address belonging to the currently authenticated user."
    )
    @GetMapping("/{addressId}")
    public AddressResponse getAddressById(
            @PathVariable Long addressId) {

        return addressService.getAddressById(
                addressId,
                SecurityUtils.getCurrentUser().getId()
        );
    }

    @Operation(
            summary = "Get my addresses",
            description = "Returns all addresses belonging to the currently authenticated user."
    )
    @GetMapping("/my-addresses")
    public List<AddressResponse> getMyAddresses() {

        return addressService.getAddressesByUserId(
                SecurityUtils.getCurrentUser().getId()
        );
    }

    @Operation(
            summary = "Update an address",
            description = "Updates an existing address belonging to the currently authenticated user."
    )
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

    @Operation(
            summary = "Delete an address",
            description = "Deletes an address belonging to the currently authenticated user."
    )
    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddress(@PathVariable Long addressId) {

        addressService.deleteAddress(
                addressId,
                SecurityUtils.getCurrentUser().getId()
        );
    }
}