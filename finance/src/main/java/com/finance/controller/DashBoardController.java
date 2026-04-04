package com.finance.controller;

import com.finance.dto.response.ApiResponse;
import com.finance.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/dashboard")
public class DashBoardController {


    @Autowired
    private DashBoardService dashboardService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ANALYST','ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboard() {

        Map<String, Object> data = Map.of(
                "totalIncome", dashboardService.getTotalIncome(),
                "totalExpense", dashboardService.getTotalExpense(),
                "balance", dashboardService.getBalance()
        );

        return ResponseEntity.ok(ApiResponse.ok(data));
    }
}

