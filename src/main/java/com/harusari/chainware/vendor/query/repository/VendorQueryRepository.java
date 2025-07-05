package com.harusari.chainware.vendor.query.repository;

import com.harusari.chainware.vendor.command.domain.aggregate.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorQueryRepository extends VendorQueryRepositoryCustom, JpaRepository<Vendor, Long> {

    Optional<Vendor> findVendorByVendorId(Long vendorId);

}