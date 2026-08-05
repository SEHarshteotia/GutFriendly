package com.gutfriendly.app.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gutfriendly.app.admin.dto.response.ShopResponse;
import com.gutfriendly.app.admin.enums.ServiceAvailabilityStatus;
import com.gutfriendly.app.admin.enums.ShopStatus;
import com.gutfriendly.app.inspector.mapper.ShopMapper;
import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.admin.repository.ShopDetailsRepository;
import com.gutfriendly.app.user.exception.ResourceNotFoundException;

@Service
public class ShopServiceImpl implements ShopsService {

	private final ShopDetailsRepository shopDetailsRepo;

	ShopServiceImpl(ShopDetailsRepository shopDetailsRepo) {
		this.shopDetailsRepo = shopDetailsRepo;
	}

	@Override
	public Page<ShopResponse> getAllShops(int page, int size, String sortBy, String direction) {

		Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		Page<ShopDetails> shopPage = shopDetailsRepo.findAll(pageable);

		List<ShopResponse> response = new ArrayList<>();

		for (ShopDetails shop : shopPage.getContent()) {
			response.add(ShopMapper.toDto(shop));
		}

		return new PageImpl<>(response, pageable, shopPage.getTotalElements());
	}

	@Override
	public Page<ShopResponse> getShopsByStatus(ShopStatus status, int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		List<ShopResponse> response = new ArrayList<>();

		Page<ShopDetails> shopPage = shopDetailsRepo.findByStatus(status, pageable);

		for (ShopDetails shop : shopPage.getContent()) {
			response.add(ShopMapper.toDto(shop));
		}
		return new PageImpl<>(response, pageable, shopPage.getTotalElements());
	}

	@Override
	public Page<ShopResponse> getShopsByServiceAvailabilityStatus(ServiceAvailabilityStatus status, int page, int size,
			String sortBy, String direction) {

		Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		Page<ShopDetails> shopPage = shopDetailsRepo.findByServiceAvailabilityStatus(status, pageable);

		List<ShopResponse> response = new ArrayList<>();

		for (ShopDetails shop : shopPage.getContent()) {
			response.add(ShopMapper.toDto(shop));
		}

		return new PageImpl<>(response, pageable, shopPage.getTotalElements());
	}

	@Override
	public Page<ShopResponse> getShopsByShopName(String shopName, int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		Page<ShopDetails> shopPage = shopDetailsRepo.findByShopNameContainingIgnoreCase(shopName, pageable);
		List<ShopResponse> response = new ArrayList<>();
		for (ShopDetails shop : shopPage.getContent()) {
			response.add(ShopMapper.toDto(shop));
		}
		return new PageImpl<>(response, pageable, shopPage.getTotalElements());

	}

	@Override
	public ShopResponse getShopById(int shopId) {

		Optional<ShopDetails> optionalShop = shopDetailsRepo.findByShopId(shopId);

		if (optionalShop.isEmpty()) {
			throw new ResourceNotFoundException("Shop not found");
		}

		return ShopMapper.toDto(optionalShop.get());
	}

	@Override
	public ShopResponse blockShop(int shopId, String BlockShopReason) {
		Optional<ShopDetails> shop = shopDetailsRepo.findByShopId(shopId);
		if (shop.isEmpty()) {
			throw new ResourceNotFoundException("Shop not found");
		}
		ShopDetails shopDetails = shop.get();
		shopDetails.setServiceAvailabilityStatus(ServiceAvailabilityStatus.NOT_SERVICEABLE);
		shopDetails.setBlocked(true);
		shopDetails.setAdminRemarks(BlockShopReason);

		return ShopMapper.toDto(shopDetailsRepo.save(shopDetails));
	}

	@Override
	public ShopResponse UnblockShop(int shopId) {
		Optional<ShopDetails> shop = shopDetailsRepo.findByShopId(shopId);
		if (shop.isEmpty()) {
			throw new ResourceNotFoundException("Shop not found");
		}
		ShopDetails shopDetails = shop.get();
		shopDetails.setServiceAvailabilityStatus(ServiceAvailabilityStatus.SERVICEABLE);
		shopDetails.setBlocked(false);
		return ShopMapper.toDto(shopDetailsRepo.save(shopDetails));
	}
}
