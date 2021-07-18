package com.hoang.webSocket.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.springframework.format.annotation.NumberFormat;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestResponseDto<T> {

    private T data ;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private String time;

    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 100, message = "Invalid status code")
    @Max(value = 599, message = "Invalid status code")
    private int status;

    private String message;


    public RestResponseDto<T> success(T data) {
        this.status = 200;
        this.message = "Success";
        this.data = data;

        return this;
    }

    public RestResponseDto<T> success() {
        this.status = 200;
        this.message = "Success";

        return this;
    }

    public RestResponseDto<T> created(T data) {
        this.status = 201;
        this.message = "Created";
        this.data = data;

        return this;
    }

    public RestResponseDto<T> created() {
        this.status = 201;
        this.message = "Created";

        return this;
    }

    public RestResponseDto<T> badRequest(T data) {
        this.status = 400;
        this.message = "Bad Request";
        this.data = data;

        return this;
    }

    public RestResponseDto<T> badRequest() {
        this.status = 400;
        this.message = "Bad Request";

        return this;
    }

    public RestResponseDto<T> unauthorized(T data) {
        this.status = 401;
        this.message = "Unauthorized";
        this.data = data;

        return this;
    }

    public RestResponseDto<T> unauthorized() {
        this.status = 401;
        this.message = "Unauthorized";

        return this;
    }

    public RestResponseDto<T> notFound(T data) {
        this.status = 404;
        this.message = "Not Found";
        this.data = data;

        return this;
    }

    public RestResponseDto<T> notFound() {
        this.status = 404;
        this.message = "Not Found";

        return this;
    }

    public RestResponseDto<T> notAllowed(T data) {
        this.status = 405;
        this.message = "Method Not Allowed";
        this.data = data;

        return this;
    }

    public RestResponseDto<T> notAllowed() {
        this.status = 405;
        this.message = "Method Not Allowed";

        return this;
    }

    public RestResponseDto<T> serverError(T data) {
        this.status = 500;
        this.message = "Server Error";
        this.data = data;

        return this;
    }

    public RestResponseDto<T> serverError() {
        this.status = 500;
        this.message = "Server Error";

        return this;
    }

}
