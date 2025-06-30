package com.harusari.chainware.order.query.service;

import com.harusari.chainware.order.query.dto.response.AvailableWarehouseResponse;

import java.util.List;

public interface OrderWarehouseQueryService {

    List<AvailableWarehouseResponse> findAvailableWarehouses(Long orderId);

}
