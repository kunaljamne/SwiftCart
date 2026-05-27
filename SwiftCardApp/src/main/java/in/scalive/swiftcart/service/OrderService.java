package in.scalive.swiftcart.service;

import java.util.List;

import in.scalive.swiftcart.dto.request.PlaceOrderRequestDTO;
import in.scalive.swiftcart.dto.request.UpdateCartItemRequestDTO;
import in.scalive.swiftcart.dto.request.UpdateOrderStatusRequestDTO;
import in.scalive.swiftcart.dto.response.OrderResponseDTO;
import in.scalive.swiftcart.dto.response.PageResponseDTO;

public interface OrderService {
	OrderResponseDTO placeOrder(PlaceOrderRequestDTO dto);
	OrderResponseDTO getOrderById(Long orderId);
	OrderResponseDTO getOrderByOrderNumber(String orderNum);
	List<OrderResponseDTO> getOrdersByUserId(Long userId);
	PageResponseDTO<OrderResponseDTO> getAllOrdersPaginated(int page,int size,String sortBy,String sortDir);
	List<OrderResponseDTO> getOrdersByStatus(String status);
	OrderResponseDTO updateOrderStatus(Long orderId,UpdateOrderStatusRequestDTO dto);
	OrderResponseDTO cancelOrder(Long orderId,String reason);
	PageResponseDTO<OrderResponseDTO> searchOrders(String keyword,int page,int size);

}
