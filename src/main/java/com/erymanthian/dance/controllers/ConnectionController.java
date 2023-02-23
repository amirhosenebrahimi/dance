package com.erymanthian.dance.controllers;

import com.erymanthian.dance.dtos.ConnectRequestCreateDTO;
import com.erymanthian.dance.dtos.ConnectRequestViewModel;
import com.erymanthian.dance.entities.ConnectRequest;
import com.erymanthian.dance.services.ConnectRequestService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/connection")
@RequiredArgsConstructor
@Validated
public class ConnectionController {

    private final ConnectRequestService connectRequestService;

    @PostMapping
    public ResponseEntity<ConnectRequest> create(Authentication authentication, @RequestBody @Valid ConnectRequestCreateDTO connectRequestCreateDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(connectRequestService.create(authentication, connectRequestCreateDTO));
    }

    @GetMapping("/my-request/")
    public ResponseEntity<List<ConnectRequest>> findMyRequest( Authentication authentication) {
        return ResponseEntity.ok(connectRequestService.findMyRequest(authentication));
    }

    @PatchMapping("/change-status/request-id/{id}/{status}")
    public ResponseEntity<ConnectRequest> update(Authentication authentication, @PathVariable Long id,
                                                 @PathVariable @Min(1) @Max(3) Short status) throws Exception {
        return ResponseEntity.ok(connectRequestService.updateStatus(authentication, id, status));
    }
}
