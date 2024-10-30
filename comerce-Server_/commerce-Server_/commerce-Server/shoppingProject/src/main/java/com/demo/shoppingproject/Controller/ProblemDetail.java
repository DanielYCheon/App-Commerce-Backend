package com.demo.shoppingproject.Controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ProblemDetail {
    private HttpStatus status;
    private String detail;
    private Map<String, String> properties = new HashMap<>();

    public static ProblemDetail forStatusAndDetail(HttpStatus status, String detail) {
        ProblemDetail problemDetail = new ProblemDetail();
        problemDetail.setStatus(status);
        problemDetail.setDetail(detail);
        return problemDetail;
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

}
