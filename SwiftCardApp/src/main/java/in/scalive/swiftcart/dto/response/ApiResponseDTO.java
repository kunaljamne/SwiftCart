package in.scalive.swiftcart.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
//using <> is called generic class and in place of T we use any letter
public class ApiResponseDTO<T> {
	private boolean success;
	private String message;
	private LocalDateTime timeStamp;
	private T data;
	
	public static <T> ApiResponseDTO<T> success(String message,T data) {
		return ApiResponseDTO.<T>builder()
				.success(true)
				.message(message)
				.data(data)
				.timeStamp(LocalDateTime.now())
				.build();
	}
	public static <T> ApiResponseDTO<T> success(String message) {
		return ApiResponseDTO.<T>builder()
				.success(true)
				.message(message)
				.timeStamp(LocalDateTime.now())
				.build();
	}
	public static <T> ApiResponseDTO<T> success(T data) {
		return ApiResponseDTO.<T>builder()
				.success(true)
				.data(data)
				.timeStamp(LocalDateTime.now())
				.build();
	}
}
