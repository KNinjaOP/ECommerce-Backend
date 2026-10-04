package com.krishna.ecommerce.service;

import com.krishna.ecommerce.dto.AddressRequest;
import com.krishna.ecommerce.dto.AddressResponse;
import com.krishna.ecommerce.exception.ResourceNotFoundException;
import com.krishna.ecommerce.model.Address;
import com.krishna.ecommerce.model.User;
import com.krishna.ecommerce.repository.AddressRepository;
import com.krishna.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {

        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public AddressResponse createAddress(
            Long userId,
            AddressRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        Address address = new Address();

        address.setUser(user);
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());

        addressRepository.save(address);

        return toAddressResponse(address);
    }


    public AddressResponse getAddressById(
            Long addressId,
            Long userId) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with id: " + addressId
                        )
                );

        if (!address.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Address not found with id: " + addressId
            );
        }

        return toAddressResponse(address);
    }

    public List<AddressResponse> getAddressesByUserId(Long userId) {

        return addressRepository.findByUserId(userId)
                .stream()
                .map(this::toAddressResponse)
                .toList();
    }

    public AddressResponse updateAddress(
            Long addressId,
            Long userId,
            AddressRequest request) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with id: " + addressId
                        )
                );

        if (!address.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Address not found with id: " + addressId
            );
        }

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());

        addressRepository.save(address);

        return toAddressResponse(address);
    }

    public void deleteAddress(Long addressId, Long userId) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with id: " + addressId
                        )
                );

        if (!address.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Address not found with id: " + addressId
            );
        }

        addressRepository.delete(address);
    }

    private AddressResponse toAddressResponse(Address address) {

        AddressResponse response = new AddressResponse();

        response.setId(address.getId());
        response.setUserId(address.getUser().getId());
        response.setStreet(address.getStreet());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setPostalCode(address.getPostalCode());
        response.setCountry(address.getCountry());

        return response;
    }
}
