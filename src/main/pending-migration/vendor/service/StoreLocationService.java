package com.gutfriendly.app.vendor.service;



import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.server.ResponseStatusException;



import com.gutfriendly.app.vendor.dto.StoreLocationRequestDTO;

import com.gutfriendly.app.vendor.dto.StoreLocationResponseDTO;

import com.gutfriendly.app.vendor.model.StoreAddress;

import com.gutfriendly.app.vendor.model.VendorDetails;

import com.gutfriendly.app.vendor.model.Store;

import com.gutfriendly.app.vendor.repository.ServiceableAreaRepo;

import com.gutfriendly.app.vendor.repository.StoreAddressRepo;

import com.gutfriendly.app.vendor.repository.VendorRepo;

import com.gutfriendly.app.vendor.repository.StoreRepo;

import com.gutfriendly.app.vendor.status.VendorStatus;



/**

 * Saves shop addresses and evaluates pincode serviceability against configured areas.

 */

@Service

public class StoreLocationService {



	private final VendorRepo vendorRepository;

	private final StoreRepo storeRepository;

	private final StoreAddressRepo addressRepository;

	private final ServiceableAreaRepo areaRepository;



	StoreLocationService(VendorRepo vendorRepository, StoreRepo storeRepository,

			StoreAddressRepo addressRepository, ServiceableAreaRepo areaRepository) {

		this.vendorRepository = vendorRepository;

		this.storeRepository = storeRepository;

		this.addressRepository = addressRepository;

		this.areaRepository = areaRepository;

	}



	/**

	 * Persists the shop address and updates shop status based on pincode serviceability.

	 *

	 * @param request location payload with vendor ID, shop ID, and address fields;

	 *                {@code shopId} is required

	 * @return serviceability result, updated status, and user-facing message

	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found

	 * @throws ResponseStatusException with {@link HttpStatus#BAD_REQUEST} if shop ID is missing

	 */

	@Transactional

	public StoreLocationResponseDTO saveLocation(StoreLocationRequestDTO request) {

		VendorDetails vendor = vendorRepository.findById(request.getVendorId())

				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));



		if (request.getShopId() == null) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop ID is required");

		}



		Store store = storeRepository.findByStoreIdAndVendor(request.getShopId(), vendor)

				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found"));



		StoreAddress address = addressRepository.findByStore(store).orElse(new StoreAddress());

		address.setStore(store);

		address.setHouseNo(request.getHouseNo());

		address.setStreet(request.getStreet());

		address.setCity(request.getCity());

		address.setState(request.getState());

		address.setPincode(request.getPincode());

		addressRepository.save(address);



		if (areaRepository.existsByPincode(request.getPincode())) {

			store.setStatus(VendorStatus.UNDER_REVIEW);

			storeRepository.save(store);



			return new StoreLocationResponseDTO(true, VendorStatus.UNDER_REVIEW,

					"Your shop location is serviceable. The shop has been sent for review.");

		}



		store.setStatus(VendorStatus.NOT_SERVICEABLE);

		storeRepository.save(store);



		return new StoreLocationResponseDTO(false, VendorStatus.NOT_SERVICEABLE,

				"Sorry! We are currently not operating in this shop's area.");

	}

}

